package com.tag.framework.security;

import com.tag.framework.model.AbstractObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccessToken extends AbstractObject
{
  private String userId;
  private long expire;
  private String tokenId;
  private ConcurrentMap<String, Object> sessionMap = new ConcurrentHashMap();

  public String getUserId() {
    return this.userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public long getExpire() {
    return this.expire;
  }
  public void setExpire(long expire) {
    this.expire = expire;
  }
  public String getTokenId() {
    return this.tokenId;
  }
  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }
  public ConcurrentMap<String, Object> getSessionMap() {
    return this.sessionMap;
  }
  public void setSessionMap(ConcurrentMap<String, Object> sessionMap) {
    this.sessionMap = sessionMap;
  }
}