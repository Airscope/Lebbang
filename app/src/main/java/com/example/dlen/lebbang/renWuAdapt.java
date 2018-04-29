package com.example.dlen.lebbang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oragee.banners.BannerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dlen on 2017/12/27.
 */

public class renWuAdapt extends RecyclerView.Adapter<renWuAdapt.ViewHolder>{
    private Context mContext;
    private List<RenWu> mRenWuList;
    private RenWuLan rwlContext;
    Bitmap bitmap;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;

    private int[] imgs = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4};
    private List<View> viewList;
    BannerView bannerView;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView touXiang;
        TextView name;
        TextView details;
        TextView phoneNum;
        View v;

        public ViewHolder(View view) {
            super(view);
            v = view;
//            cardView = (CardView) view;
//            name = (TextView) view.findViewById(R.id.user_name);
//            //phoneNum = (TextView) view.findViewById(R.id.task_desc);
//            details = (TextView) view.findViewById(R.id.task_desc);
//            touXiang = (CircleImageView) view.findViewById(R.id.tttXiang);
        }
    }

    public renWuAdapt(List<RenWu> renWuList) {
        mRenWuList = renWuList;
    }
    public renWuAdapt(List<RenWu> renWuList, RenWuLan context) {
        mRenWuList = renWuList;
        rwlContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(VIEW_HEADER);
        }
        else if (viewType == TYPE_FOOTER) {
            return new ViewHolder(VIEW_FOOTER);
        }
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView = (CardView) holder.v;
        holder.name = (TextView) view.findViewById(R.id.user_name);
        holder.phoneNum = (TextView) view.findViewById(R.id.task_desc);
        holder.details = (TextView) view.findViewById(R.id.task_desc);
        holder.touXiang = (CircleImageView) view.findViewById(R.id.tttXiang);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (haveHeaderView()) position--;
                RenWu renWu = mRenWuList.get(position);
                Intent intent = new Intent(mContext, task_information.class);
                intent.putExtra("task_data", renWu);
                String cookie = rwlContext.getSessionId();
                intent.putExtra("cookie", cookie);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            viewList = new ArrayList<View>();
            for (int i = 0; i < imgs.length; i++) {
                ImageView image = new ImageView(mContext);
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //设置显示格式
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageResource(imgs[i]);
                viewList.add(image);
            }
            LinearLayout linLayout = (LinearLayout) holder.v;
            bannerView = (BannerView) linLayout.findViewById(R.id.banner);
            //bannerView = (BannerView) findViewById(R.id.banner);
            bannerView.startLoop(true);

            bannerView.setViewList(viewList);
        }
        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            final RenWu renwu = mRenWuList.get(position);
            holder.details.setText(renwu.getDetails());
            //holder.phoneNum.setText(renwu.getPhoneNum());
            holder.name.setText(renwu.getName());
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                //从网络上获取图片
//                final Bitmap bitmap=getPicture(renwu.getImageId());
//                //发送一个Runnable对象
//                holder.touXiang.post(new Runnable(){
//
//
//                    @Override
//                    public void run() {
//                        holder.touXiang.setImageBitmap(bitmap);//在ImageView中显示从网络上获取到的图片
//                    }
//
//                });
//
//            }
//        }).start();//开启线程
            //Log.d("touxiang",renwu.getImageId());
            //holder.touXiang.setImageBitmap(returnBitmap(renwu.getImageId()));
            //holder.touXiang.setImageResource(renwu.getImageId());
        }
    }

    @Override
    public int getItemCount() {
        int count = mRenWuList.size();
        if (VIEW_FOOTER != null) {
            count++;
        }
        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        }
        else if (isFooterView(position)) {
            return TYPE_FOOTER;
        }
        else {
            return TYPE_NORMAL;
        }
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            notifyItemInserted(getItemCount() - 1);
        }
    }


    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }


    public Bitmap getPicture(String path) {
        Bitmap bm = null;
        URL url;
        try {
            url = new URL(path);//创建URL对象
            URLConnection conn = url.openConnection();//获取URL对象对应的连接
            conn.connect();//打开连接
            InputStream is = conn.getInputStream();//获取输入流对象
            bm = BitmapFactory.decodeStream(is);//根据输入流对象创建Bitmap对象
        } catch (MalformedURLException e1) {
            e1.printStackTrace();//输出异常信息
        } catch (IOException e) {
            e.printStackTrace();//输出异常信息
        }
        return bm;
    }

    public Bitmap returnBitmap(String url) {
        URL myfileUrl = null;
        Bitmap bitmap = null;
        try {
            myfileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myfileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
