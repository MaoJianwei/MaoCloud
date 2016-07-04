package org.mao.cloud.test.handler;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws UnsupportedEncodingException {

        if(msg.readableBytes() < 11)
            return;


        MaoCloudProtocol.Builder maoPB = MaoCloudProtocol.builder();
        parseField(maoPB, msg);

        MaoCloudProtocol maoP = maoPB.build();
        if(maoP.checkValid()) {
            out.add(maoP);
        }
    }


    private void parseField(MaoCloudProtocol.Builder maoPB, ByteBuf msg) throws UnsupportedEncodingException {

        byte[] protoPrefix = new byte[8];
        msg.readBytes(protoPrefix);
        maoPB.setProtocolPrefix(new String(protoPrefix, "UTF-8"));

        short dataLen = msg.readShort();
        maoPB.setPacketLen(dataLen);

        byte field = msg.readByte();
        maoPB.setSYN((field & 0x80) != 0);
        maoPB.setFIN((field & 0x40) != 0);
        maoPB.setCMD((field & 0x20) != 0);
        maoPB.setDATA((field & 0x10) != 0);

        byte[] dataOrCmd = new byte[dataLen];
        msg.readBytes(dataOrCmd);
        maoPB.setDataOrCmd(new String(dataOrCmd, "UTF-8"));
    }
}
