package com.me.http;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.me.task.Analysis;
import com.me.utils.BaseUtil;
import com.me.utils.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class MapperCallBack<T> implements Callback {
	private Save saveListener;
	private Parse parseListener;
	
	private int count = 0;
	
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
		public void save(T entity );
	}
	
	
	public interface Parse<T>{
		public T parse(String body);
	}
	
	
	
	public void onFailure(Call call, IOException e) {
		// TODO Auto-generated method stub
		
	}

	public void onResponse(Call call, Response response) throws IOException {
		
		if(parseListener==null || saveListener== null){
			throw new IOException("监听器为初始化");
		}
		
		
		
		String body = response.body().string();
		//Log.D(body);
		//交由2个注册的类进行解析和保存操作
		T entity = (T) parseListener.parse(body);
		saveListener.save(entity);
		
		
		//TODO
		//解析html中的其他链接加入到链接队列中
		HttpUrl url = response.request().url();
		String s_url = url.scheme()+"://www."+url.host()+"/";
		Log.D("d--->"+s_url);
//		analysis.MainDomin = s_url;
//		List<String> urls = analysis.GetUrls(body);
//		baseUtil.AddUrls2Queue((String[]) urls.toArray());
		
		//解析和保存完毕后将爬取成功的url加入到已经爬取过的url列表里面
		System.out.println(url.toString());
		baseUtil.addUrl2AlreadyQueue(url.toString());
		System.out.println("已处理"+count++ +"个URL");
	}



}
