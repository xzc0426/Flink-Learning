package com.xzc.source

import com.xzc.caseclass.Event
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object CustomParallelSourceFunction {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 使用自定义的数据源
    val dataStream: DataStream[Event] = env.addSource(new CustomSource).setParallelism(2)
    dataStream.print()
    env.execute()

  }
}
