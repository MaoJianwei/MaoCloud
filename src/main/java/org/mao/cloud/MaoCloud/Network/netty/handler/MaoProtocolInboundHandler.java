package org.mao.cloud.MaoCloud.Network.netty.handler;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import org.mao.cloud.MaoCloud.Network.netty.api.MaoProtocolEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by mao on 2016/7/2.
 */
@Deprecated
public class MaoProtocolInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        MaoCloudProtocol maoP = (MaoCloudProtocol) msg;

        String str = maoP.getDataOrCmd();
        MaoProtocolEvent event = new MaoProtocolEvent();
        event.dataOrCmd = str;

        if(maoP.getCMD()) {
            event.type = MaoProtocolEvent.Type.CMD;
        } else if (maoP.getDATA()) {
            event.type = MaoProtocolEvent.Type.DATA;
        } else {
            return;
        }

        ctx.fireChannelRead(maoP);
    }
}
