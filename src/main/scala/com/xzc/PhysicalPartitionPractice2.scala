package com.xzc

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/27.
 * describe: rescale 练习
 * rescale和rebalance的区别：
 * rebalance 所有的下游可以认为是在同一个group，数据上游和下游是 m*n 条链路，每个上游数据依次轮询发送给所有的下游；
 * rescale 是数据上游均匀分配下游数据，比如2个并发的上游数据，下游为4个分区，那么每个上游均匀分发被分配的两个分区；
 * rescale 可以结合taskmanager进行优化，每个分组只在同一个taskmanager内进行数据轮询发送，因为他们都在同一个物理节点，可以避免网络IO
 */
object PhysicalPartitionPractice2 {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[Int] = env.addSource(new RichParallelSourceFunction[Int] {
      override def run(ctx: SourceFunction.SourceContext[Int]): Unit = {
        for (elem <- 0 to 7) {
          //获取当前分区号，如果是0，发送偶数，否则发送奇数
          if (getRuntimeContext.getIndexOfThisSubtask == (elem) % 2)
            ctx.collect(elem)
        }
      }

      override def cancel(): Unit = ???
    }).setParallelism(2)

    dataStream.rescale.print().setParallelism(4)

    /**
     * //  数据		 上游	下游	    数据
     * //					       1 		  0 4
     * //0 2 4 6    0
     * //					       2 		  2 6
     * //================================
     * //					       3		  1 5
     * //1 3 5 7    1
     * //					       4		  3 7
     *
     */
    env.execute()
  }
}
