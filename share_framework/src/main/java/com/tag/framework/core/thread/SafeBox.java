package com.tag.framework.core.thread;

import com.tag.framework.core.task.SendCrossDomainTask;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeBox extends Thread
{
  private static final Logger LOGGER = LoggerFactory.getLogger(SafeBox.class);
  private static final int PORT = 843;
  private ServerSocket serverSocket;
  private static ExecutorService threadPool = Executors.newCachedThreadPool();

  public void run()
  {
    try {
      this.serverSocket = new ServerSocket(843);
      LOGGER.info("服务器监听端口:843");
      while (true)
      {
        try
        {
          Socket socket = this.serverSocket.accept();

          Runnable task = new SendCrossDomainTask(socket);
          threadPool.execute(task);
        }
        catch (Exception e) {
          LOGGER.error("服务器出现异常！" + e);
        }
      }
    }
    catch (Exception e) {
      LOGGER.info("843已经被占用,安全沙箱可能已经打开，并不影响服务器启动...");
    }
  }
}