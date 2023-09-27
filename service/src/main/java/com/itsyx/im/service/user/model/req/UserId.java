package com.itsyx.im.service.user.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author: Chackylee
 * @description:
 **/
@Data
public class UserId extends RequestBase {

    @NotEmpty(message = "用户id不能为空")
    private String userId;

}
