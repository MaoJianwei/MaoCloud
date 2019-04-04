package com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype;

public enum MaoCloudMonitorType {

    STATUS_REPORT(0),
    CONFIG_GET(1),
    CONFIG_MODIFY(2),
    CONFIG_MODIFY_RESULT(3),
    REQ_SHUTDOWN(4),
    REQ_REBOOT(5),
    REQ_SYSTEM_UPGRADE(6);

    private final int subType;
    MaoCloudMonitorType(int subType) {
        this.subType = subType;
    }
    public int get() {
        return subType;
    }
}
