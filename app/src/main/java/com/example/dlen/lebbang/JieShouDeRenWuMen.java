package com.example.dlen.lebbang;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dlen on 2017/12/30.
 */

public class JieShouDeRenWuMen extends AppCompatActivity {
    private RecyclerView jieShouDeRenWuMne;
    private jieShouDeRenWu[] renwus = {new jieShouDeRenWu(1, "电脑蓝屏，垃圾win10，求解决")};
    private renWuShiTu11Adapter adapter;
    private List<jieShouDeRenWu> renWusList = new ArrayList<>();

    private String hostUrl = getResources().getString(R.string.host_url);
    private ProgressDialog progressDialog;


    private void initRenWus() {
        renWusList.clear();
        for (int ctr = 0; ctr < 1; ctr++) {
            Random random = new Random();
            int index = random.nextInt(renwus.length);
            renWusList.add(renwus[index]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressDialog();
        final int userId = getIntent().getIntExtra("userId", 0);
        String cookie = getIntent().getStringExtra("cookie");
        HttpUtil.sendOkHttpRequest(cookie, hostUrl + "user/like/?user_id=" + userId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                JieShouDeRenWuMen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d("jieshouResponse", responseText);
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int task_id = jsonObject.getInt("id");
                        String publisher = jsonObject.getString("publisher_name");
                        String tag = jsonObject.getString("tag");
                        String price = jsonObject.getString("price");
                        String task_title = jsonObject.getString("task_title");
                        String head_img = jsonObject.getString("head_img");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JieShouDeRenWuMen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                    }
                });
            }
        });
        setContentView(R.layout.faburenwushitu);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.accept_task_Toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        initRenWus();
        jieShouDeRenWuMne = (RecyclerView) findViewById(R.id.ffBuRenWuShiTu);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        jieShouDeRenWuMne.setLayoutManager(layoutManager);
        adapter = new renWuShiTu11Adapter(renWusList);
        jieShouDeRenWuMne.setAdapter(adapter);
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

}
