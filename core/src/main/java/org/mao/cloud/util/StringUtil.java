package org.mao.cloud.util;


import java.nio.charset.Charset;

/**
 * Created by mao on 2016/11/2.
 */
public final class StringUtil {

    public static byte[] strGetBytes(String str){
        if (Charset.defaultCharset.displayName("UTF-8")) {
            return str.getBytes()
        }
    }

}
