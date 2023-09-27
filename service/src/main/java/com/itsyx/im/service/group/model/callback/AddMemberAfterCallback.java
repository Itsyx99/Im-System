package com.itsyx.im.service.group.model.callback;

import com.itsyx.im.service.group.model.resp.AddMemberResp;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class AddMemberAfterCallback {
    private String groupId;
    private Integer groupType;
    private String operater;
    private List<AddMemberResp> memberId;
}
