package com.me.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.me.http.MapperCallBack;
import com.me.spider.Crawl;

public class SpiderUtil {
	
	static ApplicationContext context;
	
    public static Crawl Steup()
    {
    	context = new ClassPathXmlApplicationContext("classpath:spring.xml");        
		return (Crawl) context.getBean("crawl");
    }
    
    
   public static MapperCallBack getMapperCallBack() {
		return (MapperCallBack) context.getBean("mapperCallBack");
	}
}
