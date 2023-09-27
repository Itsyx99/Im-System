package com.itsyx.im.service.friendship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itsyx.im.codec.pack.friendship.AddFriendGroupMemberPack;
import com.itsyx.im.codec.pack.friendship.DeleteFriendGroupMemberPack;
import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.enums.command.FriendshipEventCommand;
import com.itsyx.im.common.model.ClientInfo;
import com.itsyx.im.service.friendship.model.ImFriendShipGroupEntity;
import com.itsyx.im.service.friendship.model.ImFriendShipGroupMemberEntity;
import com.itsyx.im.service.friendship.dao.mapper.ImFriendShipGroupMemberMapper;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupMemberService;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupService;
import com.itsyx.im.service.user.model.ImUserDataEntity;
import com.itsyx.im.service.user.service.ImUserService;
import com.itsyx.im.service.utils.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: syx
 * @description:
 **/
@Service
public class ImFriendShipGroupMemberServiceImpl implements ImFriendShipGroupMemberService {

    @Autowired
    ImFriendShipGroupMemberMapper imFriendShipGroupMemberMapper;

    @Autowired
    @Lazy
    ImFriendShipGroupService imFriendShipGroupService;

    @Autowired
    ImUserService imUserService;

    @Autowired
    MessageProducer messageProducer;


    @Override
    @Transactional
    public ResponseVO addGroupMember(AddFriendShipGroupMemberReq req) {

        ResponseVO<ImFriendShipGroupEntity> group = imFriendShipGroupService.getGroup(req.getFromId(),req.getGroupName(),req.getAppId());
        if(!group.isOk()){
            return group;
        }

        List<String> successId = new ArrayList<>();
        for (String toId : req.getToIds()) {
            ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(toId, req.getAppId());
            if(singleUserInfo.isOk()){
                int i = this.doAddGroupMember(group.getData().getGroupId(), toId);
                if(i == 1){
                    successId.add(toId);
                }
            }
        }

        // TCP通知
        AddFriendGroupMemberPack pack = new AddFriendGroupMemberPack();
        pack.setFromId(req.getFromId());
        pack.setGroupName(req.getGroupName());
        pack.setToIds(successId);
//        pack.setSequence(seq);
        messageProducer.sendToUserExceptClient(req.getFromId(), FriendshipEventCommand.FRIEND_GROUP_MEMBER_ADD,
                pack,new ClientInfo(req.getAppId(),req.getClientType(),req.getImei()));
        return ResponseVO.successResponse(successId);
    }

    @Override
    public ResponseVO delGroupMember(DeleteFriendShipGroupMemberReq req) {
        ResponseVO<ImFriendShipGroupEntity> group = imFriendShipGroupService.getGroup(req.getFromId(),req.getGroupName(),req.getAppId());
        if(!group.isOk()){
            return group;
        }

        ArrayList list = new ArrayList();
        for (String toId : req.getToIds()) {
            ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(toId, req.getAppId());
            if(singleUserInfo.isOk()){
                int i = deleteGroupMember(group.getData().getGroupId(), toId);
                if(i == 1){
                    list.add(toId);
                }
            }
        }

        // TCP通知
        DeleteFriendGroupMemberPack pack = new DeleteFriendGroupMemberPack();
        pack.setFromId(req.getFromId());
        pack.setGroupName(req.getGroupName());
        pack.setToIds(list);
//        pack.setSequence(seq);
        messageProducer.sendToUserExceptClient(req.getFromId(), FriendshipEventCommand.FRIEND_GROUP_MEMBER_DELETE,
                pack,new ClientInfo(req.getAppId(),req.getClientType(),req.getImei()));
        return ResponseVO.successResponse(list);
    }

    @Override
    public int doAddGroupMember(Long groupId, String toId) {
        ImFriendShipGroupMemberEntity imFriendShipGroupMemberEntity = new ImFriendShipGroupMemberEntity();
        imFriendShipGroupMemberEntity.setGroupId(groupId);
        imFriendShipGroupMemberEntity.setToId(toId);
        try {
            int insert = imFriendShipGroupMemberMapper.insert(imFriendShipGroupMemberEntity);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteGroupMember(Long groupId, String toId) {
        QueryWrapper<ImFriendShipGroupMemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id",groupId);
        queryWrapper.eq("to_id",toId);

        try {
            int delete = imFriendShipGroupMemberMapper.delete(queryWrapper);
//            int insert = imFriendShipGroupMemberMapper.insert(imFriendShipGroupMemberEntity);
            return delete;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int clearGroupMember(Long groupId) {
        QueryWrapper<ImFriendShipGroupMemberEntity> query = new QueryWrapper<>();
        query.eq("group_id",groupId);
        int delete = imFriendShipGroupMemberMapper.delete(query);
        return delete;
    }
}
