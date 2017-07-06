package com.me.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.redis.RedisService;

@Component
public class BaseUtil {
	@Autowired
	public RedisService redisService;
	
	private Set<String> al_urls = new HashSet<String>();
	
	public boolean SetStartUrls(String[] urls){
		return redisService.rpush("url", urls);
	}
	
	public boolean AddUrls2Queue(String[] urls){
		return redisService.rpush("url", urls);
	}
	
	
	public boolean AddFailUrl2Queue(String url){
		return redisService.rpush("fail", url);
	}
	
	
	public String getUrlFromQueue(){
		return redisService.lpop("url");
	}
	
	public boolean addUrl2AlreadyQueue(String... urls){
		//将url放入内存中
		for(int i=0;i<urls.length;i++){
			al_urls.add(urls[i]);
		}
		return redisService.lpush("already", urls);
	}
	
	//将redis里的数据加载到内存中,每次重新启动的时候进行加载
	public void LoadAlready2RAM(String key){
		List<String> ls = redisService.lrange(key);
		for(int i=0;i<ls.size();i++){
			al_urls.add(ls.get(i));
		}
	}

	public Set<String> getAl_urls() {
		return al_urls;
	}
	
	

}
