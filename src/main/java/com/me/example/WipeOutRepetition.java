package com.me.example;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.me.redis.RedisService;
import com.me.spider.Crawl;
import com.me.utils.BaseUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class WipeOutRepetition {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");  
		RedisService service = (RedisService) context.getBean("redisService");
		
		
		final String uri = "jdbc:mysql://localhost:3306/url";
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = (Connection) DriverManager.getConnection(uri, "root", "9562");
		PreparedStatement ps = (PreparedStatement) conn.prepareStatement("SELECT url FROM url");
		ResultSet eResultSet = ps.executeQuery();
		while(eResultSet.next()){
			String string=eResultSet.getString(1);
			service.lpush("aaaa", string);
		}
		
	}
	
}