package com.xzc

import com.xzc.CustomTransform.CustomMap
import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.scala._

import scala.collection.mutable

/**
 * Created by Xu on 2022/11/25.
 * describe: 
 */
object CustomAgg {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(4)

    val dataStream: DataStream[Event] = env.addSource(new CustomSource)
    //    dataStream.keyBy(_.name)
    dataStream.keyBy(new CustomKeySelector()).map(new CustomMap2()).map(new CustomMap3).print()

    env.execute()
  }

  class CustomKeySelector extends KeySelector[Event, String]() {
    override def getKey(in: Event): String = in.name
  }

  class CustomMap2 extends MapFunction[Event, mutable.HashMap[String, Event]]() {
    override def map(t: Event): mutable.HashMap[String, Event] = {
      mutable.HashMap(t.name -> t)
    }
  }

  class CustomMap3 extends MapFunction[mutable.HashMap[String, Event], Iterable[String]]() {
    override def map(t: mutable.HashMap[String, Event]): Iterable[String] = {
      val set: Iterable[String] = t.keys
      set
    }
  }
}
