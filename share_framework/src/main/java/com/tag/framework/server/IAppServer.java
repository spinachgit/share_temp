package com.tag.framework.server;

public abstract interface IAppServer extends IServer
{
  public abstract void initLog4j();

  public abstract void initSpring();

  public abstract void startStopServer();

  public abstract void addShutdownHook();

  public abstract void startTimerServer();
}