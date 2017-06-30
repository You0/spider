package com.me.redis;

import java.io.IOException;

import java.util.Properties;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import redis.clients.jedis.JedisPool;

@Component
public class RedisPool {

	private static JedisPool pool;

	// 静态代码初始化池配置

	static {

		try {

			Properties props = new Properties();

			props.load(RedisPool.class.getClassLoader().getResourceAsStream("redis.properties"));

			pool = new JedisPool(props.getProperty("redis.ip"),
					Integer.valueOf(props.getProperty("redis.port")));

		} catch (IOException e) {

			e.printStackTrace();

		}

	}


	
	public Jedis getResource(){
		return pool.getResource();
	}
	
	

	/** 归还jedis对象 */
	public void recycleJedisOjbect(Jedis jedis) {
		pool.returnResource(jedis);
	}


}
