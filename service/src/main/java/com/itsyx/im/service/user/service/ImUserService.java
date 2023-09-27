package com.itsyx.im.service.user.service;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.service.user.model.ImUserDataEntity;
import com.itsyx.im.service.user.model.req.*;
import com.itsyx.im.service.user.model.resp.GetUserInfoResp;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public interface ImUserService {

    public ResponseVO importUser(ImportUserReq req);

    public ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    public ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId , Integer appId);

    public ResponseVO deleteUser(DeleteUserReq req);

    public ResponseVO modifyUserInfo(ModifyUserInfoReq req);

    public ResponseVO login(LoginReq req);

    ResponseVO getUserSequence(GetUserSequenceReq req);
}
