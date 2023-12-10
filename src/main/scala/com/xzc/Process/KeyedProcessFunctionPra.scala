package com.xzc.Process


import com.xzc.caseclass.EventData
import com.xzc.source.CustomSource
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * Created by Xu on 2023/4/23.
 * Describe: 处理时间
 */
object KeyedProcessFunctionPra {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val stream: DataStream[EventData] = env.addSource(new CustomSource)
    stream.keyBy(x => true).process(new KeyedProcessFunction[Boolean, EventData, String] {
      override def processElement(value: EventData, ctx: KeyedProcessFunction[Boolean, EventData, String]#Context, out: Collector[String]): Unit = {

        val cdt: Long = ctx.timerService().currentProcessingTime()
        out.collect("当前时间：" + cdt)
        //注册定时器
        ctx.timerService().registerProcessingTimeTimer(cdt + 10 * 1000L)

      }

      override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Boolean, EventData, String]#OnTimerContext, out: Collector[String]): Unit = {

        out.collect("定时器触发：" + timestamp)

      }
    }).print()
    env.execute()
  }


}


