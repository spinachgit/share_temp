package com.tag.framework.core.threadfactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThreadFactory
  implements ThreadFactory
{
  private String key;
  static final AtomicInteger poolNumber = new AtomicInteger(1);
  final ThreadGroup group;
  final AtomicInteger threadNumber = new AtomicInteger(1);
  final String namePrefix;

  public ServerThreadFactory(String key)
  {
    this.key = key;
    SecurityManager s = System.getSecurityManager();
    this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());

    this.namePrefix = ("pool-" + poolNumber.getAndIncrement() + "-thread-");
  }

  public Thread newThread(Runnable r)
  {
    Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement() + "-key-" + this.key, 0L);

    if (t.isDaemon())
      t.setDaemon(false);
    if (t.getPriority() != 5)
      t.setPriority(5);
    return t;
  }
}