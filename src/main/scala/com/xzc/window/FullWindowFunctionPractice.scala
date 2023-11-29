package com.xzc.window

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2023/1/4.
 * Describe:
 */
object FullWindowFunctionPractice {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
   // 设置并行度为1
    env.setParallelism(1)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource).assignAscendingTimestamps(_.timeStmp)
    // 生成窗口

    env.execute()
  }

}