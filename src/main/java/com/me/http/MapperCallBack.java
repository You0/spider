package com.me.http;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.me.spider.Crawl;
import com.me.task.Analysis;
import com.me.utils.BaseUtil;
import com.me.utils.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

@Component
@Scope("prototype")
public class MapperCallBack<T> implements Callback {
	private Save saveListener;
	private Parse parseListener;
	
	private static int count = 0;
	
	@Autowired
	private BaseUtil baseUtil;
	@Autowired
	private Analysis analysis;
	
	
	public void setSaveListener(Save saveListener) {
		this.saveListener = saveListener;
	}
	
	public void setParseListener(Parse parseListener) {
		this.parseListener = parseListener;
	}
	
	/**
	 * 使用范型，这里直接传入自己定义好的一个bean，然后把bean存入自己想存入的地方就行了
	 * */
	public interface Save<T>{
		public void save(String url,T entity );
	}
	
	
	public interface Parse<T>{
		public T parse(String url,String body);
	}
	
	
	
	public void onFailure(Call call, IOException e) {
		// TODO Auto-generated method stub
		System.out.println(call.request().url().toString() + "Failure");
		Crawl.semaphore.release();
		baseUtil.AddFailUrl2Queue(call.request().url().toString());
		
	}

	public void onResponse(Call call, Response response) throws IOException {
		Crawl.semaphore.release();
		
		
		if(parseListener==null || saveListener== null){
			throw new IOException("监听器为初始化");
		}
		
		HttpUrl url = response.request().url();
		Log.D(url.toString());
		
		//如果返回码不等于200也就是返回的结果不正确，说明网站此时不可用或者该链接不可用则把url加入到失败队列
		if(response.code()!=200){
			System.out.println("code Error");
			baseUtil.AddFailUrl2Queue(url.toString());
			baseUtil.addUrl2AlreadyQueue(url.toString());
			return;
		}
		
		
		
		String body = response.body().string();
		//Log.D(body);
		//交由2个注册的类进行解析和保存操作
		T entity = (T) parseListener.parse(url.toString(),body);
		saveListener.save(url.toString(),entity);
		
		
		
		//提取html里面的url时如果href时简写的话，就往前面拼接上域名头
		analysis.MainDomin = url.scheme()+"://"+url.host();
		List<String> urls = analysis.GetUrls(body);
		
		String[] s_urls = new String[urls.size()];
		for(int i=0;i<urls.size();i++){
			s_urls[i] = urls.get(i);
		}
		if(s_urls!=null && s_urls.length>0){
			baseUtil.AddUrls2Queue(s_urls);
		}
		baseUtil.addUrl2AlreadyQueue(url.toString());
		//解析和保存完毕后将爬取成功的url加入到已经爬取过的url列表里面
		System.out.println("已处理"+count++ +"个,url:"+url.toString());
		
	}



}
