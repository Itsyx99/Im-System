package com.itsyx.im.service.user.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class PullUserOnlineStatusReq extends RequestBase {

    private List<String> userList;

}
