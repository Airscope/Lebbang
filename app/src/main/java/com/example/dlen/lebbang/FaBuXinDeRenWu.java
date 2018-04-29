package com.example.dlen.lebbang;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class FaBuXinDeRenWu extends AppCompatActivity {
    private static final int IMG_COUNT = 8;
    private static final String IMG_ADD_TAG = "a";
    private final String hostUrl = getResources().getString(R.string.host_url);

    private GridView gridView;
    private GVAdapter adapter;
    private TextView releaseTextView;
    private Button setDateButton;
    private Button setTimeButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText tagEditText;
    private ImageView img;
    private List<String> list;

    private String title;
    private String description;
    private String price;
    private String tag;

    private int ddlyear;
    private int ddlday;
    private int ddlmonth;
    private int ddlhour;
    private int ddlminute;

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("username");

        setContentView(R.layout.fabuxinderenwu);

        gridView = (GridView) findViewById(R.id.gridview);
        releaseTextView = (TextView) findViewById(R.id.release);
        setDateButton = (Button) findViewById(R.id.set_date);
        setTimeButton = (Button) findViewById(R.id.set_time);
        titleEditText = (EditText) findViewById(R.id.task_title);
        priceEditText = (EditText) findViewById(R.id.price);
        descriptionEditText = (EditText) findViewById(R.id.description);
        tagEditText = (EditText) findViewById(R.id.tag);

        // 发布按钮响应函数
        releaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // 检测输入
                checkUserInput();

                // 获取当前
                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                price = priceEditText.getText().toString();
                tag = tagEditText.getText().toString();

                // 上传数据
                uploadData();
            }
        });

        // 设置日期按钮
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                DatePickerDialog datePicker = new DatePickerDialog(FaBuXinDeRenWu.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ddlyear = year;
                        ddlmonth = month;
                        ddlday = dayOfMonth;
                        setDateButton.setText(Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(dayOfMonth));
                    }
                }, 2018, 1, 1);
                datePicker.show();
            }
        });

        // 设置时间按钮
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                TimePickerDialog timePicker = new TimePickerDialog(FaBuXinDeRenWu.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ddlhour = hourOfDay;
                        ddlminute = minute;
                        setTimeButton.setText(Integer.toString(hourOfDay)+":"+Integer.toString(minute));
                    }
                }, 0, 0, true);
                timePicker.show();
            }
        });
        initData();
    }

    // TODO 上传数据
    private void uploadData() {

        Bitmap bitmap;
        Bitmap bmpCompressed;
        for (int i = 0; i < list.size() - 1; i++) {
            bitmap = BitmapFactory.decodeFile(list.get(i));
            bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            System.out.println(data);
        }

        JSONObject js = new JSONObject();
        try {
            js.put("publisher_name", username);
            js.put("task_title", title);
            js.put("description", description);
            js.put("tag", tag);
            js.put("price", price);
            js.put("image", JSONObject.NULL); // TODO

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Send", js.toString());
        HttpUtil.sendOkHttpRequest("", hostUrl+"task/publish/", 1, js.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FaBuXinDeRenWu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "连接服务器失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                String responseText = response.body().string();
                Log.d("publishResCode", responseText );
                if (responseCode == 200) {
                    FaBuXinDeRenWu.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else {
                    FaBuXinDeRenWu.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "发布失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        if (list == null) {
            list = new ArrayList<>();
            list.add(IMG_ADD_TAG);
        }
        adapter = new GVAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals(IMG_ADD_TAG)) {
                    if (list.size() < IMG_COUNT) {
                        if (ContextCompat.checkSelfPermission(FaBuXinDeRenWu.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FaBuXinDeRenWu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                        else {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 0);
                        }
                    } else
                        Toast.makeText(FaBuXinDeRenWu.this, "最多只能选择7张照片！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshAdapter();
    }

    // 检测用户输入
    private void checkUserInput() {
        // TODO
    }

    private void refreshAdapter() {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new GVAdapter();
        }
        adapter.notifyDataSetChanged();
    }

    private class GVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplication()).inflate(R.layout.activity_add_photo_gv_items, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.main_gridView_item_cb);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            String s = list.get(position);
            if (!s.equals(IMG_ADD_TAG)) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.imageView.setImageBitmap(ImageTool.createImageThumbnail(s));
            } else {
                holder.checkBox.setVisibility(View.GONE);
                holder.imageView.setImageResource(R.mipmap.ic_add_pic);
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    refreshAdapter();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }

    }

    // 响应子activity的返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            System.out.println("data null");
            return;
        }
        if (requestCode == 0) {
            final Uri uri = data.getData();
            String path = ImageTool.getImageAbsolutePath(this, uri);
            System.out.println(path);
            if (list.size() == IMG_COUNT) {
                removeItem();
                refreshAdapter();
                return;
            }
            removeItem();
            list.add(path);
            list.add(IMG_ADD_TAG);
            refreshAdapter();
        }
    }

    private void removeItem() {
        if (list.size() != IMG_COUNT) {
            if (list.size() != 0) {
                list.remove(list.size() - 1);
            }
        }
    }

}

