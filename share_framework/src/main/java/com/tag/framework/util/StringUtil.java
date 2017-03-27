package com.tag.framework.util;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StringUtil
{
  public static boolean isNumber(String str)
  {
    if (str == null) {
      return false;
    }
    boolean isNumber = str.matches("[0-9]+");
    return isNumber;
  }

  public static String toString(String str)
  {
    if ((str == null) || ("null".equalsIgnoreCase(str))) {
      return "";
    }
    return str;
  }

  public static Integer stringToIntegerNullToNull(String str)
  {
    if ((str == null) || ("".equals(str))) {
      return null;
    }
    return Integer.valueOf(str);
  }

  public static int stringToInt(String str)
  {
    if ((str == null) || ("".equals(str)) || ("null".equalsIgnoreCase(str))) {
      return 0;
    }
    return Integer.valueOf(str).intValue();
  }

  public static double stringToDouble(String str)
  {
    if ((str == null) || ("".equals(str)) || ("null".equalsIgnoreCase(str))) {
      return 0.0D;
    }
    return Double.valueOf(str).doubleValue();
  }

  public static Float stringToFloat(String str)
  {
    if ((str == null) || ("".equals(str)) || ("null".equalsIgnoreCase(str))) {
      return Float.valueOf(0.0F);
    }
    return Float.valueOf(str);
  }

  public static int[] stringArrayToIntArray(String[] ss)
  {
    int[] ii = new int[ss.length];
    for (int i = 0; i < ss.length; i++) {
      ii[i] = stringToInt(ss[i]);
    }
    return ii;
  }

  public static float[] stringArrayToFloatArray(String[] ss)
  {
    float[] ff = new float[ss.length];
    for (int i = 0; i < ss.length; i++) {
      ff[i] = stringToFloat(ss[i]).floatValue();
    }
    return ff;
  }

  public static double[] stringArrayToDoubleArray(String[] ss)
  {
    double[] dd = new double[ss.length];
    for (int i = 0; i < ss.length; i++) {
      dd[i] = stringToDouble(ss[i]);
    }
    return dd;
  }

  public static String listToString(List list)
  {
    if (list == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      Object obj = list.get(i);
      if (i != 0) {
        sb.append(",");
      }
      sb.append(obj.toString());
    }
    return sb.toString();
  }

  public static String intArrayToString(Object[] oo)
  {
    if (oo == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < oo.length; i++) {
      Object obj = oo[i];
      if (i != 0) {
        sb.append(",");
      }
      sb.append(obj.toString());
    }
    return sb.toString();
  }

  public static int[] stringToIntArray(String source, String split)
  {
    String[] spits = source.split(split);
    int length = spits.length;
    int[] dest = new int[length];
    for (int i = 0; i < length; i++) {
      dest[i] = stringToInt(spits[i]);
    }
    return dest;
  }

  public static boolean toBoolean(String str)
  {
    str = str.trim();
    return str.equals("1");
  }

  public static boolean intToBoolean(int i)
  {
    return i == 1;
  }

  public static int booleanToInt(boolean b)
  {
    return b ? 1 : 0;
  }

  public static String firstToUpperCase(String str)
  {
    String s = new StringBuilder().append(str.substring(0, 1).toUpperCase()).append(str.substring(1)).toString();
    return s;
  }

  public static String firstToLowerCase(String str)
  {
    String s = new StringBuilder().append(str.substring(0, 1).toLowerCase()).append(str.substring(1)).toString();
    return s;
  }

  public static String intToBinaryString(int num)
  {
    StringBuilder builder = new StringBuilder();
    String s = "";
    for (int i = 0; i < 31; i++) {
      if ((num & 0x1) == 1)
        s = "1";
      else {
        s = "0";
      }
      builder.append(s);
      num >>= 1;
    }
    return builder.reverse().toString();
  }

  public static int binaryStringToInt(String str)
  {
    char[] cc = str.toCharArray();

    int num = 0;
    for (int i = 0; i < cc.length; i++) {
      num += (int)Math.pow(2.0D, cc.length - 1 - i) * Integer.valueOf(String.valueOf(cc[i])).intValue();
    }
    return num;
  }

  public static final String toHex(byte[] hash) {
    StringBuffer buf = new StringBuffer(hash.length * 2);

    for (int i = 0; i < hash.length; i++) {
      if ((hash[i] & 0xFF) < 16) {
        buf.append("0");
      }
      buf.append(Long.toString(hash[i] & 0xFF, 16));
    }

    return buf.toString();
  }

  public static String md5(String inputStr)
  {
    String pwd = "";
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] s = digest.digest(inputStr.getBytes("UTF-8"));
      return toHex(s);
    }
    catch (NoSuchAlgorithmException e) {
      System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");

      e.printStackTrace();
      return null;
    } catch (Exception e) {
      System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");

      e.printStackTrace();
    }return null;
  }

  public static String md5HashValue(String value)
  {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    md.update(value.getBytes());
    return toHexString(md.digest());
  }

  private static String toHexString(byte[] in) {
    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < in.length; i++) {
      String hex = Integer.toHexString(0xFF & in[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static String lPad(String s, int paddedLength, String pad)
  {
    StringBuilder sb = new StringBuilder(s);
    while (sb.length() < paddedLength) {
      sb.insert(0, pad);
    }
    String result = sb.substring(0, paddedLength);
    return result;
  }

  public static String rPad(String s, int paddedLength, String pad)
  {
    StringBuilder sb = new StringBuilder(s);
    while (sb.length() < paddedLength) {
      sb.append(pad);
    }
    String result = sb.substring(0, paddedLength);
    return result;
  }

  public static void main(String[] args)
  {
    System.out.println(rPad("12345678", 12, "2"));
    System.out.println(lPad("12345678", 12, "2"));
  }
}