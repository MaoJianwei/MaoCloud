package org.mao.cloud.MaoCloud.Network.netty.api;

import java.io.UnsupportedEncodingException;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoCloudProtocol {


    private byte[] protocolPrefix;
    private int dataLen;
    private MainMsgType mainMsgType;
    private SubMsgType subMsgType;
    private boolean hasCheckSum;
    private boolean hasSecurePolicy;
    private byte[] data;
    private byte[] checksum;

    private MaoCloudProtocol(byte[] protocolPrefix,
                             int dataLen,
                             MainMsgType mainMsgType,
                             SubMsgType subMsgType,
                             boolean hasCheckSum,
                             boolean hasSecurePolicy,
                             byte[] data,
                             byte[] checksum) {
        this.protocolPrefix = protocolPrefix;
        this.dataLen = dataLen;
        this.mainMsgType = mainMsgType;
        this.subMsgType = subMsgType;
        this.hasCheckSum = hasCheckSum;
        this.hasSecurePolicy = hasSecurePolicy;
        this.data = data;
        this.checksum = checksum;
    }

    public boolean checkValid() {
        try {
            if (!protocolPrefix.equals("MAOCLOUD".getBytes("UTF-8")))
                return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        if (dataLen < 0)
            return false;
        if (mainMsgType==null)
            return false;
        if (subMsgType == null)
            return false;
        if (!hasSecurePolicy && !hasCheckSum)
            return false;

        if(data.length != dataLen)
            return false;

        if(!checkChecksumValid())
            return false;

        return true;
    }
    private boolean checkChecksumValid(){
        //TODO - length and value
        return true;
    }
    private byte[] calculateChecksum(){
        //TODO
        return null;
    }


    public byte[] getProtocolPrefix() {
        return this.protocolPrefix;
    }
    public int getDataLen() {
        return this.dataLen;
    }
    public MainMsgType getMainMsgType() {
        return this.mainMsgType;
    }
    public SubMsgType getSubMsgType() {
        return this.subMsgType;
    }
    public boolean getHasCheckSum() {
        return this.hasCheckSum;
    }
    public boolean getHasSecurePolicy() {
        return this.hasSecurePolicy;
    }
    public byte[] getData() {
        return this.data;
    }
    public byte[] getChecksum() {
        return this.checksum;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        private byte[] protocolPrefix = null;
        private int dataLen = 0;
        private MainMsgType mainMsgType = null;
        private SubMsgType subMsgType = null;
        private boolean hasCheckSum = false;
        private boolean hasSecurePolicy = false;
        private byte[] data = null;
        private byte[] checksum = null;

        protected Builder() {
        }

        public MaoCloudProtocol build() {
            return new MaoCloudProtocol(
                    protocolPrefix,
                    dataLen,
                    mainMsgType,
                    subMsgType,
                    hasCheckSum,
                    hasSecurePolicy,
                    data,
                    checksum
            );
        }

        public Builder setProtocolPrefix(byte[] protocolPrefix) {
            this.protocolPrefix = protocolPrefix;
            return this;
        }
        public Builder setDataLen(short dataLen) {
            this.dataLen = dataLen;
            return this;
        }
        public Builder setMainMsgType(MainMsgType mainMsgType) {
            this.mainMsgType = mainMsgType;
            return this;
        }
        public Builder setSubMsgType(SubMsgType subMsgType) {
            this.subMsgType = subMsgType;
            return this;
        }
        public Builder setHasCheckSum(boolean hasCheckSum) {
            this.hasCheckSum = hasCheckSum;
            return this;
        }
        public Builder setHasSecurePolicy(boolean hasSecurePolicy) {
            this.hasSecurePolicy = hasSecurePolicy;
            return this;
        }
        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }
        public Builder setChecksum(byte[] checksum) {
            this.checksum = checksum;
            return this;
        }
    }


    public enum MainMsgType {
        LinkControl,
        MonitorTalk,
        ClusterMaintain,
        InnerService,
        OuterService,
        Announcement
    }

    private interface SubMsgType {
    }

    public enum MsgTypeLinkControl implements SubMsgType {
        Handshake,
        Goodbye,
        Echo
    }
}
