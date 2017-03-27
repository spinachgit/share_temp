package com.tag.framework.util;

public class ArrayUtil
{
  public static int getValueByIndex(int[] ii, int index)
  {
    if (index > ii.length - 1) {
      index = ii.length - 1;
    }
    int value = ii[index];
    return value;
  }
}