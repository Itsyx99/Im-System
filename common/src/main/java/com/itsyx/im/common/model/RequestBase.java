package com.itsyx.im.common.model;

import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class RequestBase {
    private Integer appId;

    private String operater;

    private Integer clientType;

    private String imei;
}
