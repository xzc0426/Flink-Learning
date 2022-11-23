import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.xzc.TransformExercise.MyJson
import org.apache.avro.SchemaBuilder.array

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
        |"MDATA":[{"VAL":"111","Q":"0","TAG":"Ia"}],
        |"SSID":"EIA"}
        |""".stripMargin

  /*  val json1: MyJson = JSON.parseObject(json, classOf[MyJson])
    val mdata: String = json1.MDATA

    val array: JSONArray = JSON.parseArray(mdata)
    println(array.get(0))*/
  val jsonObject: JSONObject = JSON.parseObject(json)
    val value = jsonObject.getString("MDATA")
    val array1: JSONArray = JSON.parseArray(value)

    val json2: MdataJson = JSON.parseObject(array1.get(0).toString, classOf[MdataJson])
    if (json2.VAL.equals("null")||json2.VAL.equals(null)|| json2.VAL.trim == ""){
      println("kong")
    }

    val str: String = jsonObject.getString("MDATA")
    println(str+"!")
    if (str != null || str.trim != "" || str != "null"){
      val string: String = JSON.parseArray(str).getString(0)
      println(JSON.parseObject(string).getString("val".toUpperCase))
    }


    val jsonarray: JSONArray = JSON.parseArray(str)
    val json1: MdataJson = jsonarray.getObject(0, classOf[MdataJson])
    println(json1.VAL)

  }
  case class MdataJson(VAL:String,Q:String,TAG:String)
}
