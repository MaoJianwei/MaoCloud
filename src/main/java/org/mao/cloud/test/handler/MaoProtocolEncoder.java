package org.mao.cloud.test.handler;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by mao on 2016/7/1.
 */
@Deprecated
public class MaoProtocolEncoder extends MessageToByteEncoder<MaoCloudProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MaoCloudProtocol maoP, ByteBuf out) throws UnsupportedEncodingException {

        if(!maoP.checkValid())
            return;

        out.writeBytes(maoP.getProtocolPrefix().getBytes("UTF-8"));
        out.writeShort(maoP.getPacketLen());
        out.writeByte(buildField(maoP));
        out.writeBytes(maoP.getDataOrCmd().getBytes("UTF-8"));
    }

    private byte buildField(MaoCloudProtocol maoP){
        byte field = 0;
        field |= maoP.getSYN() ? 0x80 : 0x00;
        field |= maoP.getFIN() ? 0x40 : 0x00;
        field |= maoP.getCMD() ? 0x20 : 0x00;
        field |= maoP.getDATA() ? 0x10 : 0x00;
        return field;
    }
}
