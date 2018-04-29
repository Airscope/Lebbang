package com.example.dlen.lebbang;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Ferris tale on 2018/1/1.
 */

public class SquareRelativeLayout extends RelativeLayout {
        public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public SquareRelativeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareRelativeLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = widthMeasureSpec;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }