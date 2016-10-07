package org.mao.cloud.MaoCloud.Network.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.mao.cloud.MaoCloud.Network.netty.protocol.MPFactories;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPInvalidPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final String PROTOCOL_PREFIX = "MAOCLOUD";
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws UnsupportedEncodingException {

        if(msg.readableBytes() < 16) {
            //FIXME - USING LengthFieldBasedFrameDecoder, SHOULD NOT COME HERE !
            log.error(("readableBytes is not enough! Coding Attention!"));
            return;
        }

        if(!checkProtocolValid(msg)){
            log.error(("Protocol prefix is invalid!"));
            return;
        }
        log.info("Protocol prefix check OK!");

        try {
            log.info("will Decode a message...");
            MPMessageReader<MPMessage> generalReader = MPFactories.getGeneralReader();
            log.info("got generalReader, ready to Decode a message...");
            MPMessage mpMessage = generalReader.readFrom(msg);
            log.info("Decode finish, Type:{}, Version:{}, toString:{}",
                    mpMessage.getType(),
                    mpMessage.getVersion(),
                    mpMessage.toString());
            out.add(mpMessage);
        }catch(MPParseError e){
            log.error("ParseError when decode: " + e.getMessage());
        }
    }

    /**
     * Check Protocol Prefix: MAOCLOUD
     *
     * @param msg
     * @return
     */
    private boolean checkProtocolValid(ByteBuf msg){
        byte [] prefix = new byte[8];
        msg.readBytes(prefix);
        String prefixStr = new String(prefix);
        return prefixStr.equals(PROTOCOL_PREFIX);
    }
}
