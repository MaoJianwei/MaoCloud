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
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.handler.*;
import org.mao.cloud.MaoCloud.Network.netty.protocol.MPFactories;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.mao.cloud.util.ConstUtil.EOL;
import static org.mao.cloud.util.IpUtil.inetToStr;
import static org.mao.cloud.util.IpUtil.isIpv4;
import static org.mao.cloud.util.IpUtil.strToInet;

/**
 * Created by mao on 2016/10/5.
 */
public class MaoProtocolNetworkControllerImpl implements MaoProtocolNetworkController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static int TCP_BACKLOG_VALUE = 20;
    private static int SERVER_PORT = 6666; // TODO - modify
    private static int CLIENT_SCHEDULE_DELAY = 3; // seconds
    private static int CLIENT_TIMEOUT_DELAY = 1000; // seconds


    private MaoProtocolAgent agent;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
//    private EventLoopGroup clientGroup;



    public MaoProtocolNetworkControllerImpl(MaoProtocolAgent agent) {
        this.agent = agent;
    }

    public void start(Set<String> unconnectedNodes) {
        // ----- init Netty Network -----

        log.info("init NioEventLoopGroup...");
        bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("MaoCloud-MP-boss"));
        workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("MaoCloud-MP-worker"));
//        clientGroup = new NioEventLoopGroup();


        for (String node : unconnectedNodes) {
            bossGroup.schedule(new ConnectTask(strToInet(node), this),
                    CLIENT_SCHEDULE_DELAY, TimeUnit.SECONDS);
        }
        log.info("schedule ConnectTask(s) over");


        log.info("init ServerBootstrap...");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, TCP_BACKLOG_VALUE)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .option(ChannelOption.SO_KEEPALIVE, false) // TODO - VERITY - ATTENTION !!!
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                .childHandler(new NetworkChannelInitializer(this, false, null));

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

        log.info("Start!");
    }

    public void stop() {
        // TODO: 2016/10/20 close all channels first
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
        log.info("Stop!");
    }

    @Override
    public void clientReportNodeDown(InetAddress nodeAddr){
        // CLIENT_SCHEDULE_DELAY is necessary!
        // for waiting server's agent to finish its work.
        bossGroup.schedule(new ConnectTask(nodeAddr, this),
                CLIENT_SCHEDULE_DELAY, TimeUnit.SECONDS);
    }

    @Override
    public MaoProtocolNode getMaoProtocolNode(Channel channel) {
        return new MaoProtocolNode(channel, agent);
    }

    @Override
    public MPFactory getMapProtocolFactory03() {
        return MPFactories.getFactory(MPVersion.MP_03);
    }



    private class ConnectTask implements Runnable {

        private final Logger log = LoggerFactory.getLogger(getClass());

        MaoProtocolNetworkController controller;
        Bootstrap b;
        InetAddress nodeAddr;

        public ConnectTask(InetAddress nodeAddr, MaoProtocolNetworkController controller) {

            this.nodeAddr = nodeAddr;
            this.controller = controller;

            log.info("init Bootstrap...");
            b = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // TODO - CHECK
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CLIENT_TIMEOUT_DELAY)
                    .option(ChannelOption.SO_KEEPALIVE, false) // TODO - VERITY - ATTENTION !!!
                    .handler(new NetworkChannelInitializer(controller, true, nodeAddr));
            log.info("Bootstrap init ok");
        }

        @Override
        public void run() {
            log.info("New ConnectTask start...");

            if(!verifyConnectPrerequisite()){
                return;
            }

            log.info("connecting to {}...", inetToStr(nodeAddr));
            b.connect(nodeAddr, SERVER_PORT).addListener(new CheckConnectResult(nodeAddr, controller));
            log.info("CheckConnectResult for {} is added.", inetToStr(nodeAddr));
        }

        private boolean verifyConnectPrerequisite(){
            if (isIpv4(nodeAddr)) {
                Inet4Address ipv4 = agent.getLocalIpv4();
                if (ipv4 != null) {
                    if (!verifyActiveConnectionRule(ipv4, nodeAddr)) {
                        return false;
                    }
                } else {
                    log.error("Local Ip is unavailable!(null)");
                    return false; // TODO - re-get local Ip in MaoProtocolController
                }
            } else {
                //TODO - ipv6
                log.warn("Node's IP is IPv6, ignore this node! {}", inetToStr(nodeAddr));
                return false;
            }
            return true;
        }

        private boolean verifyActiveConnectionRule(InetAddress localIp, InetAddress remoteIp) {
            if (localIp.getClass().equals(remoteIp.getClass())) {
                byte[] localIpBytes = localIp.getAddress();
                byte[] remoteIpBytes = remoteIp.getAddress();

                for (int i = 0; i < localIpBytes.length; i++) {
                    if ((localIpBytes[i] & 0xFF) < (remoteIpBytes[i] & 0xFF)) {
                        log.info("Verify Pass, local: {}, remote: {}",
                                localIp.getHostAddress(), remoteIp.getHostAddress());
                        return true;
                    } else if ((localIpBytes[i] & 0xFF) > (remoteIpBytes[i] & 0xFF)) {
                        log.info("Verify Deny, local: {}, remote: {}",
                                localIp.getHostAddress(), remoteIp.getHostAddress());
                        return false;
                    }
                }
                log.error("Local Ip is equal to Remote Ip! Please troubleshoot!" + EOL +
                        "localIp: {}, remoteIp: {}", localIp.getHostAddress(), remoteIp.getHostAddress());
            } else {
                log.error("IP type not match! localIp: {}, remoteIp: {}",
                        localIp.getClass(), remoteIp.getClass());
            }
            return false;
        }
    }

    private class CheckConnectResult implements ChannelFutureListener {

        private final Logger log = LoggerFactory.getLogger(getClass());
        MaoProtocolNetworkController controller;
        InetAddress nodeAddr;

        public CheckConnectResult(InetAddress nodeAddr, MaoProtocolNetworkController controller){
            this.nodeAddr = nodeAddr;
            this.controller = controller;
        }

        @Override
        public void operationComplete(ChannelFuture future) {
            if (!future.isSuccess()) {
                log.warn("Exception while connecting {}, will re-connect in {} seconds",
                        future.cause().getMessage(), CLIENT_SCHEDULE_DELAY);
                bossGroup.schedule(new ConnectTask(nodeAddr, controller),
                        CLIENT_SCHEDULE_DELAY, TimeUnit.SECONDS);
            }
        }
    }

    private class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

        private MaoProtocolNetworkController controller;
        private boolean isRoleClient;
        private InetAddress nodeAddr;

        public NetworkChannelInitializer(MaoProtocolNetworkController controller, boolean isRoleClient, InetAddress nodeAddr) {
            this.controller = controller;
            this.isRoleClient = isRoleClient;
            this.nodeAddr = nodeAddr;
        }

        @Override
        public void initChannel(SocketChannel ch) {
            try {
                log.info("initializing pipeline for client:{} channel, {} ...", isRoleClient, nodeAddr);

                ChannelPipeline p = ch.pipeline();
                p.addLast(
                        //Attention - assume that if we use LengthFieldBasedFrameDecoder, the frame is certainly unbroken.
                        //2016.09.17
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0),
                        new MaoProtocolDecoder(),
                        new MaoProtocolEncoder(),
                        // R & W can be same, but should not be same because of multiple Echo Request msg
                        new IdleStateHandler(5, 8, 0, TimeUnit.SECONDS),
                        new ReadTimeoutHandler(10, TimeUnit.SECONDS),
                        new MaoProtocolDuplexHandler(controller, isRoleClient));

                log.info("initialize pipeline for client: {} channel, {} OK!", isRoleClient, nodeAddr);
            } catch (Throwable t) {
                t.printStackTrace();
                log.error(t.getMessage());
            }
        }
    }
}
