package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/9/17.
 */
public class MPGoodDayVer03 implements MPGoodDay {


    private String cause;
    public String getCause(){
        return cause;
    }
    private MPGoodDayVer03(String cause){
        this.cause = cause;
    }

    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPMessageReader<MPGoodDay>{
        public MPGoodDay readFrom(ByteBuf msg) throws MPParseError {

//            int dataLength = msg.readInt();
//            String causeStr;
//            if(dataLength > 0){
//                byte [] cause = new byte [dataLength];
//                msg.readBytes(cause);
//                causeStr = new String(cause);
//            }else{
//                causeStr = "";
//            }
//
//            return new MPGoodDayVer03(causeStr);
            return null;
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPGoodDay.Writer{

        MPGoodDayVer03 msg;
        private Writer(MPGoodDayVer03 msg){
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
    public static class Builder implements MPGoodDay.Builder {

        public Builder(){
        }

        public MPGoodDayVer03 build() {
            return null;
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.GOODDAY;
    }
}
