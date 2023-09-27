package com.itsyx.im.codec.pack.user;


import com.itsyx.im.common.model.UserSession;
import lombok.Data;

import java.util.List;


/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class UserStatusChangeNotifyPack {

    private Integer appId;

    private String userId;

    private Integer status;

    private List<UserSession> client;

}
