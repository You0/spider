轻量级的爬虫框架

给定一些起始的URL，和爬取URL的正则表达。则进行爬取，从这些站点入手开始爬取，不断的发散，当域名指定为一个站点时，即可以对单个站点进行全站爬取
BFS广度搜索。
预留出爬虫的解析和保存的接口，自己自定义。
Redis保存爬取进度和已经爬取过得链接,需要安装redis，IP为127.0.0.1，端口为默认值。直接安装然后运行即可使用。
多线程与访问网络方面直接交由OKHTTP处理，省时省力，不过自己同时也在仿写okhttp，以后说不定会替换成自己写的TinyHttp。


打成了jar包，方便直接使用，路径：target/spider-0.0.1-SNAPSHOT.jar
可直接下载这个jar进行使用

//使用案例
```javascript

		Crawl crawl = SpiderUtil.Steup();
		
		//设置代理，必须在start之前
		crawl.setProxyPort(1080);
		crawl.setProxyUrl("127.0.0.1");
		// 白名单，只有url满足传入的正则表达式才会进行爬取：
		crawl.setMatchRegex("xxxx");
		// 黑名单,黑名单支持传入多个值
		crawl.setBlackReg(new String[] {"xxxx"});

		// 这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[] { "xxx" });

		// 这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<bean> callBack = SpiderUtil.getMapperCallBack();
		// callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<bean>() {
			public bean parse(String url, String body) {
				return null;
			}
		});

		// callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<bean>() {
			public void save(String url, bean entity) {
			}
		});
		// 把接口设置进去
		crawl.setListener(callBack);

		// 好了配置完成愉快的等爬取完成吧！
		crawl.start();

```javascript
