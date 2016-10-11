package org.mao.cloud.MaoCloud.Network.impl;

import com.google.common.net.InetAddresses;
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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.mao.cloud.util.ConstUtil.EOL;
import static org.mao.cloud.util.IpUtil.isIpv4;
import static org.mao.cloud.util.IpUtil.isIpv6;

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

        log.info("init NioEventLoopGroup...");
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        log.info("init ServerBootstrap...");

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childHandler(new NetworkChannelInitializer(this, false));

        log.info("ServerBootstrap init ok");

        try {
            log.info("ready to bind port: {}", SERVER_PORT);

            serverChannel = serverBootstrap.bind(SERVER_PORT).sync().channel();

            log.info("bind finish, channel info Open:{}, Active:{}, LocalAddress:{}",
                    serverChannel.isOpen(),
                    serverChannel.isActive(),
                    serverChannel.localAddress().toString());
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        }

        bossGroup.schedule(new ConnectTask(this), 30, TimeUnit.SECONDS);

        log.info("schedule ConnectTask over by {} delay, {} {}", 0, 30, TimeUnit.SECONDS);
    }

    public void stop() {
        try {
            log.info("closing serverChannel...");
            serverChannel.close().sync();
            log.info("closed serverChannel.");
        } catch (Throwable t) {
            t.printStackTrace();
            log.error(t.getMessage());
        } finally {
            log.info("calling shutdownGracefully of workerGroup and bossGroup.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("called shutdownGracefully of workerGroup and bossGroup.");
        }
    }

    public MaoProtocolNode getMaoProtocolNode(String address) {
        return new MaoProtocolNode(address, agent);
    }

    private class ConnectTask implements Runnable {

        private final Logger log = LoggerFactory.getLogger(getClass());
        private MaoProtocolNetworkControllerImpl controller;

        Bootstrap b = new Bootstrap();

        public ConnectTask(MaoProtocolNetworkControllerImpl controller) {
            this.controller = controller;

            log.info("init Bootstrap...");
            b.group(controller.bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000) // TODO - VERITY - ATTENTION !!!
                    .handler(new NetworkChannelInitializer(controller, true));
            log.info("Bootstrap init ok");
        }

        @Override
        public void run() {

            log.info("New ConnectTask start...");

            try {
                while (true) {
                    InetAddress nodeIp = controller.agent.getOneUnConnectedNode();
                    log.info("get a new nodeIp: {}", nodeIp);
                    if (nodeIp == null) {
                        break;
                    }
                    if (isIpv4(nodeIp)) {
                        Inet4Address ipv4 = controller.agent.getLocalIpv4();
                        if (ipv4 != null) {
                            if (!verifyActiveConnectionRule(ipv4, nodeIp)){
                                continue;
                            }
                        } else {
                            log.error("Local Ip is unavailable!(null)");
                            break;
                        }
                    } else {
                        //TODO - ipv6
                        continue;
                    }

                    log.info("connecting to {}...", nodeIp);
                    Channel ch = b.connect(nodeIp, SERVER_PORT).sync().channel();
                    log.info("client channel info Open:{}, Active:{}, RemoteAddress:{}",
                            ch.isOpen(),
                            ch.isActive(),
                            ch.remoteAddress().toString());

                    if (ch.isActive()) {
                        //TODO - CHECK - if it will be OPEN but not ACTIVE when success?

                        MPHello hello = MPFactories.getFactory(MPVersion.MP_03)
                                .buildHello()
                                .setNodeName("MaoTestB")
                                .setNodePassword("987654321")
                                .build();

                        log.info("sending MPHello, Type:{}, Version:{}, idHashValue:{}",
                                hello.getType(),
                                hello.getVersion(),
                                hello.getHashValue());
                        ch.writeAndFlush(hello);
                        log.info("sent MPHello, Type:{}, Version:{}, idHashValue:{}",
                                hello.getType(),
                                hello.getVersion(),
                                hello.getHashValue());
                    }
                }
            } catch (Exception e) {
                log.warn("Exception while connecting others: {}", e.getMessage());
            }

            bossGroup.schedule(this, 30, TimeUnit.SECONDS);
        }

        private boolean verifyActiveConnectionRule(InetAddress localIp, InetAddress remoteIp) {
            if(localIp.getClass().equals(remoteIp.getClass())){
                byte [] localIpBytes = localIp.getAddress();
                byte [] remoteIpBytes = remoteIp.getAddress();

                for(int i = 0; i < localIpBytes.length ; i++){
                    if(localIpBytes[i] < remoteIpBytes[i]){
                        log.info("Verify Pass, local: {}, remote: {}",
                                localIp.getHostAddress(), remoteIp.getHostAddress());
                        return true;
                    } else if (localIpBytes[i] > remoteIpBytes[i]) {
                        log.info("Verify Deny, local: {}, remote: {}",
                                localIp.getHostAddress(), remoteIp.getHostAddress());
                        return false;
                    }
                }
                log.error("Local Ip is equal to Remote Ip! Please troubleshoot!"+ EOL +
                        "localIp: {}, remoteIp: {}", localIp.getHostAddress(), remoteIp.getHostAddress());
            } else {
                log.error("IP type not match! localIp: {}, remoteIp: {}",
                        localIp.getClass(), remoteIp.getClass());
            }
            return false;
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
                log.info("initializing pipeline for client:{} channel ...", isRoleClient);

                ChannelPipeline p = ch.pipeline();
                p.addLast(
                        //Attention - assume that if we use LengthFieldBasedFrameDecoder, the frame is certainly unbroken.
                        //2016.09.17
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0),
                        new MaoProtocolDecoder(),
                        new MaoProtocolEncoder(),
                        new MaoProtocolDuplexHandler(controller, isRoleClient));

                log.info("initialize pipeline for client channel: {} OK!", ch);
            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
