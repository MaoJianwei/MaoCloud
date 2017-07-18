package com.maojianwei.MaoCloud.MaoCloud.Network.cli;

import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import com.maojianwei.MaoCloud.cli.base.AbstractShellCommand;
import org.apache.karaf.shell.commands.Command;

/**
 * Created by mao on 2016/10/20.
 */
@Command(scope = "maocloud", name = "list-network-offline-nodes")
public class ListNetworkOfflineNodes extends AbstractShellCommand {

    @Override
    public void execute(){
        MaoProtocolControllerAdmin admin = getService(MaoProtocolControllerAdmin.class);

        admin.getAllUnConnectedNodes().forEach(node -> print(node));
    }
}
