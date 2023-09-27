package com.itsyx.im.service.message.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: syx
 * @description:
 **/
@Data
@TableName("im_message_body")
public class ImMessageBodyEntity {

    private Integer appId;

    /** messageBodyId*/
    private Long messageKey;

    /** messageBody*/
    private String messageBody;

    private String securityKey; // 加密

    private Long messageTime;

    private Long createTime;

    private String extra;

    private Integer delFlag;

}
