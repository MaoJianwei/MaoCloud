package com.maojianwei.MaoCloud.MaoCloud.api.communicate;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType;

/**
 * Created by mao on 2016/11/6.
 */
public abstract class MaoCloudData<ST> {

    // Main Type
    public abstract MaoCloudDataType type();

    private ST subType;
    public ST getSubType(){
        return subType;
    }


    private int dataLen = 0;
    public int getDataLen() {
        return dataLen;
    }

    protected final ByteBuf data = PooledByteBufAllocator.DEFAULT.heapBuffer();
    public ByteBuf getDataBuf() {
        return data;
    }
}
