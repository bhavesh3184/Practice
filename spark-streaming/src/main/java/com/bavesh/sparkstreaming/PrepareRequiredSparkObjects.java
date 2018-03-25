package com.bavesh.sparkstreaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PrepareRequiredSparkObjects {

    public static class PrepareObjects {


        String brokers = "";
        String topics = "";
        String master = "";
        String appname = "";

        String  KEY_DESERIALIZER_CLASS_CONFIG =  "";
        String  VALUE_DESERIALIZER_CLASS_CONFIG = "";

        String  AUTO_OFFSET_RESET_CONFIG = "";
        String  GROUP_ID_CONFIG = "";
        String  ENABLE_AUTO_COMMIT_CONFIG = "";
        String batchDuration = "1";


        public PrepareObjects() {

            LoadProperties();
        }

        public String GetMaster()
        {
            return master;
        }

        public String GetAppName()
        {
            return appname;
        }

        public void LoadProperties() {
            try {
                InputStream inputStream = SparkConsumer.class.getResourceAsStream("/config.properties");
                Properties props = new Properties();
                props.load(inputStream);

                appname = props.getProperty("appname");
                master = props.getProperty("master");
                brokers = props.getProperty("brokers");
                topics = props.getProperty("topics");
                KEY_DESERIALIZER_CLASS_CONFIG =   props.getProperty("KEY_DESERIALIZER_CLASS_CONFIG");
                VALUE_DESERIALIZER_CLASS_CONFIG =  props.getProperty("VALUE_DESERIALIZER_CLASS_CONFIG");
                AUTO_OFFSET_RESET_CONFIG = props.getProperty("AUTO_OFFSET_RESET_CONFIG");
                GROUP_ID_CONFIG = props.getProperty("GROUP_ID_CONFIG");
                ENABLE_AUTO_COMMIT_CONFIG = props.getProperty("ENABLE_AUTO_COMMIT_CONFIG");
                batchDuration = props.getProperty("batchDuration");

                inputStream.close();

            } catch (FileNotFoundException ex) {
                System.out.print("Error : " + ex.getMessage());
            } catch (IOException ex) {
                System.out.print("Error: " + ex.getMessage());
            }
        }


        public SparkConf CreateSparkConf(String appname,String master) {

            return new SparkConf().setAppName(appname).setMaster(master);
        }

        public JavaStreamingContext CreateJavaStreamingContext(SparkConf sparkConf) {

            return new JavaStreamingContext(sparkConf, Durations.seconds(Long.parseLong(batchDuration)));
        }

        public Map<String,Object> CreateKafkaParamasObject()
        {
            Map<String, Object> kafkaParams = new HashMap<>();

            kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
            kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER_CLASS_CONFIG);
            kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,VALUE_DESERIALIZER_CLASS_CONFIG);

            kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_CONFIG);
            kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
            kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT_CONFIG);

            return kafkaParams;
        }


        public JavaInputDStream<ConsumerRecord<String, String>> CreateJavaInputDStream(JavaStreamingContext jssc,
                                                                                       Map<String, Object> kafkaParams) {

            Set<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));

            // Create direct kafka stream with brokers and topics
            return KafkaUtils.createDirectStream(
                    jssc,
                    LocationStrategies.PreferConsistent(),
                    ConsumerStrategies.Subscribe(topicsSet, kafkaParams));
        }

        public JavaPairDStream<String, Integer> GetWordCount(JavaInputDStream<ConsumerRecord<String, String>> messages) {
            // Get the lines, split them into words, count the words and print
            JavaDStream<String> lines = messages.map(ConsumerRecord::value);
            JavaDStream<String> words = lines.map(x ->x);
            JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
                    .reduceByKey((i1, i2) -> i1 + i2);
            return wordCounts;
        }


    }
}
