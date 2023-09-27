package com.itsyx.im.tcp.feign;

import com.itsyx.im.common.ResponseVO;
import com.itsyx.im.common.model.message.CheckSendMessageReq;
import feign.Headers;
import feign.RequestLine;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public interface FeignMessageService {

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("POST /message/checkSend")
    public ResponseVO checkSendMessage(CheckSendMessageReq o);

}
