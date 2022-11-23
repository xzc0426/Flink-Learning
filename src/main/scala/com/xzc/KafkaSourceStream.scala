package com.xzc

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka._

import java.util.Properties

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object KafkaSourceStream {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop102:9092")
    properties.setProperty("group.id", "KafkaSourceStream")

    val dataStream: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("KafkaSourceStream", new SimpleStringSchema(), properties))
    dataStream.print()

    env.execute()
  }

}
