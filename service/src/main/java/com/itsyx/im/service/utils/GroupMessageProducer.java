package com.itsyx.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.itsyx.im.codec.pack.group.AddGroupMemberPack;
import com.itsyx.im.codec.pack.group.RemoveGroupMemberPack;
import com.itsyx.im.codec.pack.group.UpdateGroupMemberPack;
import com.itsyx.im.common.ClientType;
import com.itsyx.im.common.enums.command.Command;
import com.itsyx.im.common.enums.command.GroupEventCommand;
import com.itsyx.im.common.model.ClientInfo;
import com.itsyx.im.service.group.model.req.GroupMemberDto;
import com.itsyx.im.service.group.service.ImGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: sxx
 * @version: 1.0
 */
@Component
public class GroupMessageProducer {

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    ImGroupMemberService imGroupMemberService;

    public void producer(String userId, Command command, Object data, ClientInfo clientInfo){
        JSONObject o = (JSONObject) JSONObject.toJSON(data);
        String groupId = o.getString("groupId");
        List<String> groupMemberId = imGroupMemberService.getGroupMemberId(groupId, clientInfo.getAppId());

        if(command.equals(GroupEventCommand.ADDED_MEMBER)){
            //加入群成员 发送给管理员和被加入人本身
            List<GroupMemberDto> groupManager = imGroupMemberService.getGroupManager(groupId, clientInfo.getAppId());
            AddGroupMemberPack addGroupMemberPack = o.toJavaObject(AddGroupMemberPack.class);
            List<String> members = addGroupMemberPack.getMembers();
            for (GroupMemberDto groupMemberDto : groupManager) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && groupMemberDto.getMemberId().equals(userId)){
                    messageProducer.sendToUserExceptClient(groupMemberDto.getMemberId(),command,data,clientInfo);
                }else{
                    messageProducer.sendToUser(groupMemberDto.getMemberId(),command,data,clientInfo.getAppId());
                }
            }
            for (String member : members) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    messageProducer.sendToUserExceptClient(member,command,data,clientInfo);
                }else{
                    messageProducer.sendToUser(member,command,data,clientInfo.getAppId());
                }
            }
        }else if(command.equals(GroupEventCommand.DELETED_MEMBER)){
            //删除群成员 发送给所有群成员和被删除人本身
            RemoveGroupMemberPack pack = o.toJavaObject(RemoveGroupMemberPack.class);
            String member = pack.getMember();
            List<String> members = imGroupMemberService.getGroupMemberId(groupId, clientInfo.getAppId());
            members.add(member);
            for (String memberId : members) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    messageProducer.sendToUserExceptClient(memberId,command,data,clientInfo);
                }else{
                    messageProducer.sendToUser(memberId,command,data,clientInfo.getAppId());
                }
            }
        }else if(command.equals(GroupEventCommand.UPDATED_MEMBER)){
            //修改群成员 发送给管理员和人本身
            UpdateGroupMemberPack pack = o.toJavaObject(UpdateGroupMemberPack.class);
            String memberId = pack.getMemberId();
            List<GroupMemberDto> groupManager = imGroupMemberService.getGroupManager(groupId, clientInfo.getAppId());
            GroupMemberDto groupMemberDto = new GroupMemberDto();
            groupMemberDto.setMemberId(memberId);
            groupManager.add(groupMemberDto);
            for (GroupMemberDto member : groupManager) {
                if(clientInfo.getClientType() != ClientType.WEBAPI.getCode() && member.equals(userId)){
                    messageProducer.sendToUserExceptClient(member.getMemberId(),command,data,clientInfo);
                }else{
                    messageProducer.sendToUser(member.getMemberId(),command,data,clientInfo.getAppId());
                }
            }
        }else {
            for (String memberId : groupMemberId) {
                if(clientInfo.getClientType() != null && clientInfo.getClientType() != ClientType.WEBAPI.getCode() && memberId.equals(userId)){
                    messageProducer.sendToUserExceptClient(memberId,command, data,clientInfo);
                }else{
                    messageProducer.sendToUser(memberId,command,data,clientInfo.getAppId());
                }
            }
        }



    }

}
