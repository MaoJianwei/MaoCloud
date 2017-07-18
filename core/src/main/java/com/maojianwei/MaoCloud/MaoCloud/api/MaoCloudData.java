package com.maojianwei.MaoCloud.MaoCloud.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import com.maojianwei.MaoCloud.MaoCloud.base.MaoCloudDataType;

/**
 * Created by mao on 2016/11/6.
 */
public abstract class MaoCloudData {

    private final Byte subType = 0;

    protected final ByteBuf data = PooledByteBufAllocator.DEFAULT.heapBuffer();

    public abstract MaoCloudDataType type();

    public Byte getSubType(){
        return subType;
    }

    public ByteBuf getDataBuf() {
        return data;
    }
}
