package com.xzc

import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/27.
 * describe: 自定义分区
 */
object CustomPartitioner {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    val dataStream = env.addSource(new CustomSource)
    val dataStream = env.fromElements(1, 2, 3, 4)

    dataStream.partitionCustom(new Partitioner[Int] {
      override def partition(key: Int, numPartitions: Int): Int = {
        key % 2
      }
    }, t => t).print()

    env.execute()

  }

  /*  class CustomPartitioner(event: Event) extends Partitioner[String] {
      override def partition(key: String, numPartitions: Int): Int = {
        event.age % 2
      }
    }*/
}
