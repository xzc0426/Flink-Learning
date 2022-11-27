package com.xzc

import com.xzc.caseclass.Event
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/27.
 * describe:
 * shuffle、rebalance 分区练习
 */
object physicalPartitionPractice {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    //shuffle随机分发数据到4个分区
    //    dataStream.shuffle.print().setParallelism(4)
    //采用Round-Robin算法轮询发送，各轮询各的，去掉 .rebalance 后输出其实也是按照轮询分发数据
    dataStream.rebalance.print().setParallelism(3) //  等同 dataStream.print().setParallelism(3)
    env.execute()
  }

}
