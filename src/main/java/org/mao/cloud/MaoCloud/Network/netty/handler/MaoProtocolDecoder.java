package org.mao.cloud.MaoCloud.Network.netty.handler;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.mao.cloud.MaoCloud.Network.netty.protocol.MPFactories;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws UnsupportedEncodingException {

        if(msg.readableBytes() < 12)
            return;

        MPMessageReader<MPMessage> generalReader = MPFactories.getGeneralReader();
        MPMessage mpMessage = generalReader.readFrom(msg);
        out.add(mpMessage);


//
//        MaoCloudProtocol.Builder maoPB = MaoCloudProtocol.builder();
//        if(!parseField(maoPB, msg))
//            return;
//
//        MaoCloudProtocol maoP = maoPB.build();
//        if(maoP.checkValid()) {
//            out.add(maoP);
//        }
    }

    @Deprecated
    private boolean parseField(MaoCloudProtocol.Builder maoPB, ByteBuf msg) {

        try {
        byte[] protoPrefix = new byte[8];
        msg.readBytes(protoPrefix);

            maoPB.setProtocolPrefix(new String(protoPrefix, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

        short numMsgType = msg.readShort();

        //TODO - map Short to ENUM

        maoPB.setMainMsgType()

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
