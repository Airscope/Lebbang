package com.example.dlen.lebbang;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
//发布的

/**
 * Created by Dlen on 2017/12/30.
 */

public class renWuShiTu11Adapter extends RecyclerView.Adapter<renWuShiTu11Adapter.ViewHolder> {
    private Context mContext;
    private List<jieShouDeRenWu> mJieShouDeRenWu;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView details;
        TextView jieShouLeMa;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            details = (TextView) cardView.findViewById(R.id.task_desc_acceptTask);
            jieShouLeMa = (TextView) cardView.findViewById(R.id.jjjShouLeMa);
        }
    }

    public renWuShiTu11Adapter(List<jieShouDeRenWu> input) {
        mJieShouDeRenWu = input;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.accept_task_item, parent, false);
        final renWuShiTu11Adapter.ViewHolder holder = new renWuShiTu11Adapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                System.out.println("!!!");
                //在这里添加，这里我重载了RenWu的构造函数
                RenWu renWu = new RenWu("大玉儿");//mJieShouDeRenWu.get(position).getDetails());
                Intent intent = new Intent(mContext, RenWuJuTiXinXi.class);
                intent.putExtra("task_data", renWu);
                mContext.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        jieShouDeRenWu jsdrw = mJieShouDeRenWu.get(position);
        holder.details.setText(jsdrw.getDetails());
        String temp;
        if (jsdrw.getJieShouLeMa() == 0) temp = "已接受";
        else temp = "未接受";
        //holder.jieShouLeMa.setText(temp);
    }

    @Override
    public int getItemCount() {
        return mJieShouDeRenWu.size();
    }
}
