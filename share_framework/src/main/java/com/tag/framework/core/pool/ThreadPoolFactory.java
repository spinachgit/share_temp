package com.tag.framework.core.pool;

import com.tag.framework.core.threadfactory.ServerThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolFactory
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolFactory.class);

  public static ConcurrentHashMap<String, ExecutorService> threadPoolMap = new ConcurrentHashMap();

  public static Future submit(String threadPoolKey, Runnable task)
  {
    Future future = getSingleThreadExecutor(threadPoolKey).submit(task);
    return future;
  }

  public static Future submit(String threadPoolKey, Runnable task, Object result) {
    Future future = getSingleThreadExecutor(threadPoolKey).submit(task, result);
    return future;
  }

  public static Future submit(String threadPoolKey, Callable task) {
    Future future = getSingleThreadExecutor(threadPoolKey).submit(task);
    return future;
  }

  public static void execute(String threadPoolKey, Runnable task)
  {
    getSingleThreadExecutor(threadPoolKey).execute(task);
  }

  public static Future submitToCached(String threadPoolKey, Runnable task)
  {
    Future future = getCachedThreadExecutor(threadPoolKey).submit(task);
    return future;
  }

  public static Future submitToCached(String threadPoolKey, Runnable task, Object result) {
    Future future = getCachedThreadExecutor(threadPoolKey).submit(task, result);
    return future;
  }

  public static Future submitToCached(String threadPoolKey, Callable task) {
    Future future = getCachedThreadExecutor(threadPoolKey).submit(task);
    return future;
  }

  public static void executeToCached(String threadPoolKey, Runnable task)
  {
    getCachedThreadExecutor(threadPoolKey).execute(task);
  }

  private static ExecutorService getSingleThreadExecutor(String threadPoolKey)
  {
    synchronized (threadPoolMap) {
      ExecutorService executorService = (ExecutorService)threadPoolMap.get(threadPoolKey);
      if (executorService == null) {
        executorService = Executors.newSingleThreadExecutor(new ServerThreadFactory(threadPoolKey));
        threadPoolMap.put(threadPoolKey, executorService);
        LOGGER.info("创建一个【单线程】的【线程池】,key=【{}】", new Object[] { threadPoolKey });
      }
      return executorService;
    }
  }

  private static ExecutorService getCachedThreadExecutor(String threadPoolKey)
  {
    synchronized (threadPoolMap) {
      ExecutorService executorService = (ExecutorService)threadPoolMap.get(threadPoolKey);
      if (executorService == null) {
        executorService = Executors.newCachedThreadPool(new ServerThreadFactory(threadPoolKey));
        threadPoolMap.put(threadPoolKey, executorService);
        LOGGER.info("创建一个【多线程】的【线程池】,key=【{}】", new Object[] { threadPoolKey });
      }
      return executorService;
    }
  }

  public static List<Runnable> shutDown(String threadPoolKey, long timeout)
  {
    LOGGER.info("关闭线程池【{}】...", new Object[] { threadPoolKey });
    List list = new ArrayList();
    ExecutorService pool = (ExecutorService)threadPoolMap.remove(threadPoolKey);
    pool.shutdown();
    try {
      long waitBegin = System.currentTimeMillis();

      if (!pool.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
        LOGGER.error("等待【{}】毫秒后，任务仍未执行完毕，立即关闭线程池【{}】！", new Object[] { Long.valueOf(timeout), threadPoolKey });
        list = pool.shutdownNow();
      } else {
        long waitEnd = System.currentTimeMillis();
        long waitTime = waitEnd - waitBegin;
        LOGGER.info("关闭线程池【{}】之前，池中任务尚未执行完毕，在等待【{}】毫秒后池中任务都已执行完毕！最大等待时间【{}】毫秒", new Object[] { threadPoolKey, Long.valueOf(waitTime), Long.valueOf(timeout) });
      }
      LOGGER.info("关闭线程池【{}】成功！", new Object[] { threadPoolKey });
    } catch (InterruptedException e) {
      e.printStackTrace();
      LOGGER.error("关闭线程池【{}】的时候出现异常【{}】！！！", new Object[] { threadPoolKey, e.toString() });
    }
    return list;
  }

  @Deprecated
  public static List<Runnable> shutDownNow(String threadPoolKey)
  {
    LOGGER.info("立即关闭线程池【{}】...", new Object[] { threadPoolKey });
    ExecutorService pool = (ExecutorService)threadPoolMap.remove(threadPoolKey);
    List list = pool.shutdownNow();
    LOGGER.info("立即关闭线程池【{}】成功!", new Object[] { threadPoolKey });
    return list;
  }
}