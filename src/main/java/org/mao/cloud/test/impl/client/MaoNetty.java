//package org.mao.cloud.test.impl.client;
//
//import org.mao.cloud.MaoCloud.Network.netty.api.MaoProtocolEvent;
//import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDecoder;
//import org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolEncoder;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//
///**
// * Created by mao on 2016/7/1.
// */
//public class MaoNetty {
//
//    private static final int SERVER_PORT = 1355;
//
//
//    public static void main(String args[]) {
//
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//
//                        @Override
//                        public void initChannel(SocketChannel ch) {
//
//                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(new LengthFieldBasedFrameDecoder(65535, 8, 2, 1, 0));
//                            p.addLast(new MaoProtocolDecoder());
//                            p.addLast(new MaoProtocolEncoder());
//                            p.addLast(new MaoNettyHandler());
//                        }
//                    });
//
//            Channel ch = b
//                    .connect("127.0.0.1", SERVER_PORT)
//                    .sync()
//                    .channel();
//
//
//
//            int count = 3;
//            while((count--) > 0) {
//
//                MaoProtocolEvent event = new MaoProtocolEvent();
//                event.type = MaoProtocolEvent.Type.DATA;
//                event.dataOrCmd = "beijing 1185" + System.currentTimeMillis();
//                ch.writeAndFlush(event);
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            ch.close();
//            ch.closeFuture().sync();
//
//            System.out.println("end");
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            group.shutdownGracefully();
//        }
//    }
//}
