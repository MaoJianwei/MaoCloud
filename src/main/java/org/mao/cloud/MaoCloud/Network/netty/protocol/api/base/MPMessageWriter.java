package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

import io.netty.buffer.ByteBuf;

/**
 * Created by mao on 2016/10/6.
 */
public interface MPMessageWriter<T> {
    void writeVersion(ByteBuf out);
    void writeType(ByteBuf out);

    /**
     * Must be called before invoking writeData.
     *
     * @return data length without the one of CheckSum
     */
    int prepareData();
    void writeData(ByteBuf out);
}
