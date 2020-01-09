package com.utbm.keepit.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.utbm.keepit.R;

/**
 * 输入框前面的图标
 * 输入框类型
 * 输入框提示
 * 是否为密文
 */
public class InputView extends FrameLayout {
    private int inputIcon;
    private String inputHint;
    private boolean isPasswd;
    private View view;
    private ImageView ivIcon;
    private EditText etInput;
    public InputView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    private void init(Context context,AttributeSet attributeSet){
        if(attributeSet==null){
            return;
        }
        TypedArray typedArray=context.obtainStyledAttributes(attributeSet, R.styleable.inputView);
        inputIcon=typedArray.getResourceId(R.styleable.inputView_input_icon,R.mipmap.keep);
        inputHint=typedArray.getString(R.styleable.inputView_input_hint);
        isPasswd=typedArray.getBoolean(R.styleable.inputView_is_passwd,false);
        typedArray.recycle();
        //绑定layout布局
        view=LayoutInflater.from(context).inflate(R.layout.input_view,this,false);
        ivIcon=view.findViewById(R.id.iv_input);
        etInput=view.findViewById(R.id.ed_input);
        //布局关联属性
        ivIcon.setImageResource(inputIcon);
        etInput.setHint(inputHint);
        etInput.setInputType(isPasswd?InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD:InputType.TYPE_CLASS_TEXT);

        addView(view);
    }

    /**
     * 返回输入内容
     * @return
     */
    public String getInputStr(){
        return etInput.getText().toString().trim();
    }

}
