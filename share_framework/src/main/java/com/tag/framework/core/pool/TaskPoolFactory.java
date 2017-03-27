package com.tag.framework.core.pool;

import com.tag.framework.core.threadfactory.ServerThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskPoolFactory
{
  public static Map<String, ScheduledExecutorService> taskPoolMap = new ConcurrentHashMap();

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskPoolFactory.class);

  public static ScheduledFuture scheduleAtFixedRate(String taskPoolKey, Runnable task, long initialDelay, long period, TimeUnit unit)
  {
    ScheduledExecutorService schedule = getSingleThreadScheduledExecutor(taskPoolKey);
    ScheduledFuture future = schedule.scheduleAtFixedRate(task, initialDelay, period, unit);
    return future;
  }

  private static ScheduledExecutorService getSingleThreadScheduledExecutor(String taskPoolKey)
  {
    synchronized (taskPoolMap) {
      ScheduledExecutorService schedule = (ScheduledExecutorService)taskPoolMap.get(taskPoolKey);
      if (schedule == null) {
        schedule = Executors.newSingleThreadScheduledExecutor(new ServerThreadFactory(taskPoolKey));
        taskPoolMap.put(taskPoolKey, schedule);
      }
      return schedule;
    }
  }

  public static ScheduledFuture scheduleByThreadPool(String threadPoolKey, Runnable task, long initialDelay, long period, TimeUnit unit)
  {
    TimeTask timeTask = new TimeTask(threadPoolKey, task);

    ScheduledFuture future = scheduleAtFixedRate("PUBLIC_TIME_TASK", timeTask, initialDelay, period, unit);
    return future;
  }

  public static List<Runnable> shutDown(String taskPoolKey, int timeout)
  {
    LOGGER.info("关闭任务池【{}】...", new Object[] { taskPoolKey });
    List list = new ArrayList();
    ExecutorService pool = (ExecutorService)taskPoolMap.remove(taskPoolKey);
    pool.shutdown();
    try {
      long waitBegin = System.currentTimeMillis();

      if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
        if (!pool.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
          LOGGER.error("等待【{}】毫秒后，任务仍未执行完毕，立即关闭任务池【{}】！", new Object[] { Integer.valueOf(timeout), taskPoolKey });
          list = pool.shutdownNow();
        }
      } else {
        long waitEnd = System.currentTimeMillis();
        long waitTime = waitEnd - waitBegin;
        LOGGER.info("关闭任务池【{}】之前，池中任务尚未执行完毕，在等待【{}】毫秒后池中任务都已执行完毕！最大等待时间【{}】毫秒", new Object[] { taskPoolKey, Long.valueOf(waitTime), Integer.valueOf(timeout) });
      }

      LOGGER.info("关闭任务池【{}】成功！", new Object[] { taskPoolKey });
    } catch (InterruptedException e) {
      e.printStackTrace();
      LOGGER.error("关闭任务池【{}】的时候出现异常【{}】！！！", new Object[] { taskPoolKey, e.toString() });
    }
    return list;
  }

  @Deprecated
  public static List<Runnable> shutDownNow(String taskPoolKey)
  {
    LOGGER.info("立即关闭任务池【{}】...", new Object[] { taskPoolKey });
    ExecutorService pool = (ExecutorService)taskPoolMap.remove(taskPoolKey);
    List list = pool.shutdownNow();
    LOGGER.info("立即关闭任务池【{}】成功!", new Object[] { taskPoolKey });
    return list;
  }

  private static final class TimeTask
    implements Runnable
  {
    private String threadPoolKey;
    private Runnable task;

    public TimeTask(String threadPoolKey, Runnable task)
    {
      this.threadPoolKey = threadPoolKey;
      this.task = task;
    }

    public void run()
    {
      try {
        ThreadPoolFactory.submit(this.threadPoolKey, this.task);
      } catch (Exception e) {
        e.printStackTrace();
        TaskPoolFactory.LOGGER.error(e.getMessage());
      }
    }
  }
}