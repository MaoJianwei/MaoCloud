package com.maojianwei.MaoCloud.MaoCloud.Network.netty.handler;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessageWriter;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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

        log.info("Will encode new MPMessage...");

        ByteBuf tmp = PooledByteBufAllocator.DEFAULT.heapBuffer();
        log.info("tmp heap buffer generated: {}", tmp.toString());
        final MPMessageWriter msgWriter = msg.writer();

        tmp.writeBytes(PROTOCOL_PREFIX);
        msgWriter.writeVersionTo(tmp);
        msgWriter.writeTypeTo(tmp);

        final boolean checkSumExist = false; //TODO - Get and Write CheckSum_Exist
        log.info("got checkSumExist: {}", checkSumExist);
        final boolean securePolicy = false; //TODO - Get and Write Secure_Policy
        log.info("got securePolicy: {}", securePolicy);
        tmp.writeByte(0);
        tmp.writeByte(0);

        int dataLength = msgWriter.prepareData();
        log.info("initial Datalength: {}", dataLength);
        if(checkSumExist){
            dataLength += CHECKSUM_LENGTH;
        }
        log.info("final Datalength: {}", dataLength);
        tmp.writeInt(dataLength);

        msgWriter.writeDataTo(tmp);


        if(checkSumExist){
            byte [] packet = new byte [tmp.readableBytes()];
            tmp.readBytes(packet);

            try {
                log.info("will calculate SHA-256...");
                byte[] sha256 = MessageDigest.getInstance("SHA-256").digest(packet);
                log.info("SHA-256 checksum is {}", String.format("%064x", new BigInteger(1, sha256)));

                out.writeBytes(packet);
                out.writeBytes(sha256);
            } catch(Exception e){
                log.error("SHA-256 is not supported!");
                //msg will not be sent.
            }
        } else {
            out.writeBytes(tmp);
        }

        log.info("release tmp Bytebuf...");
        tmp.release();
        log.info("release tmp over.");
    }
}
