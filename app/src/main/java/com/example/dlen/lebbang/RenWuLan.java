package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.oragee.banners.BannerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.id.content;

/**
 * Created by Dlen on 2017/12/20.
 */

public class RenWuLan extends AppCompatActivity {
    private SwipeRefreshLayout swRenWu;
    private int userId;
    private String username;
    private String password;
    private String phoneNum;
    private String sessionId;
    private int credit;
    private static boolean isExit = false;
    private String headImg;
    private String hostUrl;
    private RecyclerView recView;
    private int lastTaskId;
    private DrawerLayout mDrawerLayout;
    private ProgressDialog progressDialog;
    private LinearLayoutManager mLinearLayoutManager;
    private List<RenWu> renWuLists = new ArrayList<RenWu>();
    private renWuAdapt adapter;
    private int[] imgs = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4};
    private List<View> viewList;
    BannerView bannerView;


    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            isExit = false;
            return false;
        }
    });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.renWuJieMianWoDeXinXi:
                final Bundle bbb = new Bundle();
                bbb.putString("name", username);
                Intent intent1;
                intent1 = new Intent(RenWuLan.this, WoDeXinXi.class);
                intent1.putExtras(bbb);
                startActivity(intent1);
                break;
            case R.id.renWuJieMianSheZhi:
                Intent intent2;
                intent2 = new Intent(RenWuLan.this, rwSheZhi.class);
                startActivity(intent2);
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renwulan);
        hostUrl = getResources().getString(R.string.host_url);
        userId = getIntent().getIntExtra("user_id", 0);
        username = getIntent().getStringExtra("username");
        sessionId = getIntent().getStringExtra("session");
        lastTaskId = 0;
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(sessionId, hostUrl+"task/summary/?id=0" , 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "加载任务信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                //Log.d("responseText", responseText);
                renWuLists.clear();
                boolean isFisrt = true;
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int taskId = jsonObject.getInt("id");
                        if (isFisrt) {
                            lastTaskId = taskId;
                            isFisrt = false;
                        }
                        String userName = jsonObject.getString("publisher_name");
                        String tag = jsonObject.getString("tag");
                        String price = jsonObject.getString("price");
                        String task_title = jsonObject.getString("task_title");
                        String imgSrc = jsonObject.getString("head_img");
                        RenWu tempRenWu = new RenWu(taskId, userName, task_title, imgSrc, price, tag, userId);
                        renWuLists.add(tempRenWu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });

        HttpUtil.sendOkHttpRequest("", hostUrl + "user/update/?username=" + username, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "加载用户信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d("responseText", responseText);
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        phoneNum = jsonObject.getString("phone");
                        credit = jsonObject.getInt("credit");
                        headImg = jsonObject.getString("head_img");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        Random rand = new Random();
        //Bundle bundle = this.getIntent().getExtras();
//        password = bundle.getString("password");
//        name = bundle.getString("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.renwulan_drawer_layout);

        NavigationView nav = (NavigationView) findViewById(R.id.nv_view);
        View header = nav.inflateHeaderView(R.layout.nav_header);
        TextView nana = (TextView) header.findViewById(R.id.xxxxMing1);
        TextView dada = (TextView) header.findViewById(R.id.ddddHuaHaoMa1);
        TextView xixi = (TextView) header.findViewById(R.id.xxxYongFen1);
        final CircleImageView userImg = (CircleImageView) header.findViewById(R.id.userImg);
        userImg.setImageResource(R.mipmap.ic_avatar_tangqi);
//        if (headImg != null) {
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    //从网络上获取图片
//                    final Bitmap bitmap = getPicture(headImg);
//                    //发送一个Runnable对象
//                    userImg.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            userImg.setImageBitmap(bitmap);//在ImageView中显示从网络上获取到的图片
//                        }
//
//                    });
//
//                }
//            }).start();//开启线程
//        }
//        else {
//            userImg.setImageResource(R.mipmap.ic_avatar_tangqi);
//        }
        xixi.setText("信用分  " + String.valueOf(credit));
        nana.setText("姓名  " + username);
        dada.setText("电话号码  " + "18032338486");

        FloatingActionButton actionRefresh = (FloatingActionButton) findViewById(R.id.action_refresh);
        actionRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swRenWu.measure(0,0);
                swRenWu.setRefreshing(true);
                refreshRenWu();
            }
        });

        FloatingActionButton actionTop = (FloatingActionButton) findViewById(R.id.action_top);
        actionTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recView.scrollToPosition(0);
            }
        });

        FloatingActionButton actionPublish = (FloatingActionButton) findViewById(R.id.action_publish);
        actionPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenWuLan.this, FaBuXinDeRenWu.class );
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        //nav.setCheckedItem(R.id.wwJieShouDeRenWu1);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.wwJieShouDeRenWu1:
                        Intent intent = new Intent(RenWuLan.this, JieShouDeRenWuMen.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("cookie", sessionId);
                        startActivity(intent);
                        break;
                    case R.id.wwFaBuDeRenWu1:
                        Intent intent2 = new Intent(RenWuLan.this, FaBuDeRenWuMen.class);
                        intent2.putExtra("userId", userId);
                        intent2.putExtra("cookie", sessionId);
                        startActivity(intent2);
                        break;
                    case R.id.xxxGaiWoDeXinXi1:
                        Intent intent3 = new Intent(RenWuLan.this, SheZhiWoDeXinXi.class);
                        startActivity(intent3);
                        break;
                    case R.id.donate:
                        Intent intent4 = new Intent(RenWuLan.this, DonateActivity.class);
                        startActivity(intent4);
                    default:
                }
                return true;
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button fbxx = (Button) findViewById(R.id.faBuRenWu);
        fbxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenWuLan.this, FaBuXinDeRenWu.class);
                startActivity(intent);
            }
        });
        //
        swRenWu = (SwipeRefreshLayout) findViewById(R.id.renWuReFresh);
        swRenWu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRenWu();
            }
        });
        //
        //initRenWu();
        recView = (RecyclerView) findViewById(R.id.gaoJiRenWuLan);
        adapter = new renWuAdapt(renWuLists, RenWuLan.this);
        recView.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recView.setLayoutManager(mLinearLayoutManager);
        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.banner, null));
        recView.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
                this.loading = false;
            }
        });
    }
    private void refreshRenWu() {
        HttpUtil.sendOkHttpRequest(sessionId, hostUrl+"task/summary/?id=0" , 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //closeProgressDialog();
                        swRenWu.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                //Log.d("responseText", responseText);
                renWuLists.clear();
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int taskId = jsonObject.getInt("id");
                        String userName = jsonObject.getString("publisher_name");
                        String tag = jsonObject.getString("tag");
                        String price = jsonObject.getString("price");
                        String task_title = jsonObject.getString("task_title");
                        String imgSrc = jsonObject.getString("head_img");
                        RenWu tempRenWu = new RenWu(taskId, userName, task_title, imgSrc, price, tag, userId);
                        renWuLists.add(tempRenWu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swRenWu.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

//    public void onClick(View v) {
//        if (v.getId() == R.id.top) {
//            recView.scrollToPosition(0);
//        }
//        else if (v.getId() == R.id.refresh) {
//            swRenWu.measure(0,0);
//            swRenWu.setRefreshing(true);
//        }
//        else if (v.getId() == R.id.plus) {
//            Intent intent = new Intent(RenWuLan.this, FaBuXinDeRenWu.class );
//            startActivity(intent);
//        }
//    }

    public void loadMoreData() {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(sessionId, hostUrl+"task/summary/?id="+lastTaskId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("more", responseBody);
                boolean isFirst = true;
                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int taskId = jsonObject.getInt("id");
                        if (isFirst) {
                            lastTaskId = taskId;
                            isFirst = false;
                        }
                        String userName = jsonObject.getString("publisher_name");
                        String tag = jsonObject.getString("tag");
                        String price = jsonObject.getString("price");
                        String task_title = jsonObject.getString("task_title");
                        String imgSrc = jsonObject.getString("head_img");
                        RenWu tempRenWu = new RenWu(taskId, userName, task_title, imgSrc, price, tag, userId);
                        renWuLists.add(tempRenWu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //renWuLists.add(new RenWu(11, "更多", "qwe", "rewqr", "xcvz","sdfsd", 2));
                }
                RenWuLan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            //finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(0);
        }
    }

    public Bitmap getPicture(String path) {
        Bitmap bm = null;
        URL url;
        try {
            url = new URL(path);//创建URL对象
            URLConnection conn = url.openConnection();//获取URL对象对应的连接
            conn.connect();//打开连接
            InputStream is = conn.getInputStream();//获取输入流对象
            bm = BitmapFactory.decodeStream(is);//根据输入流对象创建Bitmap对象
        } catch (MalformedURLException e1) {
            e1.printStackTrace();//输出异常信息
        } catch (IOException e) {
            e.printStackTrace();//输出异常信息
        }
        return bm;
    }

    public int getUserId() {
        return userId;
    }
    public String getSessionId() {
        return sessionId;
    }
}
