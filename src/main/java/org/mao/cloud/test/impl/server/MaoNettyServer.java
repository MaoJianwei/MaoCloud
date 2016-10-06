package org.mao.cloud.test.impl.server;

import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDecoder;
import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoNettyServer {

    private static final int SERVER_PORT = 1355;
    private static final ConcurrentHashMap clientMap = new ConcurrentHashMap();

    public static void main(String arg0[]){

        if(!Charset.isSupported("UTF-8")) {
            System.out.println("***** UTF-8 is not supported in this OS !!! *****");
            return;
        }


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            clientMap.put(ch.remoteAddress().getAddress().getHostAddress(), ch);

                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LengthFieldBasedFrameDecoder(65535, 8, 2, 1, 0));
                            p.addLast(new MaoProtocolDecoder());
                            p.addLast(new MaoProtocolEncoder());
                            p.addLast(new MaoNettyServerHandler(clientMap));

                        }
                    });

            sb
                    .bind(SERVER_PORT)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
