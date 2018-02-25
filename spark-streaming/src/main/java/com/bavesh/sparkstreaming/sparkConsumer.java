package com.bavesh.sparkstreaming;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class sparkConsumer {

    public static void main(String[] args)  throws Exception{

        Utilities.LocalUtilities  utilities = new  Utilities.LocalUtilities();
        Logger logger= utilities.SetLogging();

        PrepareReuiqredSparkObjects.PrepareObjects prepareObjects = new PrepareReuiqredSparkObjects.PrepareObjects();

        JavaStreamingContext jssc = prepareObjects.CreateJavaStreamingContext(prepareObjects.CreateSparkConf(prepareObjects.GetAppName(),prepareObjects.GetMaster()));

        JavaInputDStream<ConsumerRecord<String, String>> messages = prepareObjects.CreateJavaInputDStream(jssc,prepareObjects.CreateKafkaParamasObject());

        JavaPairDStream<String, Integer> wordCounts = prepareObjects.GetWordCount(messages);

        wordCounts.print();

        // Start the computation
        jssc.start();
        jssc.awaitTermination();
    }
}
