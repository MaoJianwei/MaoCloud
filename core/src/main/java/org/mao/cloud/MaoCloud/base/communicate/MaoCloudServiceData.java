package org.mao.cloud.MaoCloud.base.communicate;

import org.mao.cloud.MaoCloud.api.MaoCloudData;
import org.mao.cloud.MaoCloud.base.MaoCloudDataType;

import static org.mao.cloud.MaoCloud.base.MaoCloudDataType.INTERNAL_SERVICE;

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
