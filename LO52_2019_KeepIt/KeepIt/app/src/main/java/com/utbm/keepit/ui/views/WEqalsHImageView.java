package com.utbm.keepit.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class WEqalsHImageView extends AppCompatImageView {
    public WEqalsHImageView(Context context) {
        super(context);
    }

    public WEqalsHImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WEqalsHImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
