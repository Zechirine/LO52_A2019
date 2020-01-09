package com.utbm.keepit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.User;
import com.utbm.keepit.backend.service.UserService;
import com.utbm.keepit.ui.views.InputView;

public class RegisterActivity extends BaseActivity {
    InputView inputName, inputPwd, inputPwdConfirm;
    UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }
    /**
     * 初始化View
     */
    private void initView(){
        initNavBar(true,R.string.register,false);
        inputName = findViewById(R.id.input_name);
        inputPwd = findViewById(R.id.input_passwd);
        inputPwdConfirm = findViewById(R.id.input_confirm_passwd);
    }
    public void onRegisterClick(View view) {
        String name = inputName.getInputStr();
        String pwd = inputPwd.getInputStr();
        String pwdConfirm = inputPwdConfirm.getInputStr();
//            System.out.println(pwd);
//            System.out.println(pwdConfirm);
        //
        if(name==null){
            Toast.makeText(RegisterActivity.this, R.string.no_name, Toast.LENGTH_SHORT).show();
            return ;
        }else if(name.length()<1 || name.isEmpty()){
            Toast.makeText(RegisterActivity.this, R.string.no_name, Toast.LENGTH_SHORT).show();
            return ;}
        if(pwd==null){
            Toast.makeText(RegisterActivity.this, R.string.no_pwd, Toast.LENGTH_SHORT).show();
            return ;
        }else if(pwd.length()<1||pwd.isEmpty()){
            Toast.makeText(RegisterActivity.this, R.string.no_pwd, Toast.LENGTH_SHORT).show();
            return ;}

        if(pwdConfirm==null){
            Toast.makeText(RegisterActivity.this, R.string.no_pwd_chicker, Toast.LENGTH_SHORT).show();
            return ;
        }else if(pwdConfirm.length()<1||pwdConfirm.isEmpty()){
            Toast.makeText(RegisterActivity.this, R.string.no_pwd_chicker, Toast.LENGTH_SHORT).show();
            return ;}

        if(pwd.equals(pwdConfirm)){
            if(userService.findUserByName(name)!=null){
                Toast.makeText(RegisterActivity.this, R.string.IdenExiste, Toast.LENGTH_SHORT).show();
                return ;
            }
            else{
                try{
                    userService.createUser(new User(name, pwd));
                    Toast.makeText(RegisterActivity.this, R.string.InsertSucce, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, R.string.InsertEchou, Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        }
        else{
            Toast.makeText(RegisterActivity.this, R.string.DiffMot, Toast.LENGTH_SHORT).show();
            return ;
        }
        onBackPressed();
    }
}
