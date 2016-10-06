package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/18.
 */
public class MPEchoReplyVer03 implements MPEchoReply {



    private MPEchoReplyVer03(){
    }


    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPMessageReader<MPEchoReply>{
        private static final int ECHO_REPLY_LENGTH = 0;
        public MPEchoReply readFrom(ByteBuf msg) throws MPParseError{

//            int dataLength =  msg.readInt();
//            if(dataLength != ECHO_REPLY_LENGTH){
//                throw new MPErrorDataLength(dataLength, ECHO_REPLY_LENGTH);
//            }
//            return new MPEchoReplyVer03();
            return null;
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPEchoReply.Writer{

        MPEchoReplyVer03 msg;
        private Writer(MPEchoReplyVer03 msg){
            this.msg = msg;
        }

        @Override
        public void writeVersion(ByteBuf out){
//            out.writeByte(msg.getVersion().get());
        }

        @Override
        public void writeType(ByteBuf out){
//            out.writeByte(msg.getType().get());
        }

        @Override
        public int prepareData(){
//            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
//            data.writeBytes(msg.getHashValue());
//            return data.readableBytes();
            return -1;
        }

        @Override
        public void writeData(ByteBuf out){
//            out.writeBytes(data);
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPEchoReply.Builder {

        public Builder(){
        }

        public MPEchoReplyVer03 build() {
            return null;
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.ECHO_REPLY;
    }

}
