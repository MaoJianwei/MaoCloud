package org.mao.cloud.MaoCloud.Network.netty.handler;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoProtocolEncoder extends MessageToByteEncoder<MPMessage> {

    private final Logger log = LoggerFactory.getLogger(getClass());


    private static final byte [] PROTOCOL_PREFIX = "MAOCLOUD".getBytes();
    private static final int CHECKSUM_LENGTH = 32;

    @Override
    protected void encode(ChannelHandlerContext ctx, MPMessage msg, ByteBuf out) throws UnsupportedEncodingException {

//        if(!msg.checkValid())
//            return;

        ByteBuf tmp = PooledByteBufAllocator.DEFAULT.heapBuffer();
        final MPMessageWriter WRITER = msg.writer();

        tmp.writeBytes(PROTOCOL_PREFIX);
        WRITER.writeVersion(tmp);
        WRITER.writeType(tmp);

        final boolean checkSumExist = false; //TODO - Get and Write CheckSum_Exist
        final boolean securePolicy = false; //TODO - Get and Write Secure_Policy
        tmp.writeByte(0);
        tmp.writeByte(0);

        int dataLength = WRITER.prepareData();
        if(checkSumExist){
            dataLength += CHECKSUM_LENGTH;
        }
        tmp.writeInt(dataLength);

        WRITER.writeData(tmp);


        if(checkSumExist){
            byte [] packet = new byte [tmp.readableBytes()];
            tmp.readBytes(packet);

            try {
                byte[] sha256 = MessageDigest.getInstance("SHA-256").digest(packet);

                out.writeBytes(packet);
                out.writeBytes(sha256);
            } catch(Exception e){
                log.error("SHA-256 is not supported!");
                //msg will not be sent.
            }
        } else {
            out.writeBytes(tmp);
        }

        tmp.release();
    }
}
