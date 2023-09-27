package com.itsyx.im.tcp.reciver.process;

import com.itsyx.im.codec.proto.MessagePack;
import com.itsyx.im.tcp.util.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public abstract class BaseProcess {

    public abstract void processBefore();

    public void process(MessagePack messagePack){
        processBefore();
        NioSocketChannel channel = SessionSocketHolder.get(messagePack.getAppId(), messagePack.getToId(), messagePack.getClientType(), messagePack.getImei());
        if(channel != null){
            channel.writeAndFlush(messagePack);
        }
        processAfter();
    }

    public abstract void processAfter();

}
