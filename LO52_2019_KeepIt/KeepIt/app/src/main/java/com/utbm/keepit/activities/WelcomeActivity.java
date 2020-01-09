package com.utbm.keepit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.utbm.keepit.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {
    //延迟三秒跳转
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toLogin();
//                toMain();
            }
        },3*1000);
    }

    /**
     * 跳转到main
     */
    private void toMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 跳转到login
     */
    private void toLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
