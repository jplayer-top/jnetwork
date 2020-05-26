





# jnetwork

一个基于 自定义注解+ JavaPoet +Retrofit+ RxJava 的网络库，轻松几个注解释放你的双手

不管是MVP还是 MVVM。你会发现写的来越来越多，presenter的代码太多重复片段怎么办？有时候会各种new 一个对象，代码洁癖症的我发现了SpringBoot 好像只需要@AutoWrite 就可以将对象导入进来，省去了new环节，那么，Android是否可以同样搞定我定义的这些presenter类？OK。JnetWork 面世了。

基操勿6，本着学习的态度学一下代码生成器以及自定义注解。

目前注解功能

@AutoHost -自动更换请求host

@AutoMP -自动生成MVP中的Model 和Prsenter代码

@AutoWired -自动new Presenter 对象



- 使用方法

```css
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

app build.gradle 文件下

```
android{
    '''
        compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
   		 }

}

dependencies{
    implementation "com.github.jplayer-top.JNetWork:jnetwork:1.1.0"
    implementation "com.github.jplayer-top.JNetWork:autocode:1.1.0"
    annotationProcessor "com.github.jplayer-top.JNetWork:autocode:1.1.0"	
}

```

1 .

定义你的Server接口位置

```
public interface JNetServer {

    String HOST = "http://jplayer.top/";//主接口路径

    @AutoHost(key = "baidu")//你可以随意更改接口路径
    String BAI_DU_HOST = "http://www.baidu.com";

    @AutoHost(key = "google")
    String GOOGLE_HOST = "http://www.google.com";

    @AutoMP
    @Headers({Header$HOST.HEADER_BAI_DU_HOST})//改变主接口路径
    @GET("getList")
    Observable<IResponseBean> getList(@Query("test") String string);
}
```

2 .

在你的Application文件中初始化

```
NetworkApplication.with(this).retrofit(JNetServer.HOST);
```



3 .

在你需要使用的地方绑定自动导入，类似于ButterKnife的初始化

```
mBind = AutoWiredBind.bind(this);
```



4 .

使用AutoWired 注解导入你的presenter，类似JavaSpringBoot 的service导入

```
@AutoWired
public MainPresenter mPresenter;
```



5 .

如果需要，记得在你的onDestory()方法中销毁

```
@Override
protected void onDestroy() {
    super.onDestroy();
    mBind.unbind();
}
```