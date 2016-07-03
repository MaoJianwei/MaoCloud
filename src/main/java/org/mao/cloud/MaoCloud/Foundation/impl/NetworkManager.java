package org.mao.cloud.MaoCloud.Foundation.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.MaoCloud.Foundation.base.NodeLink;
import org.mao.cloud.MaoCloud.Foundation.base.SubscriberRole;
import org.mao.cloud.MaoCloud.Foundation.intf.NetworkService;
import org.mao.cloud.MaoCloud.Foundation.intf.NetworkSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by mao on 2016/7/3.
 */
@Component(immediate = true)
@Service
public class NetworkManager implements NetworkService {



    private ConcurrentHashMap clientMap = new ConcurrentHashMap<String, NodeLink>(); //KEY: NAME
    private ConcurrentHashMap initClientMap = new ConcurrentHashMap<InetSocketAddress, SocketChannel>(); //KEY: InetSocketAddress

    private ConcurrentHashMap<SubscriberRole, AtomicReferenceArray<NetworkSubscriber>> subscriberMap = new ConcurrentHashMap();

    // -------------------------------

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;




    // -------------------------------
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static int TCP_BACKLOG_VALUE = 20;
    private static int SERVER_PORT = 6666; // TODO - modify


    @Activate
    protected void activate(){

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
          .childHandler(new NetworkChannelInitializer());

        try {
            serverChannel = sb.bind(6666).sync().channel();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        }
    }


    @Deactivate
    protected void deactive(){

        try {
            serverChannel.close().sync();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }



    public boolean subscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return false;
    }
    public boolean unsubscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return false;
    }
    public boolean sendMessage(String nodeName, Object data){
        return false;
    }




    private class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel ch) {

            try {

                ChannelPipeline p = ch.pipeline();
                p.addLast();
                //TODO

                initClientMap.put(ch.remoteAddress(), ch);

            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
