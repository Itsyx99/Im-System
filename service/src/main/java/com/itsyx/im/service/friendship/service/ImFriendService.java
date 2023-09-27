package com.itsyx.im.service.friendship.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.model.RequestBase;
import com.itsyx.im.common.model.SyncReq;
import com.itsyx.im.service.friendship.model.req.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public interface ImFriendService {

    // 导入关系链
    public ResponseVO importFriendShip(ImporFriendShipReq req);
    // 添加单条关系链(添加好友)
    public ResponseVO addFriend(AddFriendReq req);
    // 修改关系链 来源 额外信息 备注
    public ResponseVO updateFriend(UpdateFriendReq req);
    // 删除关系链（删除好友）
    public ResponseVO deleteFriend(DeleteFriendReq req);
    // 删除关系链（删除所有好友）
    public ResponseVO deleteAllFriend(DeleteFriendReq req);
    // 获取用户所有关系链
    public ResponseVO getAllFriendShip(GetAllFriendShipReq req);
    // 获取用户之间关系链
    public ResponseVO getRelation(GetRelationReq req);
    // 添加好友
    public ResponseVO doAddFriend(RequestBase requestBase,String fromId, FriendDto dto, Integer appId);
   // 检查好友关系
    public ResponseVO checkFriendship(CheckFriendShipReq req);
    // 用户添加黑名单用户
    public ResponseVO addBlack(AddFriendShipBlackReq req);
    // 移除黑名单
    public ResponseVO deleteBlack(DeleteBlackReq req);
    // 检查黑名单
    public ResponseVO checkBlack(CheckFriendShipReq req);
    // 增量拉取好友关系信息
    ResponseVO syncFriendshipList(SyncReq req);

    List<String> getAllFriendId(String userId, Integer appId);
}
