package com.itsyx.message.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.common.constant.Constants;
import com.itsyx.message.dao.ImMessageBodyEntity;
import com.itsyx.message.model.DoStoreGroupMessageDto;
import com.itsyx.message.model.DoStoreP2PMessageDto;
import com.itsyx.message.service.StoreMessageService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Service
public class StroeGroupMessageReceiver {
    private static Logger logger = LoggerFactory.getLogger(StroeGroupMessageReceiver.class);

    @Autowired
    StoreMessageService storeMessageService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = Constants.RabbitConstants.StoreGroupMessage,durable = "true"),
                    exchange = @Exchange(value = Constants.RabbitConstants.StoreGroupMessage,durable = "true")
            ),concurrency = "1",
            ackMode = "MANUAL" // 《== 重点 手动应答ACK
    )
    public void onChatMessage(@Payload Message message,
                              @Headers Map<String,Object> headers,
                              Channel channel) throws Exception {
        String msg = new String(message.getBody(),"utf-8");
        logger.info("CHAT MSG FORM QUEUE ::: {}", msg);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            DoStoreGroupMessageDto doStoreGroupMessageDto = jsonObject.toJavaObject(DoStoreGroupMessageDto.class);
            ImMessageBodyEntity messageBody = jsonObject.getObject("messageBody", ImMessageBodyEntity.class);
            doStoreGroupMessageDto.setImMessageBodyEntity(messageBody);
            storeMessageService.doStoreGroupMessage(doStoreGroupMessageDto);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e){
            logger.error("处理消息出现异常：{}", e.getMessage());
            logger.error("RMQ_CHAT_TRAN_ERROR", e);
            logger.error("NACK_MSG:{}", msg);
            //第一个false 表示不批量拒绝，第二个false表示不重回队列
            channel.basicNack(deliveryTag, false, false);
        }

    }
}
