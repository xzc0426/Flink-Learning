package com.xzc.watermark

import com.xzc.caseclass.EventData
import org.apache.flink.api.common.eventtime._
import org.apache.flink.streaming.api.scala._

import java.time.Duration

/**
 * Created by Xu on 2022/12/3.
 * describe: 
 */
object WaterMarkPractice {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream = env.fromElements(
      EventData("a", 16, 1L)
      , EventData("a", 18, 2L)
      , EventData("a", 20, 1L)
      , EventData("b", 16, 3L))
    //设置自动生成水位线周期时间，默认200毫秒
    env.getConfig.setAutoWatermarkInterval(500L)


    //1. 有序流生成水位线 forMonotonousTimestamps
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forMonotonousTimestamps[EventData]()
        .withTimestampAssigner(new SerializableTimestampAssigner[EventData] {
          override def extractTimestamp(eventData: EventData, recordTimestamp: Long): Long = eventData.timeStamp
        }))

    //2. 乱序流生成水位线 forBoundedOutOfOrderness
    dataStream.assignTimestampsAndWatermarks(
      WatermarkStrategy
        .forBoundedOutOfOrderness[EventData](Duration.ofSeconds(2L)) //数据延迟等待时间
        .withTimestampAssigner(new SerializableTimestampAssigner[EventData] {
          override def extractTimestamp(eventData: EventData, recordTimestamp: Long): Long = eventData.timeStamp
        }))

    //3. 自定义生成水位线
    dataStream.assignTimestampsAndWatermarks(new CustomWatermarkStrategy)

  }


  class CustomWatermarkStrategy extends WatermarkStrategy[EventData] {

    //重写createTimestampAssigner，指定数据中的时间戳字段
    override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[EventData] = {
      new SerializableTimestampAssigner[EventData] {
        override def extractTimestamp(eventData: EventData, recordTimestamp: Long): Long = eventData.timeStamp
      }
    }

    override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[EventData] = {
      new WatermarkGenerator[EventData] {
        //最大延迟时间,单位毫秒
        val delay = 5000L
        //当前最大时间戳
        var maxTs = Long.MinValue + delay + 1L

        //事件触发水位线，每来一条数据进行调用
        override def onEvent(eventData: EventData, eventTimestamp: Long, output: WatermarkOutput): Unit = {
          maxTs = math.max(maxTs, eventData.timeStamp)
          val watermark = new Watermark(maxTs)
          output.emitWatermark(watermark)
        }

        //周期性生成水位线
        override def onPeriodicEmit(output: WatermarkOutput): Unit = {
          val watermark = new Watermark(maxTs - delay - 1)
          output.emitWatermark(watermark)
        }

      }

    }
  }
}

