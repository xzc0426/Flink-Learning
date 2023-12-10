package com.xzc.sink

import com.xzc.caseclass.EventData
import com.xzc.source.CustomSource
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/27.
 * describe: 
 */
object SinkToFile {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    val dataStream: DataStream[EventData] = env.addSource(new CustomSource)

    dataStream.map(_.toString)
      //.global 合并为一个流
      //.broadcast 将数据复制到每个流
      .addSink(fileSink)
    env.execute()
  }


  def fileSink: StreamingFileSink[String] = {
    StreamingFileSink.forRowFormat(new Path("./out"), new SimpleStringEncoder[String]("UTF-8")).build()
  }
}
