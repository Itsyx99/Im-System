package com.itsyx.im.codec.pack.friendship;

import lombok.Data;

/**
 * @author: syx
 * @description: 用户添加黑名单以后tcp通知数据包
 **/
@Data
public class AddFriendBlackPack {
    private String fromId;

    private String toId;

    private Long sequence;
}
