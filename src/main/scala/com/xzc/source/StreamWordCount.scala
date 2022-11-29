package com.xzc.source

import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/7.
 * describe: 
 */
object StreamWordCount {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream: DataStream[String] = environment.readTextFile("input/words.txt")
    val tuple2: DataStream[(String, Int)] = dataStream.flatMap(_.split(" ")).map(t => (t, 1))
    val tuple2KeyBy: KeyedStream[(String, Int), String] = tuple2.keyBy(_._1)
    val summed: DataStream[(String, Int)] = tuple2KeyBy.sum(1)
    summed.print()
    environment.execute()
  }
}
