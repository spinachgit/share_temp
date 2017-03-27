package com.tag.framework.service;

import com.tag.framework.security.AccessToken;

public abstract interface TokenService
{
  public abstract AccessToken checkToken(String paramString);

  public abstract AccessToken createTokenByUserId(String paramString);

  public abstract AccessToken createTokenByTokenId(String paramString1, String paramString2);

  public abstract void setAttribute(String paramString1, String paramString2, Object paramObject);

  public abstract Object getAttribute(String paramString1, String paramString2);

  public abstract void removeAttribute(String paramString1, String paramString2);
}