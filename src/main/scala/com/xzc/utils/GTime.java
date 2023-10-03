package com.xzc.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * <li>功能描述: 系统时间格式处理
 */
public class GTime implements Serializable {

    private static final long serialVersionUID = -1705094716330245682L;
    public static final int YYYY = 1;
    public static final int YYYYMM = 2;
    public static final int YYYYMMDD = 3;
    public static final int YYYYMMDDhh = 4;
    public static final int YYYYMMDDhhmm = 5;
    public static final int YYYYMMDDhhmmss = 6;
    public static final int YYYYMMDDhhmmssxxx = 7;
    public static final int YY = 11;
    public static final int YYMM = 12;
    public static final int YYMMDD = 13;
    public static final int YYMMDDhh = 14;
    public static final int YYMMDDhhmm = 15;
    public static final int YYMMDDhhmmss = 16;
    public static final int YYMMDDhhmmssxxx = 17;
    public static final int hh = 24;
    public static final int hhmm = 25;
    public static final int hhmmss = 26;
    public static final int hhmmssxxx = 27;
    public static final int log = 0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 取得本地系统的时间，时间格式由参数决定
     *
     * @param format 时间格式由常量决定
     * @return String 具有format格式的字符串
     */
    public synchronized static String getTime(int format) {
        Calendar time = Calendar.getInstance();
        return getTime(format, time);
    }

    /**
     * <li>功能描述：取得日志时间
     *
     * @return 日志时间
     */
    public synchronized static String getLogTime() {
        return sdf.format(new Date()) + " ";
    }

    /**
     * <li>功能描述：取得常用时间
     *
     * @return 时间
     */
    public synchronized static String getNormalTime(Date date) {
        if (date == null) {
            return sdf.format(new Date());
        }
        return sdf.format(date);
    }

