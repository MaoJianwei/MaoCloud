package org.mao.cloud.MaoCloud.base.communicate;

import org.mao.cloud.MaoCloud.api.MaoCloudData;
import org.mao.cloud.MaoCloud.base.MaoCloudDataType;

import static org.mao.cloud.MaoCloud.base.MaoCloudDataType.CLUSTER;

/**
 * Created by mao on 2016/11/6.
 */
public class MaoCloudClusterData extends MaoCloudData {

    @Override
    public MaoCloudDataType type() {
        return CLUSTER;
    }
}
