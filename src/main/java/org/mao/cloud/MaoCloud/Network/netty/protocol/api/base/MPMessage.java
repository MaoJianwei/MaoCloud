package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/9/16.
 */
public interface MPMessage{

    MPVersion getVersion();
    MPMessageType getType();


    //    Reader reader();
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
