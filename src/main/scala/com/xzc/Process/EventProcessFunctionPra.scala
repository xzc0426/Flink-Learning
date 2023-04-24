package com.xzc.Process

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * Created by Xu on 2023/4/24.
 * Describe: 事件时间处理
 */
object EventProcessFunctionPra {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream: DataStream[Event] = env.addSource(new CustomSource).assignAscendingTimestamps(_.timeStmp)
    stream.keyBy(x => true).process(new KeyedProcessFunction[Boolean, Event, String] {
      override def processElement(value: Event, ctx: KeyedProcessFunction[Boolean, Event, String]#Context, out: Collector[String]): Unit = {

        val cdt: Long = ctx.timerService().currentWatermark()
        out.collect(s"当前水印时间:$cdt，当前时间:${value.timeStmp}")
        //注册定时器
        ctx.timerService().registerEventTimeTimer(cdt + 10000L)

      }

      override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Boolean, Event, String]#OnTimerContext, out: Collector[String]): Unit = {

        out.collect("定时器触发：" + timestamp)

      }
    }).print()
    env.execute()
  }


}

