package com.tag.framework.service.impl;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tag.framework.conf.CoreConfig;
import com.tag.framework.core.pool.TaskPoolFactory;
import com.tag.framework.security.AccessToken;
import com.tag.framework.service.TokenService;
import com.tag.framework.util.StringUtil;
import com.tag.framework.util.TimeUtil;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

	private static ConcurrentHashMap<String, AccessToken> tokenMap = new ConcurrentHashMap();
    
	public AccessToken checkToken(String tokenId) {
		AccessToken accessToken = (AccessToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.info("token【{}】不存在！", tokenId);
			return null;
		}
		if (System.currentTimeMillis() > accessToken.getExpire()) {
			LOGGER.info("token【{}】已过期！", tokenId);
			return null;
		}
		if (!accessToken.getTokenId().equals(tokenId)) {
			LOGGER.error("用户【{}】的token错误！用户发送的token【{}】,服务器内存中token【{}】", new Object[] { accessToken.getUserId(), accessToken.getTokenId(), tokenId });
			return null;
		}
		accessToken.setExpire(System.currentTimeMillis() + CoreConfig.appConfig.getTokenExpire() * 1000L);
		return accessToken;
	}

	public AccessToken createTokenByUserId(String userId) {
		if ((userId == null) || ("".equals(userId))) {
			LOGGER.error("userId必须是非空的字符串！");
			throw new RuntimeException("userId必须是非空的字符串！");
		}

		AccessToken token = new AccessToken();
		token.setUserId(userId);
		token.setTokenId(StringUtil.md5(userId + UUID.randomUUID().toString()));
		token.setExpire(System.currentTimeMillis() + CoreConfig.appConfig.getTokenExpire() * 1000L);
		tokenMap.put(token.getTokenId(), token);

		return token;
	}

	public AccessToken createTokenByTokenId(String tokenId, String userId) {
		if ((tokenId == null) || ("".equals(tokenId))) {
			LOGGER.error("tokenId必须是非空的字符串！");
			throw new RuntimeException("tokenId必须是非空的字符串！");
		}
		if ((userId == null) || ("".equals(userId))) {
			LOGGER.error("userId必须是非空的字符串！");
			throw new RuntimeException("userId必须是非空的字符串！");
		}

		AccessToken token = new AccessToken();
		token.setUserId(userId);
		token.setTokenId(tokenId);
		token.setExpire(System.currentTimeMillis() + CoreConfig.appConfig.getTokenExpire() * 1000L);
		tokenMap.put(token.getTokenId(), token);

		return token;
	}

	public void setAttribute(String tokenId, String key, Object obj) {
		AccessToken accessToken = (AccessToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("setAttribute 的时候，accessToken 为null");
			return;
		}
		accessToken.getSessionMap().put(key, obj);
	}

	public Object getAttribute(String tokenId, String key) {
		AccessToken accessToken = (AccessToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("getAttribute 的时候，accessToken 为null");
			return null;
		}
		Object obj = accessToken.getSessionMap().get(key);
		return obj;
	}

	public void removeAttribute(String tokenId, String key) {
		AccessToken accessToken = (AccessToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("removeAttribute 的时候，accessToken 为null");
			return;
		}
		accessToken.getSessionMap().remove(key);
	}

	static {
		CleanAccessTokenTask task = new CleanAccessTokenTask();
		TaskPoolFactory.scheduleAtFixedRate("CleanAccessTokenTask", task, 10L, 10L, TimeUnit.SECONDS);
	}

	private static class CleanAccessTokenTask implements Runnable {
		public void run() {
			for (AccessToken token : TokenServiceImpl.tokenMap.values())
				if (token.getExpire() < System.currentTimeMillis()) {
					TokenServiceImpl.tokenMap.remove(token.getTokenId());
					TokenServiceImpl.LOGGER.info("删除用户【{}】过期的token【{}】,过期时间【{}】",
							new Object[] { token.getUserId(), token.getTokenId(), TimeUtil.getFormatTime(token.getExpire(), "yyyy-MM-dd HH:mm:ss") });
				}
		}
	}
}