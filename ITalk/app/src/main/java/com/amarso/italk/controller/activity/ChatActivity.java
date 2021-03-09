package com.amarso.italk.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amarso.italk.R;
import com.amarso.italk.controller.adapter.ChatAdapter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

public class ChatActivity extends Activity implements EMMessageListener {

    private ListView msg_lv;
    private TextView top_nick_tv;
    private EditText input_et;
    private Button send_btn;
    private Button delete_btn;

    private String friend_id;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msg_lv = findViewById(R.id.msg_lv);
        top_nick_tv = findViewById(R.id.top_nick_tv);
        input_et = findViewById(R.id.input_et);
        send_btn = findViewById(R.id.send_btn);
        delete_btn = findViewById(R.id.delete_record_btn);

        friend_id = getIntent().getStringExtra("nick");
        chatAdapter = new ChatAdapter(this, friend_id);
        msg_lv.setAdapter(chatAdapter);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭监听
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    private void initData() {
        //注册监听
        EMClient.getInstance().chatManager().addMessageListener(this);

        top_nick_tv.setText(friend_id);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_text = input_et.getText().toString();
                //封装消息
                EMMessage message = EMMessage.createTxtSendMessage(msg_text, friend_id);
                message.setChatType(EMMessage.ChatType.Chat);
                input_et.setText("");

                //发送成功则将消息显示在聊天框中
                chatAdapter.refreshMessage(message);

                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().chatManager().deleteConversation(friend_id, true);

                        chatAdapter.refreshConversation();
                    }
                });
            }
        });
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for(EMMessage msg : list){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.refreshMessage(msg);
                }
            });
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
