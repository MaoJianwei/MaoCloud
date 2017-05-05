package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;

/**
 * Created by mao on 2016/9/16.
 */
public interface MPFactory {

    MPMessageReader<MPMessage> getReader();


    MPHello.Builder buildHello();
    MPEchoRequest.Builder buildEchoRequest();
    MPEchoReply.Builder buildEchoReply();
    MPGoodDay.Builder buildGoodDay();
}
