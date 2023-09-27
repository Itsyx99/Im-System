package com.itsyx.im.service.user.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.user.model.UserStatusChangeNotifyContent;
import com.itsyx.im.service.user.model.req.PullFriendOnlineStatusReq;
import com.itsyx.im.service.user.model.req.PullUserOnlineStatusReq;
import com.itsyx.im.service.user.model.req.SetUserCustomerStatusReq;
import com.itsyx.im.service.user.model.req.SubscribeUserOnlineStatusReq;
import com.itsyx.im.service.user.model.resp.UserOnlineStatusResp;

import java.util.Map;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public interface ImUserStatusService {

    public void processUserOnlineStatusNotify(UserStatusChangeNotifyContent content);

    void subscribeUserOnlineStatus(SubscribeUserOnlineStatusReq req);
    // 设置用户自定义状态接口
    void setUserCustomerStatus(SetUserCustomerStatusReq req);

    Map<String, UserOnlineStatusResp> queryFriendOnlineStatus(PullFriendOnlineStatusReq req);

    Map<String, UserOnlineStatusResp> queryUserOnlineStatus(PullUserOnlineStatusReq req);
}
