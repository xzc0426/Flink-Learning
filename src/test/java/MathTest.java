/**
 * Created by Xu on 2022/12/2.
 * describe:
 */
public class MathTest {
    public static void main(String[] args) {

        Double p = 2D;
        Double pa = 1.1D;
        Double pb = 1D;

        double v = Math.abs((pa + pb) - p) ;
        double v1 = SplitAndRound(v,2);
        System.out.println(v1);
        double v2 = v1 / p;

        if ((v2 <= 0.05)) {
            System.out.println(v2);
        } else {
            System.out.println("异常" + v2);

        }
        System.out.println(pa / p);
    }
    public static double SplitAndRound(double num, int n) {
        System.out.println("输入"+num);
        num = num * Math.pow(10, n);
        return (Math.round(num)) / (Math.pow(10, n));

    }
}
