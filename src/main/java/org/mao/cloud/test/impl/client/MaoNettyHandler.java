package org.mao.cloud.test.impl.client;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import org.mao.cloud.MaoCloud.Network.netty.api.MaoProtocolEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by mao on 2016/7/2.
 */
public class MaoNettyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        MaoProtocolEvent event = new MaoProtocolEvent();
        event.type = MaoProtocolEvent.Type.DATA;
        event.dataOrCmd = "qingdao 103";

        ctx.writeAndFlush(event);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        MaoCloudProtocol maoP = (MaoCloudProtocol) msg;

        MaoProtocolEvent event = new MaoProtocolEvent();
        event.type = maoP.getCMD()? MaoProtocolEvent.Type.CMD : MaoProtocolEvent.Type.DATA;
        event.dataOrCmd = maoP.getDataOrCmd() + "beijing 1185";

        System.out.println(event.type + "\n" + maoP.getDataOrCmd());

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ctx.writeAndFlush(event);
    }
}
