package com.tag.framework.util;

import java.util.UUID;

public class UUIDService
{
  private static Integer memoryId = Integer.valueOf(1000000000);

  public static int getMemoryId()
  {
    synchronized (memoryId) {
      if (memoryId.intValue() == 2147483647) {
        memoryId = Integer.valueOf(1000000000);
      }
      Integer localInteger1 = memoryId; Integer localInteger2 = UUIDService.memoryId = Integer.valueOf(memoryId.intValue() + 1);
      return -memoryId.intValue();
    }
  }

  public static String getUUID()
  {
    UUID uuid = UUID.randomUUID();
    String str = uuid.toString().replaceAll("-", "");
    return str;
  }
}