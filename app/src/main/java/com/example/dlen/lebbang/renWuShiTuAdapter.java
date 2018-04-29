package com.example.dlen.lebbang;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
//发布的

/**
 * Created by Dlen on 2017/12/30.
 */

public class renWuShiTuAdapter extends RecyclerView.Adapter<renWuShiTuAdapter.ViewHolder> {
    private Context mContext;
    private List<faBuDeRenWu> mFaBuDeRenWu;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView details;
        TextView people;
        TextView state;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            details = (TextView) cardView.findViewById(R.id.ffBuDeRenWuJuTiXinXi);
            //people = (TextView) cardView.findViewById(R.id.ffBuDeRRenWuJieShouRenShu);
            //state = (TextView) cardView.findViewById(R.id.ffBuDeRRenWuZhuangTai);
        }
    }

    public renWuShiTuAdapter(List<faBuDeRenWu> input) {
        mFaBuDeRenWu = input;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.renwushitu_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //在这里添加，这里我重载了RenWu的构造函数
                //RenWu renWu = new RenWu(mFaBuDeRenWu.get(position).getDetails());
                //Log.d("Length2", String.valueOf(mFaBuDeRenWu.size()) );
                Intent intent = new Intent(mContext, WoFaBuDeRenWu.class);
                int taskId = mFaBuDeRenWu.get(position).getTaskId();
                int userId = mFaBuDeRenWu.get(position).getUserId();
                String price = mFaBuDeRenWu.get(position).getReward();
                intent.putExtra("taskId", taskId);
                intent.putExtra("userId", userId);
                intent.putExtra("price", price);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        faBuDeRenWu fbdrw = mFaBuDeRenWu.get(position);
        Log.d("Length2", String.valueOf(mFaBuDeRenWu.size()) );
        holder.details.setText(fbdrw.getDetails());
        //holder.people.setText("接受的人数：" + String.valueOf(fbdrw.getPeople()));
        String temp;
        if (fbdrw.getState() == 0) temp = "已完成";
        else if (fbdrw.getState() == 1) temp = "未领取";
        else temp = "已领取";
        //holder.state.setText("状态：" + temp);
    }

    @Override
    public int getItemCount() {
        return mFaBuDeRenWu.size();
    }
}
