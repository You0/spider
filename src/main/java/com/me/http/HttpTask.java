package com.me.http;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Component
public class HttpTask {
	private OkHttpClient client;
	private Proxy mProxy;
	//提交url任务
	public Call submit(String url){
		OkHttpClient.Builder builder = null;
		if(client == null){
			if(mProxy!=null){
				 builder = new OkHttpClient.Builder().proxy(mProxy);
			}else{
				builder = new OkHttpClient.Builder();
			}
			
			//builder.readTimeout(10, TimeUnit.SECONDS).connectTimeout(10,TimeUnit.SECONDS);
			
			 client = builder.build();
			 client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
			 client.newBuilder().readTimeout(10,TimeUnit.SECONDS);
			 client.newBuilder().writeTimeout(10,TimeUnit.SECONDS);
		}
		
		Request request = new Request.Builder().url(url)
				.build();
		
		
		Call call = client.newCall(request);
		return call;
	}
	
	public boolean setProxy(String addr,int port){
		Proxy proxyTest = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(addr, port));
		return true;
	}
	
	
	
}
