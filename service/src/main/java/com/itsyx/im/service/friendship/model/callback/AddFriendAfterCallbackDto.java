package com.itsyx.im.service.friendship.model.callback;

import com.itsyx.im.service.friendship.model.req.FriendDto;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class AddFriendAfterCallbackDto {

    private String fromId;

    private FriendDto toItem;
}
