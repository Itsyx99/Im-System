package com.itsyx.im.service.friendship.model.callback;


import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class DeleteFriendAfterCallbackDto {

    private String fromId;

    private String toId;
}
