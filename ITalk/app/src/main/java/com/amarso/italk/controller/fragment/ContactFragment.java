package com.amarso.italk.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amarso.italk.R;
import com.amarso.italk.controller.activity.AddFriendActivity;
import com.amarso.italk.controller.activity.ChatActivity;
import com.amarso.italk.controller.activity.InviteActivity;
import com.amarso.italk.model.Model;
import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.utils.Constant;
import com.amarso.italk.controller.adapter.ContactAdapter;
import com.amarso.italk.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private ListView listView;
    private Button add_friend_btn;
    private LinearLayout header_ll;
    private ImageView red_point;
    private ContactAdapter contactAdapter;
    private LocalBroadcastManager manager;
    private String selected_user_id;
    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点
            red_point.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };
    private BroadcastReceiver ContactChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新ui
            refresh();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_contact, null);
        View headerView = View.inflate(getActivity(), R.layout.contact_listview_header, null);

        add_friend_btn = view.findViewById(R.id.to_add_friend_btn);
        red_point = headerView.findViewById(R.id.invite_notification_iv);
        header_ll = headerView.findViewById(R.id.contact_header);
        listView = view.findViewById(R.id.contact_lv);

        contactAdapter = new ContactAdapter(getActivity());
        listView.addHeaderView(headerView);
        listView.setAdapter(contactAdapter);

        refresh();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //点击右上角加号跳转至添加界面
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
            }
        });

        //点击新的朋友条目即可查看邀请
        header_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //红点消失
                red_point.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);

                //跳转到邀请页面
                startActivity(new Intent(getActivity(), InviteActivity.class));
            }
        });

        //点击联系人条目跳转到聊天界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取被点击item的联系人数据
                TextView contact_tx = view.findViewById(R.id.contact_nick);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("nick", contact_tx.getText());

                startActivity(intent);
            }
        });

        //初始化红点显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        red_point.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        //注册广播
        manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(ContactInviteChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        manager.registerReceiver(ContactChangeReceiver, new IntentFilter(Constant.CONTACT_CHANGED));

       getContactsFromServer();

        //listview绑定contextmenu
        registerForContextMenu(listView);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int pos = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        UserInfo userInfo  = (UserInfo) listView.getItemAtPosition(pos);
        selected_user_id = userInfo.getUserid();

        getActivity().getMenuInflater().inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //删除联系人
        if(item.getItemId() == R.id.contact_delete) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().deleteContact(selected_user_id);

                        //更新本地数据库
                        Model.getInstance().getDbManager().getContactTableDAO().deleteContactById(selected_user_id);

                        if(getActivity() == null){
                            return;
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "已删除好友"+selected_user_id, Toast.LENGTH_SHORT).show();
                                //刷新ui
                                refresh();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        if(getActivity() == null){
                            return;
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                                //刷新ui
                                refresh();
                            }
                        });
                    }
                }
            });

            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //关闭广播
        manager.unregisterReceiver(ContactInviteChangeReceiver);
        manager.unregisterReceiver(ContactChangeReceiver);
    }

    //从服务器获得联系人信息
    private void getContactsFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contacts_id = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    if(contacts_id != null){

                        //保存好友信息到本地数据库
                        List<UserInfo> contacts = new ArrayList<UserInfo>();
                        for(String id: contacts_id){
                            contacts.add(new UserInfo(id));
                        }
                        Model.getInstance().getDbManager().getContactTableDAO().saveContacts(contacts, true);

                        //fragment切换过快可能导致当前线程获取不到所在activity
                        if(getActivity() == null)
                            return;

                        //刷新ui
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refresh() {
        //获取数据
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDAO().getContact();

        if(contacts != null){
            contactAdapter.refresh(contacts);
        }
    }
}