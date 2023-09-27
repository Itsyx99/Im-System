package com.itsyx.im.common.model.message;

import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class DoStoreP2PMessageDto {

    private MessageContent messageContent;

    private ImMessageBody messageBody;

}
