package com.example.dlen.lebbang;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dlen on 2017/12/20.
 */

public class zhuCe extends AppCompatActivity {

    private String hostUrl;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        hostUrl = getResources().getString(R.string.host_url);
        Button zhuJieMianZhuCeAnNiu = (Button) findViewById(R.id.zhuJieMianZhuCe);
        zhuJieMianZhuCeAnNiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) findViewById(R.id.zhuCeJieMianYongHuMing);
                TextView passw = (TextView) findViewById(R.id.zhuCeJieMianMiMa);
                String username = name.getText().toString();
                String password = passw.getText().toString();
                JSONObject js = new JSONObject();
                try {
                    js.put("username", username);
                    js.put("password", password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpUtil.sendOkHttpRequest("", hostUrl + "user/register/", 1, js.toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        zhuCe.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(zhuCe.this, "服务器无响应，请重试!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            zhuCe.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(zhuCe.this, "注册成功!请登陆！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(zhuCe.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            zhuCe.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(zhuCe.this, "用户已存在！请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
//                Intent intent = new Intent();
//                Bundle bb = new Bundle();
//                bb.putString("ps", passw.getText().toString());
//                bb.putString("nm", name.getText().toString());
//                intent.putExtras(bb);
//                setResult(2, intent);
//                finish();
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在注册...");
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
