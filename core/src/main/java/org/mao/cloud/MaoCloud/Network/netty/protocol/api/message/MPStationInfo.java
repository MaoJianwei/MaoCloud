package org.mao.cloud.MaoCloud.Network.netty.protocol.api.message;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

/**
 * Created by mao on 2017/5/5.
 */
public interface MPStationInfo extends MPMessage {

    String getStationInfo();

    interface Reader extends MPMessage.Reader<MPStationInfo>{
    }

    Writer writer();
    interface Writer extends MPMessage.Writer{
    }


    Builder builder();
    interface Builder extends MPMessage.Builder {
//        Builder setNodeName(String name);
//        Builder setNodePassword(String password);
        MPStationInfo build();
    }
}
