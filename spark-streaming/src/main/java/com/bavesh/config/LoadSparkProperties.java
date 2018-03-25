package com.bavesh.config;

import com.typesafe.config.ConfigFactory;

public class LoadSparkProperties implements  ILoadProperties {

    @Override
    public  com.bavesh.config.Config loadProperties() {

        com.typesafe.config.Config conf = ConfigFactory.load();

        SparkConfig sparkConfig = new SparkConfig();

        sparkConfig.setAppName(conf.getString("spark-config.appName"));
        sparkConfig.setMaster(conf.getString("spark-config.master"));
        sparkConfig.setBatchDuration(conf.getLong("spark-config.batchDuration"));

        return sparkConfig;
    }

}
