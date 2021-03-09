package com.amarso.italk.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amarso.italk.R;
import com.amarso.italk.model.Model;
import com.amarso.italk.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserInfo> mContactsInfo = new ArrayList<>();

    public ContactAdapter(Context context){
        mContext = context;
    }

    //刷新数据的方法
    public void refresh(List<UserInfo> contactsInfo){
        if(contactsInfo != null){
            mContactsInfo.clear();
            mContactsInfo.addAll(contactsInfo);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mContactsInfo == null ? 0 : mContactsInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactsInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView contact_id = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.contact_listview_item, null);
            contact_id = convertView.findViewById(R.id.contact_nick);

            convertView.setTag(contact_id);
        }
        else{
            contact_id = (TextView)convertView.getTag();
        }

        //获取item数据
        UserInfo contact = mContactsInfo.get(position);
        contact_id.setText(contact.getUserid());

        return convertView;
    }
}
