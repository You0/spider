轻量级的爬虫框架

给定一些起始的URL，和爬取URL的正则表达。则进行爬取，从这些站点入手开始爬取
BFS广度搜索。
预留出爬虫的解析和保存的接口，自己自定义。
Redis保存爬取进度和已经爬取过得链接,需要安装redis，IP为127.0.0.1，端口为默认值。直接安装然后运行即可使用。



//使用案例
		
		Crawl crawl = SpiderUtil.Steup();
		
		//这里传入正则，比如我要只想要爬取煎蛋网则可以这样写,只有当url满足该规则时才进行爬取，否则跳过：
		crawl.setMatchRegex("http://jandan.net/.*");
		
		//这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[]{"http://jandan.net/2017/06/30/forgetful-brain.html"});
		
		//这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<bean> callBack = SpiderUtil.getMapperCallBack();
		//callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<bean>() {
			public bean parse(String body) {
				return null;
			}
		});
		
		//callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<bean>() {
			public void save(bean entity) {
				
			}
		});
		//把接口设置进去
		crawl.setListener(callBack);
		
		
		//好了配置完成愉快的等爬取完成吧！
		crawl.start();
