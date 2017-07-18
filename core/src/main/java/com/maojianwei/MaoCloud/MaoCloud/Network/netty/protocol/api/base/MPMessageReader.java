package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base;

import io.netty.buffer.ByteBuf;

/**
 * Created by mao on 2016/9/17.
 */
public interface MPMessageReader<T> {
    T readFrom(ByteBuf buf) throws MPParseError;
}
