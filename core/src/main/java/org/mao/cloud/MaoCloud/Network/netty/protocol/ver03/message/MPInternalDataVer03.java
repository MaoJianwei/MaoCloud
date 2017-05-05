package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPInternalData;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/11/6.
 */
public class MPInternalDataVer03 implements MPInternalData {
//    private static final int ECHO_REPLY_LENGTH = 0;

    private int

    private MPInternalDataVer03(){
    }


    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPMessageReader<MPInternalData> {
        public MPInternalData readFrom(ByteBuf msg) throws MPParseError {

//            int dataLength =  msg.readInt();
//            if(dataLength != ECHO_REPLY_LENGTH){
//                throw new MPErrorDataLength(dataLength, ECHO_REPLY_LENGTH);
//            }
//            return new MPEchoReplyVer03();
            return new MPInternalDataVer03();
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPInternalData.Writer{

        MPInternalDataVer03 msg;
        private Writer(MPInternalDataVer03 msg){
            this.msg = msg;
        }

        @Override
        public void writeVersionTo(ByteBuf out){
            out.writeByte(msg.getVersion().get());
        }

        @Override
        public void writeTypeTo(ByteBuf out){
            out.writeByte(msg.getType().get());
        }

        @Override
        public int prepareData(){
//            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
//            data.writeBytes(msg.getHashValue());
//            return data.readableBytes();
            return ECHO_REPLY_LENGTH;
        }

        @Override
        public void writeDataTo(ByteBuf out){
//            out.writeBytes(data);
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPInternalData.Builder {

        public Builder(){
        }

        public MPInternalDataVer03 build() {
            return new MPInternalDataVer03();
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.INTERNAL_DATA;
    }

}
