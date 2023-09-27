package com.itsyx.im.service.friendship.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class DeleteFriendReq extends RequestBase {

    @NotBlank(message = "fromId不能为空")
    private String fromId;

    private String toId;

}
