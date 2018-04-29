package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class task_information extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String imgUrl;
    private String publishTime;
    private String description;
    private int isLike = 0;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.infoToolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView taskImageView = (ImageView) findViewById(R.id.fruit_image_view);
        final TextView taskContentText = (TextView) findViewById(R.id.task_content_text);
       TextView taskUsernameText = (TextView) findViewById(R.id.tv_name);
        TextView taskRewardText = (TextView) findViewById(R.id.task_reward_text);
        RenWu task = (RenWu) getIntent().getSerializableExtra("task_data");
        cookie = getIntent().getStringExtra("cookie");
        final int userId = task.getUserId();
        final int taskId = task.getTaskId();
        final String hostUrl = getResources().getString(R.string.host_url);
        final FloatingActionButton request_button = (FloatingActionButton) findViewById(R.id.request_button);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        showProgressDialog();
        HttpUtil.sendOkHttpRequest("",hostUrl + "task/detail/?task_id=" + taskId + "&user_id=" + userId, 0, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                task_information.this.runOnUiThread(new Runnable() {
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
                //Log.d("detail", responseText);
                try {
                    JSONObject jsData = new JSONObject(responseText);
                    imgUrl = jsData.getString("image");
                    publishTime = jsData.getString("time");
                    description = jsData.getString("description");
                    isLike = jsData.getInt("like");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                task_information.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isLike == 1) {
                            request_button.setImageResource(R.drawable.ic_delete);
                            request_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cancel)));
                        }
                        else {
                            request_button.setImageResource(R.drawable.ic_done);
                            request_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.apply)));
                        }
                        taskContentText.setText(description);
                        closeProgressDialog();
                    }
                });
            }
        });
        collapsingToolbarLayout.setTitle("任务信息");
        Glide.with(this).load(R.drawable.taskbg).into(taskImageView);
        taskRewardText.setText(task.getPrice());
        taskUsernameText.setText(task.getName());
        ImageView taskImg = (ImageView) findViewById(R.id.task_info_img);
        Glide.with(this).load(R.drawable.ai).into(taskImg);

        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject js = new JSONObject();
                try {
                    js.put("user_id", userId);
                    js.put("task_id", taskId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showProgressDialog();
                if (isLike == 0) {
                    HttpUtil.sendOkHttpRequest("", hostUrl + "task/like/", 1, js.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            task_information.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgressDialog();
                                    Toast.makeText(getApplicationContext(), "连接服务器失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int responseCode = response.code();
                            if (responseCode == 200) {
                                task_information.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_SHORT).show();
                                        request_button.setImageResource(R.drawable.ic_delete);
                                        //request_button.setBackgroundColor(getResources().getColor(R.color.cancel));
                                        request_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cancel)));
                                        isLike = 1;
                                    }
                                });
                            } else {
                                task_information.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "申请失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

                else {
                    JSONObject jsdata = new JSONObject();
                    try {
                        jsdata.put("user_id", userId);
                        jsdata.put("task_id", taskId);
                        jsdata.put("query", "like");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Log.d("jsdata", js.toString());
                    HttpUtil.sendOkHttpRequest(cookie, hostUrl + "user/cancel/", 1, jsdata.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            task_information.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgressDialog();
                                    Toast.makeText(getApplicationContext(), "连接服务器失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            int responseCode = response.code();
                            String responseText = response.body().string();
                            Log.d("cancel", responseText);
                            if (responseCode == 200) {
                                task_information.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_SHORT).show();
                                        request_button.setImageResource(R.drawable.ic_done);
                                        request_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.apply)));
                                        isLike = 0;
                                    }
                                });
                            } else {
                                task_information.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "申请失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
