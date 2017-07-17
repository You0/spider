package com.me.example;



import com.me.http.MapperCallBack;
import com.me.http.MapperCallBack.Parse;
import com.me.http.MapperCallBack.Save;
import com.me.spider.Crawl;

import com.me.utils.SpiderUtil;
import com.mysql.jdbc.Connection;


public class Example {

	public static void main(String[] args) throws Exception {
		// 使用案例

		Crawl crawl = SpiderUtil.Steup();
		
		//设置代理，必须在start之前
		crawl.setProxyPort(1080);
		crawl.setProxyUrl("127.0.0.1");
		// 白名单，只有url满足传入的正则表达式才会进行爬取：
		crawl.setMatchRegex("xxxx");
		// 黑名单,黑名单支持传入多个值
		crawl.setBlackReg(new String[] {"xxxx"});

		// 这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[] { "xxx" });

		// 这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<bean> callBack = SpiderUtil.getMapperCallBack();
		// callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<bean>() {
			public bean parse(String url, String body) {
				return null;
			}
		});

		// callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<bean>() {
			public void save(String url, bean entity) {
			}
		});
		// 把接口设置进去
		crawl.setListener(callBack);

		// 好了配置完成愉快的等爬取完成吧！
		crawl.start();

	}
}
