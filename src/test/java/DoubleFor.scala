/**
 * Created by Xu on 2022/11/28.
 * describe: 
 */
object DoubleFor {
  def main(args: Array[String]) {
    var a = 0;
    var b = 0;
    // for 循环
    for( a <- 1 to 3; b <- 1 to 3){
      println( "Value of a: " + a );
      println( "Value of b: " + b );
    }
  }
}
