package com.bavesh.config;

public class SparkConfig implements Config {

    private String appName;
    private String master;
    private long batchDuration;

    public long getBatchDuration() {
        return batchDuration;
    }

    public void setBatchDuration(long batchDuration) {
        this.batchDuration = batchDuration;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
