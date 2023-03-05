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
    //设置自动生成水位线周期时间，默认200毫秒
    env.getConfig.setAutoWatermarkInterval(5000L)


    //1. 序流生成水位线 forMonotonousTimestamps
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forMonotonousTimestamps[(String, String, Long)]()
        .withTimestampAssigner(new SerializableTimestampAssigner[(String, String, Long)] {
          override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
        }))

    //2. 乱序流生成水位线 forBoundedOutOfOrderness
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forBoundedOutOfOrderness[(String, String, Long)](Duration.ofSeconds(2L)) //数据延迟等待时间
        .withTimestampAssigner(new SerializableTimestampAssigner[(String, String, Long)] {
          override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
        }))

    //3. 自定义生成水位线
    dataStream.assignTimestampsAndWatermarks(new CustomWatermarkStrategy)

  }


  class CustomWatermarkStrategy extends WatermarkStrategy[(String, String, Long)] {

    //重写createTimestampAssigner，指定数据中的时间戳字段
    override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[(String, String, Long)] = {
      new SerializableTimestampAssigner[(String, String, Long)] {
        override def extractTimestamp(element: (String, String, Long), recordTimestamp: Long): Long = element._3
      }
    }

    override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[(String, String, Long)] = {
      new WatermarkGenerator[(String, String, Long)] {
        //最大延迟时间
        val delay = 5000L
        //属性保存最大时间戳
        var maxTs = Long.MinValue + delay + 1

        //事件触发水位线，每来一条数据进行调用
        override def onEvent(event: (String, String, Long), eventTimestamp: Long, output: WatermarkOutput): Unit = {
          maxTs = math.max(maxTs, event._3)
          val watermark = new Watermark(maxTs)
          output.emitWatermark(watermark)
        }

        /* /**
          * 如果不想周期性生成，则只调用onEvent直接发送水位线
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

