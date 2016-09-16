package org.mao.cloud.test.handler;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import org.mao.cloud.MaoCloud.Network.netty.api.MaoProtocolEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by mao on 2016/7/2.
 */
@Deprecated
public class MaoProtocolOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise){

        MaoProtocolEvent event = (MaoProtocolEvent) msg;

        MaoCloudProtocol.Builder maoPB = MaoCloudProtocol.builder();
        maoPB.setProtocolPrefix("MAO Link");
        maoPB.setPacketLen(Short.parseShort(String.valueOf(event.dataOrCmd.length())));

        if(event.type.equals(MaoProtocolEvent.Type.CMD)){
            maoPB.setCMD(true);
        } else if (event.type.equals(MaoProtocolEvent.Type.DATA)) {
            maoPB.setDATA(true);
        } else {
            return;
        }

        maoPB.setDataOrCmd(event.dataOrCmd);


        ctx.write(maoPB.build(), promise);
    }
}
