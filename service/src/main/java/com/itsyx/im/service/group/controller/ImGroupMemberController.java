package com.itsyx.im.service.group.controller;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.group.model.req.*;
import com.itsyx.im.service.group.service.ImGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@RestController
@RequestMapping("v1/group/member")
public class ImGroupMemberController {

    @Autowired
    ImGroupMemberService groupMemberService;

    @RequestMapping("/importGroupMember")
    public ResponseVO importGroupMember(@RequestBody @Validated ImportGroupMemberReq req)  {
        return groupMemberService.importGroupMember(req);
    }

    @RequestMapping("/add")
    public ResponseVO addMember(@RequestBody @Validated AddGroupMemberReq req)  {
        return groupMemberService.addMember(req);
    }

    @RequestMapping("/remove")
    public ResponseVO removeMember(@RequestBody @Validated RemoveGroupMemberReq req)  {
        return groupMemberService.removeMember(req);
    }

    @RequestMapping("/update")
    public ResponseVO updateGroupMember(@RequestBody @Validated UpdateGroupMemberReq req)  {
        return groupMemberService.updateGroupMember(req);
    }
    // 禁言群成员
    @RequestMapping("/speak")
    public ResponseVO speak(@RequestBody @Validated SpeaMemberReq req)  {
        return groupMemberService.speak(req);
    }

}
