import com.alibaba.fastjson.{JSON, JSONObject}

import java.util
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by Xu on 2022/11/28.
 * describe: 
 */
object DataTransform {
  def main(args: Array[String]): Unit = {

    val json =
      """
        |{"DT":"2022-11-23 11:00:00",
        |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
        |"MDATA":[{"VAL":"111","Q":"0","TAG":"Ia"},{"VAL":"2","Q":"0","TAG":"Ic"}],
        |"SSID":"EIA"}
        |""".stripMargin

    val resultMap = mutable.HashMap[String, String]()
    val jSONObject: JSONObject = JSON.parseObject(json)
    val mdata: String = jSONObject.getString("MDATA").trim
    val mpid: String = jSONObject.getString("MPID")
    resultMap += ("MPID" -> mpid)
    val mdataList: util.List[util.HashMap[String, String]] = JSON.parseArray(mdata, classOf[util.HashMap[String, String]])
    /* mdataList.forEach(t => {
       val value: String = t.get("VAL").toString
       resultMap += ("VAL", value)
       resultMap
     })*/

    //    val toString1: String = mdataList.get(0).get("VAL").toString
    //    resultMap+=("VAL"->toString1)
    import collection.JavaConversions._
    val size: Int = mdataList.size()
    for (elem <- mdataList.toList) {
      for (i <- 1 to size) {
        val str: String = elem.get("VAL")
        resultMap += ("VAL" + i -> str)
      }
    }

    /*   val value: util.Iterator[util.HashMap[String, String]] = mdataList.iterator()
      while (value.hasNext) {

        println(value.next().get("VAL").toString)
      }*/


    println(resultMap)

  }
}
