package com.tag.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UrlService
{
  public static String getSortedUrl(Map parameterMap)
  {
    Map map = new TreeMap(parameterMap);

    Set<String> keySet = map.keySet();
    StringBuilder sb = new StringBuilder();
    for (String key : keySet) {
      sb.append(key).append("=").append((String)map.get(key)).append("&");
    }
    String str = sb.substring(0, sb.length() - 1);
    return str;
  }

  public static Map<String, String> getMapbyUrlParams(String params)
  {
    Map map = new TreeMap();
    String[] ss = params.split("&");
    for (String s : ss) {
      int index = s.indexOf("=");
      if (index > 0) {
        String key = s.substring(0, index);
        String value = s.substring(index + 1);
        map.put(key, value);
      }

    }

    return map;
  }

  public static String HttpGetGo(String goUrl)
    throws Exception
  {
    StringBuffer readOneLineBuff = new StringBuffer();
    String content = "";
    URL url = new URL(goUrl);
    URLConnection conn = url.openConnection();
    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
    String line = "";
    while ((line = reader.readLine()) != null) {
      readOneLineBuff.append(line);
    }
    content = readOneLineBuff.toString();
    reader.close();
    return content;
  }

  public static String urlEncode(String str)
  {
    String result = null;
    try {
      result = URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String urlDecode(String str)
  {
    String result = null;
    try {
      result = URLDecoder.decode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args)
  {
    String s1 = "jo040cBZON3Wb/cN5OD3KOJ76IR2Zjpso6hZIdm+5w2vkvWSsx/uVwyTSGInJ3l/zlX/142s+WjP303z53lMjRlZRl8/1lHjZKI/ILb1D7WV40wbQkl7LjvEV5nAc+H/QIsJqDwCr3lt97Adcp+s54UJuj4RjukVBCSk9UZBIWuuYe/TIWw6nw6kmHqeNXQT+ggX9rKtj/H/L3dBGxnm1Vcg4pM0xu8OeNepyLWyyXPgiZr1jNBsgcNaPmpsgNsZVRt+DGVBXQuVYm3yfm4oChVujFQtfNQRnjh2OkPeViZr8/ZIO6D8auikKnC3WuunNOVFZANRz6bdYbAII6IBCdXClo30pO1dgpratyG257fTtc+8XTVx+XJbN7cZNZw4NkGe/hl/tWn/rdqAbbTdKA==";

    String s2 = "jo040cBZON3Wb%2FcN5OD3KOJ76IR2Zjpso6hZIdm%2B5w2vkvWSsx%2FuVwyTSGInJ3l%2FzlX%2F142s%2BWjP303z53lMjRlZRl8%2F1lHjZKI%2FILb1D7WV40wbQkl7LjvEV5nAc%2BH%2FQIsJqDwCr3lt97Adcp%2Bs54UJuj4RjukVBCSk9UZBIWuuYe%2FTIWw6nw6kmHqeNXQT%2BggX9rKtj%2FH%2FL3dBGxnm1Vcg4pM0xu8OeNepyLWyyXPgiZr1jNBsgcNaPmpsgNsZVRt%2BDGVBXQuVYm3yfm4oChVujFQtfNQRnjh2OkPeViZr8%2FZIO6D8auikKnC3WuunNOVFZANRz6bdYbAII6IBCdXClo30pO1dgpratyG257fTtc%2B8XTVx%2BXJbN7cZNZw4NkGe%2Fhl%2FtWn%2FrdqAbbTdKA%3D%3D";

    s1 = urlEncode("元宝");
    s2 = urlDecode(s1);

    System.out.println(new StringBuilder().append("编码前=").append(s1).toString());
    System.out.println(new StringBuilder().append("编码后=").append(s2).toString());
    if (s1.equals(s2))
      System.out.println("编码前后字符串一样");
    else
      System.out.println("编码前后字符串不一样...");
  }
}