package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.internal.ConcurrentSet;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.MaoCloud.Network.base.NodeLink;
import org.mao.cloud.MaoCloud.Network.base.SubscriberRole;
import org.mao.cloud.MaoCloud.Network.intf.NetworkService;
import org.mao.cloud.MaoCloud.Network.intf.NetworkSubscriber;
import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDecoder;
import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolEncoder;
import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolInboundHandler;
import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolOutboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mao on 2016/7/3.
 */
@Component(immediate = true)
@Service
public class NetworkManager implements NetworkService {



    private ConcurrentHashMap clientMap = new ConcurrentHashMap<String, NodeLink>(); //KEY: NAME
    private ConcurrentHashMap initClientMap = new ConcurrentHashMap<InetSocketAddress, SocketChannel>(); //KEY: InetSocketAddress

    private ConcurrentHashMap<SubscriberRole, ConcurrentSet<NetworkSubscriber>> subscriberMap = new ConcurrentHashMap();

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

        subscriberMap.put(SubscriberRole.Monitor, new ConcurrentSet<NetworkSubscriber>());
        subscriberMap.put(SubscriberRole.Cluster, new ConcurrentSet<NetworkSubscriber>());
        subscriberMap.put(SubscriberRole.Service, new ConcurrentSet<NetworkSubscriber>());



        // ----- init Netty Network -----
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
          .childHandler(new NetworkChannelInitializer());

        try {
            serverChannel = sb.bind(SERVER_PORT).sync().channel();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        }
    }


    @Deactivate
    protected void deactivate(){

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


    /**
     *
     * @param role
     * @param subscriber
     * @return {@code false} if the subscriber already exists in the set
     */
    public boolean subscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return subscriberMap.get(role).add(subscriber);
    }
    public boolean unsubscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return subscriberMap.get(role).remove(subscriber);
    }
    public boolean sendMessage(String nodeName, Object data){
        return false;
    }




    private class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel ch) {

            try {

                ChannelPipeline p = ch.pipeline();
                p.addLast(
                        //Attention - assume that if we use LengthFieldBasedFrameDecoder, the frame is certainly unbroken.
                        //2016.09.17
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0),
                        new MaoProtocolDecoder(),
                        new MaoProtocolInboundHandler(),
                        new MaoProtocolEncoder(),
                        new MaoProtocolOutboundHandler());



                initClientMap.put(ch.remoteAddress(), ch);

            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
