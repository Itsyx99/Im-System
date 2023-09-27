package com.itsyx.im.common.model.message;

import com.itsyx.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class MessageReadedContent extends ClientInfo {

    private long messageSequence; // 消息序列号

    private String fromId;

    private String groupId;

    private String toId;

    private Integer conversationType; // 会话类型

}
