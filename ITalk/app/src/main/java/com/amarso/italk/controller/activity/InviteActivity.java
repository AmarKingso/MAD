package com.amarso.italk.controller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amarso.italk.R;
import com.amarso.italk.controller.adapter.InviteAdapter;
import com.amarso.italk.model.Model;
import com.amarso.italk.model.bean.InvitationInfo;
import com.amarso.italk.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class InviteActivity extends Activity {

    private ListView invite_lv;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager manager;
    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新ui
            refresh();
        }
    };
    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String id = invitationInfo.getUser().getUserid();
                        EMClient.getInstance().contactManager().acceptInvitation(id);

                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDAO().updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT, id);

                        //ui刷新
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "接受邀请", Toast.LENGTH_SHORT).show();

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "接受邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onReject(InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String id = invitationInfo.getUser().getUserid();
                        EMClient.getInstance().contactManager().declineInvitation(id);

                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDAO().deleteInvitation(id);

                        //刷新ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "已拒绝", Toast.LENGTH_SHORT).show();

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "拒绝失败", Toast.LENGTH_SHORT).show();

                                refresh();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        invite_lv = findViewById(R.id.invite_lv);
        //初始化listview
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unregisterReceiver(ContactInviteChangeReceiver);
    }

    private void initData() {
        inviteAdapter = new InviteAdapter(this, mOnInviteListener);
        invite_lv.setAdapter(inviteAdapter);

        refresh();

        //注册邀请信息变化的广播
        manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(ContactInviteChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
    }

    private void refresh() {
        //获取数据库中所有邀请信息
        List<InvitationInfo> invitations = Model.getInstance().getDbManager().getInviteTableDAO().getInvitations();

        //刷新适配器
        inviteAdapter.refresh(invitations);
    }
}
