package com.tag.framework.core.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopServerThread extends Thread
{
  private static final Logger serverStartAndStopLog = LoggerFactory.getLogger("ServerStartAndStopLog");
  private int shutdownPort;

  public StopServerThread(int shutdownPort)
  {
    this.shutdownPort = shutdownPort;
  }

  public void run()
  {
    ServerSocket serverSocket = null;
    try
    {
      serverSocket = new ServerSocket(this.shutdownPort);
    } catch (Exception e) {
      serverStartAndStopLog.error("停止端口：【{}】被占用，服务器启动失败！", new Object[] { Integer.valueOf(this.shutdownPort) });
      e.printStackTrace();
      System.exit(0);
    }
    try
    {
      while (true) {
        Socket socket = serverSocket.accept();
        String inetAddress = socket.getInetAddress().toString();

        if ((inetAddress.indexOf("127.0.0.1") != -1) || (inetAddress.indexOf("localhost") != -1)) {
          InputStream is = socket.getInputStream();
          OutputStream os = socket.getOutputStream();
          byte[] bb = new byte[1024];
          int len = is.read(bb);
          String msg = new String(bb, 0, len);
          if ("shutdown".equals(msg)) {
            byte[] bb2 = "正在关闭服务器,可能需要几秒钟，请等待......".getBytes();
            os.write(bb2);
            os.flush();

            System.exit(0);
          } else {
            byte[] bb2 = "非法的命令".getBytes();
            os.write(bb2);
            os.flush();
          }
          if (os != null) os.close();
          if (is != null) is.close(); 
        }
        else
        {
          serverStartAndStopLog.error("来自外网的非法命令：" + inetAddress);
        }
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}