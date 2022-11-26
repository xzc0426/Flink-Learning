package com.xzc

import com.xzc.CustomTransform.CustomMap
import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.scala._

import scala.collection.mutable

/**
 * Created by Xu on 2022/11/25.
 * describe: 自定义分组、聚合
 * 注意：
 * 1. max后续数据只会取第一条数据的非匹配值，maxby会取后续数据的值
 * 2. keyby传参：
 *  ①索引从0-n ,即将弃用
 *  ②位置传参的样式为"_1"-"_n",即将弃用
 *  ③Lambda表达式
 *  ④继承KeySelector，自定义keyby
 * 3.maxby传参：
 *  ①位置传参的样式为"_1"-"_n"
 *  ②索引位置
 *  ③Lambda表达式
 *
 */
object CustomAgg {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    val dataStream = env.fromElements(("a", "q", 1), ("a", "w", 2), ("a", "e", 1), ("b", "r", 10), ("b", "t", 1))
    //    dataStream.keyBy(_.name)
    //    dataStream.keyBy(new CustomKeySelector()) //.map(new CustomMap2()).map(new CustomMap3).print()

    //    dataStream.keyBy(_._1).maxBy(1).print()
    dataStream.keyBy(_._1).max("_3").print()
    env.execute()
  }

  //按照 Event.name 分组
  class CustomKeySelector extends KeySelector[Event, String]() {
    override def getKey(in: Event): String = in.name
  }

  //自定义 Map 返回 HashMap[String, Event]
  class CustomMap2 extends MapFunction[Event, mutable.HashMap[String, Event]]() {
    override def map(t: Event): mutable.HashMap[String, Event] = {
      mutable.HashMap(t.name -> t)
    }
  }

  //自定义 Map 返回 Iterable[String]
  class CustomMap3 extends MapFunction[mutable.HashMap[String, Event], Iterable[String]]() {
    override def map(t: mutable.HashMap[String, Event]): Iterable[String] = {
      val set: Iterable[String] = t.keys
      set
    }
  }
}
