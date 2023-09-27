package com.itsyx.im.service.group.controller;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.model.SyncReq;
import com.itsyx.im.service.group.model.req.*;
import com.itsyx.im.service.group.service.GroupMessageService;
import com.itsyx.im.service.group.service.ImGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@RestController
@RequestMapping("v1/group")
public class ImGroupController {

    @Autowired
    ImGroupService groupService;

    @Autowired
    private GroupMessageService groupMessageService;

    // 导入群组
    @PostMapping("/importGroup")
    public ResponseVO importGroup(@RequestBody @Validated ImportGroupReq req)  {
        return groupService.importGroup(req);
    }
    // 创建群组(带有群员)
    @PostMapping("/createGroup")
    public ResponseVO createGroup(@RequestBody @Validated CreateGroupReq req)  {
        return groupService.createGroup(req);
    }

    // 获取群组信息(包括群成员)
    @GetMapping("/getGroupInfo")
    public ResponseVO getGroupInfo(@RequestBody @Validated GetGroupReq req)  {
        return groupService.getGroup(req);
    }
   // 修改群信息
    @PutMapping("/update")
    public ResponseVO update(@RequestBody @Validated UpdateGroupReq req)  {
        return groupService.updateBaseGroupInfo(req);
    }

    // 获取用户加入群组
    @GetMapping("/getJoinedGroup")
    public ResponseVO getJoinedGroup(@RequestBody @Validated GetJoinedGroupReq req)  {
        return groupService.getJoinedGroup(req);
    }

    // 解散群组
    @PutMapping("/destroyGroup")
    public ResponseVO destroyGroup(@RequestBody @Validated DestroyGroupReq req)  {
        return groupService.destroyGroup(req);
    }

    // 转移群
    @PutMapping("/transferGroup")
    public ResponseVO transferGroup(@RequestBody @Validated TransferGroupReq req)  {
        return groupService.transferGroup(req);
    }

    // 禁言 解禁
    @PutMapping("/forbidSendMessage")
    public ResponseVO forbidSendMessage(@RequestBody @Validated MuteGroupReq req)  {
        return groupService.muteGroup(req);
    }

    @RequestMapping("/sendMessage")
    public ResponseVO sendMessage(@RequestBody @Validated SendGroupMessageReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        req.setOperater(identifier);
        return ResponseVO.successResponse(groupMessageService.send(req));
    }

    @RequestMapping("/syncJoinedGroup")
    public ResponseVO syncJoinedGroup(@RequestBody @Validated SyncReq req, Integer appId, String identifier)  {
        req.setAppId(appId);
        return groupService.syncJoinedGroupList(req);
    }
}
