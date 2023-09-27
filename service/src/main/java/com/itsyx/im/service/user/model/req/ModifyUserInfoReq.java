package com.itsyx.im.service.user.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class ModifyUserInfoReq extends RequestBase {

    // 用户id
    @NotEmpty(message = "用户id不能为空")
    private String userId;

    // 用户名称
    private String nickName;

    //位置
    private String location;

    //生日
    private String birthDay;

    private String password;

    // 头像
    private String photo;

    // 性别
    private Integer userSex;

    // 个性签名
    private String selfSignature;

    // 加好友验证类型（Friend_AllowType） 1不需要验证
    private Integer friendAllowType;

    private String extra;


}
