package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEcho;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/17.
 */
public class MPEchoVer03 implements MPEcho {

    public static final Reader READER = new Reader();

    public static class Reader implements MPMessageReader<MPEcho>{
        private static final int ECHO_LENGTH = 0;
        public MPEcho readFrom(ByteBuf msg) throws MPParseError{

            int dataLength =  msg.readInt();
            if(dataLength != ECHO_LENGTH){
                throw new MPErrorDataLength(dataLength, ECHO_LENGTH);
            }
            return new MPEchoVer03();
        }
    }
}
