package com.itsyx.im.service.friendship.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;

/**
 * @author: syx
 * @description:
 **/
public interface ImFriendShipGroupMemberService {

    // 向标签分组添加成员列表
    public ResponseVO addGroupMember(AddFriendShipGroupMemberReq req);
    // 清除标签分组成员
    public ResponseVO delGroupMember(DeleteFriendShipGroupMemberReq req);
    // 清除标签分组某个成员
    public int doAddGroupMember(Long groupId, String toId);

    // 清除标签分组成员
    public int clearGroupMember(Long groupId);
}
