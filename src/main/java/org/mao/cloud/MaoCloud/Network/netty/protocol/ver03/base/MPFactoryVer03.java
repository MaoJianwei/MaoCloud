package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPEchoVer03;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPGoodDayVer03;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPHelloVer03;

/**
 * Created by mao on 2016/9/17.
 */
public class MPFactoryVer03 implements MPFactory {

    public static final MPFactoryVer03 INSTANCE = new MPFactoryVer03();
    private static final Reader READER = new Reader();

    public MPMessageReader<MPMessage> getReader(){
        return READER;
    }

    private static class Reader implements MPMessageReader<MPMessage> {

        public MPMessage readFrom(ByteBuf msg) throws MPParseError {
            byte type = msg.readByte();
            byte params = msg.readByte();
            msg.skipBytes(1);

            boolean checksumExist = (params & 0x80) > 0;
            boolean securePolicy = (params & 0x40) > 0;

            if(checksumExist){
                verifyChecksum(msg);
            }

            if(securePolicy){
                ;//TODO
            }

            switch(type >>> 4){
                case 0:
                    switch(type & 0x0f){
                        case 0:
                            return MPHelloVer03.READER.readFrom(msg);
                        case 1:
                            return MPGoodDayVer03.READER.readFrom(msg);
                        case 2:
                            return MPEchoVer03.READER.readFrom(msg);
                        default:
                            throw new MPErrorType(2, type>>>4, type&0x0f, false);
                    }
                default:
                    throw new MPErrorType(2, type>>>4, type&0x0f, true);
            }
        }
        private boolean verifyChecksum(ByteBuf msg){
            return true; //TODO
        }
    }
}
