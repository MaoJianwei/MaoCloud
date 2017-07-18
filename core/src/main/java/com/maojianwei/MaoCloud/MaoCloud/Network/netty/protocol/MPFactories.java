package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.base.MPFactoryVer03;
import io.netty.buffer.ByteBuf;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.exception.MPUnsupportedVersion;

/**
 * Created by mao on 2016/9/17.
 */
public final class MPFactories {

    private static final GeneralReader GENERAL_READER = new GeneralReader();

    public static MPMessageReader<MPMessage> getGeneralReader(){
        return GENERAL_READER;
    }

    public static MPFactory getFactory(MPVersion version){
        switch(version){
            case MP_03:
                return MPFactoryVer03.INSTANCE;
            default:
                throw new IllegalArgumentException("Unknown version: " + version);
        }
    }


    private static class GeneralReader implements MPMessageReader<MPMessage> {

        public MPMessage readFrom(ByteBuf msg) throws MPParseError {
            byte version = msg.readByte();
            switch (version){
                case 3:
                    return MPFactoryVer03.INSTANCE.getReader().readFrom(msg);
                default:
                    throw new MPUnsupportedVersion(version);
            }
        }
    }
}
