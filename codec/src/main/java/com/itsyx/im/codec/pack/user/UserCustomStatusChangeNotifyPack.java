package com.itsyx.im.codec.pack.user;


import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class UserCustomStatusChangeNotifyPack {

    private String customText;

    private Integer customStatus;

    private String userId;

}
