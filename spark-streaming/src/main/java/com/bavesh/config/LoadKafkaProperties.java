package com.bavesh.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadKafkaProperties implements ILoadProperties {

    Logger logger = LoggerFactory.getLogger(LoadKafkaProperties.class);

   /*public static void main(String[] args) {
        LoadKafkaProperties loadKafkaProperties = new LoadKafkaProperties();
        loadKafkaProperties.loadProperties();
    }*/

    @Override
    public com.bavesh.config.Config loadProperties() {

        Config conf = ConfigFactory.load();

        KafkaConfig kafkaConfig = new KafkaConfig();

        kafkaConfig.setBrokers(conf.getString("kafka-config.brokers"));
        kafkaConfig.setTopics(conf.getString("kafka-config.topics"));

        HashMap<String,Object> props =  new HashMap<>();
        List<? extends ConfigObject> data2 = conf.getObjectList("kafka-config.properties");
        data2.stream().forEach(e -> {
                    Map<String, Object> plan = e.unwrapped();
                    String key = plan.keySet().toArray()[0].toString();
                    Object value = plan.values().toArray()[0];
                    props.put(key,value);
                }
        );
        kafkaConfig.setProps(props);

       // kafkaConfig.getProps().forEach((x,y) -> System.out.println(x +","+y));
        return kafkaConfig;
    }


}
