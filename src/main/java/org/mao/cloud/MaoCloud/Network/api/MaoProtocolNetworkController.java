package org.mao.cloud.MaoCloud.Network.api;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;

/**
 * Created by mao on 2016/10/5.
 */
public interface MaoProtocolNetworkController {

    void start();
    void stop();
    MaoProtocolNode getMaoProtocolNode(String address);

}
