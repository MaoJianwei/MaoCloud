package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;

/**
 * Created by mao on 2016/9/16.
 */
public interface MPFactory {

    MPMessageReader<MPMessage> getReader();



    MPHello.Builder buildHello();
}
