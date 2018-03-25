package com.bavesh.sparkstreaming;

import com.bavesh.config.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SparkConsumer {

    private static Logger logger = LogManager.getLogger(SparkConsumer.class);

    public static void main(String[] args) throws Exception {

        logger.info("Spark Streaming start!!");

        ILoadProperties loadProperties = new LoadKafkaProperties();
        KafkaConfig kafkaConfig = (KafkaConfig) loadProperties.loadProperties();

        ILoadProperties  loadSparkProperties = new LoadSparkProperties();
        SparkConfig sparkConfig  =  (SparkConfig)loadSparkProperties.loadProperties();

        ILoadProperties  loadStreamProperties = new LoadStreamProperties();
        StreamConfig streamConfig  =  (StreamConfig)loadStreamProperties.loadProperties();

        JavaStreamingContext jssc =  new JavaStreamingContext(new SparkConf().setAppName(sparkConfig.getAppName()).setMaster(sparkConfig.getMaster()),
                                         Durations.seconds(sparkConfig.getBatchDuration()));

        Set<String> topicsSet = new HashSet<>(Arrays.asList(streamConfig.getTopics().split(",")));

        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaConfig.getProps()));

        //JavaPairDStream<String, Integer> wordCounts = prepareObjects.GetWordCount(messages);
        // Get the lines, split them into words, count the words and print
        JavaDStream<String> lines = messages.map(ConsumerRecord::value);
        JavaDStream<String> words = lines.map(x ->x);
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2);

        wordCounts.print();

        // Start the computation
        jssc.start();
        jssc.awaitTermination();
    }
}
