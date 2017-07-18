package com.maojianwei.MaoCloud.util;

import com.google.common.net.InetAddresses;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * Created by mao on 2016/10/11.
 */
public final class IpUtil {

    //for the active connection rule, privately define the value for invalid ip.
    public static final Inet4Address INVALID_IPV4_ADDRESS =
            (Inet4Address) InetAddresses.forString("255.255.255.255");
    public static final Inet6Address INVALID_IPV6_ADDRESS =
            (Inet6Address) InetAddresses.forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");

    public static final int IPV4_LENGTH = 4;
    public static final int IPV6_LENGTH = 16;


    /**
     * Returns the {@link InetAddress} having the given string representation.
     *
     * This comment is copied form Guava.InetAddresses.forString().
     *
     * <p>This deliberately avoids all nameservice lookups (e.g. no DNS).
     *
     * @param ipStr {@code String} containing an IPv4 or IPv6 string literal, e.g.
     *     {@code "192.168.0.1"} or {@code "2001:db8::1"}
     * @return {@link InetAddress} representing the argument
     * @throws IllegalArgumentException if the argument is not a valid IP string literal
     */
    public static InetAddress strToInet(String ipStr){
        return InetAddresses.forString(ipStr);
    }
    public static String inetToStr(InetAddress ipAddr){
        return isIpv4(ipAddr)
                ? InetAddresses.toAddrString(ipAddr).replace("/", "")
                : InetAddresses.toAddrString(ipAddr);
    }

    public static boolean isIpv4(InetAddress ip){
        return ip.getAddress().length == IPV4_LENGTH;
    }
    public static boolean isIpv6(InetAddress ip){
        return ip.getAddress().length == IPV6_LENGTH;
    }
}
