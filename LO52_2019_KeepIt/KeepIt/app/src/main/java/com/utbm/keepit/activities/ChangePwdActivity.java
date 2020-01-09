package com.utbm.keepit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.utbm.keepit.MyApp;
import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.User;
import com.utbm.keepit.backend.service.UserService;
import com.utbm.keepit.ui.views.InputView;


public class ChangePwdActivity extends BaseActivity {
        private InputView oldPwd,newPwd,confirmPwd;
        UserService userService=new UserService();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_pwd);
            initView();
        }
        private void initView(){
            initNavBar(true,R.string.passwd_change,false);
            newPwd=fd(R.id.input_pwdn);
            oldPwd=fd(R.id.input_pwdo);
            confirmPwd=fd(R.id.input_pwdc);
        }
        public void onChangeClick(View view){
            User u = MyApp.getUser();
            String pwdo=oldPwd.getInputStr();
            String pwdn=newPwd.getInputStr();
            String pwdc=confirmPwd.getInputStr();
//            String phone=UserHelper.getInstance().getPhone();

            if(pwdo==null){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd, Toast.LENGTH_SHORT).show();
                return ;
            }else if(pwdo.length()<1 || pwdo.isEmpty()){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd, Toast.LENGTH_SHORT).show();
                return ;}

            if(pwdn==null){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd_new, Toast.LENGTH_SHORT).show();
                return ;
            }else if(pwdn.length()<1||pwdn.isEmpty()){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd_new, Toast.LENGTH_SHORT).show();
                return ;}

            if(pwdc==null){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd_new_chicker, Toast.LENGTH_SHORT).show();
                return ;
            }else if(pwdc.length()<1||pwdc.isEmpty()){
                Toast.makeText(ChangePwdActivity.this, R.string.no_pwd_new_chicker, Toast.LENGTH_SHORT).show();
                return ;}

            if(pwdc.equals(pwdn)){
                if(userService.changePwd(u,pwdn)){
                    Toast.makeText(ChangePwdActivity.this, "change pwd success", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ChangePwdActivity.this, "unknown problem", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }else{
                Toast.makeText(ChangePwdActivity.this, "2 pwd different", Toast.LENGTH_SHORT).show();
                return ;
            }




        }
    }
