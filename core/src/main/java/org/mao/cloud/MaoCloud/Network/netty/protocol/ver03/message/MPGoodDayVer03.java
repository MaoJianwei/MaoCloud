package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

import java.io.UnsupportedEncodingException;

import static org.mao.cloud.util.ConstUtil.STR_NULL;

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
    public static class Reader implements MPGoodDay.Reader {
        public MPGoodDay readFrom(ByteBuf msg) throws MPParseError {

            byte [] cause = null;
            int dataLength = msg.readInt();

            if(dataLength < 0){
                throw new MPErrorDataLength(dataLength, 0);
            } else if (dataLength > 0) {
                cause = new byte [dataLength];
                msg.readBytes(cause);
            }

            return cause == null ? new MPGoodDayVer03(STR_NULL) : new MPGoodDayVer03(new String(cause));
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPGoodDay.Writer{

        MPGoodDayVer03 msg;
        ByteBuf data;

        private Writer(MPGoodDayVer03 msg){
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
            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
            try {
                byte [] msgByte = msg.getCause().getBytes("UTF-8");
                data.writeBytes(msgByte);
                return data.readableBytes();
            } catch (UnsupportedEncodingException e) {
                // TODO - design more efficient mechanism to fit EXCEPTION
                return 0;
            }
        }

        @Override
        public void writeDataTo(ByteBuf out){
            out.writeBytes(data);
            data.release();
            data = null;
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPGoodDay.Builder {

        String cause;

        public Builder(){
            cause = STR_NULL;
        }

        public Builder setCause(String cause) {
            this.cause = cause;
            return this;
        }

        public MPGoodDayVer03 build() {
            return new MPGoodDayVer03(cause);
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.GOODDAY;
    }
}
