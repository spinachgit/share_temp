package com.tag.framework.service;

import java.io.Serializable;
import java.util.Collection;

public abstract interface BaseService<T>
{
  public abstract T getById(Serializable paramSerializable);

  public abstract void deleteById(Serializable paramSerializable);

  public abstract void deleteByIds(String paramString1, String paramString2);

  public abstract void deleteNotTrueByIds(String paramString1, String paramString2);

  public abstract void save(T paramT);

  public abstract void saveAll(Collection<T> paramCollection);

  public abstract void update(T paramT);

  public abstract void updateAll(Collection<T> paramCollection);

  public abstract void delete(T paramT);

  public abstract void deleteAll(Collection<T> paramCollection);

  @Deprecated
  public abstract void saveOrUpdate(T paramT);

  @Deprecated
  public abstract void saveOrUpdateAll(Collection<T> paramCollection);
}