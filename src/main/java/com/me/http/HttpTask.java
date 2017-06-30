package com.me.http;

import org.springframework.stereotype.Component;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Component
public class HttpTask {
	private OkHttpClient client = new OkHttpClient();
	//提交url任务
	public Call submit(String url){
		Request request = new Request.Builder().url(url)
				.build();
		
		return client.newCall(request);
	}
	
	
}
