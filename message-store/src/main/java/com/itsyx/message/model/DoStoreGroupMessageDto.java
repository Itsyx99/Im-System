package com.itsyx.message.model;

import com.itsyx.im.common.model.message.GroupChatMessageContent;
import com.itsyx.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author: syx
 * @description:
 **/
@Data
public class DoStoreGroupMessageDto {

    private GroupChatMessageContent groupChatMessageContent;

    private ImMessageBodyEntity imMessageBodyEntity;

}
