package org.mao.cloud.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.mao.cloud.util.ConstUtil.STR_NULL;


/**
 * Created by mao on 2016/11/2.
 */
public final class StringUtil {

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String UNEXPECTED_UnsupportedEncodingException =
            "Should not come into UnsupportedEncodingException's catch, {}, will return byte[1]";
    private static final String SERIALIZATION_INSTEAD =
            "System doesn't support UTF-8 encoding, use Serialization instead.";

    /**
     * Get bytes of string in UTF-8 format.
     *
     * @param str
     * @return str's byte[]; otherwise byte[1]
     * @throws UnsupportedEncodingException will not be thrown forever
     */
    public static byte[] strGetBytes(String str) {

        // TODO - finish this

        if(Charset.isSupported(ENCODING_UTF_8)) {
            try {
                return str.getBytes(ENCODING_UTF_8);
            } catch (UnsupportedEncodingException e) {
                log.warn(UNEXPECTED_UnsupportedEncodingException, e.getMessage());
                // TODO - consider if we should redisign this
                return strSerialize(str);
            }
        } else {
            // TODO - consider if we should redisign this
            log.warn(SERIALIZATION_INSTEAD);
            return strSerialize(str);
        }
    }

    private static byte [] strSerialize(String str) {
        // TODO
        return new byte[1];
    }

    /**
     *
     * @param strBytes
     * @return
     */
    public static String bytesGetStr(byte [] strBytes) {
        if(Charset.isSupported(ENCODING_UTF_8)){
            try {
                return new String(strBytes, ENCODING_UTF_8);
            } catch (UnsupportedEncodingException e) {
                log.warn(UNEXPECTED_UnsupportedEncodingException, e.getMessage());
                // TODO - consider if we should redisign this
                return strDeserialize(strBytes);
            }
        } else {
            // TODO - consider if we should redisign this
            log.warn(SERIALIZATION_INSTEAD);
            return strDeserialize(strBytes);
        }
    }

    private static String strDeserialize(byte [] str) {
        // TODO
        return STR_NULL;
    }
}
