package com.cum.chat.shop1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by 2-1Ping on 2017/2/16.
 */

public class HomeActivity extends Activity {
    private RecyclerView mRecyclerView;
    private List<Cloth> mDatas;
    private ClothAdapter mAdapter;

    String username;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.coat_activity);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        Log.d("userId",username);

        ip=getIPAddress(getApplicationContext());
        Log.d("ip",ip);
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ClothAdapter(mDatas));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                HomeActivity.this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter.setOnItemClickLitener(new ClothAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent1=new Intent(HomeActivity.this,ClothDetailActivity.class);
                intent1.putExtra("userId",username);
                intent1.putExtra("ip",ip);
                intent1.putExtra("goodsId","coat"+position);
                startActivity(intent1);


            }

            @Override
            public void onItemLongClick(View view, int position)
            {

            }
        });
    }

    protected void initData()
    {
        mDatas = new ArrayList<Cloth>();
        Cloth cloth=new Cloth(getRes("a1"),"1");
        Cloth cloth1=new Cloth(getRes("a2"),"1");
        Cloth cloth2=new Cloth(getRes("a3"),"1");
        Cloth cloth3=new Cloth(getRes("a4"),"1");
        Cloth cloth4=new Cloth(getRes("a5"),"1");
        Cloth cloth5=new Cloth(getRes("a6"),"1");
        mDatas.add(0,cloth);
        mDatas.add(1,cloth1);
        mDatas.add(2,cloth2);
        mDatas.add(3,cloth3);
        mDatas.add(4,cloth4);
        mDatas.add(5,cloth5);

    }
    public Bitmap getRes(String name) {
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(name, "drawable",appInfo.packageName);
        return BitmapFactory.decodeResource(getResources(),resID);
    }
    private   String getIPAddress(Context context) {
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

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
   private String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


}
