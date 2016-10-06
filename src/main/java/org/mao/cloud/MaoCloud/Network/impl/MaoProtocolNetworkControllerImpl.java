package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mao on 2016/10/5.
 */
public class MaoProtocolNetworkControllerImpl implements MaoProtocolNetworkController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static int TCP_BACKLOG_VALUE = 20;
    private static int SERVER_PORT = 6666; // TODO - modify


    private MaoProtocolAgent agent;


    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;


    public MaoProtocolNetworkControllerImpl(MaoProtocolAgent agent){
        this.agent = agent;
    }

    public void start(){
        // ----- init Netty Network -----
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childHandler(new NetworkChannelInitializer(this))
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // TODO - CHECK

        try {
            serverChannel = serverBootstrap.bind(SERVER_PORT).sync().channel();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        }
    }

    public void stop(){
        try {
            serverChannel.close().sync();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public MaoProtocolNode getMaoProtocolNode(String address){
        return new MaoProtocolNode(address, agent);
    }


    private class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

        private MaoProtocolNetworkControllerImpl controller;

        public  NetworkChannelInitializer(MaoProtocolNetworkControllerImpl controller){
            this.controller = controller;
        }

        @Override
        public void initChannel(SocketChannel ch) {
            try {
                ChannelPipeline p = ch.pipeline();
                p.addLast(
                        //Attention - assume that if we use LengthFieldBasedFrameDecoder, the frame is certainly unbroken.
                        //2016.09.17
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0),
                        new MaoProtocolDecoder(),
                        new MaoProtocolEncoder(),
                        new MaoProtocolDuplexHandler(controller));

            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
