package com.me.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.me.spider.Crawl;

public class SpiderUtil {
    public static Crawl Steup()
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");        
		return (Crawl) context.getBean("crawl");
    }
}
