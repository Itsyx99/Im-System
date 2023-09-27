package com.itsyx.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.codec.proto.MessagePack;
import com.itsyx.im.common.constant.Constants;
import com.itsyx.im.common.enums.command.Command;
import com.itsyx.im.common.model.ClientInfo;
import com.itsyx.im.common.model.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Service
public class MessageProducer {
    private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserSessionUtils userSessionUtils;

    private String queueName = Constants.RabbitConstants.MessageService2Im;

    public boolean sendMessage(UserSession session,Object msg){
        try {
            logger.info("send message == " + msg);
            rabbitTemplate.convertAndSend(queueName,session.getBrokerId()+"",msg);
            return true;
        }catch (Exception e){
            logger.error("send error :" + e.getMessage());
            return false;
        }
    }

    // 包装数据，调用sendMessage
    public boolean sendPack(String toId, Command command,Object msg,UserSession session){
        MessagePack messagePack = new MessagePack();
        messagePack.setCommand(command.getCommand());
        messagePack.setToId(toId);
        messagePack.setClientType(session.getClientType());
        messagePack.setAppId(session.getAppId());
        messagePack.setImei(session.getImei());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
        messagePack.setData(jsonObject);

        String body = JSONObject.toJSONString(messagePack);
        return sendMessage(session, body);
    }

    // 发送给某个用户所有端的方法
    public List<ClientInfo> sendToUser(String toId,Command command,Object data,Integer appId){
        List<UserSession> userSession = userSessionUtils.getUserSession(appId, toId);
        List<ClientInfo> list = new ArrayList<>();
        for (UserSession session : userSession) {
            boolean b = sendPack(toId, command, data, session);
            if(b){
                list.add(new ClientInfo(session.getAppId(),session.getClientType(),session.getImei()));
            }
        }
        return list;
    }

    //发送给某个用户的指定客户端
    public void sendToUser(String toId, Command command, Object data, ClientInfo clientInfo){
        UserSession userSession = userSessionUtils.getUserSession(clientInfo.getAppId(), toId, clientInfo.getClientType(), clientInfo.getImei());
        sendPack(toId,command,data,userSession);
    }

    public void sendToUser(String toId, Integer clientType, String imei, Command command, Object data, Integer appId){
        if(clientType != null && StringUtils.isNotBlank(imei)){
            //发送给其他端
            ClientInfo clientInfo = new ClientInfo(appId, clientType, imei);
            sendToUserExceptClient(toId,command,data,clientInfo);
        }else{
            //发送给所有端
            sendToUser(toId,command,data,appId);
        }
    }

    private boolean isMatch(UserSession sessionDto, ClientInfo clientInfo) {
        return Objects.equals(sessionDto.getAppId(), clientInfo.getAppId())
                && Objects.equals(sessionDto.getImei(), clientInfo.getImei())
                && Objects.equals(sessionDto.getClientType(), clientInfo.getClientType());
    }

    //发送给除了某一端的其他端
    public void sendToUserExceptClient(String toId, Command command, Object data, ClientInfo clientInfo){
        List<UserSession> userSession = userSessionUtils.getUserSession(clientInfo.getAppId(), toId);
        for (UserSession session : userSession) {
            if(!isMatch(session,clientInfo)){
                sendPack(toId,command,data,session);
            }
        }
    }

}
