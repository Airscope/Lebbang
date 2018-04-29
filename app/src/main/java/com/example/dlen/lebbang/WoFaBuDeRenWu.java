package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dlen on 2017/12/23.
 */



public class WoFaBuDeRenWu extends AppCompatActivity {
    private static final String[] sqr = new String[]{
            "玉儿1",
            "玉儿2",
            "玉儿3",
            "玉儿4",
            "玉儿5"
    };
    private ListView renWuList;

    private CardView userCardView;

    private List<UserInformation> userList = new ArrayList<>();

    private ImageView mytaskImg;

    private ProgressDialog progressDialog;

    private UserAdapter userAdapter;
    private String description = "";
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytask);
        int taskId = getIntent().getIntExtra("taskId", 0);
        int userId = getIntent().getIntExtra("userId", 0);
        final String price = getIntent().getStringExtra("price");

        Toolbar toolbar = (Toolbar) findViewById(R.id.infoToolbar_mytask);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_mytask);
        ImageView taskImageView = (ImageView) findViewById(R.id.mytask_image_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("任务信息");
        Glide.with(this).load(R.drawable.taskbg).into(taskImageView);
        String hostUrl = getResources().getString(R.string.host_url);
        showProgressDialog();
        HttpUtil.sendOkHttpRequest("", hostUrl + "task/detail/?task_id=" + taskId + "&user_id=" + userId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WoFaBuDeRenWu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "连接服务器失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                try {
                    JSONObject jsData = new JSONObject(responseText);
                    String imgUrl = jsData.getString("image");
                    String publishTime = jsData.getString("time");
                    description = jsData.getString("description");
                    int isLike = jsData.getInt("like");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WoFaBuDeRenWu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        TextView mytaskPrice = (TextView) findViewById(R.id.price_mytask);
                        mytaskPrice.setText(price);
                        TextView mytaskDescription = (TextView) findViewById(R.id.mytask_content_text);
                        mytaskDescription.setText(description);
                    }
                });
            }
        });

        showProgressDialog();
        HttpUtil.sendOkHttpRequest("", hostUrl + "task/by/?task_id=" + taskId + "&query=like", 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WoFaBuDeRenWu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(), "连接服务器失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //userList.clear();
                String responseText = response.body().string();
                Log.d("user", responseText);
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int userId = jsonObject.getInt("id");
                        String username = jsonObject.getString("username");
                        String phoneNum = jsonObject.getString("phone");
                        int credit = jsonObject.getInt("credit");
                        String head_img = jsonObject.getString("head_img");
                        UserInformation user = new UserInformation(userId, username, phoneNum, credit, head_img);
                        //userList.add(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                WoFaBuDeRenWu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        //userAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        mytaskImg = (ImageView) findViewById(R.id.mytask_img);
        Glide.with(this).load(R.drawable.tofel).into(mytaskImg);
        renWuList = (ListView) findViewById(R.id.renWuShiTu22);
        userCardView = (CardView) findViewById(R.id.mytask_getUser);
        finishButton = (Button) findViewById(R.id.button_finish);
        initList();
        //ArrayAdapter<String> SQR = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sqr);
        //renWuList.setAdapter(SQR);
        userAdapter = new UserAdapter(WoFaBuDeRenWu.this, R.layout.user_item, userList, WoFaBuDeRenWu.this);
        renWuList.setAdapter(userAdapter);
//        renWuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(WoFaBuDeRenWu.this, ShenQingRen.class);
//                startActivity(intent);
//            }
//        });

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

    private void initList() {
        for (int i = 0; i < 5; i++) {
            userList.add(new UserInformation(12, "小明", "135555555", 0, ""));
        }
    }

    public void showResult() {
        renWuList.setVisibility(View.GONE);
        userCardView.setVisibility(View.VISIBLE);
        finishButton.setVisibility(View.VISIBLE);
    }
}
