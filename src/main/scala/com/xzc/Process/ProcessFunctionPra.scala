package com.xzc.Process

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * Created by Xu on 2023/4/23.
 * Describe: 
 */
object ProcessFunctionPra {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val stream: DataStream[Event] = env.addSource(new CustomSource).assignAscendingTimestamps(_.timeStmp)

    stream.process(new ProcessFunction[Event, String] {
      override def processElement(value: Event, ctx: ProcessFunction[Event, String]#Context, out: Collector[String]): Unit = {

        if (value.age < 10) {
          out.collect(value.name)
          val cdt: Long = ctx.timerService().currentProcessingTime()
          out.collect("当前时间：" + cdt)
          //          ctx.timerService().registerProcessingTimeTimer(cdt + 10000)
        }
      }

      //      override def onTimer(timestamp: Long, ctx: ProcessFunction[Event, String]#OnTimerContext, out: Collector[String]): Unit = {
      //        //        super.onTimer(timestamp, ctx, out)
      //        out.collect("定时器触发:" + timestamp)
      //      }
    }).print()


    env.execute()
  }

}
