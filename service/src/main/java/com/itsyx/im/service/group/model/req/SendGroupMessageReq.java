package com.itsyx.im.service.group.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class SendGroupMessageReq extends RequestBase {

    //客户端传的messageId
    private String messageId;

    private String fromId;

    private String groupId;

    private int messageRandom;

    private long messageTime;

    private String messageBody;
    /**
     * 这个字段缺省或者为 0 表示需要计数，为 1 表示本条消息不需要计数，即右上角图标数字不增加
     */
    private int badgeMode;

    private Long messageLifeTime;

    private Integer appId;

}
