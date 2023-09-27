package com.itsyx.im.common.model.message;

import com.itsyx.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class MessageContent extends ClientInfo {

    private String messageId;  //消息id

    private String fromId;  // 发送者

    private String toId;  // 接收者

    private String messageBody; // 消息内容

    private Long messageTime;  //

    private String extra;

    private Long messageKey;

    private long messageSequence; // 序列号 用来确定消息顺序

}
