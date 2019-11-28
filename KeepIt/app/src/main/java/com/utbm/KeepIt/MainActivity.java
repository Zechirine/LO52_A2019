package com.utbm.KeepIt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import DAO.LoginDao;

public class MainActivity extends AppCompatActivity {
    private LoginDao userDao;
    private int age;
    private int height;
    private float weight;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定对应的界面
        setContentView(R.layout.activity_main);

        System.out.println("------------------------------------------------------------------------------------------------");
        userDao = new LoginDao(this);

        String name = getIntent().getStringExtra("username");
        Cursor userInfo = userDao.getUserInfoByName(name);
        userInfo.moveToFirst();

            this.age = userInfo.getInt(userInfo.getColumnIndex("age"));
            this.height = userInfo.getInt(userInfo.getColumnIndex("height"));
            this.weight = userInfo.getFloat(userInfo.getColumnIndex("weight"));
            this.level = userInfo.getInt(userInfo.getColumnIndex("level"));

            TextView ageView = findViewById(R.id.userAge);
            TextView heightView = findViewById(R.id.userHeight);
            TextView weightView = findViewById(R.id.userWeight);
            TextView levelView = findViewById(R.id.userLevel);

            ageView.setText(""+this.age+" "+this.height+" "+this.weight+" "+ this.level);


    }



}
