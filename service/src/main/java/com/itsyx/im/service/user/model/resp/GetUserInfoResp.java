package com.itsyx.im.service.user.model.resp;

import com.itsyx.im.service.user.model.ImUserDataEntity;
import lombok.Data;

import java.util.List;

/**
 * @author: syx
 * @description:
 **/
@Data
public class GetUserInfoResp {

    private List<ImUserDataEntity> userDataItem;

    private List<String> failUser;


}
