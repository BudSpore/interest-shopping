package com.cum.chat.shop1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cum.chat.shop1.gradationscroll.GradationScrollView;
import com.cum.chat.shop1.gradationscroll.StatusBarUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2-1Ping on 2017/2/16.
 */

public class ClothDetailActivity extends Activity implements GradationScrollView.ScrollViewListener{
    private GradationScrollView scrollView;

    private ListView listView;
     List<String>users;

    private TextView textView;
    private int height;
    private ImageView imageview;
    String username;
    String ip;
    String goodsId;
//    ImageButton back;

    TextView oldprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.activity_clothdetail);
        Intent intent=getIntent();
        username=intent.getStringExtra("userId");
        ip=intent.getStringExtra("ip");
        goodsId=intent.getStringExtra("goodsId");
        Log.d("data",username+ip+goodsId);
        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textview);
        oldprice= (TextView) findViewById(R.id.oldprice);
        oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
        imageview = (ImageView) findViewById(R.id.iv_banner);
//        back= (ImageButton) findViewById(R.id.returnhome);
        imageview.setFocusable(true);
        imageview.setFocusableInTouchMode(true);
        imageview.requestFocus();
        String tip=goodsId.replace("coat","");
        int a= Integer.parseInt(tip);
        a=a+1;
        tip= String.valueOf(a);
        imageview.setImageBitmap(getRes("a"+tip));

        initListeners();
//        initData();
        updateMysql(ip,username,goodsId);
        getMysql(goodsId);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                updateMysql("192.168.1.103","mimi","coat0");
//            }
//        });
    }
    public Bitmap getRes(String name) {
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(name, "drawable",appInfo.packageName);
        return BitmapFactory.decodeResource(getResources(),resID);
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = imageview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = imageview.getHeight();

                scrollView.setScrollViewListener(ClothDetailActivity.this);
            }
        });
    }



    private void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClothDetailActivity.this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);
    }


    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            textView.setBackgroundColor(Color.argb((int) 0, 144,151,166));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            textView.setTextColor(Color.argb((int) alpha, 255,255,255));
            textView.setBackgroundColor(Color.argb((int) alpha, 144,151,166));
        } else {    //滑动到banner下面设置普通颜色
            textView.setBackgroundColor(Color.argb((int) 255, 144,151,166));
        }
    }
    public void updateMysql(final String userId, final String userName, final String goodsId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("1","1");
                HttpClient httpClient=new DefaultHttpClient();
                Log.d("2","2");

                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/browseCloth.php");
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
                        users=new ArrayList<String>();
                        for(int i=0;i<s.length;i+=2){
                            Log.d("item",s[i]);
                            users.add(s[i]+","+s[i+1]);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        });
                        //192.168.1.104,xiaomo1,10.88.141.81,mimi
                        //10.88.141.81,mimi192.168.1.104,xiaomo1
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
