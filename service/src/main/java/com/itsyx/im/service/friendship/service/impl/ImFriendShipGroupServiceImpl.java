package com.itsyx.im.service.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itsyx.im.codec.pack.friendship.AddFriendGroupPack;
import com.itsyx.im.codec.pack.friendship.DeleteFriendGroupPack;
import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.enums.DelFlagEnum;
import com.itsyx.im.common.enums.FriendShipErrorCode;
import com.itsyx.im.common.enums.command.FriendshipEventCommand;
import com.itsyx.im.common.model.ClientInfo;
import com.itsyx.im.service.friendship.model.ImFriendShipGroupEntity;
import com.itsyx.im.service.friendship.dao.mapper.ImFriendShipGroupMapper;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.itsyx.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.itsyx.im.service.friendship.model.req.DeleteFriendShipGroupReq;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupMemberService;
import com.itsyx.im.service.friendship.service.ImFriendShipGroupService;
import com.itsyx.im.service.user.service.ImUserService;
import com.itsyx.im.service.utils.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImFriendShipGroupServiceImpl implements ImFriendShipGroupService {

    @Autowired
    ImFriendShipGroupMapper imFriendShipGroupMapper;

    @Autowired
    ImFriendShipGroupMemberService imFriendShipGroupMemberService;

    @Autowired
    ImUserService imUserService;

    @Autowired
    MessageProducer messageProducer;

    @Override
    @Transactional
    public ResponseVO addGroup(AddFriendShipGroupReq req) {

        QueryWrapper<ImFriendShipGroupEntity> query = new QueryWrapper<>();
        query.eq("group_name", req.getGroupName());
        query.eq("app_id", req.getAppId());
        query.eq("from_id", req.getFromId());

        ImFriendShipGroupEntity entity = imFriendShipGroupMapper.selectOne(query);

        if (entity != null && entity.getDelFlag() == DelFlagEnum.NORMAL.getCode()) {
            return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_IS_EXIST);
        } else if(entity != null && entity.getDelFlag() == DelFlagEnum.DELETE.getCode()){
            //更新删除状态
            entity.setDelFlag(DelFlagEnum.NORMAL.getCode());
            try {
                int update1 = imFriendShipGroupMapper.updateById(entity);

                if (update1 != 1) {
                    return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_CREATE_ERROR);
                }

                if (update1 == 1 && CollectionUtil.isNotEmpty(req.getToIds())) {
                    AddFriendShipGroupMemberReq addFriendShipGroupMemberReq = new AddFriendShipGroupMemberReq();
                    addFriendShipGroupMemberReq.setFromId(req.getFromId());
                    addFriendShipGroupMemberReq.setGroupName(req.getGroupName());
                    addFriendShipGroupMemberReq.setToIds(req.getToIds());
                    addFriendShipGroupMemberReq.setAppId(req.getAppId());
                    imFriendShipGroupMemberService.addGroupMember(addFriendShipGroupMemberReq);
                    return ResponseVO.successResponse();
                }
            } catch (DuplicateKeyException e) {
                e.getStackTrace();
                return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_IS_EXIST);
            }
        }

        //写入db
        ImFriendShipGroupEntity insert = new ImFriendShipGroupEntity();
        insert.setAppId(req.getAppId());
        insert.setCreateTime(System.currentTimeMillis());
        insert.setDelFlag(DelFlagEnum.NORMAL.getCode());
        insert.setGroupName(req.getGroupName());
        insert.setFromId(req.getFromId());
        try {
            int insert1 = imFriendShipGroupMapper.insert(insert);

            if (insert1 != 1) {
                return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_CREATE_ERROR);
            }

            if (insert1 == 1 && CollectionUtil.isNotEmpty(req.getToIds())) {
                AddFriendShipGroupMemberReq addFriendShipGroupMemberReq = new AddFriendShipGroupMemberReq();
                addFriendShipGroupMemberReq.setFromId(req.getFromId());
                addFriendShipGroupMemberReq.setGroupName(req.getGroupName());
                addFriendShipGroupMemberReq.setToIds(req.getToIds());
                addFriendShipGroupMemberReq.setAppId(req.getAppId());
                imFriendShipGroupMemberService.addGroupMember(addFriendShipGroupMemberReq);
                return ResponseVO.successResponse();
            }
        } catch (DuplicateKeyException e) {
            e.getStackTrace();
            return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_IS_EXIST);
        }

        // 添加 群组标签tcp通知
        AddFriendGroupPack addFriendGropPack = new AddFriendGroupPack();
        addFriendGropPack.setFromId(req.getFromId());
        addFriendGropPack.setGroupName(req.getGroupName());
//        addFriendGropPack.setSequence(seq);
        messageProducer.sendToUserExceptClient(req.getFromId(), FriendshipEventCommand.FRIEND_GROUP_ADD, addFriendGropPack,new ClientInfo(req.getAppId(),req.getClientType(),req.getImei()));
        return ResponseVO.successResponse();
    }

    @Override
    @Transactional
    public ResponseVO deleteGroup(DeleteFriendShipGroupReq req) {

        for (String groupName : req.getGroupName()) {
            QueryWrapper<ImFriendShipGroupEntity> query = new QueryWrapper<>();
            query.eq("group_name", groupName);
            query.eq("app_id", req.getAppId());
            query.eq("from_id", req.getFromId());
            query.eq("del_flag", DelFlagEnum.NORMAL.getCode());

            ImFriendShipGroupEntity entity = imFriendShipGroupMapper.selectOne(query);

            if (entity != null) {
                ImFriendShipGroupEntity update = new ImFriendShipGroupEntity();
                update.setGroupId(entity.getGroupId());
                update.setDelFlag(DelFlagEnum.DELETE.getCode());
                imFriendShipGroupMapper.updateById(update);
                imFriendShipGroupMemberService.clearGroupMember(entity.getGroupId());

                //TCP通知
                DeleteFriendGroupPack deleteFriendGroupPack = new DeleteFriendGroupPack();
                deleteFriendGroupPack.setFromId(req.getFromId());
                deleteFriendGroupPack.setGroupName(groupName);
//                deleteFriendGroupPack.setSequence(seq);
                messageProducer.sendToUserExceptClient(req.getFromId(), FriendshipEventCommand.FRIEND_GROUP_DELETE, deleteFriendGroupPack,new ClientInfo(req.getAppId(),req.getClientType(),req.getImei()));
            }
        }
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO getGroup(String fromId, String groupName, Integer appId) {
        QueryWrapper<ImFriendShipGroupEntity> query = new QueryWrapper<>();
        query.eq("group_name", groupName);
        query.eq("app_id", appId);
        query.eq("from_id", fromId);
        query.eq("del_flag", DelFlagEnum.NORMAL.getCode());

        ImFriendShipGroupEntity entity = imFriendShipGroupMapper.selectOne(query);
        if (entity == null) {
            return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_SHIP_GROUP_IS_NOT_EXIST);
        }
        return ResponseVO.successResponse(entity);
    }

}
