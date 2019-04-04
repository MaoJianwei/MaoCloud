package com.maojianwei.MaoCloud.MaoCloud.base.communicate;

import com.maojianwei.MaoCloud.MaoCloud.api.communicate.MaoCloudData;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudMonitorType;

import static com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType.MONITOR;

/**
 * Created by mao on 2016/11/6.
 */
public class MaoCloudMonitorData extends MaoCloudData<MaoCloudMonitorType> {

    private MaoCloudMonitorData() {

    }

    @Override
    public MaoCloudDataType type() {
        return MONITOR;
    }


    public static Builder builder() {
        return new Builder();
    }
    private static class Builder {
        public MaoCloudMonitorData build() {
            return new MaoCloudMonitorData();
        }
    }
}
