package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dlen on 2017/12/21.
 */

public class RenWuJuTiXinXi extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renwujutixinxi);
        RenWu task = (RenWu) getIntent().getSerializableExtra("task_data");
        int userId = task.getUserId();
        int taskId = task.getTaskId();
        String hostUrl = getResources().getString(R.string.host_url);
        Log.d("jutixinxi", "jutixinxi");
        img = (ImageView) findViewById(R.id.rrrWuTuPian);
        Glide.with(this).load(R.drawable.blue).into(img);
        showProgressDialog();
        HttpUtil.sendOkHttpRequest("",hostUrl + "task/detail/task_id=" + taskId + "&user_id=" + userId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RenWuJuTiXinXi.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d("detail", responseText);
                try {
                    JSONObject jsData = new JSONObject(responseText);
                    String imgUrl = jsData.getString("img");
                    String publishTime = jsData.getString("time");
                    String description = jsData.getString("description");
                    int isLike = jsData.getInt("like");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RenWuJuTiXinXi.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                    }
                });
            }
        });
        //903226403
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
};
