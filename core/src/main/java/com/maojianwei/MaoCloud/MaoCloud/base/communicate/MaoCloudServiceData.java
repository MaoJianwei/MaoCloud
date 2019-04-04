package com.maojianwei.MaoCloud.MaoCloud.base.communicate;

import com.maojianwei.MaoCloud.MaoCloud.api.communicate.MaoCloudData;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType;
import com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudServiceType;

import static com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype.MaoCloudDataType.INTERNAL_SERVICE;

/**
 * Created by mao on 2016/11/6.
 */
public abstract class MaoCloudServiceData extends MaoCloudData<MaoCloudServiceType> {

    MaoCloudServiceType serviceId = getSubType(); // TODO

    @Override
    public MaoCloudDataType type() {
        return INTERNAL_SERVICE;
    }
}
