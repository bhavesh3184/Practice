package com.bavesh.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadStreamProperties implements ILoadProperties {

    Logger logger = LoggerFactory.getLogger(LoadStreamProperties.class);

    /*public static void main(String[] args) {
        LoadKafkaProperties loadKafkaProperties = new LoadKafkaProperties();
        loadKafkaProperties.loadProperties();
    }*/

    @Override
    public com.bavesh.config.Config loadProperties() {

        Config conf = ConfigFactory.load();

        StreamConfig streamConfig = new StreamConfig();

        streamConfig.setTopics(conf.getString("stream-config.topics"));

        return streamConfig;
    }


}
