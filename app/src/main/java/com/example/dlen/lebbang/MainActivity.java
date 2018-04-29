package com.example.dlen.lebbang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.button;
import static android.R.attr.defaultHeight;
import static android.R.attr.host;
import static com.example.dlen.lebbang.HttpUtil.sendOkHttpRequest;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> Names = null;
    private ArrayList<String> Passwords = null;
    private ProgressDialog progressDialog;
    private String hostUrl;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.zhuJieMianBangZhu:
                Intent intent1;
                intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent1);
                break;
            case R.id.zhuJieMianLianXiWoMen:
                Intent intent2;
                intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:18033302080"));
                startActivity(intent2);
                break;
            case R.id.zhuJieMianGuanYuLeBang:
                Intent intent3;
                intent3 = new Intent(MainActivity.this, GuanYuLeBang.class);
                startActivity(intent3);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 2){
            String content1 = data.getStringExtra("ps");
            String content2 = data.getStringExtra("nm");
            Passwords.add(content1);
            Names.add(content2);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hostUrl = getResources().getString(R.string.host_url);
        Names = new ArrayList<String>();
        Passwords = new ArrayList<String>();
        Names.add("1");
        Passwords.add("1");
        Button zhuJieMianZhuCeAnNiu = (Button) findViewById(R.id.zhuJieMianZhuCe);
        Button zhuJieMianDengLuAnNiu = (Button) findViewById(R.id.zhuJieMianDengLu);
        final Bundle bundle=new Bundle();
        zhuJieMianZhuCeAnNiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, zhuCe.class);
                startActivityForResult(intent, 2);
            }
        });
        zhuJieMianDengLuAnNiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RenWuLan.class);
                TextView name = (TextView) findViewById(R.id.zhuJieMianYongHuMing);
                TextView passw = (TextView) findViewById(R.id.zhuJieMianMiMa);
                if (Names.isEmpty()) return  ;
//                else if (Names.contains(name.getText().toString()) == false ||  Names.contains(name.getText().toString()) && Integer.valueOf(Passwords.get(Names.indexOf(name.getText().toString()))) != Integer.valueOf(passw.getText().toString())) {
//                    Toast.makeText(MainActivity.this, "用户不存在或密码错误", Toast.LENGTH_LONG).show();
//                    System.out.println(Names.get(0));//1
//                    System.out.println(Passwords.get(0));//2
//                    System.out.println(Passwords.get(Names.indexOf(name.getText().toString())));//2
//                    System.out.println(Names.indexOf(name.getText().toString()));//0
//                    System.out.println(passw.getText().toString());//2
//                }
                else{
                    String curName = name.getText().toString();
                    String curPwd = passw.getText().toString();
                    JSONObject js = new JSONObject();
                    try {
                        js.put("username", curName);
                        js.put("password", curPwd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //startActivity(intent);
                    showProgressDialog();
                    OkHttpClient client = new OkHttpClient();
                    //final Request request = null;
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, js.toString());
                    final Request request = new Request.Builder()
                            .url(hostUrl + "user/login/")
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue( new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            closeProgressDialog();
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "不能连接到服务器！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseText = response.body().string();
                            int responseCode = response.code();
                            Headers headers = response.headers();
                            List<Cookie> cookies = Cookie.parseAll(request.url(), headers);
                            StringBuilder session = new StringBuilder();
                            for(Cookie cookie : cookies){
                                session.append(cookie.name()).append("=").append(cookie.value()+";");
                            }
                            //String s = session.substring(0, session.indexOf(";"));
                            //Log.d("cookies", session.toString());
                            //Log.d("responseCode", String.valueOf(responseCode));
                            int userId = 0;
                            String username = "";
                            if (responseCode == 200) {
                                try {
                                    JSONObject jsData = new JSONObject(responseText);
                                    userId = jsData.getInt("id");
                                    username = jsData.getString("username");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                closeProgressDialog();
                                Intent intent = new Intent(MainActivity.this, RenWuLan.class);
                                intent.putExtra("user_id", userId);
                                intent.putExtra("username", username);
                                intent.putExtra("session", session.toString());
                                startActivity(intent);
                            }
                            else {
                                closeProgressDialog();
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在登陆...");
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