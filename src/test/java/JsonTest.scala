import com.alibaba.fastjson2.{JSON, JSONArray, JSONObject}

import java.util
import scala.collection.mutable

/**
 * Created by Xu on 2022/11/23.
 * describe: 
 */
object JsonTest {
  def main(args: Array[String]): Unit = {

    val json =
      """
        |{"DT":"2022-11-23 11:00:00",
        |"MPID":"0004000000000000000000000000000000000000000000000000000215260215",
        |"MDATA":[{"VAL":"111","Q":"0","TAG":"Ia"},{"VAL":"null","Q":"0","TAG":"Ic"}],
        |"SSID":"EIA"}
        |""".stripMargin
    //,{"VAL":"222","Q":"0","TAG":"Ic"}
    /*  val json1: MyJson = JSON.parseObject(json, classOf[MyJson])
      val mdata: String = json1.MDATA

      val array: JSONArray = JSON.parseArray(mdata)
      println(array.get(0))*/
    val jsonObject: JSONObject = JSON.parseObject(json)
    val value = jsonObject.getString("MDATA")
    val array1: JSONArray = JSON.parseArray(value)

    val json2: MdataJson = JSON.parseObject(array1.get(0).toString, classOf[MdataJson])
    if (json2.VAL.equals("null") || json2.VAL.equals(null) || json2.VAL.trim == "") {
      println("kong")
    }

    val str: String = jsonObject.getString("MDATA")
    println(str + "!")
    if (str != null || str.trim != "" || str != "null") {
      val string: String = JSON.parseArray(str).getString(0)
      println(JSON.parseObject(string).getString("val".toUpperCase))
    }


    val jsonarray: JSONArray = JSON.parseArray(str)
//    val json1: MdataJson = jsonarray.getObject(0, classOf[MdataJson])
//    println(json1.VAL)

    println("======解析jsonarray======")
    //解析jsonarray
    val dataMap = new mutable.HashMap[String, String]

    val nObject: JSONObject = JSON.parseObject(json)
    val str1: String = nObject.getString("MDATA")
    val array2: JSONArray = JSON.parseArray(str1)
    val value1: util.Iterator[AnyRef] = array2.iterator()
    /*while (value1.hasNext) {
      val value2: AnyRef = value1.next
      val entry= value2
      dataMap.put(entry.getKey.toString, entry.getValue.toString)
    }*/
    //    println(value1.next().toString)


    val array4: Array[AnyRef] = JSON.parseArray(str).toArray

    /*for (elem <- array4) {
      val nObject1 = JSON.parseObject(elem.toString,classOf[MdataJson])
      if(nObject1.VAL == "null"){
        println("发现null")
      }
    }*/
    for (elem <- array4) {
      val nObject1: JSONObject = JSON.parseObject(elem.toString)
      if (nObject1.getString("VAL") == "null") {
        println("发现null " + nObject1.getString("TAG"))
      }

    }



    //    println("json3.VAL"+json3.map(println(_)))

  }

  case class MdataJson(VAL: String, Q: String, TAG: String)
}
