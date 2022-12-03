package com.xzc.source

import com.xzc.caseclass.Event
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

    val dataStream2: DataStream[Event] = environment.fromElements(Event("may", 16, 1L), Event("w", 18, 1L))
    dataStream2.print()

    val events = List(Event("may", 26, 1L), Event("w", 28, 1L))
    val dataStream3: DataStream[Event] = environment.fromCollection(events)
    dataStream3.print()

    environment.execute()

  }
}

