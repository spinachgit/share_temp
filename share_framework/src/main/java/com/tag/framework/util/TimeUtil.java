package com.tag.framework.util;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil
{
  public static int currentTimeSecond()
  {
    int time = (int)(System.currentTimeMillis() / 1000L);
    return time;
  }

  public static int dayOf1970()
  {
    int currentTimeSecond = currentTimeSecond() + 28800;
    int dayOf1970 = currentTimeSecond / 86400;

    return dayOf1970;
  }

  public static int weekOf1970()
  {
    int dayOf1970 = dayOf1970();
    int weekOf1970 = (dayOf1970 + 3) / 7;

    return weekOf1970;
  }

  public static long getRefreshEveryDayTaskDelayTime()
  {
    long time = getTomorrowTime() - System.currentTimeMillis();
    return time;
  }

  public static int getSecondByFormatDate(String dateStr, String formatStr)
  {
    if (("0".equals(dateStr)) || ("".equals(dateStr))) {
      return 0;
    }

    SimpleDateFormat format = new SimpleDateFormat(formatStr);
    Date date = null;
    try {
      date = format.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return (int)(date.getTime() / 1000L);
  }

  public static Date getDateByFormatDate(String dateStr, String formatStr)
  {
    SimpleDateFormat format = new SimpleDateFormat(formatStr);
    Date date = null;
    try {
      date = format.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return date;
  }

  public static long getNextTimeMillis(int hour, int minute, int second, int milliSecond)
  {
    if ((hour < 0) || (hour > 24)) {
      throw new RuntimeException("hour is error!");
    }
    Calendar cal = Calendar.getInstance();

    cal.set(11, hour);
    cal.set(12, minute);
    cal.set(13, second);
    cal.set(14, milliSecond);
    long time = cal.getTimeInMillis();
    long currentTime = System.currentTimeMillis();
    if (currentTime <= time) {
      return time - currentTime;
    }
    return time - currentTime + 86400000L;
  }

  public static long getNextWeekTimeMillis(int week, int hour, int minute, int second, int milliSecond)
  {
    if ((hour < 0) || (hour > 24)) {
      throw new RuntimeException("hour is error!");
    }
    Calendar cal = Calendar.getInstance();

    cal.set(7, week + 1);
    cal.set(11, hour);
    cal.set(12, minute);
    cal.set(13, second);
    cal.set(14, milliSecond);
    long time = cal.getTimeInMillis();
    long currentTime = System.currentTimeMillis();
    if (currentTime <= time) {
      return time - currentTime;
    }
    return time - currentTime + 604800000L;
  }

  public static long getTodayTime(int hh, int mm, int ss, int ms)
  {
    Calendar cal = Calendar.getInstance();
    cal.set(11, hh);
    cal.set(12, mm);
    cal.set(13, ss);
    cal.set(14, ms);

    long time = cal.getTimeInMillis();
    return time;
  }

  public static long getTime(int year, int month, int day, int hh, int mm, int ss, int ms)
  {
    Calendar cal = Calendar.getInstance();

    cal.set(1, year);
    cal.set(2, month);
    cal.set(5, day);

    cal.set(11, hh);
    cal.set(12, mm);
    cal.set(13, ss);
    cal.set(14, ms);

    long time = cal.getTimeInMillis();
    return time;
  }

  public static long getTomorrowTime()
  {
    long time = getTodayTime(24, 0, 0, 0);
    return time;
  }

  public static long getTodayZeroHourTime()
  {
    long time = getTodayTime(0, 0, 0, 0);
    return time;
  }

  public static long getYesterdayTime()
  {
    long time = getTodayTime(-24, 0, 0, 0);
    return time;
  }

  public static boolean checkTimeIsToday(long time)
  {
    long beginTime = getTodayZeroHourTime();
    long endTime = beginTime + 86400000L;

    if ((time > beginTime) && (time <= endTime)) {
      return true;
    }
    return false;
  }

  public static boolean checkIsSameDayByHour(long time, int hour)
  {
    long beginTime = getTodayZeroHourTime() + hour * 3600 * 1000;
    long endTime = beginTime + 86400000L;

    Calendar cal = Calendar.getInstance();
    int currentHour = cal.get(11);
    if ((currentHour >= 0) && (currentHour < 5)) {
      beginTime -= 86400000L;
      endTime -= 86400000L;
    }

    if ((time > beginTime) && (time <= endTime)) {
      return true;
    }
    return false;
  }

  public static long getTodayTimeMillisByHHMMSS(String hhmmss)
  {
    int[] ii = StringUtil.stringToIntArray(hhmmss, ":");

    int hour = ii[0];
    int minute = ii[1];
    int second = ii[2];

    if ((hour < 0) || (hour > 24)) {
      throw new RuntimeException("hour is error!");
    }

    Calendar cal = Calendar.getInstance();
    cal.set(11, hour);
    cal.set(12, minute);
    cal.set(13, second);
    long time = cal.getTimeInMillis();

    return time;
  }

  public static String getFormatTime(long time, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String str = sdf.format(new Date(time));
    return str;
  }

  public static String getFormatDate(Date date, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String str = sdf.format(date);
    return str;
  }

  public static boolean checkIsSameDay(int time1, int time2)
  {
    Date date1 = new Date(time1 * 1000L);
    Date date2 = new Date(time2 * 1000L);
    long t1 = getTime(date1.getYear(), date1.getMonth(), date1.getDay(), 0, 0, 0, 0);
    long t2 = getTime(date2.getYear(), date2.getMonth(), date2.getDay(), 0, 0, 0, 0);

    if (t1 == t2) {
      return true;
    }
    return false;
  }

  public static int getWeek()
  {
    Calendar cal = Calendar.getInstance();

    int dayOfWeek = cal.get(7);

    int week = dayOfWeek - 1;

    return week;
  }

  public static long getTimeByWeekStr(String weekStr)
  {
    String[] ss = weekStr.split("\\|");
    String[] hms = ss[1].split(":");
    int week = Integer.valueOf(ss[0]).intValue();

    week += 1;
    int hour = Integer.valueOf(hms[0]).intValue();
    int minute = Integer.valueOf(hms[1]).intValue();
    int second = Integer.valueOf(hms[2]).intValue();

    Calendar cal = Calendar.getInstance();
    cal.set(7, week);
    cal.set(11, hour);
    cal.set(12, minute);
    cal.set(13, second);

    long time = cal.getTimeInMillis();
    return time;
  }

  public static void main(String[] args)
  {
    System.out.println((getTomorrowTime() - System.currentTimeMillis()) / 3600000.0D);
  }
}