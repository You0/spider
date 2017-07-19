package com.me.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
		List<String> arr = new ArrayList<String>();
		for(int i=0;i<urls.length;i++){
			if(!al_urls.contains(urls[i])){
				arr.add(urls[i]);
			}
		}
		
		String[] us = new String[arr.size()];
		for(int i=0;i<us.length;i++){
			us[i] = arr.get(i);
		}

		return redisService.rpush("url", us);
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
		
		//System.out.println("内存中已缓存:"+al_urls.size()+"条");
		return redisService.lpush("already", urls);
	}
	
	//将redis里的数据加载到内存中,每次重新启动的时候进行加载
	public void LoadAlready2RAM(String key){
		List<String> ls = redisService.lrange(key);
		int i=0;
		for(i=0;i<ls.size();i++){
			boolean result = al_urls.add(ls.get(i));
		}
		System.out.println("已加载爬取过的URL"+i+"条");
		System.out.println("已加载爬取过的URL"+i+"条");
		System.out.println("已加载爬取过的URL"+i+"条");
		System.out.println("已加载爬取过的URL"+i+"条");
		System.out.println("已加载爬取过的URL"+i+"条");
	}
	
	
	public void LoadFail2Url(){
		List<String> ls = redisService.lrange("fail");
		for(int i=0;i<ls.size();i++){
			redisService.lpush("url", ls.get(i));
		}
	}
	
	
	public void DeleteFail(){
		redisService.delete("fail");
	}
	
	
	
	

	public Set<String> getAl_urls() {
		return al_urls;
	}
	
	

}
