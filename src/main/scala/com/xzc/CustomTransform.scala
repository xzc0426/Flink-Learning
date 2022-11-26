package com.xzc

import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.{FilterFunction, FlatMapFunction, MapFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * Created by Xu on 2022/11/23.
 * describe: 包含自定义map、flatmap、filter
 */
object CustomTransform {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    val dataStream: DataStream[Event] = environment.fromElements(Event("w", 1), Event("w", 2), Event("z", 3))
    dataStream.map(new CustomMap).filter(new CustomFilter("w")).flatMap(new CustomFlatMap).print("999")

    environment.execute()
  }

  class CustomMap extends MapFunction[Event, String] {
    override def map(t: Event): String = t.name
  }

  class CustomFilter(key: String) extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      if (t.equals(key)) {
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
