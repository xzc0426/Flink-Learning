import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.flink.api.common.functions.{RichFlatMapFunction, RichMapFunction}
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.util.Collector

import java.util
import java.util.List
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable


/**
 * Created by Xu on 2022/11/28.
 * describe: 
 */
object DataQualityAnalysis {
  def main(args: Array[String]): Unit = {
    val json =
      """
        |{"DT":"2022-11-23 11:00:00",
        |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
        |"MDATA":[{"VAL":"3","Q":"0","TAG":"Ia"},{"VAL":"4","Q":"0","TAG":"Ic"}],
        |"SSID":"EIA"}
        |""".stripMargin
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)

    val dataStream: DataStream[String] = environment.fromElements(json)
    dataStream.map(new CustomMap).flatMap(new CustomFlatMap).print()
    environment.execute()
  }


  class CustomMap extends RichMapFunction[String, mutable.HashMap[String, String]] {
    override def map(str: String): mutable.HashMap[String, String] = {
      //      val resultMap = new util.HashMap[String, String]()
      val resultMap: mutable.HashMap[String, String] = mutable.HashMap[String, String]()

      val jSONObject: JSONObject = JSON.parseObject(str)
      val mdata: String = jSONObject.getString("MDATA")
      val mpid: String = jSONObject.getString("MPID")
      resultMap += ("MPID" -> mpid) //resultMap.put("MPID", mpid)
      val mdataList: util.List[util.HashMap[String, String]] = JSON.parseArray(mdata, classOf[util.HashMap[String, String]])
      var i = 0
      for (elem <- mdataList.toList) {
        val str: String = elem.get("VAL")
        i += 1
        resultMap += ("VAL" + i -> str)
        println(s"$str || ${i}")
      }
//      resultMap.+=("MDATA" -> mdataList.toString)
      resultMap += ("MDATANUM" -> mdataList.size().toString)
      //      println(s"mdataList:$mdataList")
      resultMap
    }
  }

  class CustomFlatMap extends RichFlatMapFunction[mutable.HashMap[String, String], mutable.HashMap[String, String]] {

    override def flatMap(value: mutable.HashMap[String, String], out: Collector[mutable.HashMap[String, String]]): Unit = {
      val mdatanum: String = value.getOrElse("MDATANUM", "3")
      println(s"mdatanum:\n$mdatanum")

      mdatanum match {
        case "2" => {
          val num1: String = value.getOrElse("VAL1", 1).toString
          val num2: String = value.getOrElse("VAL2", 1).toString
          val resultNum: Double = (num1.toDouble + num2.toDouble) / 2

          value += ("resultNum" -> resultNum.toString)
          out.collect(value)
        }
        case _ =>
      }

    }
  }


}