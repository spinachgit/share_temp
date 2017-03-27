package com.tag.framework.service;

import com.tag.framework.page.Page;

import java.util.List;
import java.util.Map;

public abstract interface MybatisBaseServiceOld
{
  public abstract <T> int insert(T paramT);

  public abstract <T> int update(T paramT);

  public abstract <K, V> int delete(Map<K, V> paramMap);

  public abstract <K, V, T> T selectObject(Map<K, V> paramMap);

  public abstract <K, V, T> List<T> selectObjectList(Map<K, V> paramMap);

  public abstract <K, V, O> Map<K, O> selectMap(Map<K, V> paramMap);

  public abstract <K, V, O> List<Map<K, O>> selectMapList(Map<K, V> paramMap);

  public abstract <K, V, T> Page<T> page(Map<K, V> paramMap, int paramInt1, int paramInt2);
}