package com.tag.framework.util;

import com.tag.framework.conf.CoreConfig;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfPathUtil
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfPathUtil.class);
  private static String confPath;

  static
  {
    URL url = ConfPathUtil.class.getResource("/");
    try {
      if (url != null) {
        confPath = URLDecoder.decode(CoreConfig.class.getResource("/").getPath(), "utf-8");
        LOGGER.info("通过 【ConfPathUtil.class.getResource(\"/\").getPath()】获取的confPath为【{}】 ", new Object[] { confPath });
      } else {
        confPath = URLDecoder.decode(System.getProperty("file.separator") + System.getProperty("user.dir") + System.getProperty("file.separator") + "conf" + System.getProperty("file.separator"), "utf-8");
        LOGGER.info("通过【System.getProperty(\"user.dir\")+\\+conf】获取的confPath为【{}】 ", new Object[] { confPath });
      }
    } catch (UnsupportedEncodingException e) {
      LOGGER.info("通过 【ConfPathUtil.class.getResource(\"/\").getPath()】获取的confPath为【{}】 ", new Object[] { e });
    }
  }

  public static String getConfPath()
  {
    return confPath;
  }
}