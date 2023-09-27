package com.itsyx.im.service.user.model.req;

import com.itsyx.im.common.model.RequestBase;
import lombok.Data;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Data
public class SetUserCustomerStatusReq extends RequestBase {

    private String userId;

    private String customText;

    private Integer customStatus;

}
