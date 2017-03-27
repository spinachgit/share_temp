package com.tag.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUtil
{
  public static List<String> readLines(File file)
  {
    if (!file.isFile()) {
      throw new RuntimeException("文件" + file.getName() + "不是一个标准文件!");
    }

    List list = new ArrayList();

    FileReader fr = null;
    BufferedReader br = null;
    try {
      fr = new FileReader(file);
      br = new BufferedReader(fr);
      String line = br.readLine();
      while ((line != null) && (line.length() > 0)) {
        list.add(line);
        line = br.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (fr != null) {
        try {
          fr.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return list;
  }

  public static File makeDir(String dirName)
  {
    File file = new File(dirName);
    try {
      if (!file.exists())
      {
        if (!file.exists())
          file.mkdirs();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("创建目录【" + dirName + "】失败!");
    }

    return file;
  }

  public static File makeDirAndFile(String fileName)
  {
    File file = new File(fileName);
    try {
      if (!file.exists())
      {
        File dir = file.getParentFile();
        if (!dir.exists()) {
          dir.mkdirs();
        }

      }

      file.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("创建文件【" + fileName + "】失败!");
    }

    return file;
  }

  public static void writeToFile(String outputFile, String str)
  {
    try
    {
      FileOutputStream out = new FileOutputStream(outputFile);
      out.write(str.getBytes("UTF-8"));
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String md5(File file)
  {
    String value = null;
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(byteBuffer);
      BigInteger bi = new BigInteger(1, messageDigest.digest());
      value = bi.toString(16);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return value;
  }

  public void uploadFileBySrping(HttpServletRequest request, HttpServletResponse response, String dir)
    throws IllegalStateException, IOException
  {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

    if (multipartResolver.isMultipart(request))
    {
      MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;

      Iterator iter = multiRequest.getFileNames();
      while (iter.hasNext())
      {
        long begin = System.currentTimeMillis();

        MultipartFile file = multiRequest.getFile((String)iter.next());
        if (file != null)
        {
          String clientFileName = file.getOriginalFilename();

          if (clientFileName.trim() != "") {
            String tempFileName = UUIDService.getUUID();

            makeDir(dir);

            File localFile = new File(dir + "/" + tempFileName);
            file.transferTo(localFile);
            String md5FileName = md5(localFile);
            localFile.renameTo(new File(dir + "/" + md5FileName));
          }
        }

        long end = System.currentTimeMillis();
        System.out.println(end - begin);
      }
    }
  }

  public static void main(String[] args)
  {
    File file = new File("D:/hao_0.JPG");
    String md5 = md5(file);
    System.out.println(md5);
    file.delete();
    boolean isSuccess = file.renameTo(new File("D:/" + md5 + ".JPG"));
    System.out.println(isSuccess);
  }
}