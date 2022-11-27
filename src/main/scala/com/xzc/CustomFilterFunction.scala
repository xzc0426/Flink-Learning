package com.xzc

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.streaming.api.scala._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer

/**
 * Created by Xu on 2022/11/24.
 * describe: 
 */
object CustomFilterFunction {
  val log: Logger = LoggerFactory.getLogger(classOf[CustomFilter])

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val json =
      """
        |{"DT":"2022-11-23 11:00:00",
        |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
        |"MDATA":[{"VAL":"2","Q":"0","TAG":"Ia"},{"VAL":"","Q":"0","TAG":"Ic"}],
        |"SSID":"EIA"}
        |""".stripMargin

    val json2 =
      """
        |{"MPID":"0008114560317215753862",
        |"DT":"2022-11-23 11:40:00",
        |"SSID":"EMS",
        |"FROZEN":"0",
        |"PERIOD":"0",
        |"IDTYPE":"bpmpid",
        |"MDATA":[{"TAG":"P","VAL":"0.000000","Q":"0"},{"TAG":"Q","VAL":"","Q":"0"},{"TAG":"cos","VAL":"0.000000","Q":"0"},{"TAG":"Ia","VAL":"0.000000","Q":"0"},{"TAG":"I","VAL":"0.000000","Q":"0"},{"TAG":"Ib","VAL":"0.000000","Q":"0"},{"TAG":"Ic","VAL":"1","Q":"0"}]}
        |""".stripMargin

    val json3 =
      """
        |{"DT":"2022-11-23 11:00:00",
        |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
        |"MDATA":{"TAG":"P","VAL":" ","Q":"0"},
        |"SSID":"EIA"}
        |""".stripMargin
    val dataStream: DataStream[String] = env.fromElements(json3)
    dataStream.filter(_.contains("MDATA")).filter(new CustomFilter2).print("异常JSON:")

    env.execute()

  }

  private class CustomFilter extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      val jsonObject: JSONObject = JSON.parseObject(t)
      //通过getString获取数组，避免空指针，后续判断数据格式
      val mdata: String = jsonObject.getString("MDATA")
      val mdataObjects: Array[AnyRef] = JSON.parseArray(mdata).toArray
      val listBuffer = new ListBuffer[String]()
      for (elem <- mdataObjects) {
        val parseObject: JSONObject = JSON.parseObject(elem.toString)
        val val1: String = parseObject.getString("VAL")
        listBuffer += val1
      }
      if (listBuffer.contains("null") || listBuffer.contains("")) {
        println(s"\nlistBuffer:$listBuffer\n缓冲区大小:${listBuffer.size}\n异常json:$jsonObject")
        listBuffer.clear()
        true
      } else {
        listBuffer.clear()
        false
      }
    }

  }

  //判断非数组情况
  private class CustomFilter2 extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      val jsonObject: JSONObject = JSON.parseObject(t)
      //通过getString获取数组，避免空指针，后续判断数据格式
      val mdata: String = jsonObject.getString("MDATA")
      if (mdata.startsWith("[") && "null" != mdata) {
        log.warn(s"mdata：$mdata 是数组")
        val mdataObjects: Array[AnyRef] = JSON.parseArray(mdata).toArray
        val listBuffer = new ListBuffer[String]()
        for (elem <- mdataObjects) {
          val parseObject: JSONObject = JSON.parseObject(elem.toString)
          val val1: String = parseObject.getString("VAL")
          listBuffer += val1
        }
        if (listBuffer.contains("null") || listBuffer.contains("")) {
          println(s"\nlistBuffer:$listBuffer\n缓冲区大小:${listBuffer.size}\n异常json:$jsonObject")
          listBuffer.clear()
          true
        } else {
          listBuffer.clear()
          false
        }
      } else if (mdata.startsWith("{")) {
        log.warn(s"mdata：$mdata 不是数组")
        val mdataObjects: JSONObject = JSON.parseObject(mdata)
        val str: String = mdataObjects.getString("VAL")
        if (str == "null" || str.trim == "") {
          println(s"出现异常值:$str\n 异常对象:$mdataObjects")
          true
        } else {
          false
        }
      } else {
        false
      }

    }

  }

  private class CustomFilter3 extends FilterFunction[String] {
    override def filter(t: String): Boolean = {

      val jsonObject: JSONObject = JSON.parseObject(t)
      //通过getString获取数组，避免空指针，后续判断数据格式
      val mdata: String = jsonObject.getString("MDATA")
      if (mdata.startsWith("[") && ("null" != mdata)) {
        val mdataObjects: Array[AnyRef] = JSON.parseArray(mdata).toArray
        for (elem <- mdataObjects) {
          val parseObject: JSONObject = JSON.parseObject(elem.toString)
          val val1: String = parseObject.getString("VAL")
          //发送异常数据
          /*   if (val1 == "null" || val1.trim == "") {
          println(s"出现异常数据:$val1 || $elem")
          return true
        } else {
          println(s"正常数据:$val1 || $elem")
          return false
        }

      }*/
          while ("null" == val1 || "" == val1.trim) {
            println(s"出现异常数据:$val1 \n 异常对象:$elem")
            return true
          }
        }
        false
      } else {
        true
      }
    }
  }

  //直接输出数据
  private class CustomFilter4 extends FilterFunction[String] {
    override def filter(t: String): Boolean = {
      true
    }
  }
}
