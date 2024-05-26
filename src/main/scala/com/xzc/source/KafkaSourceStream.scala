package com.xzc.source

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka._

import java.util.Properties

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object KafkaSourceStream {
  private val Source_Topic: String = "KafkaSourceStream"

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop102:9092")
    properties.setProperty("group.id", "KafkaSourceStream")
    properties.setProperty("auto.offset.reset", "latest")

    val dataStream: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String](Source_Topic, new SimpleStringSchema(), properties)).setParallelism(3).name("KafkaSourceStream")
    dataStream.print()

    env.execute("KafkaSourceStream")
  }

}
