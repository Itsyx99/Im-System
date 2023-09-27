package com.itsyx.im.service.user.controller;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.user.model.req.GetUserInfoReq;
import com.itsyx.im.service.user.model.req.ModifyUserInfoReq;
import com.itsyx.im.service.user.model.req.UserId;
import com.itsyx.im.service.user.service.ImUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@RestController
@RequestMapping("/v1/user/data")
public class ImUserDataController {

    private static Logger logger = LoggerFactory.getLogger(ImUserDataController.class);

    @Autowired
    ImUserService imUserService;

    @GetMapping("/getUserInfo")
    public ResponseVO getUserInfo(@RequestBody GetUserInfoReq req){//@Validated
        return imUserService.getUserInfo(req);
    }

    @GetMapping("/getSingleUserInfo")
    public ResponseVO getSingleUserInfo(@RequestBody @Validated UserId req){
        return imUserService.getSingleUserInfo(req.getUserId(),req.getAppId());
    }

    @PutMapping("/modifyUserInfo")
    public ResponseVO modifyUserInfo(@RequestBody @Validated ModifyUserInfoReq req){
        return imUserService.modifyUserInfo(req);
    }
}
