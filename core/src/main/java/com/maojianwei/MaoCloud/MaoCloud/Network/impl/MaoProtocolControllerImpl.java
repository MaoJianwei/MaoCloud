package com.maojianwei.MaoCloud.MaoCloud.Network.impl;

import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolAgent;
import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolController;
import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolListener;
import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import com.maojianwei.MaoCloud.util.IpUtil;
import io.netty.util.internal.ConcurrentSet;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mao on 2016/10/5.
 */
@Component(immediate = true)
@Service
public class MaoProtocolControllerImpl implements MaoProtocolController, MaoProtocolControllerAdmin {

    private static final Logger log = LoggerFactory.getLogger(MaoProtocolControllerImpl.class);

    private Inet4Address ipv4 = IpUtil.INVALID_IPV4_ADDRESS;
    @Deprecated //TODO - ipv6 compatible
    private Inet6Address ipv6 = IpUtil.INVALID_IPV6_ADDRESS;


    private MaoProtocolAgent agent = new MaoProtocolNodeAgent();
    private MaoProtocolNetworkController networkController = new MaoProtocolNetworkControllerImpl(agent);

    private Set<MaoProtocolListener> maoProtocolListeners = new ConcurrentSet<>();

    private ConcurrentSet<String> configuredNodeSet = new ConcurrentSet<>();
    private ConcurrentSet<String> unConnectedNodes = new ConcurrentSet<>();
    // Deprecated - private ConcurrentSet<String> connectingNodes = new ConcurrentSet<>();
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

        networkController.start(new HashSet<>(unConnectedNodes));

        log.info("activate finish !");
    }

    @Deactivate
    private void deactivate() {
        log.info("Deactivating...");
        networkController.stop();
        log.info("deactivate OK !");
    }

    // --- MaoProtocolController ---

    @Override
    public void addListener(MaoProtocolListener listener) {
        maoProtocolListeners.add(listener);
    }

    @Override
    public void removeListener(MaoProtocolListener listener) {
        maoProtocolListeners.remove(listener);
    }

    // --- MaoProtocolControllerAdmin ---
    @Override
    public List<String> getAllUnConnectedNodes(){
        return new ArrayList<>(unConnectedNodes);
    }

    @Override
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

        // TODO: 2016/11/6 to be real
//        configuredNodeSet.add("pi-sky.maojianwei.com");

//        configuredNodeSet.add("cloud-server1.maojianwei.com");
//        configuredNodeSet.add("cloud-server2.maojianwei.com");

//        configuredNodeSet.add("10.117.6.230");
//        configuredNodeSet.add("10.117.6.235");

        configuredNodeSet.add("10.103.73.55");
        configuredNodeSet.add("10.103.71.54");
        configuredNodeSet.add("10.103.71.37");
    }

    // filter to exclude myself ip.
    private void initUnconnectedNodes() {
        configuredNodeSet.stream()
                .filter(address ->
                        !address.equals(ipv4.getHostAddress()) &&
                                (ipv6 == null || !address.equals(ipv6.getHostAddress().split("%")[0]))
                )
                .forEach(address -> unConnectedNodes.add(address));
        log.info("All unConnectedNodes is: {}", unConnectedNodes.toString());
    }


    private class MaoProtocolNodeAgent implements MaoProtocolAgent {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Override
        public boolean addConnectedNode(MaoProtocolNode node) { // TODO: 2016/10/20 nodes lock
            String nodeIp = node.getAddressStr();
            if(!unConnectedNodes.remove(nodeIp)){
                log.error("Can not find nodeAddr: {} in unConnectedNodes !!!" +
                        " Please troubleshoot !!!", nodeIp);
            }

            connectedNodes.put(nodeIp, node);

            log.info("New Node is up: {}", node.getAddressStr());

            for (MaoProtocolListener l : maoProtocolListeners) {
                l.nodeConnected(node);
            }
            return true;
        }

        @Override
        public boolean removeConnectedNode(MaoProtocolNode node) {
            String nodeIp = node.getAddressStr();
            if(connectedNodes.remove(nodeIp) == null){
                log.error("connectedNodes can't find {} when removing", nodeIp);
            }
            boolean ret = unConnectedNodes.add(nodeIp);
            log.info("Node is down: {}, take back: {}", nodeIp, ret);

            for (MaoProtocolListener l : maoProtocolListeners) {
                l.nodeUnconnected(node);
            }
            return ret;
        }

        @Override
        public void processMessage(MPMessage msg) {
            for (MaoProtocolListener l : maoProtocolListeners) {
                l.processMessage(msg);
            }
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
