package com.itsyx.im.service.group.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author: syx
 * @description:
 **/
@Data
public class GetGroupReq extends RequestBase {

    private String groupId;

}
