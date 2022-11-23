package com.xzc

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

    val dataStream2: DataStream[Event] = environment.fromElements(Event("may", 16), Event("w", 18))
    dataStream2.print()

    val events = List(Event("may", 26), Event("w", 28))
    val dataStream3: DataStream[Event] = environment.fromCollection(events)
    dataStream3.print()

    environment.execute()

  }
}

