package cn.edu.zzti.soft.noads.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static String FORMAT_SECOND = "%d秒";
    private static String FORMAT_MINUTE = "%d分钟";
    private static String FORMAT_MINUTE_SECOND = "%d分%d秒";
    private static String FORMAT_HOUR = "%d小时";
    private static String FORMAT_HOUR_MINUTE = "%d小时%d分";
    private static String FORMAT_HOUR_MINUTE_SECOND = "%d小时%d分%d秒";
    private static String FORMAT_DAY = "%d天";
    private static String FORMAT_DAY_HOUR = "%d天%d小时";
    private static String FORMAT_DAY_HOUR_MINUTE = "%d天%d小时%d分";
    private static String FORMAT_DAY_HOUR_MINUTE_SECOND = "%d天%d小时%d分%d秒";
    private static Locale LOCALE_TYPE = Locale.CHINA;

    public static long DAY_TIME = 86400000;

    private TimeUtil() {
    }

    public static long getCurrentMills() {
        android.text.format.Time now = new android.text.format.Time();
        now.setToNow();
        return now.toMillis(false);
    }

    public static String formatDate(int date) {
        String temp = String.valueOf(date);
        return new StringBuilder().append(temp.substring(0, 4)).append("-").append(temp.substring(4, 6)).append("-")
                .append(temp.substring(6)).toString();
    }

    public static String formatDate(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd",LOCALE_TYPE);
        return format.format(date);
    }

    public static String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatYearMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTimeForLabel(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTimeToHHmm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTimeToYMDHHmm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTimeToYMDHHmmByBackslash(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", LOCALE_TYPE);
        return format.format(new Date(time));
    }
    public static String formatTimeToYMDByBackslash(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", LOCALE_TYPE);
        return format.format(new Date(time));
    }
    public static String formatTimeToChineseMD(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日",LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatTimeWithUnderline(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss",LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatSimpleTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd HH:mm",LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatFullTimeWithZone(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",LOCALE_TYPE);
        return format.format(new Date(time));
    }

    public static String formatLength(int second) {
        if (second < 60) {
            return String.format(FORMAT_SECOND, second);
        } else if ((second >= 60) && (second < 60 * 60)) {
            int min = second / 60;
            int sec = second % 60;
            return (sec == 0) ? String.format(FORMAT_MINUTE, min) : String.format(FORMAT_MINUTE_SECOND, min, sec);
        } else if ((second >= 60 * 60) && (second < 24 * 60 * 60)) {
            int hour = second / (60 * 60);
            int mod = second % (60 * 60);
            if (mod == 0) {
                return String.format(FORMAT_HOUR, hour);
            } else {
                int min = mod / 60;
                int sec = mod % 60;
                return (sec == 0) ? String.format(FORMAT_HOUR_MINUTE, hour, min) : String.format(FORMAT_HOUR_MINUTE_SECOND, hour, min, sec);
            }
        } else {
            int day = second / (24 * 60 * 60);
            int mod = second % (24 * 60 * 60);
            if (mod == 0) {
                return String.format(FORMAT_DAY, day);
            } else {
                int hour = mod / (60 * 60);
                int mod2 = mod % (60 * 60);
                if (mod2 == 0) {
                    return String.format(FORMAT_DAY_HOUR, day, hour);
                } else {
                    int min = mod2 / 60;
                    int sec = mod2 % 60;
                    return (sec == 0) ? String.format(FORMAT_DAY_HOUR_MINUTE, day, hour, min) : String.format(FORMAT_DAY_HOUR_MINUTE_SECOND, day, hour, min, sec);
                }
            }
        }
    }

    public static String formatSimpleLength(int second) {
        if (second < 60) {
            return String.format(FORMAT_SECOND, second);
        } else if ((second >= 60) && (second < 60 * 60)) {
            int min = second / 60;
            return String.format(FORMAT_MINUTE, min);
        } else if ((second >= 60 * 60) && (second < 24 * 60 * 60)) {
            int hour = second / (60 * 60);
            return String.format(FORMAT_HOUR, hour);
        } else {
            int day = second / (24 * 60 * 60);
            return String.format(FORMAT_DAY, day);
        }
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(final long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.MONTH);
    }

    public static int getDay(final long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public static String getDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return getDayOfWeek(cal);
    }

    public static String getDayOfWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    public static String formatDuration(int duration) {
        if (duration <= 1800) {
            return "半小时";
        } else if (duration <= 3600 * 4) {
            return Integer.toString((int) Math.ceil((double) duration / 3600)) + "小时";
        } else {
            return Integer.toString((int) Math.ceil((double) duration / (3600 * 24))) + "天";
        }
    }

    public static String formatTimeSequence(long startT, long targetT) {
        StringBuilder sb = new StringBuilder();
        int startDay = (int) startT / 86400;
        int targetDay = (int) targetT / 86400;
        if (targetDay >= startDay) {
            sb.append("第").append(targetDay - startDay + 1).append("天");
            sb.append('(');
        }
        sb.append(formatTime(targetT * 1000));
        if (targetDay >= startDay) {
            sb.append(')');
        }

        return sb.toString();
    }

    public static long getLastWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SATURDAY) {
            calendar.set(Calendar.WEEK_OF_MONTH, 1 +
                    calendar.get(Calendar.WEEK_OF_MONTH));
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTimeInMillis();
    }

    public static String formatMessageTime(final long baseT, int year, long msgT) {
        long currentMills = getCurrentMills(); // 即时获取当前系统时间，不使用baseT, year
        int currentYear = 0;
        long interval = (currentMills - msgT);
        int intervalSec = (int) interval / 1000;
        if (interval <  Constant.Time.MINUTE) {
            return "刚刚";
        } else if (interval <=  Constant.Time.DAY) {
            return formatSimpleLength(intervalSec)+ "前";
        } else {
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(msgT);
            int msgYear = calendar.get(Calendar.YEAR);
            if (msgYear == currentYear) {
                return formatTimeForLabel(msgT);
            } else {
                return formatDate(msgT);
            }
        }
    }  public static String formatMessageTime(final long msgT) {
        long currentMills = getCurrentMills(); // 即时获取当前系统时间，不使用baseT, year
        int currentYear = 0;
        long interval = (currentMills - msgT);
        int intervalSec = (int) interval / 1000;
        if (interval <  Constant.Time.MINUTE) {
            return "刚刚";
        } else if (interval <= Constant.Time.DAY) {
            return formatSimpleLength(intervalSec)+ "前";
        } else {
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(msgT);
            int msgYear = calendar.get(Calendar.YEAR);
            if (msgYear == currentYear) {
                return formatTimeForLabel(msgT);
            } else {
                return formatDate(msgT);
            }
        }
    }

    public static long formatSpecialTimeToMillisecond(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",LOCALE_TYPE);
        try{
            return format.parse(time).getTime();

        } catch (ParseException e) {
           Logger.e("TimeUtil","format special time error.",e);
        }
        return  Constant.INVALID;
    }
    public static long formatSpecialTimeToMillisecond(String time,String dateStr,String timeStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy"+dateStr+"MM"+dateStr+"dd hh"+timeStr+"mm"+timeStr+"ss",LOCALE_TYPE);
        try{
            return format.parse(time).getTime();

        } catch (ParseException e) {
            Logger.e("TimeUtil","format special time error.",e);
        }
        return  Constant.INVALID;
    }

    public static long formatSpecialTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        try{
            return format.parse(time).getTime();

        } catch (ParseException e) {
           Logger.e("TimeUtil","format special time error.",e);
        }
        return  Constant.INVALID;
    }

    // 11-12 20:52
    public static long formUmengNotificationTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm",LOCALE_TYPE);
        try{

            Logger.e( Constant.TAG,"getYear : "+getYear());
            return formatSpecialYearTime(getYear())+format.parse(time).getTime();

        } catch (ParseException e) {
            Logger.e( Constant.TAG,"format special time error.",e);
        }
        return  Constant.INVALID;
    }

    public static long formatSpecialYearTime(int year){
        SimpleDateFormat format = new SimpleDateFormat("yyyy",LOCALE_TYPE);
        try{
            return format.parse(String.valueOf(year)).getTime();

        } catch (ParseException e) {
            Logger.e( Constant.TAG,"format special time error.",e);
        }
        return  Constant.INVALID;
    }
    //获取当前月的第一天
    public static String getMonthFirstDay(){
        String monthDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置1号为,本月第一天;
        monthDay = format.format(calendar.getTime());
        return monthDay;
    }
    //获取上月的第一天
    public static String getLastMonthFirstDay(){
        String monthDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置1号为,本月第一天;
        monthDay = format.format(calendar.getTime());
        Logger.e("time","月+:"+monthDay);
        return monthDay;
    }
    //获取上周的第一天
    public static String getLastWeekFirstDay(){
        String monthDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        monthDay = format.format(calendar.getTime());
        Logger.e("time","周+:"+monthDay);
        return monthDay;
    }
    //获取昨日日期
    public static String getYesterday(){
        String monthDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        monthDay = format.format(calendar.getTime());
        Logger.e("time","昨日+:"+monthDay);
        return monthDay;
    }
    //获取当前周的第一天
    public static String getWeekFirstDay(){
        String monthDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        calendar.add(Calendar.DATE, -day_of_week + 1);
        monthDay = format.format(calendar.getTime());
        Logger.e("time","周+:"+monthDay);
        return monthDay;
    }
    //获取指定日期的月的最后一天
    public static String getMonthLastDay(Date date){
        String time;
        SimpleDateFormat dateFormater = new SimpleDateFormat("MM/dd",LOCALE_TYPE);
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.setTime(date);

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        time = dateFormater.format(cal.getTime());
        Logger.e("TAG",time);
        return time;
    }
    public static String getWeekLastDay(Date date){
        String time;
        SimpleDateFormat dateFormater = new SimpleDateFormat("MM/dd",LOCALE_TYPE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        time = dateFormater.format(cal.getTime());
        return time;
    }
    public static Date str2date(String str){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd",LOCALE_TYPE);
        Date date = null;
        try {
            date =  df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String formatDateStr(Date date){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd",LOCALE_TYPE);
        return format.format(date);
    }

}
