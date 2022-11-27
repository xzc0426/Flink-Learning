package com.xzc

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/26.
 * describe: 
 */
object CustomReduceFunction {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    val dataStream: DataStream[(String, String, Int)] = env.fromElements(("b", "t", 1), ("a", "q", 1), ("a", "w", 2), ("a", "e", 1), ("b", "r", 10), ("b", "e", 1), ("b", "e", 1))
    dataStream.map(t => (t._1, 1L)).keyBy(_._1)
      .reduce(new CustomReduce) //统计每个单词出现的次数
      .keyBy(t => true) //所有数据放到一个分组 keyBy(t => "1")
      .reduce(new CustomMaxBy) //获取最大值
      //.maxBy("_2") //获取最大值
      .print()
    env.execute()

  }

  class CustomReduce extends ReduceFunction[(String, Long)] {
    override def reduce(t: (String, Long), t1: (String, Long)): (String, Long) = {
      (t._1, t._2 + t1._2)

    }
  }

  class CustomMaxBy extends ReduceFunction[(String, Long)] {
    override def reduce(t: (String, Long), t1: (String, Long)): (String, Long) = if (t1._2 >= t._2) t1 else t

  }
}
