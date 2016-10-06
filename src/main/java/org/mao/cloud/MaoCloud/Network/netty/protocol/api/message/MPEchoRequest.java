package org.mao.cloud.MaoCloud.Network.netty.protocol.api.message;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

/**
 * Created by mao on 2016/9/17.
 */
public interface MPEchoRequest extends MPMessage {

    //    Reader reader();
    interface Reader extends MPMessage.Reader<MPEchoRequest>{
    }

    Writer writer();
    interface Writer extends MPMessage.Writer{
    }


    Builder builder();
    interface Builder extends MPMessage.Builder {
        MPEchoRequest build();
    }
}
