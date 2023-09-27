package com.itsyx.im.service.group.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: syx
 * @description:
 **/
@Data
public class DestroyGroupReq extends RequestBase {

    @NotNull(message = "群id不能为空")
    private String groupId;

}
