package com.xzc

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._

/**
 * Created by Xu on 2022/11/7.
 * describe: 
 */
object StreamWorldCountBySocket {

  def main(args: Array[String]): Unit = {

    val parameterTool: ParameterTool = ParameterTool.fromArgs(args)

    val hostname: String = parameterTool.get("hostname")

    val port: Int = parameterTool.getInt("port")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = env.socketTextStream(hostname, port)
    dataStream.flatMap(_.split(" ")).map(t => (t, 1)).keyBy(_._1).sum(1).print()

    env.execute()
  }

}
