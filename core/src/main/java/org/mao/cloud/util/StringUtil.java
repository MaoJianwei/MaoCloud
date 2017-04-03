package org.mao.cloud.util;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by mao on 2016/11/2.
 */
public final class StringUtil {

    /**
     * Get bytes of string in UTF-8 format.
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException will not be thrown forever
     */
    public static byte[] strGetBytes(String str) throws UnsupportedEncodingException {
        return Charset.isSupported("UTF-8") ? str.getBytes("UTF-8") : new byte[1];
    }
}
