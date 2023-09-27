package com.itsyx.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author: syx
 * @description: 审批好友申请通知报文
 **/
@Data
public class ApproverFriendRequestPack {

    private Long id;

    //1同意 2拒绝
    private Integer status;

    private Long sequence;
}
