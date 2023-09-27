package com.itsyx.im.service.group.model.resp;

import com.itsyx.im.service.group.dao.ImGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author: syx
 * @description:
 **/
@Data
public class GetJoinedGroupResp {

    private Integer totalCount;

    private List<ImGroupEntity> groupList;

}
