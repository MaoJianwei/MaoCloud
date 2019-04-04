package com.maojianwei.MaoCloud.MaoCloud.base.communicate;

import com.google.common.annotations.Beta;
import com.maojianwei.MaoCloud.MaoCloud.api.communicate.MaoCloudData;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudClusterType;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType;

import static com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType.CLUSTER;

/**
 * Created by mao on 2016/11/6.
 */
@Beta
public class MaoCloudClusterData extends MaoCloudData<MaoCloudClusterType> {

    @Override
    public MaoCloudDataType type() {
        return CLUSTER;
    }
}
