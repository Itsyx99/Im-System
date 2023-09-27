package com.itsyx.im.common.model.message;

import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class OfflineMessageContent {

    private Integer appId;

    /** messageBodyId*/
    private Long messageKey;

    /** messageBody*/
    private String messageBody;

    private Long messageTime;

    private String extra;

    private Integer delFlag;

    private String fromId;

    private String toId;

    /** 序列号*/
    private Long messageSequence;

    private String messageRandom;

    private Integer conversationType; //会话类型

    private String conversationId; // 会话ID

}
