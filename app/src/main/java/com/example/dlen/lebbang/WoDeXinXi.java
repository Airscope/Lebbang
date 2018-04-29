package com.example.dlen.lebbang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Dlen on 2017/12/20.
 */

public class WoDeXinXi extends AppCompatActivity {
    private TextView xyf;
    private TextView dhhm;
    private TextView xm;
    private String namee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wodexinxi);
        Bundle bundle = this.getIntent().getExtras();
        namee = bundle.getString("name");
        System.out.println(namee);
        //生成一个随机分数
        int XinYongFen = rand.nextInt(100);
        xyf = (TextView) findViewById(R.id.xxxYongFen);
        xyf.setText("信用分：" + String.valueOf(XinYongFen));
        //给一个名字
        xm = (TextView) findViewById(R.id.xxxxMing);
        xm.setText(namee);
        //生成一个随机电话号码
        String tempStr = "";
        for (int ctr = 0; ctr < 10; ctr++)
            tempStr += String.valueOf(rand.nextInt(10));
        dhhm = (TextView) findViewById(R.id.ddddHuaHaoMa);
        dhhm.setText("电话号码： 1" + tempStr);
        Button xiuGaiXinXI = (Button) findViewById(R.id.xxxGaiWoDeXinXi);
        xiuGaiXinXI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WoDeXinXi.this, SheZhiWoDeXinXi.class);
                startActivity(intent);
            }
        });
        Button woLingQuDeRenWu = (Button) findViewById(R.id.wwJieShouDeRenWu);
        woLingQuDeRenWu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WoDeXinXi.this, RenWuJuTiXinXi.class);
                startActivity(intent);
            }
        });
        Button woFaBuDeRenWu = (Button) findViewById(R.id.wwFaBuDeRenWu);
        woFaBuDeRenWu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WoDeXinXi.this, WoFaBuDeRenWu.class);
                startActivity(intent);
            }
        });
    }
}
