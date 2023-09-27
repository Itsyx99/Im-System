package com.itsyx.im.tcp.reciver;

import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.codec.proto.MessagePack;
import com.itsyx.im.common.constant.Constants;
//import com.itsyx.im.tcp.reciver.process.BaseProcess;
//import com.itsyx.im.tcp.reciver.process.ProcessFactory;
import com.itsyx.im.tcp.reciver.process.BaseProcess;
import com.itsyx.im.tcp.reciver.process.ProcessFactory;
import com.itsyx.im.tcp.util.MqFactory;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @description: 处理用户下线通知
 * 多端同步:
 * @author: syx
 * @version: 1.0
 */
@Slf4j
public class MessageReciver {

    private static String brokerId;

    private static void startReciverMessage() {
        try {
            Channel channel = MqFactory.getChannel(Constants.RabbitConstants.MessageService2Im + brokerId);
            channel.queueDeclare(Constants.RabbitConstants.MessageService2Im + brokerId , true, false, false, null);
            channel.exchangeDeclare(Constants.RabbitConstants.MessageService2Im, BuiltinExchangeType.DIRECT,true); // 路由器
            channel.queueBind(Constants.RabbitConstants.MessageService2Im + brokerId , Constants.RabbitConstants.MessageService2Im, brokerId);
            channel.basicConsume(Constants.RabbitConstants.MessageService2Im + brokerId, false,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            //TODO 处理消息服务发来的消息
                            try {
                                String msgStr = new String(body);
                                log.info(msgStr);
                                MessagePack messagePack = JSONObject.parseObject(msgStr, MessagePack.class);
                                BaseProcess messageProcess = ProcessFactory.getMessageProcess(messagePack.getCommand());
                                messageProcess.process(messagePack);
                                channel.basicAck(envelope.getDeliveryTag(),false);
                            }catch (Exception e){
                                e.printStackTrace();
                                channel.basicNack(envelope.getDeliveryTag(),false,false);
                            }
                        }
                    }
            );
        } catch (Exception e) {
            log.error("接收消息出现异常：{}",e.getMessage());
        }
    }

    public static void init() {
        startReciverMessage();
    }

    public static void init(String brokerId) {
        if (StringUtils.isBlank(MessageReciver.brokerId)) {
            MessageReciver.brokerId = brokerId;
        }
        startReciverMessage();
    }


}
