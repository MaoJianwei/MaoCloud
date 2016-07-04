package org.mao.cloud.test.impl.server;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import org.mao.cloud.MaoCloud.Network.netty.api.MaoProtocolEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoNettyServerHandler extends ChannelInboundHandlerAdapter {

    ConcurrentHashMap clientMap;

    public MaoNettyServerHandler(ConcurrentHashMap clientMap){
        this.clientMap = clientMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MaoCloudProtocol maoP = (MaoCloudProtocol) msg;

        MaoProtocolEvent event = new MaoProtocolEvent();
        event.type = maoP.getCMD()? MaoProtocolEvent.Type.CMD : MaoProtocolEvent.Type.DATA;
        event.dataOrCmd = maoP.getDataOrCmd();

        System.out.println(event.type + "\n" + event.dataOrCmd);

        ctx.writeAndFlush(event);
    }
}
