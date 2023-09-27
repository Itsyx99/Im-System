package com.itsyx.im.common.model.message;

import com.itsyx.im.common.model.ClientInfo;
import lombok.Data;
import sun.dc.pr.PRError;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class MessageReciveAckContent extends ClientInfo {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageSequence;


}
