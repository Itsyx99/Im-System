package com.itsyx.im.service.user.model.req;

import com.itsyx.im.common.model.RequestBase;
import com.itsyx.im.service.user.model.ImUserDataEntity;
import lombok.Data;

import java.util.List;


@Data
public class ImportUserReq extends RequestBase {

    private List<ImUserDataEntity> userData;


}
