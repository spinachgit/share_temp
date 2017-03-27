package com.tag.framework.core.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DumpMemoryTask
  implements Runnable
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DumpMemoryTask.class);

  public void run()
  {
    double maxMemory = Runtime.getRuntime().maxMemory() / 1024.0D / 1024.0D / 1024.0D;
    double totalMemory = Runtime.getRuntime().totalMemory() / 1024.0D / 1024.0D / 1024.0D;
    double freeMemory = Runtime.getRuntime().freeMemory() / 1024.0D / 1024.0D / 1024.0D;
    double useMemory = totalMemory - freeMemory;
    int useRate = (int)Math.round(useMemory / totalMemory * 100.0D);

    LOGGER.info("-服务器内存，最大={}G，分配={}G,空闲={}G，使用={}G，使用比={}%", new Object[] { Double.valueOf(Math.round(maxMemory * 1000.0D) / 1000.0D), Double.valueOf(Math.round(totalMemory * 1000.0D) / 1000.0D), Double.valueOf(Math.round(freeMemory * 1000.0D) / 1000.0D), Double.valueOf(Math.round(useMemory * 1000.0D) / 1000.0D), Integer.valueOf(useRate) });
  }
}