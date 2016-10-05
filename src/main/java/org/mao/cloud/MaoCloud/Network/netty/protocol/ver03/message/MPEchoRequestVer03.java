package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/17.
 */
public class MPEchoRequestVer03 implements MPEchoRequest {

    public static final Reader READER = new Reader();

    public MPMessageType getType(){return MPMessageType.ECHO_REQUEST;}


    public static class Reader implements MPMessageReader<MPEchoRequest>{
        private static final int ECHO_REQUEST_LENGTH = 0;
        public MPEchoRequest readFrom(ByteBuf msg) throws MPParseError{

            int dataLength =  msg.readInt();
            if(dataLength != ECHO_REQUEST_LENGTH){
                throw new MPErrorDataLength(dataLength, ECHO_REQUEST_LENGTH);
            }
            return new MPEchoRequestVer03();
        }
    }
}
