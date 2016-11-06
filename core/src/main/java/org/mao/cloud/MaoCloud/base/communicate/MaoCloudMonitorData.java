package org.mao.cloud.MaoCloud.base.communicate;

import org.mao.cloud.MaoCloud.api.MaoCloudData;
import org.mao.cloud.MaoCloud.base.MaoCloudDataType;

import static org.mao.cloud.MaoCloud.base.MaoCloudDataType.MONITOR;

/**
 * Created by mao on 2016/11/6.
 */
public class MaoCloudMonitorData extends MaoCloudData {

    @Override
    public MaoCloudDataType type() {
        return MONITOR;
    }
}
