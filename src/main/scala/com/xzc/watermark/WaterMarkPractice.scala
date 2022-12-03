package com.xzc.watermark

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, TimestampAssigner, TimestampAssignerSupplier, Watermark, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._

import java.time.Duration

/**
 * Created by Xu on 2022/12/3.
 * describe: 
 */
object WaterMarkPractice {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream = env.fromElements(("a", "q", 1L), ("a", "w", 2L), ("a", "e", 1L), ("b", "r", 3L), ("b", "t", 5L))

    //有序流生成水位线
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forMonotonousTimestamps[(String, String, Long)]()
        .withTimestampAssigner(new SerializableTimestampAssigner[(String, String, Long)] {
          override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
        }))

    //乱序流生成水位线
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forBoundedOutOfOrderness[(String, String, Long)](Duration.ofSeconds(2)) //数据延迟等待时间
        .withTimestampAssigner(new SerializableTimestampAssigner[(String, String, Long)] {
          override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
        }))

    dataStream.assignTimestampsAndWatermarks(new CustomWatermarkStrategy)
  }


  //自定义周期水位线生成
  class CustomWatermarkStrategy extends WatermarkStrategy[(String, String, Long)] {

    //重写createTimestampAssigner，指定数据中的时间戳字段
    override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[(String, String, Long)] = {
      new SerializableTimestampAssigner[(String, String, Long)] {
        override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
      }

    }

    override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[(String, String, Long)] = {
      new WatermarkGenerator[(String, String, Long)] {
        //延迟时间
        val delay = 5000L
        //属性保存最大时间戳
        var maxTs = Long.MinValue + delay + 1

        //事件触发水位线
        override def onEvent(event: (String, String, Long), eventTimestamp: Long, output: WatermarkOutput): Unit = {
          maxTs = math.max(maxTs, event._3)
        }

        /* /**
          * 如果不想周期性生成，在onEvent直接发送水位线,onPeriodicEmit方法则废除，未测试
          *
          * @param event
          * @param eventTimestamp
          * @param output
          */
         override def onEvent(event: (String, String, Long), eventTimestamp: Long, output: WatermarkOutput): Unit = {
           output.emitWatermark(new Watermark(delay))
         }*/

        //周期性生成水位线
        override def onPeriodicEmit(output: WatermarkOutput): Unit = {
          val watermark = new Watermark(maxTs - delay - 1)
          output.emitWatermark(watermark)
        }

      }

    }
  }


}
