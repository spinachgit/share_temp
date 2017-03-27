package com.tag.framework.core.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendCrossDomainTask
  implements Runnable
{
  private static final Logger LOGGER = LoggerFactory.getLogger(SendCrossDomainTask.class);
  public static final String crossDomainPolicy = "";
  private Socket socket;

  public SendCrossDomainTask(Socket socket)
  {
    this.socket = socket;
  }

  public void run()
  {
    try
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
      PrintWriter pw = new PrintWriter(this.socket.getOutputStream());

      char[] by = new char[22];
      br.read(by, 0, 22);
      String head = new String(by);
      if (head.equals("<policy-file-request/>")) {
        pw.print("");
        pw.flush();
        this.socket.close();
        LOGGER.info("通过端口: [843] 校验 as3策略文件 成功...");
      }
      else {
        this.socket.close();
      }
    }
    catch (Exception e) {
      LOGGER.error("服务器出现异常！" + e);
    }
  }
}