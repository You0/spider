package com.me.spider;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.me.http.HttpTask;
import com.me.http.MapperCallBack;
import com.me.task.AnsyTask;
import com.me.utils.BaseUtil;

@Controller("crawl")
public class Crawl {
	@Autowired
	private BaseUtil baseUtil;
	
	@Autowired
	private HttpTask httpTask;
	
	private String regexUrl;
	private Pattern pattern;
	
	private MapperCallBack listener;
	//private Save saveListener;
	
	private BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1000);
	
	public void setStartUrls(String[] urls){
		baseUtil.SetStartUrls(urls);
	}
	
	public void setMatchRegex(String reg){
		this.regexUrl = reg;
	}
	
	public void setListener(MapperCallBack listener) {
		this.listener = listener;
	}
	
//	public void setParseListener(MapperCallBack parseListener) {
//		this.parseListener = parseListener;
//	}
	
	
//	public void setSaveListener(Save saveListener) {
//		this.saveListener = saveListener;
//	}
	
	
	public boolean match(String str)
	{
		if(pattern==null){
		   pattern = Pattern.compile(regexUrl);
		}
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	
	/**
	 * 开始执行任务
	 * 
	 * */
	public void start()
	{
		
		
		AnsyTask.runTask(new Runnable() {
			
			public void run() {
				try {
					while(true){
						//多线程并发管理交给okhttp去处理
						String url = blockingQueue.take();
						httpTask.submit(url);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		//每次重新启动的时候将爬取过得url重新加入回去
		baseUtil.LoadAlready2RAM("already");
		while(true){
			String url;
			if(!(url=baseUtil.getUrlFromQueue()).equals("nil")){
				if(match(url)) {
					try {
						blockingQueue.put(url);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
