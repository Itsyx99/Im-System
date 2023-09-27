package com.itsyx.im.service.conversation.model;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class UpdateConversationReq extends RequestBase {

    private String conversationId;

    private Integer isMute;

    private Integer isTop;

    private String fromId;


}
