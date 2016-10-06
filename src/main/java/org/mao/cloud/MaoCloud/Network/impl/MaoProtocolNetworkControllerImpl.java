package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.handler.*;
import org.mao.cloud.MaoCloud.Network.netty.protocol.MPFactories;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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


    public MaoProtocolNetworkControllerImpl(MaoProtocolAgent agent) {
        this.agent = agent;
    }

    public void start() {
        // ----- init Netty Network -----
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childHandler(new NetworkChannelInitializer(this, false));

        try {
            serverChannel = serverBootstrap.bind(SERVER_PORT).sync().channel();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        }



        bossGroup.scheduleAtFixedRate(new ConnectTask(this), 0, 30, TimeUnit.SECONDS);
    }

    public void stop() {
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

    public MaoProtocolNode getMaoProtocolNode(String address) {
        return new MaoProtocolNode(address, agent);
    }

    private class ConnectTask implements Runnable {

        private final Logger log = LoggerFactory.getLogger(getClass());
        private MaoProtocolNetworkControllerImpl controller;

        public ConnectTask(MaoProtocolNetworkControllerImpl controller) {
            this.controller = controller;
        }

        @Override
        public void run() {

            try {
                Bootstrap b = new Bootstrap();
                b.group(controller.bossGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                        .handler(new NetworkChannelInitializer(controller, true));


                while (true) {
                    String nodeIp = controller.agent.getOneUnConnectedNode();
                    if(nodeIp == null){
                        break;
                    }

                    Channel ch = b.connect(nodeIp, SERVER_PORT).sync().channel();
                    if (ch.isActive()) {
                        //TODO - CHECK - if it will be OPEN but not ACTIVE when success?

                        MPHello hello = MPFactories.getFactory(MPVersion.MP_03)
                                .buildHello()
                                .setNodeName("MaoTestB")
                                .setNodePassword("987654321")
                                .build();
                        ch.writeAndFlush(hello);
                    }
                }
            } catch (Exception e) {
                log.warn("Exception while connecting others: {}", e.getMessage());
            }
        }
    }


    private class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

        private MaoProtocolNetworkControllerImpl controller;
        private boolean isRoleClient;

        public NetworkChannelInitializer(MaoProtocolNetworkControllerImpl controller, boolean isRoleClient) {
            this.controller = controller;
            this.isRoleClient = isRoleClient;
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
                        new MaoProtocolDuplexHandler(controller, isRoleClient));

            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
