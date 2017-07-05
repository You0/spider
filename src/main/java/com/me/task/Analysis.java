package com.me.task;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.LinkRef;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.me.utils.Log;

@Component
public class Analysis {
	public static String MainDomin;

	// 解析html中的所有url地址
	public List<String> GetUrls(String html) {

		LinkedList<String> linkedList = new LinkedList<String>();
		// 使用Jsoup解析文档中的全部URL
		Document doc = Jsoup.parse(html);
		Elements links = doc.getElementsByAttribute("href");
		for (Element link : links) {
			String linkHref = link.attr("href");
			// Log.D(linkHref);
			try {
				if ((linkHref.charAt(0)) == '/') {
					linkHref = MainDomin + linkHref;
				}

				// 图片文件则不放入
				if (linkHref.charAt(0) != 'h' || linkHref.length() < 3 || linkHref.endsWith(".jpg")
						|| linkHref.endsWith(".png") || linkHref.endsWith(".jpeg")) {
				} else {
					if (linkHref.indexOf(MainDomin) != -1)
						linkedList.add(linkHref);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		// for(String s:linkedList){
		// Log.D(s);
		// }
		return linkedList;
	}

	// 解析HTML中的image URL
	public List<String> ImageDate(String html) {
		LinkedList<String> linkedList = new LinkedList<String>();
		String regx = "http://.*?\\.(jpg|jpeg|png)";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			linkedList.add(matcher.group());
		}
		return linkedList;
	}
}
