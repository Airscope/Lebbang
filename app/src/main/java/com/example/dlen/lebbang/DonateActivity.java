package com.example.dlen.lebbang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        ImageView imageView = (ImageView) findViewById(R.id.donate_img);
        Glide.with(this).load(R.drawable.donate).into(imageView);

    }
}
