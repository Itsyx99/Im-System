package com.itsyx.im.service.friendship.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.friendship.model.ImFriendShipGroupEntity;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupReq;

/**
 * @author: syx
 * @description:
 **/
public interface ImFriendShipGroupService {

    public ResponseVO addGroup(AddFriendShipGroupReq req);

    public ResponseVO deleteGroup(DeleteFriendShipGroupReq req);

    public ResponseVO<ImFriendShipGroupEntity> getGroup(String fromId, String groupName, Integer appId);


}
