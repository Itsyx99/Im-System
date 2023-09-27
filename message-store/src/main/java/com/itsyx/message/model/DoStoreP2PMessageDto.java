package com.itsyx.message.model;

import com.itsyx.im.common.model.message.MessageContent;
import com.itsyx.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author: syx
 * @description:
 **/
@Data
public class DoStoreP2PMessageDto {

    private MessageContent messageContent;

    private ImMessageBodyEntity imMessageBodyEntity;

}
