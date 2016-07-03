package org.mao.cloud.MaoCloud.Foundation.base;

import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by mao on 2016/7/3.
 */
public class NodeLink {

    //private int id;
    private String name;
    private InetSocketAddress address;


    //--------------- hang up -----------
    private SocketChannel channel;

}
