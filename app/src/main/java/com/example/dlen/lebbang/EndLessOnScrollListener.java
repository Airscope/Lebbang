package com.example.dlen.lebbang;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by ASUS on 2017/12/29.
 */

public abstract class EndLessOnScrollListener extends  RecyclerView.OnScrollListener{
    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从0开始
    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    public boolean loading = true;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d("isLoading", String.valueOf(loading));
        if (dy > 0) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//        Log.d("totalItemCount", String.valueOf(totalItemCount));
//        Log.d("visibleItemCount", String.valueOf(visibleItemCount));
//        Log.d("firstVisibleItem", String.valueOf(firstVisibleItem));
            if (loading) {
                //Log.d("wnwn","firstVisibleItem: " +firstVisibleItem);
                //Log.d("wnwn","totalPageCount:" +totalItemCount);
                //Log.d("wnwn", "visibleItemCount:" + visibleItemCount)；

                if (totalItemCount - previousTotal != 0) {
                    //说明数据已经加载结束
                    loading = false;
                    previousTotal = totalItemCount;
                }
//            else if(totalItemCount<previousTotal)
//            {
//                previousTotal=totalItemCount;
//                loading=true;
//            }

            }
            //这里需要好好理解
//        if (!loading && totalItemCount-visibleItemCount <= firstVisibleItem){
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 1)) {
                currentPage++;
                loading = true;
                onLoadMore(currentPage);

            }
        }
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     * */
    public abstract void onLoadMore(int currentPage);
}
