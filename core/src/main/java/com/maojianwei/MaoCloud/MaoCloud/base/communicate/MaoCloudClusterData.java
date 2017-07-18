package com.maojianwei.MaoCloud.MaoCloud.base.communicate;

import com.maojianwei.MaoCloud.MaoCloud.api.MaoCloudData;
import com.maojianwei.MaoCloud.MaoCloud.base.MaoCloudDataType;

import static com.maojianwei.MaoCloud.MaoCloud.base.MaoCloudDataType.CLUSTER;

/**
 * Created by mao on 2016/11/6.
 */
public class MaoCloudClusterData extends MaoCloudData {

    @Override
    public MaoCloudDataType type() {
        return CLUSTER;
    }
}
