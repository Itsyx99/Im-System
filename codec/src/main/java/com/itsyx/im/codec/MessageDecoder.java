package com.itsyx.im.codec;


import com.itsyx.im.codec.proto.Message;
import com.itsyx.im.codec.utils.ByteBufToMessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @description: 消息解码类
 * @author: syx
 * @version: 1.0
 */
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
         //请求头（
        // 指令
        // 版本
        // clientType
        // 消息解析类型
        // appId
        // imei长度
        // bodylen
        // ）+ imei号 + 请求体

        if(in.readableBytes() < 28){
            return;
        }

        Message message = ByteBufToMessageUtils.transition(in);
        if(message == null){
            return;
        }

        out.add(message);
    }
}
