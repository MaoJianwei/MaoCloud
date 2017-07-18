package com.maojianwei.MaoCloud.MaoCloud.Network.cli;

import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import com.maojianwei.MaoCloud.cli.base.AbstractShellCommand;
import org.apache.karaf.shell.commands.Command;
import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;

import java.util.List;

/**
 * Created by mao on 2016/10/16.
 */
@Command(scope = "maocloud", name = "list-network-peer",
        description = "List all network infos of node peers.")
public class ListNetworkPeer extends AbstractShellCommand {

    @Override
    protected void execute(){
        MaoProtocolControllerAdmin controllerAdmin = getService(MaoProtocolControllerAdmin.class);
        List<MaoProtocolNode> nodes = controllerAdmin.getAllConnectedNodes();
        nodes.forEach(node->{
            print(node.getChannelInfo());
        });
    }
}
