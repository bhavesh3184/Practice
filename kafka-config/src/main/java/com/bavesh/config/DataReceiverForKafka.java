package com.bavesh.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class DataReceiverForKafka {

        public static void main(String[] args) {

            Properties config = new Properties();

            config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
            config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
            config.put(ConsumerConfig.GROUP_ID_CONFIG,"2402");
            config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");

            KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(config);
            consumer.subscribe(Arrays.asList("firstTest"));

            while(true)
            {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println("Partition: " + consumerRecord.partition() +
                            ", Offset: " + consumerRecord.offset() +
                            ", Key: " + consumerRecord.key() +
                            ", Value: " + consumerRecord.value());

                }
                consumer.commitSync();

            }

        }

    }

