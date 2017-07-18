package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base;

import io.netty.buffer.ByteBuf;

/**
 * Created by mao on 2016/10/6.
 */
public interface MPMessageWriter {
    void writeVersionTo(ByteBuf out);
    void writeTypeTo(ByteBuf out);

    /**
     * Must be called before invoking writeDataTo.
     *
     * @return data length without the one of CheckSum
     */
    int prepareData();
    void writeDataTo(ByteBuf out);
}
