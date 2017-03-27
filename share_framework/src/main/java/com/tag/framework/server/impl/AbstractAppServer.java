package com.tag.framework.server.impl;

import com.tag.framework.conf.CoreConfig;
import com.tag.framework.core.pool.TaskPoolFactory;
import com.tag.framework.core.pool.ThreadPoolFactory;
import com.tag.framework.core.task.DumpMemoryTask;
import com.tag.framework.core.thread.AppServerShutdownHook;
import com.tag.framework.core.thread.StopServerThread;
import com.tag.framework.model.conf.AppConfig;
import com.tag.framework.server.IAppServer;
import com.tag.framework.util.ConfPathUtil;
import com.tag.framework.util.TimeUtil;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class AbstractAppServer
  implements IAppServer
{
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAppServer.class);

  private static final Logger serverStartAndStopLog = LoggerFactory.getLogger("ServerStartAndStopLog");

  private static String confPath = ConfPathUtil.getConfPath();

  public AbstractAppServer()
  {
    init();
  }

  public void init()
  {
    initLog4j();
    initSpring();
  }

  public void start()
  {
    long begin = System.currentTimeMillis();
    serverStartAndStopLog.info("开始启动服务器！");

    startStopServer();
    startTimerServer();
    addShutdownHook();

    long end = System.currentTimeMillis();
    serverStartAndStopLog.info("服务器启动成功！启动耗时：【{}】秒", new Object[] { Double.valueOf((end - begin) / 1000.0D) });
  }

  public void stop()
  {
    serverStartAndStopLog.info("开始关闭服务器！");
    long begin = System.currentTimeMillis();

    stopTaskPools(TaskPoolFactory.taskPoolMap);

    stopThreadPools(ThreadPoolFactory.threadPoolMap);

    long end = System.currentTimeMillis();
    serverStartAndStopLog.info("服务器关闭成功！关闭耗时：【{}】秒", new Object[] { Double.valueOf((end - begin) / 1000.0D) });
  }

  public void initLog4j()
  {
    String time = "【" + TimeUtil.getFormatTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "】";
    System.out.println(time + "开始初始化log4j...");

    DOMConfigurator.configure(confPath + "/log4j.xml");
    LOGGER.info("log4j初始化成功...");
  }

  public void initSpring()
  {
    LOGGER.info("开始初始化spring...");
    String[] files = { confPath + "spring/spring-main.xml" };
    ApplicationContext context = new FileSystemXmlApplicationContext(files);
    LOGGER.info("初始化spring结束...");
  }

  public void startStopServer()
  {
    StopServerThread thread = new StopServerThread(CoreConfig.appConfig.getShutdownPort());
    thread.start();
    LOGGER.info("打开服务器关闭端口成功...");
  }

  public void addShutdownHook()
  {
    AppServerShutdownHook hook = new AppServerShutdownHook(this);
    Runtime.getRuntime().addShutdownHook(hook);
    LOGGER.info("添加钩子成功...");
  }

  public void startTimerServer()
  {
    DumpMemoryTask dumpMemoryTask = new DumpMemoryTask();
    TaskPoolFactory.scheduleAtFixedRate("DUMP_MEMORY_TASK", dumpMemoryTask, 60L, 300L, TimeUnit.SECONDS);
  }

  private void stopThreadPools(Map<String, ExecutorService> threadPoolMap)
  {
    for (String key : threadPoolMap.keySet())
    {
      long timeout = 100L;
      if (key.startsWith("DB_"))
      {
        timeout = 10000L;
      }
      List taskList = ThreadPoolFactory.shutDown(key, timeout);
      if (taskList.size() > 0) {
        LOGGER.error("线程池【{}】关闭后，未完成的任务数量【{}】", new Object[] { key, Integer.valueOf(taskList.size()) });
      }
    }
    LOGGER.info("线程池工厂【ThreadPoolFactory】中的所有任务池都已关闭！");
  }

  private void stopTaskPools(Map<String, ScheduledExecutorService> taskPoolMap)
  {
    for (String key : taskPoolMap.keySet())
    {
      List taskList = TaskPoolFactory.shutDown(key, 100);
      if (taskList.size() > 0) {
        LOGGER.error("任务池【{}】关闭后，未完成的任务数量【{}】", new Object[] { key, Integer.valueOf(taskList.size()) });
      }
    }
    LOGGER.info("任务池工厂【TaskPoolFactory】中的所有任务池都已关闭！");
  }
}