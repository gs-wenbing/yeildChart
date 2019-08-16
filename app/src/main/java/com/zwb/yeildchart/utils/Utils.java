package com.zwb.yeildchart.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    /**
     * px像素，转换成dp
     *
     * @param context
     * @param Pixels
     * @return
     */
    public static int pixelsToDip(Context context, int Pixels) {
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Pixels,
                context.getResources().getDisplayMetrics());
        return dip;
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }
    /**
     * 获取当前日期  yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date stringToDate(String strDate, String format) {
        if (strDate == null) {
            return null;
        }
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String类型的日期时间转化为Long类型.
     * @param strTime String形式的日期时间
     * @param formatType 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Long 类型数据
     */
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    /**
     * 获取指定日期所在周的第一天和最后一天
     * @param date
     * @return
     */
    public static String[] getFirstAndLastOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = calendar.getTime();
        return new String[]{getStringByFormat(firstDayOfMonth,"yyyy-MM-dd"),getStringByFormat(lastDayOfMonth,"yyyy-MM-dd")};
    }
    /**
     * @param str                  要修改的字符串
     * @param absoluteSizeSpan1    修改特定位置字符串的字号
     * @param foregroundColorSpan1 修改特定位置字符串的颜色
     * @param i                    要修改的字符串的起始位置
     * @param j                    要修改的字符串的结束位置
     * @return
     */
    public static CharSequence setTextViewContentStyle(String str,
                                                       AbsoluteSizeSpan absoluteSizeSpan1,
                                                       ForegroundColorSpan foregroundColorSpan1, int i, int j) {
        // TODO Auto-generated method stub
        SpannableString spStr = new SpannableString(str);
        if (foregroundColorSpan1 != null) {
            spStr.setSpan(foregroundColorSpan1, i, j,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (absoluteSizeSpan1 != null) {
            spStr.setSpan(absoluteSizeSpan1, i, j,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spStr;
    }


}
