package com.itsyx.im.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.codec.pack.LoginPack;
import com.itsyx.im.codec.pack.message.ChatMessageAck;
import com.itsyx.im.codec.pack.user.LoginAckPack;
import com.itsyx.im.codec.pack.user.UserStatusChangeNotifyPack;
import com.itsyx.im.codec.proto.Message;
import com.itsyx.im.codec.proto.MessagePack;
import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.constant.Constants;
import com.itsyx.im.common.enums.ImConnectStatusEnum;
import com.itsyx.im.common.enums.command.GroupEventCommand;
import com.itsyx.im.common.enums.command.MessageCommand;
import com.itsyx.im.common.enums.command.SystemCommand;
import com.itsyx.im.common.enums.command.UserEventCommand;
import com.itsyx.im.common.model.UserClientDto;
import com.itsyx.im.common.model.UserSession;
import com.itsyx.im.common.model.message.CheckSendMessageReq;
import com.itsyx.im.tcp.feign.FeignMessageService;
import com.itsyx.im.tcp.publish.MqMessageProducer;
import com.itsyx.im.tcp.redis.RedisManager;
import com.itsyx.im.tcp.util.SessionSocketHolder;
import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.net.InetAddress;

public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {

    private Integer brokerId;

    private String logicUrl;

    private FeignMessageService feignMessageService;



    public NettyServerHandler(Integer brokerId,String logicUrl) {
        this.brokerId = brokerId;
        feignMessageService = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))//设置超时时间
                .target(FeignMessageService.class, logicUrl);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Integer command = msg.getMessageHeader().getCommand();
        // 登录
        if (command == SystemCommand.LOGIN.getCommand()){
            LoginPack loginPack = JSON.parseObject(JSONObject.toJSONString(msg.getMessagePack()), LoginPack.class);

            ctx.channel().attr(AttributeKey.valueOf(Constants.UserId)).set(loginPack.getUserId());
            ctx.channel().attr(AttributeKey.valueOf(Constants.AppId)).set(msg.getMessageHeader().getAppId());
            ctx.channel().attr(AttributeKey.valueOf(Constants.ClientType)).set(msg.getMessageHeader().getClientType());
            ctx.channel().attr(AttributeKey.valueOf(Constants.Imei)).set(msg.getMessageHeader().getImei());

            // Redis map
            UserSession userSession = new UserSession();
            userSession.setAppId(msg.getMessageHeader().getAppId());
            userSession.setClientType(msg.getMessageHeader().getClientType());
            userSession.setUserId(loginPack.getUserId());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE_STATUS.getCode());
            userSession.setImei(msg.getMessageHeader().getImei());
            userSession.setBrokerId(brokerId);
            //设置服务器节点ip地址
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                userSession.setBrokerHost(localHost.getHostAddress());
            }catch (Exception e){
                e.printStackTrace();
            }
            // 存储到redis
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<String, String> map = redissonClient.getMap(msg.getMessageHeader().getAppId() + Constants.RedisConstants.UserSessionConstants + loginPack.getUserId());

            map.put(msg.getMessageHeader().getClientType().toString()+":"+msg.getMessageHeader().getImei(),JSONObject.toJSONString(userSession));

            // 将channel存起来
            SessionSocketHolder.put(msg.getMessageHeader().getAppId(),loginPack.getUserId(),msg.getMessageHeader().getClientType(),msg.getMessageHeader().getImei(), (NioSocketChannel) ctx.channel());

            UserClientDto dto = new UserClientDto();
            dto.setImei(msg.getMessageHeader().getImei());
            dto.setUserId(loginPack.getUserId());
            dto.setClientType(msg.getMessageHeader().getClientType());
            dto.setAppId(msg.getMessageHeader().getAppId());

            // 使用redis发布订阅模式 踢掉其他端用户
            RTopic topic = redissonClient.getTopic(Constants.RedisConstants.UserLoginChannel);
            topic.publish(JSONObject.toJSONString(dto));

            // 用户改变为登录-在线状态 通知
            UserStatusChangeNotifyPack userStatusChangeNotifyPack = new UserStatusChangeNotifyPack();
            userStatusChangeNotifyPack.setAppId(msg.getMessageHeader().getAppId());
            userStatusChangeNotifyPack.setUserId(loginPack.getUserId());
            userStatusChangeNotifyPack.setStatus(ImConnectStatusEnum.ONLINE_STATUS.getCode());
            MqMessageProducer.sendMessage(userStatusChangeNotifyPack,msg.getMessageHeader(), UserEventCommand.USER_ONLINE_STATUS_CHANGE.getCommand());
            
            // 补充登录ACK 通知客户端登录成功
            MessagePack<LoginAckPack> loginSuccess = new MessagePack<>();
            LoginAckPack loginAckPack = new LoginAckPack();
            loginAckPack.setUserId(loginPack.getUserId());
            loginSuccess.setCommand(SystemCommand.LOGINACK.getCommand());
            loginSuccess.setData(loginAckPack);
            loginSuccess.setImei(msg.getMessageHeader().getImei());
            loginSuccess.setAppId(msg.getMessageHeader().getAppId());
            ctx.channel().writeAndFlush(loginSuccess);

        }else if (command == SystemCommand.LOGOUT.getCommand()){  // 退出登录
            //删除session
//            String userId = (String) ctx.channel().attr(AttributeKey.valueOf(Constants.UserId)).get();
//            Integer appId = (Integer) ctx.channel().attr(AttributeKey.valueOf(Constants.AppId)).get();
//            Integer clientType = (Integer) ctx.channel().attr(AttributeKey.valueOf(Constants.ClientType)).get();
//            SessionSocketHolder.remove(appId,userId,clientType);
//            //redis 删除
//            RedissonClient redissonClient = RedisManager.getRedissonClient();
//            RMap<String, String> map = redissonClient.getMap(appId + Constants.RedisConstants.UserSessionConstants + userId);
//            map.remove(clientType);
//            ctx.channel().close();
            SessionSocketHolder.removeUserSession((NioSocketChannel) ctx.channel());

        }else if(command == SystemCommand.PING.getCommand()){   // 心跳检测处理
            ctx.channel().attr(AttributeKey.valueOf(Constants.ReadTime)).set(System.currentTimeMillis());

        }else if (command == MessageCommand.MSG_P2P.getCommand()|| command == GroupEventCommand.MSG_GROUP.getCommand()){
            //1.调用校验消息发送方的接口。
            //如果成功投递到mq
            //失败则直接ack
            try {
                String toId = "";
                CheckSendMessageReq req = new CheckSendMessageReq();
                req.setAppId(msg.getMessageHeader().getAppId());
                req.setCommand(msg.getMessageHeader().getCommand());
                JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(msg.getMessagePack()));
                String fromId = jsonObject.getString("fromId");
                if(command == MessageCommand.MSG_P2P.getCommand()){
                    toId = jsonObject.getString("toId");
                }else {
                    toId = jsonObject.getString("groupId");
                }
                req.setToId(toId);
                req.setFromId(fromId);

                ResponseVO responseVO = feignMessageService.checkSendMessage(req);
                if(responseVO.isOk()){
                    MqMessageProducer.sendMessage(msg,command);
                }else{
                    Integer ackCommand = 0;
                    if(command == MessageCommand.MSG_P2P.getCommand()){
                        ackCommand = MessageCommand.MSG_ACK.getCommand();
                    }else {
                        ackCommand = GroupEventCommand.GROUP_MSG_ACK.getCommand();
                    }

                    ChatMessageAck chatMessageAck = new ChatMessageAck(jsonObject.getString("messageId"));
                    responseVO.setData(chatMessageAck);
                    MessagePack<ResponseVO> ack = new MessagePack<>();
                    ack.setData(responseVO);
                    ack.setCommand(ackCommand);
                    ctx.channel().writeAndFlush(ack);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            MqMessageProducer.sendMessage(msg,command); // 发送消息到逻辑层
        }
        System.out.println(msg);
    }

}
