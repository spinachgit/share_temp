package com.tag.framework.util;

import com.tag.framework.context.AppContext;
import com.tag.framework.service.TokenService;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil
{
  private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtil.class);

  public static String bm_token = "bm_token";

  public static void setAttribute(String tokenId, String key, Object obj)
  {
    if ((tokenId == null) || ("".equals(tokenId))) {
      LOGGER.error("用户还未登录！没有tokenId, 不能操作session");
      return;
    }

    TokenService tokenService = (TokenService)AppContext.getBean("tokenService");

    tokenService.setAttribute(tokenId, key, obj);
  }

  public static void setAttribute(HttpServletRequest request, String key, Object obj)
  {
    String tokenId = CookieUtil.getCookieValue(request, bm_token);

    setAttribute(tokenId, key, obj);
  }

  private static void setAttribute(String key, Object obj)
  {
    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

    setAttribute(request, key, obj);
  }

  public static Object getAttribute(HttpServletRequest request, String key)
  {
    String tokenId = CookieUtil.getCookieValue(request, bm_token);

    if ((tokenId == null) || ("".equals(tokenId))) {
      LOGGER.error("用户还未登录！没有tokenId, 不能操作session");
      return null;
    }

    TokenService tokenService = (TokenService)AppContext.getBean("tokenService");

    Object object = tokenService.getAttribute(tokenId, key);

    return object;
  }

  private static Object getAttribute(String key)
  {
    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

    Object object = getAttribute(request, key);

    return object;
  }

  public static void removeAttribute(HttpServletRequest request, String key)
  {
    String tokenId = CookieUtil.getCookieValue(request, bm_token);

    if ((tokenId == null) || ("".equals(tokenId))) {
      LOGGER.error("用户还未登录！没有tokenId, 不能操作session");
      return;
    }

    TokenService tokenService = (TokenService)AppContext.getBean("tokenService");

    tokenService.removeAttribute(tokenId, key);
  }

  private static void removeAttribute(String key)
  {
    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

    removeAttribute(request, key);
  }

  public static String getPosterIp(HttpServletRequest request)
  {
    String ip = request.getHeader("x-forwarded-for");
    if ((ip != null) && (ip.length() != 0)) {
      while ((ip != null) && (ip.equals("unknow")))
      {
        ip = request.getHeader("x-forworded-for");
      }
    }

    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  public static void print(HttpServletRequest req)
  {
    Map parameters = new WeakHashMap();

    for (Enumeration e = req.getParameterNames(); e.hasMoreElements(); ) {
      String name = (String)e.nextElement();
      String[] v = req.getParameterValues(name);
      if (v.length == 1) {
        if (!v[0].equals(""))
        {
          parameters.put(name, v[0]);
        }
      } else parameters.put(name, v);

    }

    for (Enumeration e = req.getAttributeNames(); e.hasMoreElements(); ) {
      String name = (String)e.nextElement();
      Object v = req.getAttribute(name);
      parameters.put(name, v);
    }

    HttpSession session = req.getSession();
    for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
      String name = (String)e.nextElement();
      Object v = session.getAttribute(name);
      parameters.put(name, v);
    }

    Set keys = parameters.keySet();
    Iterator it = keys.iterator();

    while (it.hasNext()) {
      String key = (String)it.next();
      Object value = parameters.get(key);
      System.out.println("key:" + key + ", value:" + value);
    }
  }
}