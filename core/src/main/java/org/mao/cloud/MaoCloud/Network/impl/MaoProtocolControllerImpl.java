package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.util.internal.ConcurrentSet;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolController;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static org.mao.cloud.util.IpUtil.INVALID_IPV4_ADDRESS;
import static org.mao.cloud.util.IpUtil.INVALID_IPV6_ADDRESS;
import static org.mao.cloud.util.IpUtil.getIpFromString;

/**
 * Created by mao on 2016/10/5.
 */
@Component(immediate = true)
@Service
public class MaoProtocolControllerImpl implements MaoProtocolController, MaoProtocolControllerAdmin {

    private static final Logger log = LoggerFactory.getLogger(MaoProtocolControllerImpl.class);


    private MaoProtocolAgent agent = new MaoProtocolNodeAgent();
    private MaoProtocolNetworkController networkController = new MaoProtocolNetworkControllerImpl(agent);

    private Inet4Address ipv4 = INVALID_IPV4_ADDRESS;
    @Deprecated //TODO - ipv6 compatible
    private Inet6Address ipv6 = INVALID_IPV6_ADDRESS;

    private ConcurrentSet<String> configuredNodeSet = new ConcurrentSet<>();
    private ConcurrentLinkedQueue<String> unConnectedNodes = new ConcurrentLinkedQueue<>();
    private ConcurrentSet<String> connectingNodes = new ConcurrentSet<>();
    private ConcurrentMap<String, MaoProtocolNode> connectedNodes = new ConcurrentHashMap<>();

    @Activate
    private void activate() {

        log.info("Init...");

        if (!initLocalIps()) {
            // TODO - upgrade to check periodically
            log.error("Something wrong while init local ips, telecommunication will not start, please troubleshoot!");
            return;
        }

        getConfiguredNodeSet();

        initUnconnectedNodes();

        networkController.start();

        log.info("activate finish !");
    }

    @Deactivate
    private void deactivate() {
        log.info("Deactivating...");
        networkController.stop();
        log.info("deactivate OK !");
    }


    // --- MaoProtocolControllerAdmin ---
    public List<MaoProtocolNode> getAllConnectedNodes(){
        return new ArrayList<>(connectedNodes.values());
    }


    private boolean initLocalIps() {

//        Set<String> localAddresses = new HashSet<>();
        try {
            Enumeration<NetworkInterface> intfs = NetworkInterface.getNetworkInterfaces();
            while (intfs.hasMoreElements()) {
                NetworkInterface intf = intfs.nextElement();
                if (intf.isLoopback() || intf.isVirtual() || !intf.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = intf.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        ipv4 = (Inet4Address) ip;
                    } else if (ip instanceof Inet6Address) {
                        //TODO

                        // localAddresses.add(addresses.nextElement().getHostAddress().split("%")[0]);
                        // split() serve to IPv6,
                        // because the value will be such as 2001:da8:215:389:9097:e45c:94b0:9be5%ens32

                        // For ipv6, false==isSiteLocalAddress() and false==isLinkLocalAddress()
                        // can be simply considered as Global routable address
                    } else {
                        throw new Exception("Unsupported Address, not ipv4 or ipv6, " + ip);
                    }
                }
            }
            return true;
        } catch (SocketException e) {
            log.warn("Can not gain local IP, because: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error got: " + e.getMessage());
        }
        return false;
    }

    private void getConfiguredNodeSet(){
        configuredNodeSet.add("10.103.89.180"); //VM

        configuredNodeSet.add("10.103.89.201");
        configuredNodeSet.add("10.103.89.247");
        configuredNodeSet.add("10.117.6.230");
        configuredNodeSet.add("10.117.6.235");
        configuredNodeSet.add("10.210.107.70");
        configuredNodeSet.add("10.210.107.74");
        configuredNodeSet.add("10.210.107.79");

        configuredNodeSet.add("10.210.107.254");
    }

    // filter to exclude myself ip.
    private void initUnconnectedNodes(){
        configuredNodeSet.stream()
                .filter(address ->
                        !address.equals(ipv4.getHostAddress()) &&
                                (ipv6 == null || !address.equals(ipv6.getHostAddress().split("%")[0]))
                )
                .forEach(address -> unConnectedNodes.offer(address));
        log.info("All unConnectedNodes is: {}", unConnectedNodes.toString());
    }


    private class MaoProtocolNodeAgent implements MaoProtocolAgent {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Override
        public boolean addConnectedNode(MaoProtocolNode node) {
            connectingNodes.remove(node.getAddress());
            connectedNodes.put(node.getAddress(), node);

            log.info("New Node is up: {}", node.getAddress());
            return true;
        }

        @Override
        public InetAddress getOneUnConnectedNode() {
            InetAddress nodeIp = null;
            if (!unConnectedNodes.isEmpty()) {
                String nodeIpStr = unConnectedNodes.poll();
                connectingNodes.add(nodeIpStr);
                nodeIp = getIpFromString(nodeIpStr);
            }
            log.info("poll a unConnected node: {}", nodeIp);
            return nodeIp;
        }



        //Todo - this is not the responsibility of Agent.
        @Override
        public Inet4Address getLocalIpv4(){
            return ipv4;
        }
        @Override
        public Inet6Address getLocalIpv6(){
            return ipv6;
        }
    }
}
