package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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

public class FaBuDeRenWuMen extends AppCompatActivity {
    private RecyclerView faBuDeRenWuMne;
    private faBuDeRenWu[] renwus = {new faBuDeRenWu(133, "打玉儿", 0), new faBuDeRenWu(32, "打玉2儿", 2), new faBuDeRenWu(33, "打玉3儿", 1), new faBuDeRenWu(34, "打玉4儿", 0)};
    private renWuShiTuAdapter adapter;
    private List<faBuDeRenWu> renWusList = new ArrayList<>();
    private ProgressDialog progressDialog;


    private void initRenWus() {
        renWusList.clear();
        for (int ctr = 0; ctr < 20; ctr++) {
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
        String hostUrl = getResources().getString(R.string.host_url);
        HttpUtil.sendOkHttpRequest(cookie, hostUrl + "user/mypublish/?user_id=" + userId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FaBuDeRenWuMen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "不能连接服务器！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d("response", responseText);
                renWusList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int taskId = jsonObject.getInt("id");
                        String publisher = jsonObject.getString("publisher_name");
                        String tag = jsonObject.getString("tag");
                        String price = jsonObject.getString("price");
                        String task_title = jsonObject.getString("task_title");
                        String head_img = jsonObject.getString("head_img");
                        faBuDeRenWu task = new faBuDeRenWu(taskId, task_title, 0, taskId, userId, price);
                        renWusList.add(task);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                Log.d("Length", String.valueOf(renWusList.size()) );
                FaBuDeRenWuMen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        setContentView(R.layout.faburenwushitu);
        //initRenWus();
        faBuDeRenWuMne = (RecyclerView) findViewById(R.id.ffBuRenWuShiTu);
        adapter = new renWuShiTuAdapter(renWusList);
        faBuDeRenWuMne.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        faBuDeRenWuMne.setLayoutManager(layoutManager);


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
