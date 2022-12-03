package com.xzc.source

import com.xzc.caseclass.Event
import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.watermark.Watermark

import java.util.Calendar
import scala.util.Random

/**
 * Created by Xu on 2022/11/23.
 * describe: 实现 ParallelSourceFunction 接口，接口中的泛型是自定义数据源中的类型,此接口支持多并行度读取 source
 */
class CustomSource extends ParallelSourceFunction[Event] {
  // 标志位，用来控制循环的退出
  var running = true

  override def run(sourceContext: SourceFunction.SourceContext[Event]): Unit = {
    val random = new Random()

    val names: Array[String] = Array("a", "b", "c", "d")
    val ages: Array[Int] = Array(11, 12, 13, 14)
    while (running) {

      val event: Event = Event(
        names(random.nextInt(names.length)), // 随机选择一个 name
        ages(random.nextInt(ages.length)), // 随机选择一个 age
        Calendar.getInstance.getTimeInMillis //返回此日历的时间值(以毫秒为单位)
      )

      //调用collect发送数据
      sourceContext.collect(event)
      /*    //发送带时间戳的数据
          sourceContext.collectWithTimestamp(event, event.timeStmp)
          //发送带水位线的数据
          sourceContext.emitWatermark(new Watermark(event.timeStmp - 1L))*/


      Thread.sleep(1000)
    }

  }

  override def cancel(): Unit = running = false
}
