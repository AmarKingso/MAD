package com.amarso.italk.model;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amarso.italk.model.bean.InvitationInfo;
import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.utils.Constant;
import com.amarso.italk.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

//全局事件监听
public class EventListener {

    private Context mContext;
    private final LocalBroadcastManager manager;

    public EventListener(Context context){
        mContext = context;

        //创建发送广播的管理者对象
        manager = LocalBroadcastManager.getInstance(mContext);
        //注册联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(mContextListener);
    }

    private final EMContactListener mContextListener = new EMContactListener() {
        //联系人增加后执行
        @Override
        public void onContactAdded(String id) {
            //数据更新
            Model.getInstance().getDbManager().getContactTableDAO().saveContact(new UserInfo((id)), true);

            //发送联系人变换的广播
            manager.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后执行
        @Override
        public void onContactDeleted(String id) {
            Model.getInstance().getDbManager().getContactTableDAO().deleteContactById(id);
            Model.getInstance().getDbManager().getInviteTableDAO().deleteInvitation(id);

            manager.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接收到新邀请后执行
        @Override
        public void onContactInvited(String id, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(id));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            //有新邀请有小红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            manager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //联系人接受邀请后执行
        @Override
        public void onFriendRequestAccepted(String id) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(id));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BT_PEER);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            manager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));

        }

        //联系人拒绝邀请后执行
        @Override
        public void onFriendRequestDeclined(String s) {
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            manager.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
