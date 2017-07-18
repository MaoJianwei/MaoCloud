package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.message;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import io.netty.buffer.ByteBuf;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base.MPMessageType;

/**
 * Created by mao on 2016/9/18.
 */
public class MPEchoReplyVer03 implements MPEchoReply {

    private static final int ECHO_REPLY_LENGTH = 0;

    private MPEchoReplyVer03(){
    }

    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPEchoReply.Reader{
        public MPEchoReply readFrom(ByteBuf msg) throws MPParseError{
            return new MPEchoReplyVer03();
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
        public void writeVersionTo(ByteBuf out){
            out.writeByte(msg.getVersion().get());
        }

        @Override
        public void writeTypeTo(ByteBuf out){
            out.writeByte(msg.getType().get());
        }

        @Override
        public int prepareData(){
            return ECHO_REPLY_LENGTH;
        }

        @Override
        public void writeDataTo(ByteBuf out){
            ;
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPEchoReply.Builder {

        public Builder(){
        }

        public MPEchoReplyVer03 build() {
            return new MPEchoReplyVer03();
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.LINK_ECHO_REPLY;
    }

}
