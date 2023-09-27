package com.itsyx.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author: syx
 * @description: 已读好友申请通知报文
 **/
@Data
public class ReadAllFriendRequestPack {

    private String fromId;

    private Long sequence;
}
