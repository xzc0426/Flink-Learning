package com.xzc

import org.apache.flink.api.scala._

/**
 * Created by Xu on 2022/11/6.
 * describe: 
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val ds: DataSet[String] = env.readTextFile("input/words.txt")
    val wordAndCount: DataSet[(String, Int)] = ds.flatMap(_.split(" ")).map(word => (word, 1))
    //按照单词进行分组
    val wordAndCountGroup: GroupedDataSet[(String, Int)] = wordAndCount.groupBy(0)
    //按照元素位置1进行聚合，也就是（k,v）二元数组中的第2个元素v
    val summed: AggregateDataSet[(String, Int)] = wordAndCountGroup.sum(1)
    summed.print()


  }
}
