package com.itsyx.im.service.friendship.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsyx.im.service.friendship.model.ImFriendShipEntity;
import com.itsyx.im.service.friendship.model.req.CheckFriendShipReq;
import com.itsyx.im.service.friendship.model.resp.CheckFriendShipResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImFriendShipMapper extends BaseMapper<ImFriendShipEntity> {

    public List<CheckFriendShipResp> checkFriendShip(CheckFriendShipReq req);


    List<CheckFriendShipResp> checkFriendShipBoth(CheckFriendShipReq toId);


    List<CheckFriendShipResp> checkFriendShipBlack(CheckFriendShipReq req);


    List<CheckFriendShipResp> checkFriendShipBlackBoth(CheckFriendShipReq toId);

    @Select(" select max(friend_sequence) from im_friendship where app_id = #{appId} AND from_id = #{userId} ")
    Long getFriendShipMaxSeq(Integer appId,String userId);

    @Select(
            " select to_id from im_friendship where from_id = #{userId} AND app_id = #{appId} and status = 1 and black = 1 "
    )
    List<String> getAllFriendId(String userId,Integer appId);
}
