package com.me.example;

import com.me.http.MapperCallBack;
import com.me.http.MapperCallBack.Parse;
import com.me.http.MapperCallBack.Save;
import com.me.spider.Crawl;
import com.me.utils.Log;
import com.me.utils.SpiderUtil;

public class Example {
	public static void main(String[] args) {
		//使用案例
		
		Crawl crawl = SpiderUtil.Steup();
		
		//这里传入正则，比如我要只想要爬取煎蛋网则可以这样写：
		crawl.setMatchRegex("http://jandan.net/.*");
		
		//这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[]{"http://jandan.net/2017/06/30/forgetful-brain.html"});
		
		//这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<bean> callBack = SpiderUtil.getMapperCallBack();
		//callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<bean>() {
			public bean parse(String body) {
				return null;
			}
		});
		
		//callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<bean>() {
			public void save(bean entity) {
				
			}
		});
		//把接口设置进去
		crawl.setListener(callBack);
		
		
		//好了配置完成愉快的等爬取完成吧！
		crawl.start();

	}
}
