package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/18.
 */
public class MPEchoReplyVer03 implements MPEchoReply {

    public static final Reader READER = new Reader();


    public MPMessageType getType(){return MPMessageType.ECHO_REPLY;}



    public static class Reader implements MPMessageReader<MPEchoReply> {
        private static final int ECHO_REPLY_LENGTH = 0;
        public MPEchoReply readFrom(ByteBuf msg) throws MPParseError {

            int dataLength =  msg.readInt();
            if(dataLength != ECHO_REPLY_LENGTH){
                throw new MPErrorDataLength(dataLength, ECHO_REPLY_LENGTH);
            }
            return new MPEchoReplyVer03();
        }
    }


}
