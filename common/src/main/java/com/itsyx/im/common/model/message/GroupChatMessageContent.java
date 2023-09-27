package com.itsyx.im.common.model.message;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class GroupChatMessageContent extends MessageContent {

    private String groupId;

    private List<String> memberId;

}
