package com.cum.chat.shop1.uitest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cum.chat.shop1.IJavaCallback2JS;
import com.cum.chat.shop1.R;
import com.cum.chat.shop1.SimpleJavaJsBridge;
import com.cum.chat.shop1.annotation.JavaCallback4JS;
import com.cum.chat.shop1.annotation.Param;
import com.cum.chat.shop1.annotation.ParamResponseStatus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by 2-1Ping on 2017/2/17.
 */

public class GoodsMain extends Activity {
    WebView mWebView;
    private SimpleJavaJsBridge mSimpleJavaJsBridge;
    String username;
    String ip;
    String goodsId;
    List<String>name=new ArrayList<String>();
    Timer timer = new Timer();
    String names="";
    String postname="";
    int num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsmain);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        Log.d("userName",username);
        ip=getIPAddress(getApplicationContext());
        Log.d("userId",ip);

        mWebView= (WebView) findViewById(R.id.webview1);
        initWebView();
        initData();
//        mWebView.loadUrl("http://123.207.32.211:8099/shop/goodsMain.html");//搜索页
        mWebView.loadUrl("http://123.207.32.211:8099/shop/index.html");//主页

    }
    //    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    updatetohtml(names);
//                }
//            });
//
//        }
//    };
    public void getGoodsId(String goodsId){
        this.goodsId=goodsId;
        Log.d("传过来的goodsId",goodsId);
        updateMysql(ip,username,goodsId);
//        getMysql(goodsId);

    }
    public void initWebView(){
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return super.shouldOverrideUrlLoading(view, url);

            }


            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("error",description);
                Log.d("error",failingUrl);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                if(num==1)
                {
                    updatetohtml(postname);
                    Log.d("updatetohtml","我把用户名发过去啦");
                }


            }

        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setBuiltInZoomControls(false);
        settings.setBlockNetworkImage(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setDatabaseEnabled(true);
//        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

        settings.setPluginState(WebSettings.PluginState.ON);

        // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setGeolocationDatabasePath(dir);

            /* 解决空白页问题 */
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebView.loadUrl("http://123.207.32.211:8099/CUM/login.html");
        settings.setAllowFileAccess(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings("all")

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {

                super.onReceivedIcon(view, icon);

            }


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }


        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
//                num++;
                num=0;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void initData() {
        JavaInterfaces4JS javaInterfaces4JS = new JavaInterfaces4JS(this);
        mSimpleJavaJsBridge = new SimpleJavaJsBridge.Builder().addJavaInterface4JS(javaInterfaces4JS)
                .setWebView(mWebView)
                .setJSMethodName4Java("_JSNativeBridge._handleMessageFromNative")
                .setProtocol("niu","receive_msg").create();
    }
    public void setResult(String result){
        if(result != null){
            Toast.makeText(GoodsMain.this,result,Toast.LENGTH_LONG).show();
        }
    }

    private String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }
    private String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    public void updateMysql(final String userId, final String userName, final String goodsId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("1","1");
                HttpClient httpClient=new DefaultHttpClient();
                Log.d("2","2");

                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/php/browseCloth.php");
                Log.d("3","3");

                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                Log.d("4","4");

                List<NameValuePair> params=new ArrayList<NameValuePair>();
                Log.d("5","5");
                Log.d("data",userName);

                params.add(new BasicNameValuePair("userId",userId));
                Log.d("6","6");

                params.add(new BasicNameValuePair("userName",userName));//真坑，以后前后两个字符串都写一样的吧！
                Log.d("7","7");

                params.add(new BasicNameValuePair("goodsId",goodsId));
                Log.d("8","8");
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    Log.d("9","9");

                    httpPost.setEntity(entity);
                    Log.d("10","10");

                    httpClient.execute(httpPost);
                    Log.d("11","11");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("12","12");
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("13","13");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("14","14");
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    Log.d("91","91");
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        Log.d("92","92");
                        final String response= EntityUtils.toString(entity,"utf-8");
                        Log.d("返回值",response);

                    }
                    getMysql(goodsId);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void getMysql(final String goodsId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("1","1");
                HttpClient httpClient=new DefaultHttpClient();
                Log.d("2","2");

                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/php/AcBroUser.php");
                Log.d("3","3");

                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                Log.d("4","4");

                List<NameValuePair> params=new ArrayList<NameValuePair>();
                Log.d("5","5");
                params.add(new BasicNameValuePair("goodsId",goodsId));
                Log.d("6","6");
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    Log.d("7","7");

                    httpPost.setEntity(entity);
                    Log.d("8","8");

                    httpClient.execute(httpPost);
                    Log.d("9","9");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("10","10");
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("11","11");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("12","12");
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    Log.d("91","91");
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        Log.d("92","92");
                        final String response= EntityUtils.toString(entity,"utf-8");
                        Log.d("浏览该商品的有",response);
                        String[] ary = response.split(",");
                        String[] s=response.split("[,]");
                        for (int i=0;i<s.length;i+=2)
                        {
                            names+=s[i+1]+",";
                            name.add(names);
                        }
                        names=names.substring(0,names.length()-1);
                        Log.d("用户名在这里呢", names);
                        num=1;
                        postname=names;
                        names="";
//                        num++;
//                        timer.schedule(task,6000);


//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
                        //192.168.1.104,xiaomo1,10.88.141.81,mimi
                        //10.88.141.81,mimi192.168.1.104,xiaomo1
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void updatetohtml(final String username){
        Log.d("mooooooooo",username);
        IInvokeJS invokeJS = mSimpleJavaJsBridge.createInvokJSCommand(IInvokeJS.class);
        invokeJS.exam(username,new IJavaCallback2JS() {
            @JavaCallback4JS
            public void test(@ParamResponseStatus("msg")String statusMsg, @Param("msg") String msg) {
                Toast.makeText(GoodsMain.this," 状态信息="+statusMsg+"  msg="+msg,Toast.LENGTH_LONG).show();
            }

        });






//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d("a1","1");
//                HttpClient httpClient=new DefaultHttpClient();
//                Log.d("a2","2");
//
//                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/js/goodsMain.js");
//                Log.d("a3","3");
//
//                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
//                Log.d("a4","4");
//
//                List<NameValuePair> params=new ArrayList<NameValuePair>();
//                Log.d("a5","5");
//                    params.add(new BasicNameValuePair("username",username));
//
//                Log.d("a6","6");
//                try {
//                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
//                    Log.d("a7","7");
//
//                    httpPost.setEntity(entity);
//                    Log.d("a8","8");
//
//                    httpClient.execute(httpPost);
//                    Log.d("a9","9");
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    Log.d("a10","10");
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                    Log.d("a11","11");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("a12","12");
//                }
//                try {
//                    HttpResponse httpResponse=httpClient.execute(httpPost);
//                    Log.d("a91","91");
//                    if(httpResponse.getStatusLine().getStatusCode()==200){
//                        HttpEntity entity=httpResponse.getEntity();
//                        Log.d("a92","92");
//                        final String response= EntityUtils.toString(entity,"utf-8");
//                        Log.d("请求到网页上了，看看返回值吧",response);
//
//
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}
