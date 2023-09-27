package com.itsyx.im.service.friendship.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: syx
 * @description: 删除分组，同时删除分组下的成员
 **/
@Data
public class DeleteFriendShipGroupReq extends RequestBase {

    @NotBlank(message = "fromId不能为空")
    private String fromId;

    @NotEmpty(message = "分组名称不能为空")
    private List<String> groupName;

}
