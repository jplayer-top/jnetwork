



# jnetwork

一个基于自定义注解+ JavaPoet +Retrofit+ RxJava 的网络库，轻松几个注解释放你的双手

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