package com.tag.framework.model.conf;

import java.io.Serializable;

class AbstractObject
  implements Serializable, Cloneable
{
  public static final long serialVersionUID = 1L;

  public String toString()
  {
    return null ; //ToStringBuilder.reflectionToString(this);
  }

  public Object clone()
  {
    Object obj = null;
    try {
      obj = super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return obj;
  }
}