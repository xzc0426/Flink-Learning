package com.xzc.source

import com.xzc.caseclass.EventData
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object SourceCollectionStream {
  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream1: DataStream[Int] = environment.fromElements(1, 2, 3, 4)
    dataStream1.print()

    val dataStream2: DataStream[EventData] = environment.fromElements(EventData("may", 16, 1L), EventData("w", 18, 1L))
    dataStream2.print()

    val events = List(EventData("may", 26, 1L), EventData("w", 28, 1L))
    val dataStream3: DataStream[EventData] = environment.fromCollection(events)
    dataStream3.print()

    environment.execute()

  }
}

