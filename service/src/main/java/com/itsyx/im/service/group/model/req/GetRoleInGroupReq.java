package com.itsyx.im.service.group.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author: syx
 * @description:
 **/
@Data
public class GetRoleInGroupReq extends RequestBase {

    private String groupId;

    private List<String> memberId;
}
