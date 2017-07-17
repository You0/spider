package com.me.spider;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
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
import com.me.utils.Log;

import okhttp3.Call;

@Controller("crawl")
public class Crawl {
	@Autowired
	private BaseUtil baseUtil;

	@Autowired
	private HttpTask httpTask;
	//白名单
	private String regexUrl;
	//黑名单
	private String[] BlackReg;
	
	private Pattern WritePattern;

	private Pattern BlackPattern;
	
	private MapperCallBack listener;
	
	public static Semaphore semaphore = new Semaphore(100);
	
	private String proxyUrl = null;
	private int proxyPort;
	// private Save saveListener;

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}
	
	
	
	public void setBlackReg(String[] blackReg) {
		BlackReg = blackReg;
	}

	public void setStartUrls(String[] urls) {
		baseUtil.SetStartUrls(urls);
	}

	public void setMatchRegex(String reg) {
		this.regexUrl = reg;
	}

	public void setListener(MapperCallBack listener) {
		this.listener = listener;
	}

	public boolean match(String str) {
		if (WritePattern == null) {
			WritePattern = Pattern.compile(regexUrl);
		}
		Matcher matcher = WritePattern.matcher(str);
		return matcher.matches();
	}
	
	
	public boolean BlackMatch(String str){
		for(int i=0;i<BlackReg.length;i++){
			BlackPattern = Pattern.compile(BlackReg[i]);
			Matcher matcher = BlackPattern.matcher(str);
			if(matcher.matches()){
				return true;
			}
		}
		return false;
	}
	
	

	/**
	 * 开始执行任务
	 * 
	 */
	public void start() {
		// 每次重新启动的时候将爬取过得url重新加入回去
		baseUtil.LoadAlready2RAM("already");
		//将失败的url链接载入队列，并删除该队列
		baseUtil.LoadFail2Url();
		baseUtil.DeleteFail();
		if(proxyUrl!=null){
			httpTask.setProxy(proxyUrl, proxyPort);
		}
		// 取出url然后交给okhttp处理,这个线程一直存活。
		AnsyTask.runTask(new Runnable() {

			public void run() {
				try {
					while (true) {
						String url;
						
						if (!(url = baseUtil.getUrlFromQueue()).equals("nil")) {
							if (match(url) && !BlackMatch(url)) {
								if (baseUtil.getAl_urls().contains(url)) {
									//System.out.println("重复，跳过。");
									continue;
								}
								//System.out.println(url);
								semaphore.acquire();
								Call call = httpTask.submit(url);
								call.enqueue(listener);
								
								//call.cancel();
							}
							//System.out.println(url);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
}