    /**
     * 得到当前时间后若干若干分钟后的时间
     *
     * @param format 时间格式
     * @param m      延时分钟
     * @return 结果时间字符串
     */
    public synchronized static String getAfterTime(int format, int m) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.MINUTE, m);
        return getTime(format, time);
    }

    /**
     * 功能描述：获取两时间相差#天#小时#分#秒#毫秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public synchronized static String getTimes(String date1, String date2) {
        long l = getTimesValue(date1, date2);
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long mmin = l - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s
                * 1000;
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒" + mmin + "毫秒";
    }

    /**
     * 功能描述：获取两时间（yyyyMMddHHmmss）相差，翻译成类似：#天#小时#分#秒的格式
     *
     * @param subtime 两个yyyyMMddHHmmss格式的时间的差值long类型数据
     * @return
     */
    public synchronized static String getDueTimes(long subtime) {
        long day = subtime / (24 * 60 * 60 * 1000);
        long hour = (subtime / (60 * 60 * 1000) - day * 24);
        long min = ((subtime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (subtime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + (day > 0 ? (day + "天") : "") + (hour > 0 ? (hour + "小时") : "")
                + (min > 0 ? (min + "分") : "") + (s > 0 ? (s + "秒") : "");
    }

    public synchronized static String getDueTimesF(long subtime) {
        long day = subtime / (24 * 60 * 60 * 1000);
        long hour = (subtime / (60 * 60 * 1000) - day * 24);
        long min = ((subtime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        // long s = (subtime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + (day > 0 ? (day + "天") : "") + (hour > 0 ? (hour + "小时") : "")
                + (min > 0 ? (min + "分") : "");
    }


    /**
     * 功能描述：获取两时间相差毫秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public synchronized static long getTimesValue(String date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = null;
        Date date = null;
        try {
            now = df.parse(date1);
            date = df.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now.getTime() - date.getTime();
    }

    /**
     * 功能描述：获取两时间相差时间
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public synchronized static long getTimesValue(String date1, String date2, String format) {
        if (format == null || "".equals(format)) {
            format = "yyyyMMddHHmmss";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date now = null;
        Date date = null;
        try {
            now = df.parse(date1);
            date = df.parse(date2);
            return now.getTime() - date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到当前时间之前若干分钟的时间
     *
     * @param format 时间格式
     * @param m      倒退分钟
     * @return 结果时间字符串
     */
    public synchronized static String getBeforTime(int format, int m) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.MINUTE, -m);
        return getTime(format, time);

    }

    /**
     * 得到格式化的时间
     *
     * @param format 时间格式
     * @param time   时间
     * @return 格式化字符串
     */
    private synchronized static String getTime(int format, Calendar time) {
        StringBuffer cTime = new StringBuffer(10);
        int miltime = time.get(Calendar.MILLISECOND);
        int second = time.get(Calendar.SECOND);
        int minute = time.get(Calendar.MINUTE);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int day = time.get(Calendar.DAY_OF_MONTH);
        int month = time.get(Calendar.MONTH) + 1;
        int year = time.get(Calendar.YEAR);
        if (format > 10) {
            year = year - 2000;

        }
        if (format < 20) {
            if ((format < 10))
                cTime.append(year);
            else if (format < 20)
                cTime.append(getFormatTime(year, 2));
        }
        if ((format < 20 && format > 11) || (format > 1 && format < 10))
            cTime.append(getFormatTime(month, 2));
        if ((format < 20 && format > 12) || (format > 2 && format < 10))
            cTime.append(getFormatTime(day, 2));
        if ((format > 13 && format < 20) || (format > 3 && format < 10)
                || (format > 23 && format < 30))
            cTime.append(getFormatTime(hour, 2));
        if ((format > 14 && format < 20) || (format > 4 && format < 10)
                || (format > 24 && format < 30))
            cTime.append(getFormatTime(minute, 2));
        if ((format > 15 && format < 20) || (format > 5 && format < 10)
                || (format > 25 && format < 30))
            cTime.append(getFormatTime(second, 2));
        if ((format > 16 && format < 20) || (format > 6 && format < 10)
                || (format > 26 && format < 30))
            cTime.append(getFormatTime(miltime, 3));
        return cTime.toString();
    }

    /**
     * 产生任意位的字符串
     *
     * @param time   要转换格式的时间
     * @param format 转换的格式
     * @return String 转换的时间
     */
    private synchronized static String getFormatTime(int time, int format) {
        StringBuffer numm = new StringBuffer();
        int length = String.valueOf(time).length();

        if (format < length)
            return null;

        for (int i = 0; i < format - length; i++) {
            numm.append("0");
        }
        numm.append(time);
        return numm.toString().trim();
    }

    /**
     * 将时间转换为新格式
     *
     * @param ftime 原格式 为空时表示“yyyyMMddhhmmss”
     * @param ttime 目标格式 为空时表示“yyyy-MM-dd HH:mm:ss”
     * @param time  日期
     * @return String 转换后日期串，空为转换失败
     */

    public synchronized static String FormatTime(String ftime, String ttime, String time) {
        if ("".equals(time))
            return "";

        if (ftime.equals(""))
            ftime = "yyyyMMddhhmmss";
        if (ttime.equals(""))
            ttime = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(ftime);
        SimpleDateFormat tosdf = new SimpleDateFormat(ttime);
        String value = time;
        try {
            Date date = sdf.parse(time);
            value = tosdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 返回两个日期之间包含的自然天数
     *
     * @param sDate 起始日期，格式为YYYYMMDD
     * @param eDate 结束日期，格式为YYYYMMDD
     * @return 天数
     */
    public synchronized static int getDays(String sDate, String eDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = df.parse(sDate);
            endDate = df.parse(eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = endDate.getTime() - startDate.getTime();
        return (int) (l / (24 * 60 * 60 * 1000) + 1);
    }

    // /**
    // * 取两个日期之间的工作日天数
    // * @param sDate 开始日期
    // * @param eDate 结束日期
    // * @return 工作日天数
    // */
    // public synchronized static int getWorkdays(String sDate, String eDate)
    // {
    // return (new Holiday()).getWorkdays(sDate, eDate);
    // }
    //
    // /**
    // * 取两个日期之间的节假日天数
    // * @param sDate
    // * @param eDate
    // * @return
    // */
    // public synchronized static int getHolidays(String sDate, String eDate)
    // {
    // return (new Holiday()).getHolidays(sDate, eDate);
    // }
    //
    // /**
    // * 取某一天之前若干工作日的日期
    // * @param sDate 基准日期，格式：yyyyMMdd
    // * @param days 工作日数
    // * @return 日期格式：yyyyMMdd
    // */
    // public synchronized static String getWorkdateBefore(String sDate, int days)
    // {
    // return (new Holiday()).getWorkdateBefore(sDate, days);
    // }
    //
    // /**
    // * 取某一天之后若干工作日的日期
    // * @param sDate 基准日期，格式：yyyyMMdd
    // * @param days 工作日数
    // * @return 日期格式：yyyyMMdd
    // */
    // public synchronized static String getWorkdateAfter(String sDate, int days)
    // {
    // return (new Holiday()).getWorkdateAfter(sDate, days);
    // }
    // */

    /**
     * <p>得到指定周的指定天数的日期
     *
     * @param weekNo   周数 格式200628
     * @param day_week 整数:-6,上周第一天;1, 本周第一天;8,次周第一天
     * @return 日期格式 20060827
     */
    public synchronized static String dayOfWeek(String weekNo, int day_week, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.YEAR, Integer.parseInt(weekNo.substring(0, 4)));
        cal.set(GregorianCalendar.WEEK_OF_YEAR, Integer.parseInt(weekNo.substring(4)));
        cal.add(GregorianCalendar.DAY_OF_YEAR, 1 - cal.get(GregorianCalendar.DAY_OF_WEEK));
        cal.add(GregorianCalendar.DATE, day_week - 1);
        return dateFormat.format(cal.getTime());
    }

    /**
     * 计算周时间
     * <p>
     * 根据一年中的某一天计算本周开始日期及结束日期
     *
     * @param sDate   日期 格式YYYYMMDD
     * @param format 输出格式
     * @return 开始日期, 结束日期(两个日期都是按照format参数进行格式化 ， 逗号分割两个日期)
     */
    public synchronized static String weekBeging_EndDate(String sDate, String format) {
        if (format.equals("")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            // 第几天
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            GregorianCalendar calBegin = new GregorianCalendar();
            calBegin.setTime(cal.getTime());
            calBegin.add(GregorianCalendar.DAY_OF_YEAR, 1 - number);
            cal.add(GregorianCalendar.DAY_OF_YEAR, 7 - number);
            return bartDateFormat1.format(calBegin.getTime()) + ","
                    + bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 计算周时间的每一天的日期
     * <p>
     * 根据一年中的某一天计算本周开始日期至结束日期
     *
     * @param sDate   日期 格式YYYYMMDD
     * @param format 输出格式
     * @return 本周的每一天(日期都是按照format参数进行格式化 ， 逗号分割两个日期)
     */
    public synchronized static String[] weekBegingToEndDate(String sDate, String format) {
        if (format.equals("")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
        String weekDay[] = {"", "", "", "", "", "", ""};
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            // 第几天
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            GregorianCalendar calBegin = new GregorianCalendar();
            calBegin.setTime(cal.getTime());
            calBegin.add(GregorianCalendar.DAY_OF_YEAR, 1 - number);
            weekDay[0] = bartDateFormat1.format(calBegin.getTime());

            GregorianCalendar cal2 = new GregorianCalendar();
            cal2.setTime(cal.getTime());
            cal2.add(GregorianCalendar.DAY_OF_YEAR, 2 - number);
            weekDay[1] = bartDateFormat1.format(cal2.getTime());

            GregorianCalendar cal3 = new GregorianCalendar();
            cal3.setTime(cal.getTime());
            cal3.add(GregorianCalendar.DAY_OF_YEAR, 3 - number);
            weekDay[2] = bartDateFormat1.format(cal3.getTime());

            GregorianCalendar cal4 = new GregorianCalendar();
            cal4.setTime(cal.getTime());
            cal4.add(GregorianCalendar.DAY_OF_YEAR, 4 - number);
            weekDay[3] = bartDateFormat1.format(cal4.getTime());

            GregorianCalendar cal5 = new GregorianCalendar();
            cal5.setTime(cal.getTime());
            cal5.add(GregorianCalendar.DAY_OF_YEAR, 5 - number);
            weekDay[4] = bartDateFormat1.format(cal5.getTime());

            GregorianCalendar cal6 = new GregorianCalendar();
            cal6.setTime(cal.getTime());
            cal6.add(GregorianCalendar.DAY_OF_YEAR, 6 - number);
            weekDay[5] = bartDateFormat1.format(cal6.getTime());

            cal.add(GregorianCalendar.DAY_OF_YEAR, 7 - number);
            weekDay[6] = bartDateFormat1.format(cal.getTime());
            return weekDay;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 计算周时间的每一天的日期
     * <p>
     * 根据一年中的某一天计算本周开始日期至结束日期
     *
     * @param sDate    日期 格式YYYYMMDD
     * @param format  输出格式
     * @param isChina 是否是中国格式的星期（每周开始是周日还是周一）
     * @return 本周的每一天(日期都是按照format参数进行格式化 ， 逗号分割两个日期)
     */
    public synchronized static String[] weekBegingToEndDate(String sDate, String format,
                                                            boolean isChina) {
        if (!isChina) {
            return weekBegingToEndDate(sDate, format);
        }
        if (format.equals("")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
        String weekDay[] = {"", "", "", "", "", "", ""};
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            // 第几天
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            if (number == 1) {
                number = 8;
            }
            GregorianCalendar cal2 = new GregorianCalendar();
            cal2.setTime(cal.getTime());
            cal2.add(GregorianCalendar.DAY_OF_YEAR, 2 - number);
            weekDay[0] = bartDateFormat1.format(cal2.getTime());

            GregorianCalendar cal3 = new GregorianCalendar();
            cal3.setTime(cal.getTime());
            cal3.add(GregorianCalendar.DAY_OF_YEAR, 3 - number);
            weekDay[1] = bartDateFormat1.format(cal3.getTime());

            GregorianCalendar cal4 = new GregorianCalendar();
            cal4.setTime(cal.getTime());
            cal4.add(GregorianCalendar.DAY_OF_YEAR, 4 - number);
            weekDay[2] = bartDateFormat1.format(cal4.getTime());

            GregorianCalendar cal5 = new GregorianCalendar();
            cal5.setTime(cal.getTime());
            cal5.add(GregorianCalendar.DAY_OF_YEAR, 5 - number);
            weekDay[3] = bartDateFormat1.format(cal5.getTime());

            GregorianCalendar cal6 = new GregorianCalendar();
            cal6.setTime(cal.getTime());
            cal6.add(GregorianCalendar.DAY_OF_YEAR, 6 - number);
            weekDay[4] = bartDateFormat1.format(cal6.getTime());

            GregorianCalendar calBegin = new GregorianCalendar();
            calBegin.setTime(cal.getTime());
            calBegin.add(GregorianCalendar.DAY_OF_YEAR, 7 - number);
            weekDay[5] = bartDateFormat1.format(calBegin.getTime());

            cal.add(GregorianCalendar.DAY_OF_YEAR, 8 - number);
            weekDay[6] = bartDateFormat1.format(cal.getTime());
            return weekDay;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * <p>检查当前周的开始日期和结束日期是否在一个月
     *
     * @param sDate 日期 格式YYYYMMDD
     * @return true:在同一个月（即:周不跨月）/flase:不在同一个月
     */
    public synchronized static boolean weekOfMonth(String sDate) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            // 第几天
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            GregorianCalendar calBegin = new GregorianCalendar();
            calBegin.setTime(cal.getTime());
            calBegin.add(GregorianCalendar.DAY_OF_MONTH, -(number - 1));
            cal.add(GregorianCalendar.DAY_OF_MONTH, 7 - number);
            if (calBegin.get(GregorianCalendar.MONTH) == cal.get(GregorianCalendar.MONTH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    /**
     * <p>返回这一天是一周的第几天
     *
     * @param date 日期 格式YYYYMMDD
     * @return 第几天 周日为1，以此类推
     * @author wsjjiang
     */
    public synchronized static int weekThatDay(String date) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date sdate = bartDateFormat.parse(date);
            GregorianCalendar day = new GregorianCalendar();
            day.setTime(sdate);
            return day.get(GregorianCalendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * <p> 返回这一天是不是一周的第几天
     *
     * @param date    日期 格式YYYYMMDD
     * @param weekDay 周几 周日为1，以此类推
     * @return true/false
     * @author wsjjiang
     */
    public synchronized static boolean weekIsThatDay(String date, int weekDay) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date sdate = bartDateFormat.parse(date);
            GregorianCalendar day = new GregorianCalendar();
            day.setTime(sdate);
            if (day.get(GregorianCalendar.DAY_OF_WEEK) == weekDay)
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * <p>比较当前月份
     *
     * @param date 日期 格式YYYYMM
     * @return -1以往月份/0当前月/1下个月/2未来月份（比当前月份最少大两个月）
     * @author wsjjiang
     */
    public synchronized static int monthPlace(String date) {
        // 取得当前时间
        String thisMonth = getTime(YYYYMM);
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMM");
        try {
            Date sdate = bartDateFormat.parse(date);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(sdate);
            sdate = bartDateFormat.parse(thisMonth);
            GregorianCalendar cal1 = new GregorianCalendar();
            cal1.setTime(sdate);
            if (cal.get(Calendar.YEAR) < cal1.get(Calendar.YEAR)) {
                return -1;
            } else {
                if (cal.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) == 1) {
                    if (cal.get(Calendar.MONTH) == 0 && cal1.get(Calendar.MONTH) == 11) {
                        return 1;
                    }
                }
                if (cal.get(Calendar.YEAR) > cal1.get(Calendar.YEAR)) {
                    return 2;
                }
            }
            if (cal.get(Calendar.MONTH) < cal1.get(Calendar.MONTH)) {
                return -1;
            } else {
                if (cal.get(Calendar.MONTH) == cal1.get(Calendar.MONTH)) {
                    return 0;
                } else {
                    cal1.add(Calendar.MONTH, 1);
                    if (cal.get(Calendar.MONTH) == cal1.get(Calendar.MONTH)) {
                        return 1;
                    } else {
                        return 2;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * <p>星期的计算
     *
     * @param sDate 日期 格式YYYYMMDD
     * @param num   与sDate所在周相距的周数，正负整数。例如：-1，上周；0，当前周
     * @return 日期所在年的周数，例：200633
     */
    public synchronized static String weekAccount(String sDate, int num) {
        if (sDate.equals(""))
            return "";
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            String Week = "";
            String Year = "";
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(GregorianCalendar.WEEK_OF_YEAR, num);
            Year = cal.get(Calendar.YEAR) + "";
            if (Integer.parseInt(bartDateFormat.format(cal.getTime()).toString().substring(4, 8)) > 1225
                    && cal.get(Calendar.WEEK_OF_YEAR) == 1) {
                Year = cal.get(Calendar.YEAR) + 1 + "";
            }
            if (cal.get(Calendar.WEEK_OF_YEAR) < 10)
                Week = "0" + cal.get(Calendar.WEEK_OF_YEAR);
            else
                Week = cal.get(Calendar.WEEK_OF_YEAR) + "";
            return Year + Week;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * <p>星期的计算
     *
     * @param sDate  日期 格式YYYYMMDD
     * @param num    正负整数
     * @param format 返回时间的格式
     * @return 日期加减指定的周数后的指定格式的时间,
     * <p>例如：sData，20060803；num，2；format，yyyyMMdd；则返回：20060817
     */
    public synchronized static String weekAccount(String sDate, int num, String format) {
        if (sDate.equals(""))
            return "";
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(GregorianCalendar.WEEK_OF_YEAR, num);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 计算指定月第一天的日期
     * <p>主要用来按指定格式对日期进行格式化
     *
     * @param sDate  基准日期 格式YYYYMMDD
     * @param num    与sDate相距的月数，正负整数。例如：0，当前月；1，下月
     * @param format 输出格式
     * @return 某一月第一天的日期
     */
    public synchronized static String firstDayOfMonth(String sDate, int num, String format) {
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar cal = Calendar.getInstance();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            cal.add(GregorianCalendar.MONTH, num);
            int day = cal.getActualMinimum(GregorianCalendar.DAY_OF_MONTH);
            cal.set(GregorianCalendar.DATE, day);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// end of firstDayOfMonth

    /**
     * <p>计算指定月的最后一天
     *
     * @param sDate  日期 格式YYYYMMDD
     * @param num    与sDate相距的月数，正负整数。例如：0，当前月；1，下月
     * @param format 输出格式
     * @return 某一月最后一天的日期
     */
    public synchronized static String lastDayOfMonth(String sDate, int num, String format) {
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar cal = Calendar.getInstance();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            cal.add(GregorianCalendar.MONTH, num);
            int day = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            cal.set(GregorianCalendar.DATE, day);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// end of lastDayOfMonth

    /**
     * <p>计算指定天数后的日期
     *
     * @param sDate  日期 格式YYYYMMDD
     * @param num    与sDate相距的天数，正负整数。例如：-1，前一天；0，当天；1，第二天
     * @param format 输出格式
     * @return 日期加减指定的天数后的指定格式的日期
     */
    public synchronized static String thatDay(String sDate, int num, String format) {
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            GregorianCalendar cal = new GregorianCalendar();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            cal.add(GregorianCalendar.DATE, num);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// end of thatDay

    /**
     * 日期的计算
     * <p>计算指定日期所在周第一天日期
     *
     * @param sDate  指定的日期 格式YYYYMMDD
     * @param format 输出格式
     * @return sDate所在周的第一天的日期
     */
    public synchronized static String firstDayOfWeek(String sDate, String format) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            GregorianCalendar cal = new GregorianCalendar();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            cal.add(GregorianCalendar.DAY_OF_YEAR, 1 - number);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// End of firstDayOfWeek

    /**
     * 日期的计算
     * <p>计算指定日期所在周最后一天日期，
     *
     * @param sDate  日期 格式YYYYMMDD
     * @param format 输出格式
     * @return sDate所在周的最后一天的日期
     */
    public synchronized static String lastDayOfWeek(String sDate, String format) {
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            GregorianCalendar cal = new GregorianCalendar();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            int number = cal.get(GregorianCalendar.DAY_OF_WEEK);
            cal.add(GregorianCalendar.DATE, 7 - number);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);

            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// end of lastDayOfWeek

    /**
     * 日期的计算
     * <p>计算指定月的最后一天
     *
     * @param sDate  日期 格式YYYYMMDD
     * @param format 输出格式
     * @return 某一月第一天的日期
     */
    public synchronized static String lastDayOfMonth(String sDate, String format) {
        if (format.equals("")) {
            format = "yyyyMMdd";
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Calendar cal = Calendar.getInstance();
            Date date = bartDateFormat.parse(sDate);
            cal.setTime(date);
            int day = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            cal.set(GregorianCalendar.DATE, day);
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// End of lastDayOfMonth

    /**
     * 日期的计算
     * <p>计算两个日期相差的时间
     *
     * @param sDate 日期 格式YYYYMMDDHHMMSS
     * @param eDate 日期 格式YYYYMMDDHHMMSS
     * @param flag  正整数,1：代表年，2代表月，3代表日，4代表小时，5代表分钟，6代表秒
     * @return 根据参数返回两个日期相差的时间
     * @author wsjjiang
     */
    public synchronized static long subDate(String sDate, String eDate, int flag) {
        long subRlt = 0;
        if (sDate.length() < 14 || eDate.length() < 14) {
            return subRlt;
        }
        String reg = "['|-]";
        sDate = sDate.replaceAll(reg, "");
        eDate = eDate.replaceAll(reg, "");
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Calendar cal = Calendar.getInstance();
            Date date = bartDateFormat.parse(sDate);
            Calendar cal1 = Calendar.getInstance();
            Date date1 = bartDateFormat.parse(eDate);
            cal.setTime(date);
            cal1.setTime(date1);
            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            int second1 = cal1.get(Calendar.SECOND);
            int minute1 = cal1.get(Calendar.MINUTE);
            int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
            int day1 = cal1.get(Calendar.DAY_OF_MONTH);
            int month1 = cal1.get(Calendar.MONTH);
            int year1 = cal1.get(Calendar.YEAR);
            if (flag == 1) {
                cal.set(year, 0, 0, 0, 0, 0);
                cal1.set(year1, 0, 0, 0, 0, 0);
            }
            if (flag == 2) {
                cal.set(year, month, 0, 0, 0, 0);
                cal1.set(year1, month1, 0, 0, 0, 0);
            }
            if (flag == 3) {
                cal.set(year, month, day, 0, 0, 0);
                cal1.set(year1, month1, day1, 0, 0, 0);
            }
            if (flag == 4) {
                cal.set(year, month, day, hour, 0, 0);
                cal1.set(year1, month1, day1, hour1, 0, 0);
            }
            if (flag == 5) {
                cal.set(year, month, day, hour, minute, 0);
                cal1.set(year1, month1, day1, hour1, minute1, 0);
            }
            if (flag == 6) {
                cal.set(year, month, day, hour, minute, second);
                cal1.set(year1, month1, day1, hour1, minute1, second1);
            }
            subRlt = cal1.getTimeInMillis() - cal.getTimeInMillis();
            if (flag == 1) {
                subRlt = year1 - year;
            }
            if (flag == 2) {
                subRlt = (year1 - year) * 12 + month1 - month;
            }
            if (flag == 3) {
                subRlt = subRlt / 1000 / 3600 / 24;
            }
            if (flag == 4) {
                subRlt = subRlt / 1000 / 3600;
            }
            if (flag == 5) {
                subRlt = subRlt / 1000 / 60;
            }
            if (flag == 6) {
                subRlt = subRlt / 1000;
            }
            return subRlt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }// End of subDate

    /**
     * 时间的计算
     *
     * @param sDate  日期 格式YYYYMMDDHHMMSS/HHMMSS
     * @param num    正负整数
     * @param flag   时间计算的标志，正整数,1：代表年，2代表月，3代表日，4代表小时，5代表分钟，6代表秒
     *               24代表计算小时,25代表计算分钟,26代表计算秒;注:1-7计算使用14位时间格式,24-26使用
     *               "HHmmss"时间格式计算;
     * @param format 返回时间的格式
     * @return 按照指定格式返回时间
     * @author wsjjiang
     */
    public synchronized static String timeAccount(String sDate, int num, int flag, String format) {
        String formatStr = "";
        if (sDate.equals("")) {
            if (flag > 1 && flag < 7) {
                sDate = getTime(YYYYMMDDhhmmss);
            } else {
                if (flag > 23)
                    sDate = getTime(hhmmss);
                else
                    return "";
            }
        }
        if (flag > 1 && flag < 7) {
            formatStr = "yyyyMMddHHmmss";
            if (sDate.length() < 14) {

                for (int i = 0; i < (14 - sDate.length()); ) {
                    sDate += "0";
                }
            }
        } else {
            if (flag > 23) {
                formatStr = "HHmmss";
                if (sDate.length() < 6) {
                    for (int i = 0; i < (6 - sDate.length()); ) {
                        sDate += "0";
                    }
                } else {
                    sDate = sDate.substring(sDate.length() - 6, sDate.length());
                }
            } else {
                return "";
            }
        }

        SimpleDateFormat bartDateFormat = new SimpleDateFormat(formatStr);
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            if (flag == 1) {
                if (format.equals("")) {
                    format = "yyyy";

                }
                cal.add(GregorianCalendar.YEAR, num);
            }
            if (flag == 2) {
                if (format.equals("")) {
                    format = "yyyyMM";

                }
                cal.add(GregorianCalendar.MONTH, num);
            }
            if (flag == 3) {
                if (format.equals("")) {
                    format = "yyyyMMdd";

                }
                cal.add(GregorianCalendar.DAY_OF_YEAR, num);
            }
            if (flag == 4) {
                if (format.equals("")) {
                    format = "yyyyMMddHH";

                }
                cal.add(GregorianCalendar.HOUR, num);
            }
            if (flag == 5) {
                if (format.equals("")) {
                    format = "yyyyMMddHHmm";

                }
                cal.add(GregorianCalendar.MINUTE, num);
            }
            if (flag == 6) {
                if (format.equals("")) {
                    format = "yyyyMMddHHmmss";

                }
                cal.add(GregorianCalendar.SECOND, num);
            }
            if (flag == 24) {
                if (format.equals("")) {
                    format = "HH";

                }
                cal.add(GregorianCalendar.HOUR, num);
            }
            if (flag == 25) {
                if (format.equals("")) {
                    format = "HHmm";

                }
                cal.add(GregorianCalendar.MINUTE, num);
            }
            if (flag == 26) {
                if (format.equals("")) {
                    format = "HHmmss";

                }
                cal.add(GregorianCalendar.SECOND, num);
            }
            SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(format);
            return bartDateFormat1.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }// end of timeAccount

    /**
     * 日期比较
     * <p>测试该日期是否在某指定的日期之后。
     *
     * @param sDate 原始时间 格式yyyyMMddHHmmss
     * @param eDate 对比时间 格式yyyyMMddHHmmss
     * @return 当且仅当此 sDate 比 eDate 晚，才返回 true；否则返回 false。
     */
    public synchronized static boolean after(String sDate, String eDate) {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date1 = DateFormat.parse(sDate);
            Date date2 = DateFormat.parse(eDate);
            return date1.after(date2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 日期比较
     * <p>测试该日期是否在某指定的日期之前。
     *
     * @param sDate 原始时间 格式yyyyMMddHHmmss
     * @param eDate 对比时间 格式yyyyMMddHHmmss
     * @return 当且仅当此 sDate 比 eDate 早，才返回 true；否则返回 false。
     */
    public synchronized static boolean before(String sDate, String eDate) {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date1 = DateFormat.parse(sDate);
            Date date2 = DateFormat.parse(eDate);
            return date1.before(date2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 日期的计算
     * <p>日期的计算
     *
     * @param sDate 字符类型日期 格式yyyyMMdd
     * @param num   天数 正负整数
     * @return 计算后的日期
     */
    public synchronized static String dateAccount(String sDate, int num) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(GregorianCalendar.DAY_OF_YEAR, num);
            return bartDateFormat.format(cal.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 日期格式化
     * <p>按照指定的时间格式将给定的时间进行格式化
     *
     * @param sDate     日期
     * @param srcFormat 输入时间格式
     * @param tarFormat 输出时间格式
     * @return 某一月第一天的日期
     */
    public synchronized static String formatDate(String sDate, String srcFormat, String tarFormat) {
        if (srcFormat == null || srcFormat.equalsIgnoreCase("") || tarFormat == null
                || tarFormat.equalsIgnoreCase("")) {
            return sDate;
        }
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(srcFormat);
        SimpleDateFormat bartDateFormat1 = new SimpleDateFormat(tarFormat);
        try {
            Date date = bartDateFormat.parse(sDate);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            GregorianCalendar cal2 = new GregorianCalendar();
            cal2.setTime(cal.getTime());
            return bartDateFormat1.format(cal2.getTime());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sDate;
    }// End of formatDate

    /**
     * <li>功能描述：本方法的功能描述。
     *
     * @param stime 时间格式yyyy-MM-dd
     * @return int -1 标示出错
     * @author 张炳威
     */
    public static int getDaysWeekInMonth(String stime) {
        Calendar c = Calendar.getInstance();
        int WEEK_OF_MONTH = -1;
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date f = null;
        try {
            f = bartDateFormat.parse(stime);
            c.setTime(f);
            WEEK_OF_MONTH = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        } catch (ParseException e) {
        }
        return WEEK_OF_MONTH;
    }
}
