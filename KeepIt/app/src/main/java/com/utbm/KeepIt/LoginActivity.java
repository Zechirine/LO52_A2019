package com.utbm.KeepIt;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DAO.LoginDao;

public class LoginActivity extends AppCompatActivity {

    //定义控件
    private EditText username , pwd ;
    private Button loginbtn;
    private LoginDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dao = new LoginDao(this) ;

        //获取控件
        username = (EditText) findViewById(R.id.activity_login_et_username);
        pwd = (EditText) findViewById(R.id.activity_login_et_pwd);
        loginbtn = (Button) findViewById(R.id.activity_login_btn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断登陆成功与否
                if(dao.login(username.getText().toString(),pwd.getText().toString())){
                    //登陆成功页面跳转
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username",username.getText().toString());
                    startActivity(intent);
                }else {
                    //登陆失败，显示提示信息
                    Toast.makeText(LoginActivity.this, "用户名和密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
