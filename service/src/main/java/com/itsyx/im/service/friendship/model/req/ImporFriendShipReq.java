package com.itsyx.im.service.friendship.model.req;

import com.itsyx.im.common.enums.FriendShipStatusEnum;
import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class ImporFriendShipReq extends RequestBase {

    @NotBlank(message = "fromId不能为空")
    private String fromId;

    private List<ImportFriendDto> friendItem;

    @Data
    public static class ImportFriendDto{

        private String toId;

        private String remark;

        private String addSource;

        private Integer status = FriendShipStatusEnum.FRIEND_STATUS_NO_FRIEND.getCode();

        private Integer black = FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode();
    }

}
