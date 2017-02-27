package com.design.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private JedisPool jedisPool;
	private JedisPoolConfig jedisPoolConfig;
	private String host;
	private int port;

	// private String password;
	// private int timeout;
	public Jedis getResource() {
		if (jedisPool == null) {
			synchronized (this) {
				if (jedisPool == null) {
					jedisPool = new JedisPool(jedisPoolConfig, host, port);
				}
			}
		}
		return jedisPool.getResource();
	}

	public void returnResource(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String generateKey(Class<?> clazz, String methodName) {
		StringBuilder builder = new StringBuilder(clazz.getName());
		builder.append(".");
		builder.append(methodName);
		return builder.toString();
	}
}
