package com.me.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.me.utils.Log;

import redis.clients.jedis.Jedis;

@Service
public class RedisService {
	@Autowired
	private RedisPool redisPool;
	
	
	public boolean exists(String key) {
        Jedis jedis = null;

        boolean var3;
        try {
            jedis = (Jedis)redisPool.getResource();
            var3 = jedis.exists(key).booleanValue();
        } finally {
            if(jedis != null) {
                jedis.close();
            }

        }

        return var3;
    }

    public long delete(String... keys) {
        Jedis jedis = null;

        long var3;
        try {
            jedis = (Jedis)redisPool.getResource();
            var3 = jedis.del(keys).longValue();
        } finally {
            if(jedis != null) {
                jedis.close();
            }

        }

        return var3;
    }
	
	
	public boolean lpush(String key,String... value){
		Jedis jedis = null;
		Long var3;
        try {
            jedis = (Jedis)redisPool.getResource();
            var3 = jedis.lpush(key, value);
        } finally {
            if(jedis != null) {
                jedis.close();
            }

        }
        return var3.longValue()>0 ? true:false;
	}
	
	
	public boolean rpush(String key,String... value){
		Jedis jedis = null;
		Long var3;
        try {
            jedis = (Jedis)redisPool.getResource();
            var3 = jedis.rpush(key, value);
        } finally {
            if(jedis != null) {
                jedis.close();
            }

        }
        return var3.longValue()>0 ? true:false;
	}
	
	public String lpop(String key){
		Jedis jedis = null;
		String var3;
        try {
            jedis = (Jedis)redisPool.getResource();
            var3 = jedis.lpop(key);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        if(var3==null){
        	return "nil";
        }
        return var3;
	}
	
	
	//返回整个列表
	public List<String> lrange(String key){
		Jedis jedis = null;
		List<String> list;
        try {
            jedis = (Jedis)redisPool.getResource();
            list = jedis.lrange(key, 0, -1);
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
        return list;
	}
	
	
	
	
	
	public static void main(String[] args) {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");        
    	
		RedisService redisService = (RedisService) context.getBean("redisService");
		redisService.lpush("dd", "77");
	}

}
