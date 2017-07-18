package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;

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
