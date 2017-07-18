package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/9/16.
 */
public interface MPMessage{

    MPVersion getVersion();
    MPMessageType getType();

    /**
     * Deals from the DataLength segment of protocol.
     *
     * @param <T>
     */
    interface Reader<T> extends MPMessageReader<T>{
    }

    Writer writer();
    interface Writer extends MPMessageWriter{
    }

    Builder builder();
    interface Builder{
        MPMessage build();
    }
}
