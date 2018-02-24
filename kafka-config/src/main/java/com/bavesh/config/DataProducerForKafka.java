package com.bavesh.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class DataProducerForKafka {

    public static void main(String[] args) {

        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        config.put(ProducerConfig.RETRIES_CONFIG,"3");
        config.put(ProducerConfig.LINGER_MS_CONFIG,"1");

        Producer<String,String> producer = new KafkaProducer<String, String>(config);

        try {
            int i = 1;
            while (true) {
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("firstTest", "key" + i, "value" + i);
                producer.send(record);
                i=i+1;

                Thread.sleep(10000);
            }
        }
        catch(Exception ex)
        {
            producer.close();
        }

    }

}
