package com.example.dlen.lebbang;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASUS on 2018/1/1.
 */

public class UserAdapter extends ArrayAdapter<UserInformation> {

    private int resourceId;
    WoFaBuDeRenWu mActivity;

    public UserAdapter(Context context, int viewResourceId, List<UserInformation> objects, WoFaBuDeRenWu activity) {
        super(context, viewResourceId, objects);
        resourceId = viewResourceId;
        mActivity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserInformation user = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.username = (TextView) view.findViewById(R.id.like_username);
            viewHolder.credit = (TextView) view.findViewById(R.id.mytask_credit2);
            viewHolder.phone = (TextView) view.findViewById(R.id.mytask_phone);
            viewHolder.button = (Button) view.findViewById(R.id.mytask_accept);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.username.setText(user.getUsername());
        //viewHolder.credit.setText(user.getCredit());
        viewHolder.phone.setText(user.getPhoneNum());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.showResult();
            }
        });

        return view;
    }

    class ViewHolder {
        TextView username;
        TextView credit;
        TextView phone;
        Button button;
    }
}
