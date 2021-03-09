package com.amarso.italk.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amarso.italk.R;
import com.amarso.italk.model.bean.InvitationInfo;
import com.amarso.italk.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;


public class InviteAdapter extends BaseAdapter {

    private Context mContext;
    private List<InvitationInfo> mInviteList = new ArrayList<>();
    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context, OnInviteListener onInviteListener){
        mContext = context;
        mOnInviteListener = onInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> inviteList){
        if(inviteList != null){
            mInviteList.clear();
            mInviteList.addAll(inviteList);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInviteList == null ? 0 : mInviteList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInviteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler holder = null;
        if(convertView == null){
            holder = new ViewHodler();

            convertView = View.inflate(mContext, R.layout.invite_listview_item, null);

            holder.user_id = convertView.findViewById(R.id.invite_id);
            holder.reason = convertView.findViewById(R.id.invite_reason);
            holder.accept_btn = convertView.findViewById(R.id.invite_accept_btn);
            holder.reject_btn = convertView.findViewById(R.id.invite_reject_btn);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHodler) convertView.getTag();
        }

        //获取item数据
        InvitationInfo invitationInfo = mInviteList.get(position);

        //展示item数据
        UserInfo user = invitationInfo.getUser();
        //id
        holder.user_id.setText(user.getUserid());

        //隐藏按钮
        holder.accept_btn.setVisibility(View.GONE);
        holder.reject_btn.setVisibility(View.GONE);

        //原因
        if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE){
            if(invitationInfo.getReason() == null){
                holder.reason.setText("交个朋友");
            }
            else{
                holder.reason.setText(invitationInfo.getReason());
            }

            holder.accept_btn.setVisibility(View.VISIBLE);
            holder.reject_btn.setVisibility(View.VISIBLE);
        }
        else if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT){
            if(invitationInfo.getReason() == null){
                holder.reason.setText("接受邀请");
            }
            else{
                holder.reason.setText(invitationInfo.getReason());
            }
        }
        else if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BT_PEER){
            if(invitationInfo.getReason() == null){
                holder.reason.setText("邀请被接受");
            }
            else{
                holder.reason.setText(invitationInfo.getReason());
            }
        }

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInviteListener.onAccept(invitationInfo);
            }
        });

        holder.reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnInviteListener.onReject(invitationInfo);
            }
        });

        return convertView;
    }

    private class ViewHodler{
        private TextView user_id;
        private TextView reason;
        private Button accept_btn;
        private Button reject_btn;
    }

    public interface OnInviteListener{
        void onAccept(InvitationInfo invitationInfo);

        void onReject(InvitationInfo invitationInfo);
    }
}
