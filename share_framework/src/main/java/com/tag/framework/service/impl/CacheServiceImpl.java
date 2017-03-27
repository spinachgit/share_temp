package com.tag.framework.service.impl;

import com.tag.framework.service.CacheService;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl
  implements CacheService, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);

  private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap();

  public void put(String key, Object obj)
  {
    if (key == null) {
      LOGGER.error("缓存对象【{}】失败，key不能为null！", new Object[] { obj });
      throw new RuntimeException("缓存对象【{" + obj + "}】失败，key不能为null！");
    }

    cache.put(key, obj);
  }

  public Object get(String key)
  {
    if (key == null) {
      LOGGER.error("获取缓存对象失败，key不能为null！");
      throw new RuntimeException("获取缓存对象失败，key不能为null！");
    }

    return cache.get(key);
  }

  public void remove(String key)
  {
    if (key == null) {
      LOGGER.error("删除缓存对象失败，key不能为null！");
      throw new RuntimeException("删除缓存对象失败，key不能为null！");
    }

    cache.remove(key);
  }

  public Set<String> keySet()
  {
    return cache.keySet();
  }

  public int size()
  {
    return cache.size();
  }

  public boolean containsKey(String key)
  {
    return cache.containsKey(key);
  }
}