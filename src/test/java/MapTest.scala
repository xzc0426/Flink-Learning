import java.lang.Integer.parseInt
import java.util

/**
 * Created by Xu on 2022/11/18.
 * describe: 
 */
object MapTest {
  def main(args: Array[String]): Unit = {
    /* val stringToString = new util.HashMap[String, String]()
     val str: String = stringToString.remove("a")
     println(str)*/


    /*  val s = """This is known as a
                |"multiline" string
                |or 'heredoc' syntax.""".stripMargin

      println(s)*/

    val str = "1,2"
    var list: List[String] = str.split(',').toList
    var newList: List[String] = Nil
   /*    for (elem <- list) {
         if (elem != "1") {
           elem::list
         }
       }
       println(list)*/

    println("================")
    for (elem <- list)
      yield elem != "1"
    println(newList)
    println(str :: newList)
    println("================")

    val list1: List[String] = list.filterNot(_ != "1")
    println(list1)
    println("================")
    val str2:String = "1"
    val i: Int = java.lang.Integer.parseInt(str2)
    val value: Any = parseInt("2")
    println(value)

  }
}
