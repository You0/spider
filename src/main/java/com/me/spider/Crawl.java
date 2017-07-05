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
import com.me.utils.Log;

@Controller("crawl")
public class Crawl {
	@Autowired
	private BaseUtil baseUtil;

	@Autowired
	private HttpTask httpTask;

	private String regexUrl;
	private Pattern pattern;

	private MapperCallBack listener;
	// private Save saveListener;


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
		if (pattern == null) {
			pattern = Pattern.compile(regexUrl);
		}
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 开始执行任务
	 * 
	 */
	public void start() {
		// 每次重新启动的时候将爬取过得url重新加入回去
		baseUtil.LoadAlready2RAM("already");

		// 取出url然后交给okhttp处理,这个线程一直存活。
		AnsyTask.runTask(new Runnable() {

			public void run() {
				try {
					while (true) {
						String url;
						if (!(url = baseUtil.getUrlFromQueue()).equals("nil")) {
							if (match(url)) {
								if (baseUtil.getAl_urls().contains(url)) {
									// System.out.println("重复，跳过。");
									continue;
								}
								httpTask.submit(url).enqueue(listener);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
}
