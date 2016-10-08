package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/9/17.
 */
public class MPEchoRequestVer03 implements MPEchoRequest {

    private MPEchoRequestVer03(){
    }


    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPMessageReader<MPEchoRequest>{
        private static final int ECHO_REQUEST_LENGTH = 0;
        public MPEchoRequest readFrom(ByteBuf msg) throws MPParseError{

//            int dataLength =  msg.readInt();
//            if(dataLength != ECHO_REQUEST_LENGTH){
//                throw new MPErrorDataLength(dataLength, ECHO_REQUEST_LENGTH);
//            }
//            return new MPEchoRequestVer03();
            return null;
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPEchoRequest.Writer{

        MPEchoRequestVer03 msg;
        private Writer(MPEchoRequestVer03 msg){
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
    public static class Builder implements MPEchoRequest.Builder {

        public Builder(){
        }

        public MPEchoRequestVer03 build() {
            return null;
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.ECHO_REQUEST;
    }
}
