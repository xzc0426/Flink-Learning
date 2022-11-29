package com.xzc.transform

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/27.
 * describe: 自定义 RichMapFunction
 */
object CustomTransformRichFunction {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    val dataStream: DataStream[(String, String, Int)] = env.fromElements(("a", "q", 1), ("a", "w", 2), ("a", "e", 1), ("b", "r", 10), ("b", "t", 1))

    dataStream.map(new CustomRichMap).print()
    env.execute()
  }

  class CustomRichMap extends RichMapFunction[(String, String, Int), String] {

    override def open(parameters: Configuration): Unit = {
      println(s"作业开始：${getRuntimeContext.getJobId}")
      println(s"并行子任务的编号：${getRuntimeContext.getIndexOfThisSubtask}")
    }

    override def map(in: (String, String, Int)): String = in._1


    override def close(): Unit = {
      println(s"作业结束：${getRuntimeContext.getJobId}")
    }
  }
}
