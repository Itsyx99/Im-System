package com.itsyx.im.codec.pack.message;

import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class MessageReciveServerAckPack {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageSequence;

    private Boolean serverSend;
}
