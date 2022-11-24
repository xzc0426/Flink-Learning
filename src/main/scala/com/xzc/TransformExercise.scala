package com.xzc

import com.alibaba.fastjson.JSON
import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.{FilterFunction, FlatMapFunction, MapFunction}
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

import scala.util.parsing.json.JSONObject

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object TransformExercise {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    val dataStream: DataStream[Event] = environment.fromElements(Event("w", 1), Event("w", 2), Event("z", 3))
    dataStream.map(new CustomMap).filter(new CustomFilter).flatMap(new CustomFlatMap).print("999")

    environment.execute()
  }

  class CustomMap extends MapFunction[Event, String] {
    override def map(t: Event): String = t.name
  }

  class CustomFilter extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      if (t.equals("w")) {
        true
      } else {
        false
      }
    }
  }

  class CustomFlatMap extends FlatMapFunction[String, String] {
    override def flatMap(t: String, collector: Collector[String]): Unit = {
      if (t == "w") {
        collector.collect("w1")
        collector.collect("w2")
      } else {
        collector.collect("other")
      }
    }
  }

}
