package com.itsyx.im.service.message.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.config.AppConfig;
import com.itsyx.im.common.enums.*;
import com.itsyx.im.service.friendship.dao.ImFriendShipEntity;
import com.itsyx.im.service.friendship.model.req.GetRelationReq;
import com.itsyx.im.service.friendship.service.ImFriendService;
import com.itsyx.im.service.group.dao.ImGroupEntity;
import com.itsyx.im.service.group.model.resp.GetRoleInGroupResp;
import com.itsyx.im.service.group.service.ImGroupMemberService;
import com.itsyx.im.service.group.service.ImGroupService;
import com.itsyx.im.service.user.model.ImUserDataEntity;
import com.itsyx.im.service.user.service.ImUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Service
public class CheckSendMessageService {

    @Autowired
    ImUserService imUserService;

    @Autowired
    ImFriendService imFriendService;

    @Autowired
    ImGroupService imGroupService;

    @Autowired
    ImGroupMemberService imGroupMemberService;

    @Autowired
    AppConfig appConfig;


    // 查询用户是否被禁用或者被禁言
    public ResponseVO checkSenderForvidAndMute(String fromId,Integer appId){

        ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(fromId, appId);
        if(!singleUserInfo.isOk()){
            return singleUserInfo;
        }

        ImUserDataEntity user = singleUserInfo.getData();
        if(user.getForbiddenFlag() == UserForbiddenFlagEnum.FORBIBBEN.getCode()){
            return ResponseVO.errorResponse(MessageErrorCode.FROMER_IS_FORBIBBEN);
        }else if (user.getSilentFlag() == UserSilentFlagEnum.MUTE.getCode()){
            return ResponseVO.errorResponse(MessageErrorCode.FROMER_IS_MUTE);
        }

        return ResponseVO.successResponse();
    }
    // 校验好友关系
    public ResponseVO checkFriendShip(String fromId,String toId,Integer appId){

        if(appConfig.isSendMessageCheckFriend()){   //发送消息是否校验关系链
            GetRelationReq fromReq = new GetRelationReq();
            fromReq.setFromId(fromId);
            fromReq.setToId(toId);
            fromReq.setAppId(appId);
            ResponseVO<ImFriendShipEntity> fromRelation = imFriendService.getRelation(fromReq); // 获取用户之间关系链
            if(!fromRelation.isOk()){
                return fromRelation;
            }

            GetRelationReq toReq = new GetRelationReq();
            toReq.setFromId(toId);
            toReq.setToId(fromId);
            toReq.setAppId(appId);
            ResponseVO<ImFriendShipEntity> toRelation = imFriendService.getRelation(toReq);
            if(!toRelation.isOk()){
                return toRelation;
            }

            if(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode() != fromRelation.getData().getStatus()){  // 将对方删除
                return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_IS_DELETED);
            }

            if(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode() != toRelation.getData().getStatus()){  //对方将自己删除
                return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_IS_DELETED);
            }

            if(appConfig.isSendMessageCheckBlack()){   // 校验黑名单
                if(FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode() != fromRelation.getData().getBlack()){
                    return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_IS_BLACK);
                }

                if(FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode() != toRelation.getData().getBlack()){
                    return ResponseVO.errorResponse(FriendShipErrorCode.TARGET_IS_BLACK_YOU);
                }
            }
        }

        return ResponseVO.successResponse();
    }

    // 发送群消息前置校验
    public ResponseVO checkGroupMessage(String fromId,String groupId,Integer appId){

        ResponseVO responseVO = checkSenderForvidAndMute(fromId, appId);
        if(!responseVO.isOk()){
            return responseVO;
        }

        //判断群逻辑
        ResponseVO<ImGroupEntity> group = imGroupService.getGroup(groupId, appId);
        if(!group.isOk()){
            return group;
        }

        //判断群成员是否在群内
        ResponseVO<GetRoleInGroupResp> roleInGroupOne = imGroupMemberService.getRoleInGroupOne(groupId, fromId, appId);
        if(!roleInGroupOne.isOk()){
            return roleInGroupOne;
        }
        GetRoleInGroupResp data = roleInGroupOne.getData();

        //判断群是否被禁言
        //如果禁言 只有群管理和群主可以发言
        ImGroupEntity groupData = group.getData();
        if(groupData.getMute() == GroupMuteTypeEnum.MUTE.getCode()
         && (data.getRole() != GroupMemberRoleEnum.MAMAGER.getCode() ||
                data.getRole() != GroupMemberRoleEnum.OWNER.getCode() )){
            return ResponseVO.errorResponse(GroupErrorCode.THIS_GROUP_IS_MUTE);
        }
        // 判断个人是否禁言
        if(data.getSpeakDate() != null && data.getSpeakDate() > System.currentTimeMillis()){
            return ResponseVO.errorResponse(GroupErrorCode.GROUP_MEMBER_IS_SPEAK);
        }

        return ResponseVO.successResponse();
    }


}
