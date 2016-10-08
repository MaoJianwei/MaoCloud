package org.mao.cloud.MaoCloud.Network.netty.protocol.api.message;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

/**
 * Created by mao on 2016/9/18.
 */
public interface MPEchoReply extends MPMessage{

    //    Reader reader();
    interface Reader extends MPMessage.Reader<MPEchoReply>{
    }

    Writer writer();
    interface Writer extends MPMessage.Writer{
    }


    Builder builder();
    interface Builder extends MPMessage.Builder {
        MPEchoReply build();
    }
}
