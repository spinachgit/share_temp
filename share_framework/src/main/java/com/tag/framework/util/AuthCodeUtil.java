package com.tag.framework.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthCodeUtil
{
  private static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
  private static final String BM_CODE = "bm_code";
  private static Random random = new Random();

  public static void createAuthCodeAndOutput(int wide, int high, int size, HttpServletResponse response)
    throws IOException
  {
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0L);
    response.setContentType("image/gif");

    String authCode = createAuthCode(size);

    String authCodeMD5 = StringUtil.md5(authCode.toUpperCase());
    CookieUtil.writeCookie(response, "bm_code", authCodeMD5);

    OutputStream os = null;
    try {
      os = response.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }

    outputImage(wide, high, os, authCode);
  }

  public static boolean checkAuthCodeFromCookie(String authCode, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    response.setCharacterEncoding("utf-8");
    String authCodeMd5 = StringUtil.md5(authCode.toUpperCase());

    String authCodeMd5FromCookie = CookieUtil.getCookieValue(request, "bm_code");

    CookieUtil.removeCookie("bm_code", request, response);

    if (authCodeMd5.equals(authCodeMd5FromCookie)) {
      return true;
    }
    return false;
  }

  public static String createAuthCode(int authSize)
  {
    return createAuthCode(authSize, "23456789ABCDEFGHJKLMNPQRSTUVWXYZ");
  }

  public static String createAuthCode(int authSize, String sources)
  {
    if ((sources == null) || (sources.length() == 0)) {
      sources = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    }
    int codesLen = sources.length();
    Random rand = new Random(System.currentTimeMillis());
    StringBuilder authCode = new StringBuilder(authSize);
    for (int i = 0; i < authSize; i++) {
      authCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
    }
    return authCode.toString();
  }

  public static void outputImage(int wide, int high, File outputFile, String code)
    throws IOException
  {
    if (outputFile == null) {
      return;
    }
    File dir = outputFile.getParentFile();
    if (!dir.exists())
      dir.mkdirs();
    try
    {
      outputFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(outputFile);
      outputImage(wide, high, fos, code);
      fos.close();
    } catch (IOException e) {
      throw e;
    }
  }

  public static void outputImage(int wide, int high, OutputStream os, String code)
    throws IOException
  {
    int w = wide;
    int h = high;
    int authSize = code.length();
    BufferedImage image = new BufferedImage(w, h, 1);
    Random rand = new Random();
    Graphics2D g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Color[] colors = new Color[5];
    Color[] colorSpaces = { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW };

    float[] fractions = new float[colors.length];
    for (int i = 0; i < colors.length; i++) {
      colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
      fractions[i] = rand.nextFloat();
    }
    Arrays.sort(fractions);
    g2.setColor(Color.GRAY);
    g2.fillRect(0, 0, w, h);
    Color c = getRandColor(200, 250);
    g2.setColor(c);
    g2.fillRect(0, 2, w, h - 4);

    Random random = new Random();
    g2.setColor(getRandColor(160, 200));
    for (int i = 0; i < 20; i++) {
      int x = random.nextInt(w - 1);
      int y = random.nextInt(h - 1);
      int xl = random.nextInt(6) + 1;
      int yl = random.nextInt(12) + 1;
      g2.drawLine(x, y, x + xl + 40, y + yl + 20);
    }

    float yawpRate = 0.05F;
    int area = (int)(yawpRate * w * h);
    for (int i = 0; i < area; i++) {
      int x = random.nextInt(w);
      int y = random.nextInt(h);
      int rgb = getRandomIntColor();
      image.setRGB(x, y, rgb);
    }

    shear(g2, w, h, c);

    g2.setColor(getRandColor(100, 160));
    int fontSize = h - 4;
    Font font = new Font("Fixedsys", 1, fontSize);
    g2.setFont(font);
    char[] chars = code.toCharArray();
    for (int i = 0; i < authSize; i++) {
      AffineTransform affine = new AffineTransform();
      affine.setToRotation(0.7853981633974483D * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), w / authSize * i + fontSize / 2, h / 2);
      g2.setTransform(affine);
      g2.drawChars(chars, i, 1, (w - 10) / authSize * i + 5, h / 2 + fontSize / 2 - 10);
    }

    g2.dispose();
    ImageIO.write(image, "jpg", os);
  }

  private static Color getRandColor(int fc, int bc) {
    if (fc > 255)
      fc = 255;
    if (bc > 255)
      bc = 255;
    int r = fc + random.nextInt(bc - fc);
    int g = fc + random.nextInt(bc - fc);
    int b = fc + random.nextInt(bc - fc);
    return new Color(r, g, b);
  }

  private static int getRandomIntColor() {
    int[] rgb = getRandomRgb();
    int color = 0;
    for (int c : rgb) {
      color <<= 8;
      color |= c;
    }
    return color;
  }

  private static int[] getRandomRgb() {
    int[] rgb = new int[3];
    for (int i = 0; i < 3; i++) {
      rgb[i] = random.nextInt(255);
    }
    return rgb;
  }

  private static void shear(Graphics g, int w1, int h1, Color color) {
    shearX(g, w1, h1, color);
    shearY(g, w1, h1, color);
  }

  private static void shearX(Graphics g, int w1, int h1, Color color)
  {
    int period = random.nextInt(2);

    boolean borderGap = true;
    int frames = 1;
    int phase = random.nextInt(2);

    for (int i = 0; i < h1; i++) {
      double d = (period >> 1) * Math.sin(i / period + 6.283185307179586D * phase / frames);
      g.copyArea(0, i, w1, 1, (int)d, 0);
      if (borderGap) {
        g.setColor(color);
        g.drawLine((int)d, i, 0, i);
        g.drawLine((int)d + w1, i, w1, i);
      }
    }
  }

  private static void shearY(Graphics g, int w1, int h1, Color color)
  {
    int period = random.nextInt(40) + 10;

    boolean borderGap = true;
    int frames = 20;
    int phase = 7;
    for (int i = 0; i < w1; i++) {
      double d = (period >> 1) * Math.sin(i / period + 6.283185307179586D * phase / frames);
      g.copyArea(i, 0, 1, h1, 0, (int)d);
      if (borderGap) {
        g.setColor(color);
        g.drawLine(i, (int)d, i, 0);
        g.drawLine(i, (int)d + h1, i, h1);
      }
    }
  }

  public static void main(String[] args)
    throws IOException
  {
    File dir = new File("D:/verifies");
    int w = 300; int h = 80;
    for (int i = 0; i < 50; i++) {
      String authCode = createAuthCode(4);
      File file = new File(dir, new StringBuilder().append(authCode).append(".jpg").toString());
      outputImage(w, h, file, authCode);
    }
  }
}