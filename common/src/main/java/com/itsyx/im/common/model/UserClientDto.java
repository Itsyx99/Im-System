package com.itsyx.im.common.model;

import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class UserClientDto {

    private Integer appId;

    private Integer clientType;

    private String userId;

    private String imei;

}
