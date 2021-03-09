package com.amarso.italk.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amarso.italk.R;
import com.amarso.italk.model.Model;
import com.amarso.italk.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class LoginActivity extends Activity {

    private EditText act_text;
    private EditText psw_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        act_text = findViewById(R.id.act_text);
        psw_text = findViewById(R.id.psw_text);
    }

    public void signin(View view) {
        final String account = act_text.getText().toString().trim();
        final String password = psw_text.getText().toString().trim();

        //简单校验
        if(TextUtils.isEmpty(account)){
            Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        EMClient.getInstance().login(account, password, new EMCallBack() {
            //登录成功
            @Override
            public void onSuccess() {
                //对模型层数据进行处理
                Model.getInstance().loginSuccess(new UserInfo(account));

                //保存用户信息到本地数据库
                Model.getInstance().getUserAccountDao().addAccount(new UserInfo(account));

                //弹出登录成功提示框
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "登录失败"+s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });

    }

    public void signup(View view) {
        //获取输入账户和密码
        final String account = act_text.getText().toString().trim();
        final String password = psw_text.getText().toString().trim();

        //简单校验
        if(TextUtils.isEmpty(account)){
            Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //注册成功，弹出提示框
                    EMClient.getInstance().createAccount(account, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    //注册失败
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "注册失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
