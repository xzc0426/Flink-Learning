package com.xzc.window

import com.xzc.caseclass.Event
import com.xzc.source.CustomSource
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, SlidingEventTimeWindows, SlidingProcessingTimeWindows, TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.runtime.operators.window.assigners.{SlidingWindowAssigner, TumblingWindowAssigner}

import java.time.Duration


/**
 * Created by Xu on 2022/12/16.
 * describe: 窗口
 */
object CustomWindow {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream: DataStream[Event] = env.addSource(new CustomSource).assignTimestampsAndWatermarks(WatermarkStrategy.forMonotonousTimestamps().withTimestampAssigner(
      new SerializableTimestampAssigner[Event] {
        override def extractTimestamp(element: Event, recordTimestamp: Long): Long = element.timeStmp
      }
    ))
    dataStream.keyBy(_.name)
    //    .window(TumblingEventTimeWindows.of(Time.hours(1),Time.minutes(10)))  //基于事件时间的滚动窗口，第二个参数为 offset
    //    .window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8))) //基于处理时间的滚动窗口，第二个参数为 offset，东八区时间早8小时，-8就变成了UTC时间
    //    .window(SlidingEventTimeWindows.of(Time.minutes(10), Time.minutes(1)))  //基于事件时间的滑动窗口
    //    .window(SlidingProcessingTimeWindows.of(Time.minutes(10), Time.minutes(1))) //基于处理时间的滑动窗口
    //    .window(EventTimeSessionWindows.withGap(Time.seconds(10)))  //基于事件时间的会话窗口
    //    .countWindow(10)  //滚动计数窗口，大小为10
    //    .countWindow(10, 2) //滑动步长为2的计数窗口


    env.execute()
  }

}
