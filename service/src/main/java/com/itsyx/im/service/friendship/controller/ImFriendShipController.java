package com.itsyx.im.service.friendship.controller;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.model.SyncReq;
import com.itsyx.im.service.friendship.model.req.*;
import com.itsyx.im.service.friendship.service.ImFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/friendship")
public class ImFriendShipController {

    @Autowired
    ImFriendService imFriendShipService;

    // 导入关系链
    @PostMapping("/importFriendShip")
    public ResponseVO importFriendShip(@RequestBody @Validated ImporFriendShipReq req){
        return imFriendShipService.importFriendShip(req);
    }
    // 添加好友
    @PostMapping("/addFriend")
    public ResponseVO addFriend(@RequestBody @Validated AddFriendReq req){
        return imFriendShipService.addFriend(req);
    }
    // 更新好友关系链(add_source,remark,extra)信息
    @PutMapping("/updateFriend")
    public ResponseVO updateFriend(@RequestBody @Validated UpdateFriendReq req){
        return imFriendShipService.updateFriend(req);
    }
    // 软删除好友链关系
    @DeleteMapping("/deleteFriend")
    public ResponseVO deleteFriend(@RequestBody @Validated DeleteFriendReq req){
        return imFriendShipService.deleteFriend(req);
    }
    // 软删除所有好友链关系
    @DeleteMapping("/deleteAllFriend")
    public ResponseVO deleteAllFriend(@RequestBody @Validated DeleteFriendReq req){
        return imFriendShipService.deleteAllFriend(req);
    }
    // 获取用户所有关系链
    @GetMapping("/getAllFriendShip")
    public ResponseVO getAllFriendShip(@RequestBody @Validated GetAllFriendShipReq req){
        return imFriendShipService.getAllFriendShip(req);
    }
    // 获取用户之间关系链
    @GetMapping("/getRelation")
    public ResponseVO getRelation(@RequestBody @Validated GetRelationReq req){
        return imFriendShipService.getRelation(req);
    }
    // 检查用户到好友列表的关系 checkType: 1单向   2双向
    @GetMapping("/checkFriend")
    public ResponseVO checkFriend(@RequestBody @Validated CheckFriendShipReq req){
        return imFriendShipService.checkFriendship(req);
    }
    // 用户添加黑名单用户
    @PostMapping("/addBlack")
    public ResponseVO addBlack(@RequestBody @Validated AddFriendShipBlackReq req){
        return imFriendShipService.addBlack(req);
    }
    // 移除(更新)黑名单
    @PutMapping("/deleteBlack")
    public ResponseVO deleteBlack(@RequestBody @Validated DeleteBlackReq req){
        return imFriendShipService.deleteBlack(req);
    }
    // 检查黑名单 checkType：1单向 2双向
    @GetMapping("/checkBlack")
    public ResponseVO checkBlack(@RequestBody @Validated CheckFriendShipReq req){
        return imFriendShipService.checkBlack(req);
    }
    // 增量拉取好友关系信息
    @RequestMapping("/syncFriendshipList")
    public ResponseVO syncFriendshipList(@RequestBody @Validated SyncReq req, Integer appId){
        req.setAppId(appId);
        return imFriendShipService.syncFriendshipList(req);
    }


}
