package com.tag.framework.util;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AjaxUtil
{
  private static final Logger LOGGER = LoggerFactory.getLogger(AjaxUtil.class);

  public static boolean checkIsAjax(HttpServletRequest request)
  {
    String requestType = request.getHeader("x-requested-with");
    boolean isAjax = false;
    if ((requestType != null) && (requestType.equals("XMLHttpRequest"))) {
      isAjax = true;
    }

    request.setAttribute("isAjax", Boolean.valueOf(isAjax));
    return isAjax;
  }
}