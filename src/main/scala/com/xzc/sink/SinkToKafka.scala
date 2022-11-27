package com.xzc.sink

import com.xzc.CustomSource
import com.xzc.caseclass.Event
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

/**
 * Created by Xu on 2022/11/27.
 * describe: 
 */
object SinkToKafka {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    dataStream.map(_.toString).addSink(new FlinkKafkaProducer[String]("hadoop102:9092", "topic", new SimpleStringSchema()))
    env.execute()

  }
}
