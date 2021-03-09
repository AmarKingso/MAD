package com.amarso.italk.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amarso.italk.R;
import com.amarso.italk.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddFriendActivity extends Activity {

    private Button search_btn;
    private Button add_friend_btn;
    private EditText add_id_et;
    private RelativeLayout contact_info;
    private TextView add_nick_tv;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        search_btn = findViewById(R.id.search_btn);
        add_friend_btn = findViewById(R.id.add_friend_btn);
        add_id_et = findViewById(R.id.add_id_et);
        contact_info = findViewById(R.id.contact_info);
        add_nick_tv = findViewById(R.id.add_nick_tv);

        //查找用户
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = add_id_et.getText().toString();

                if(TextUtils.isEmpty(user_id)) {
                    Toast.makeText(getApplicationContext(), "未输入用户ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //默认用户存在
                        //显示结果
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                contact_info.setVisibility(View.VISIBLE);
                                add_nick_tv.setText(user_id);
                            }
                        });
                    }
                });
            }
        });

        //添加好友
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(user_id, "交个朋友");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "发送请求成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "发送请求失败"+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

}
