package com.me.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.me.http.MapperCallBack;
import com.me.http.MapperCallBack.Parse;
import com.me.http.MapperCallBack.Save;
import com.me.spider.Crawl;
import com.me.task.Analysis;
import com.me.utils.Log;
import com.me.utils.SpiderUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Example {
	static long count = 0;
	 static Connection conn;
	public static void main(String[] args) throws Exception {
		// 使用案例

		final String uri = "jdbc:mysql://localhost:3306/new_jav";
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(uri, "root", "9562");

		Crawl crawl = SpiderUtil.Steup();

		// crawl.setProxyPort(1080);
		// crawl.setProxyUrl("127.0.0.1");
		// 这里传入正则，比如我要只想要爬取煎蛋网则可以这样写：
		crawl.setMatchRegex("https://www.javbus.com/.*");
		// 黑名单
		crawl.setBlackReg(new String[] { "https://www.javbus.com/en.*", "https://www.javbus.com/ja.*",
				"https://www.javbus.com/ko.*", "https://www.javbus.com/uncensored.*" });

		// 这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[] { "https://www.javbus.com/" });

		// 这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<Movie> callBack = SpiderUtil.getMapperCallBack();
		// callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<Movie>() {
			public Movie parse(String url, String body) {
				System.out.println(url);
				Movie movie = null;

				Document document = Jsoup.parse(body);
				Elements els = document.getElementsByClass("item");
				if (url.contains("-")) {
					System.out.println("count"+count++ );
					movie = new Movie();
					LinkedList<String> images;
					images = (LinkedList<String>) Analysis.ImageDate(body);
					StringBuilder stringBuilder = new StringBuilder();
					for (int i = 0; i < images.size(); i++) {
						String img = images.get(i);
						if (img.contains("cover")) {
							movie.setCover(img);
						}
						if (img.contains("dmm")) {
							stringBuilder.append(img + " ");
						}
					}

					movie.setImgs(stringBuilder.toString());
					// 这部影片的文字信息
					Elements text = document.getElementsByTag("p");
					int i = 0;
					boolean hasDirector = false;
					boolean hasSerise = false;
					for (Element ctext : text) {
						String str = ctext.text();
						if (str == null) {
							str = "";
						}
						if (str.contains("識別碼")) {
							String fh = str.split(":")[1].substring(1);
							movie.setFh(fh);

							// http://cc3001.dmm.co.jp/litevideo/freepv/i/ipz/ipz00947/ipz00947_dmb_w.mp4
							String video = "http://cc3001.dmm.co.jp/litevideo/freepv/";
							video += String.valueOf(fh.charAt(0)).toLowerCase();
							String pre = fh.split("-")[0].toLowerCase();
							String subpre = null;
							if (pre.length() > 3) {
								subpre = pre.substring(0, pre.length() - 1);
							} else {
								subpre = pre;
							}
							String bottom = fh.split("-")[1].toLowerCase();
							video += "/" + subpre + "/" + pre + "00" + bottom + "/" + pre + "00" + bottom
									+ "_dmb_w.mp4";

							//System.out.println(video);
							movie.setVideo(video);

						} else if (str.contains("發行日期")) {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							try {
								movie.setDate(simpleDateFormat.parse(str.split(":")[1].substring(1)));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (str.contains("長度")) {
							movie.setTime(str.split(":")[1].substring(1));
						} else if (str.contains("導演")) {
							movie.setDirector(str.split(":")[1].substring(1));
						} else if (str.contains("製作商")) {
							String[] strings = str.split(":");
							if(strings.length>1)
							movie.setCompany(strings[1]);
						} else if (str.contains("系列")) {
							String[] strings = str.split(":");
							if(strings.length>1)
							movie.setCompany(strings[1]);
						} else if (str.contains("類別")) {
							i = 1;
						} else if (i == 1) {
							i = 0;
							movie.setType(str);
						} else if (str.contains("演員")) {
							i = 2;
						} else if (i == 2) {
							i = 0;
							if (!str.contains("Terms"))
								movie.setActor(str);
						}

					}

					// 这部影片的标题部分
					Elements title = document.getElementsByTag("title");
					for (Element ctitle : title) {
						// System.out.println(ctitle.text());
						movie.setTitle(ctitle.text());
					}
					//System.out.println(movie.toString());
					
				}
				
				return movie;
			}
		});

		// callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<Movie>() {
			public void save(String url, Movie entity) {
				try {
					if (entity == null) {
						return;
					}
					PreparedStatement ps = (PreparedStatement) conn.prepareStatement(
							"INSERT into movie(title,type,rating,actor,cover,imgs,video,fh,company,series,director,time,title_fc,c_date) values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?)");

					ps.setObject(1, entity.getTitle());
					ps.setObject(2, entity.getType());
					ps.setObject(3, 0);
					ps.setObject(4, entity.getActor());
					ps.setObject(5, entity.getCover());
					ps.setObject(6, entity.getImgs());
					ps.setObject(7, entity.getVideo());
					ps.setObject(8, entity.getFh());
					ps.setObject(9, entity.getCompany());
					ps.setObject(10, entity.getSeries());
					ps.setObject(11, entity.getDirector());
					ps.setObject(12, entity.getTime());
					ps.setObject(13, "");
					ps.setObject(14, entity.getDate());

					//System.out.println(ps.toString());
					ps.execute();
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						if(conn.isClosed()){
							conn = (Connection) DriverManager.getConnection(uri, "root", "9562");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		// 把接口设置进去
		crawl.setListener(callBack);

		// 好了配置完成愉快的等爬取完成吧！
		crawl.start();

	}
}
