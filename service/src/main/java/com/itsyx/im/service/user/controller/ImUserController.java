package com.itsyx.im.service.user.controller;

import com.itsyx.im.common.ClientType;
import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.route.RouteHandle;
import com.itsyx.im.common.route.RouteInfo;
import com.itsyx.im.common.utils.RouteInfoParseUtil;
import com.itsyx.im.service.user.model.req.*;
import com.itsyx.im.service.user.service.ImUserService;
import com.itsyx.im.service.user.service.ImUserStatusService;
import com.itsyx.im.service.utils.ZKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@RestController
@RequestMapping("/v1/user")
public class ImUserController {
    @Autowired
    private ImUserService imUserService;
    @Autowired
    private RouteHandle routeHandle;
    @Autowired
    private ZKit zKit;

    @Autowired
    private ImUserStatusService imUserStatusService;

    @RequestMapping(value = "/importUser",method = RequestMethod.POST)
    public ResponseVO importUser(@RequestBody ImportUserReq req) {
        return imUserService.importUser(req);
    }

    @DeleteMapping("/deleteUser")
    public ResponseVO deleteUser(@RequestBody @Validated DeleteUserReq req) {
        return imUserService.deleteUser(req);
    }

    /**
     * @param req
     * @return com.itsyx.im.common.ResponseVO
     * @description im的登录接口，返回im地址
     * @author chackylee
     */
    @GetMapping("/login")
    public ResponseVO login(@RequestBody @Validated LoginReq req) {
        ResponseVO login = imUserService.login(req);

        if (login.isOk()) {
            List<String> allNode = new ArrayList<>();
            // 去zk获取一个im地址,返回给sdk

            if (req.getClientType() == ClientType.WEB.getCode()) {
                allNode = zKit.getAllWebNode();
            } else {
                allNode = zKit.getAllTcpNode();
            }
            // ip:port
            String s = routeHandle.routeServer(allNode, req.getUserId());
            RouteInfo parse = RouteInfoParseUtil.parse(s);
            return ResponseVO.successResponse(parse);
        }

        return ResponseVO.errorResponse();
    }

    // 获取用户sequence
    @RequestMapping("/getUserSequence")
    public ResponseVO getUserSequence(@RequestBody @Validated GetUserSequenceReq req, Integer appId) {
        req.setAppId(appId);
        return imUserService.getUserSequence(req);
    }

    // 临时订阅用户
    @RequestMapping("/subscribeUserOnlineStatus")
    public ResponseVO subscribeUserOnlineStatus(@RequestBody @Validated SubscribeUserOnlineStatusReq req, Integer appId,String identifier) {
        req.setAppId(appId);
        req.setOperater(identifier);
        imUserStatusService.subscribeUserOnlineStatus(req);
        return ResponseVO.successResponse();
    }
    // 设置用户自定义状态接口
    @RequestMapping("/setUserCustomerStatus")
    public ResponseVO setUserCustomerStatus(@RequestBody @Validated SetUserCustomerStatusReq req, Integer appId,String identifier) {
        req.setAppId(appId);
        req.setOperater(identifier);
        imUserStatusService.setUserCustomerStatus(req);
        return ResponseVO.successResponse();
    }

    // 拉取所有好友在线状态
    @RequestMapping("/queryFriendOnlineStatus")
    public ResponseVO queryFriendOnlineStatus(@RequestBody @Validated PullFriendOnlineStatusReq req, Integer appId,String identifier) {
        req.setAppId(appId);
        req.setOperater(identifier);
        return ResponseVO.successResponse(imUserStatusService.queryFriendOnlineStatus(req));
    }
    // 拉取指定用户在线状态
    @RequestMapping("/queryUserOnlineStatus")
    public ResponseVO queryUserOnlineStatus(@RequestBody @Validated PullUserOnlineStatusReq req, Integer appId,String identifier) {
        req.setAppId(appId);
        req.setOperater(identifier);
        return ResponseVO.successResponse(imUserStatusService.queryUserOnlineStatus(req));
    }



}
