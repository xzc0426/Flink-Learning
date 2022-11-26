/**
 * Created by Xu on 2022/11/26.
 * describe:
 */
public class StringToNum {
    public static void main(String[] args) {

        String str = "1.000011";

        if (str.contains(".")) {
            Double aDouble = StringToDouble(str);
            System.out.println(aDouble);
        } else {
            Integer anInt = StringToInt(str);
            System.out.println(anInt);
        }

    }

    public static Double StringToDouble(String in) {

        Double aDouble = Double.parseDouble(in);
        return aDouble;
    }

    public static Integer StringToInt(String in) {

        int anInt = Integer.parseInt(in);
        return anInt;
    }

}
