package com.amarso.italk.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amarso.italk.R;
import com.amarso.italk.model.Model;
import com.amarso.italk.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends Activity {

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断账号是否已经登录
                if(EMClient.getInstance().isLoggedInBefore()){
                    //获取登录的用户信息
                    UserInfo user = Model.getInstance().getUserAccountDao().getAccountByUserid(EMClient.getInstance().getCurrentUser());

                    //账户信息不存在则跳转到登录界面
                    if(user == null){
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    else {
                        //登陆成功后
                        Model.getInstance().loginSuccess(user);

                        //跳转到主界面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                }
                else{
                    try {
                        Thread.sleep(sleepTime);
                    }catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
    }
}
