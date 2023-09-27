package com.itsyx.im.service.user.model.resp;

import com.itsyx.im.common.model.UserSession;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class UserOnlineStatusResp {

    private List<UserSession> session;

    private String customText;

    private Integer customStatus;

}
