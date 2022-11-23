package com.xzc

import com.alibaba.fastjson.JSON
import com.xzc.caseclass.Event
import org.apache.flink.api.common.functions.{FilterFunction, MapFunction}
import org.apache.flink.streaming.api.scala._

import scala.util.parsing.json.JSONObject

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object TransformExercise {

  val json =
    """
      |{"DT":"2022-11-23 11:00:00",
      |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
      |"MDATA":[{"VAL":"0","Q":"0","TAG":"Ia"}],
      |"SSID":"EIA"}
      |""".stripMargin

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    val dataStream: DataStream[Event] = environment.fromElements(Event("1", 1), Event("2", 2))
    dataStream.map(new GetNameMap).filter(new CustomFilter).print("999")
    println(json)
    environment.execute()
  }

  class GetNameMap extends MapFunction[Event, String] {
    override def map(t: Event): String = t.name
  }

  class CustomFilter extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      if (t.equals("1")) {
        true
      } else {
        false
      }
    }
  }

/*  class jsonFilter extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      val json1: MyJson = JSON.parseObject(t, classOf[MyJson])
      val mdata: String = json1.MDATA
      mdata

    }
  }*/

  case class MyJson(
                     DT: String, MPID: String, MDATA: String, SSID: String
                   )
}
