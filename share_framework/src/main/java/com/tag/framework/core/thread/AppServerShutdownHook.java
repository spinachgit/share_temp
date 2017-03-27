package com.tag.framework.core.thread;

import com.tag.framework.server.IAppServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppServerShutdownHook extends Thread
{
  private static final Logger LOGGER = LoggerFactory.getLogger(AppServerShutdownHook.class);
  private IAppServer server;

  public AppServerShutdownHook(IAppServer server)
  {
    this.server = server;
  }

  public void run()
  {
    this.server.stop();
  }
}