package com.utbm.keepit.ui.views;
import android.content.Context;

import android.util.AttributeSet;

import android.widget.ListView;

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    //重点是重写这个函数，可以告诉 父listView 子的高度
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}