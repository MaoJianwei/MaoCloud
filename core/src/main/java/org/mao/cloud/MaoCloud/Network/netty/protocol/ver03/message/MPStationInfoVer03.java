package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPStationInfo;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;
import org.mao.cloud.util.StringUtil;

import static org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength.POSITIVE;
import static org.mao.cloud.util.ConstUtil.STR_NULL;
import static org.mao.cloud.util.StringUtil.bytesGetStr;

/**
 * Created by mao on 2017/5/5.
 */
public class MPStationInfoVer03 implements MPStationInfo {

    private String info;
    public String getStationInfo(){
        return info;
    }

    private MPStationInfoVer03(String info){
        this.info = info;
    }

    //TODO - should be updated - considered CheckSum
    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPStationInfo.Reader{

        @Override
        public MPStationInfo readFrom(ByteBuf msg) throws MPParseError {

            int dataLength = msg.readInt();
            if(dataLength < 0){
                throw new MPErrorDataLength(dataLength, POSITIVE);
            }

            byte [] stationInfo = new byte [dataLength];
            msg.readBytes(stationInfo);

            return new MPStationInfoVer03(bytesGetStr(stationInfo));
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPStationInfo.Writer{

        MPStationInfoVer03 msg;
        byte [] dataBytes;
        ByteBuf data;

        private Writer(MPStationInfoVer03 msg){
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
            byte [] infoBytes = StringUtil.strGetBytes(msg.getStationInfo());

            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
            data.writeBytes(infoBytes);
            return data.readableBytes();
        }

        @Override
        public void writeDataTo(ByteBuf out){
            out.writeBytes(data);
            data.release();
            data = null;
        }
    }

    public Builder builder() {
        return new Builder();
    }
    public static class Builder implements MPStationInfo.Builder {

        private String info;

        public Builder(){
            this.info = STR_NULL;
        }

        public Builder setStationInfo(String info){
            this.info = info;
            return this;
        }

        public MPStationInfoVer03 build() {
            return new MPStationInfoVer03(info);
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.NOTIFICATION_STATION_INFO;
    }
}
