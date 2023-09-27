package com.itsyx.im.service.friendship.controller;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupReq;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupMemberService;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: syx
 * @description: 标签组
 **/
@RestController
@RequestMapping("/v1/friendship/group")
public class ImFriendShipGroupController {

    @Autowired
    ImFriendShipGroupService imFriendShipGroupService;

    @Autowired
    ImFriendShipGroupMemberService imFriendShipGroupMemberService;

    // 添加标签分组
    @PostMapping("/add")
    public ResponseVO add(@RequestBody @Validated AddFriendShipGroupReq req)  {
        return imFriendShipGroupService.addGroup(req);
    }

    // 删除标签分组(列表)
    @DeleteMapping("/del")
    public ResponseVO del(@RequestBody @Validated DeleteFriendShipGroupReq req)  {
        return imFriendShipGroupService.deleteGroup(req);
    }
    // 向标签分组添加成员列表
    @PostMapping("/member/add")
    public ResponseVO memberAdd(@RequestBody @Validated AddFriendShipGroupMemberReq req)  {
        return imFriendShipGroupMemberService.addGroupMember(req);
    }

    @DeleteMapping("/member/del")
    public ResponseVO memberdel(@RequestBody @Validated DeleteFriendShipGroupMemberReq req)  {
        return imFriendShipGroupMemberService.delGroupMember(req);
    }


}
