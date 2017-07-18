package com.maojianwei.MaoCloud.MaoCloud.base.communicate;

import com.maojianwei.MaoCloud.MaoCloud.api.MaoCloudData;
import com.maojianwei.MaoCloud.MaoCloud.base.MaoCloudDataType;

import static com.maojianwei.MaoCloud.MaoCloud.base.MaoCloudDataType.INTERNAL_SERVICE;

/**
 * Created by mao on 2016/11/6.
 */
public class MaoCloudServiceData extends MaoCloudData {

    Byte serviceId = getSubType();

    @Override
    public MaoCloudDataType type() {
        return INTERNAL_SERVICE;
    }
}
