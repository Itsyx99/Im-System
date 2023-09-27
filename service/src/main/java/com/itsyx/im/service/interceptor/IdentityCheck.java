package com.itsyx.im.service.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.common.BaseErrorCode;
import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.config.AppConfig;
import com.itsyx.im.common.constant.Constants;
import com.itsyx.im.common.enums.GateWayErrorCode;
import com.itsyx.im.common.enums.ImUserTypeEnum;
import com.itsyx.im.common.exception.ApplicationExceptionEnum;
import com.itsyx.im.common.utils.SigAPI;
import com.itsyx.im.service.user.model.ImUserDataEntity;
import com.itsyx.im.service.user.service.ImUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Component
public class IdentityCheck {

    private static Logger logger = LoggerFactory.getLogger(IdentityCheck.class);

    @Autowired
    ImUserService imUserService;

    //10000 123456 10001 123456789
    @Autowired
    AppConfig appConfig;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public ApplicationExceptionEnum checkUserSig(String identifier,
                                                 String appId,String userSig){

        String cacheUserSig = stringRedisTemplate.opsForValue()
                .get(appId + ":" + Constants.RedisConstants.userSign + ":"
                + identifier + userSig);
        if(!StringUtils.isBlank(cacheUserSig) && Long.valueOf(cacheUserSig)
         >  System.currentTimeMillis() / 1000){
            this.setIsAdmin(identifier,Integer.valueOf(appId));
            return BaseErrorCode.SUCCESS;
        }

        //获取秘钥
        String privateKey = appConfig.getPrivateKey();

        //根据appid + 秘钥创建sigApi
        SigAPI sigAPI = new SigAPI(Long.valueOf(appId), privateKey);

        //调用sigApi对userSig解密
        JSONObject jsonObject = sigAPI.decodeUserSig(userSig);

        //取出解密后的appid 和 操作人 和 过期时间做匹配，不通过则提示错误
        Long expireTime = 0L;
        Long expireSec = 0L;
        Long time = 0L;
        String decoerAppId = "";
        String decoderidentifier = "";

        try {
            decoerAppId = jsonObject.getString("TLS.appId");
            decoderidentifier = jsonObject.getString("TLS.identifier");
            String expireStr = jsonObject.get("TLS.expire").toString();
            String expireTimeStr = jsonObject.get("TLS.expireTime").toString();
            time = Long.valueOf(expireTimeStr);
            expireSec = Long.valueOf(expireStr);
            expireTime = Long.valueOf(expireTimeStr) + expireSec;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("checkUserSig-error:{}",e.getMessage());
        }

        if(!decoderidentifier.equals(identifier)){
            return GateWayErrorCode.USERSIGN_OPERATE_NOT_MATE;
        }

        if(!decoerAppId.equals(appId)){
            return GateWayErrorCode.USERSIGN_IS_ERROR;
        }

        if(expireSec == 0L){
            return GateWayErrorCode.USERSIGN_IS_EXPIRED;
        }

        if(expireTime < System.currentTimeMillis() / 1000){
            return GateWayErrorCode.USERSIGN_IS_EXPIRED;
        }

        //appid + "xxx" + userId + sign
        String genSig = sigAPI.genUserSig(identifier, expireSec,time,null);
        if (genSig.toLowerCase().equals(userSig.toLowerCase()))
        {
            String key = appId + ":" + Constants.RedisConstants.userSign + ":"
                    +identifier + userSig;

            Long etime = expireTime - System.currentTimeMillis() / 1000;
            stringRedisTemplate.opsForValue().set(
                    key,expireTime.toString(),etime, TimeUnit.SECONDS
            );
            // 校验本次请求是否是后台调用
            this.setIsAdmin(identifier,Integer.valueOf(appId));
            return BaseErrorCode.SUCCESS;
        }

        return GateWayErrorCode.USERSIGN_IS_ERROR;
    }


    /**
     * 根据appid,identifier判断是否App管理员,并设置到RequestHolder
     * @param identifier
     * @param appId
     * @return
     */
    public void setIsAdmin(String identifier, Integer appId) {
        //去DB或Redis中查找, 后面写
        ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(identifier, appId);
        if(singleUserInfo.isOk()){
            RequestHolder.set(singleUserInfo.getData().getUserType() == ImUserTypeEnum.APP_ADMIN.getCode());
        }else{
            RequestHolder.set(false);
        }
    }
}
