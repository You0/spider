package com.me.redis;

import java.io.IOException;

import java.util.Properties;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import redis.clients.jedis.JedisPool;

@Component
public class RedisPool {

	private static JedisPool pool;
	public static String ip;
	public static String port;
	public static String pwd;
	// 静态代码初始化池配置

	static {

		try {

			Properties props = new Properties();

			props.load(RedisPool.class.getClassLoader().getResourceAsStream("redis.properties"));

			if(ip!=null && port!=null){
				pool = new JedisPool(ip,
						Integer.valueOf(port));
			}else{
				pool = new JedisPool(props.getProperty("redis.ip"),
						Integer.valueOf(props.getProperty("redis.port")));
			}
			

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void SetRedisServer(String ip,String port)
	{
		pool = new JedisPool(ip,
				Integer.valueOf(port));
		
	}
	

	
	public Jedis getResource(){
		return pool.getResource();
	}
	
	

	/** 归还jedis对象 */
	public void recycleJedisOjbect(Jedis jedis) {
		pool.returnResource(jedis);
	}


}
