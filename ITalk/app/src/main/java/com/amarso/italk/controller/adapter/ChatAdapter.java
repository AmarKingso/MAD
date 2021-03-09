package com.amarso.italk.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amarso.italk.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<EMMessage> messages = new ArrayList<>();
    private String friend_id;

    public ChatAdapter(Context context, String id){
        mContext = context;
        friend_id = id;

        getChatRecord();
    }

    private void getChatRecord() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friend_id);
        //还未创建对话或未有消息
        if(conversation == null || conversation.getLastMessage() == null)
            return;

        List<EMMessage> record = conversation.loadMoreMsgFromDB(conversation.getLastMessage().getMsgId(), 50);
        if(record == null){
            record = conversation.getAllMessages();
        }
        else {
            record.addAll(conversation.getAllMessages());
        }
        refreshMessages(record);
    }

    //刷新单条消息
    public void refreshMessage(EMMessage message){
        if(messages != null){
            messages.add(message);

            notifyDataSetChanged();
        }
    }

    //刷新多条消息
    public void refreshMessages(List<EMMessage> message){
        if(messages != null){
            messages.addAll(message);

            notifyDataSetChanged();
        }
    }

    public void refreshConversation(){
        messages.clear();

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EMMessage msg = messages.get(position);
        TextView msg_tv = null;

        //根据消息来源判断判断气泡布局
        if(msg.getFrom().equals(friend_id)){
            convertView = View.inflate(mContext, R.layout.chat_friend_bubble_item, null);
            msg_tv = convertView.findViewById(R.id.friend_msg_tv);
        }
        else{
            convertView = View.inflate(mContext, R.layout.chat_self_bubble_item, null);
            msg_tv = convertView.findViewById(R.id.my_msg_tv);
        }

        msg_tv.setText(((EMTextMessageBody) msg.getBody()).getMessage());

        return convertView;
    }
}
