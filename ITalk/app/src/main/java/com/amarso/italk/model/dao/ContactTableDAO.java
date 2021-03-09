package com.amarso.italk.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

//联系人表操作类
public class ContactTableDAO {

    private DBHelper mHelper;

    public ContactTableDAO(DBHelper helper){
        mHelper = helper;
    }

    //获取所有联系人
    public List<UserInfo> getContact(){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfo> users = new ArrayList<>();

        while(cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();

            userInfo.setUserid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USERID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

            users.add(userInfo);
        }

        cursor.close();

        return users;
    }

    //通过用户id查询其信息
    public UserInfo getContactById(String id){
        if(id == null) {
            return null;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_USERID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        UserInfo userInfo = null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();

            userInfo.setUserid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USERID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
        }

        cursor.close();

        return userInfo;
    }

    //通过id获取多个联系人信息
    public List<UserInfo> getContactsById(List<String> ids){
        if(ids == null || ids.size() <= 0){
            return null;
        }

        List<UserInfo> contacts = new ArrayList<>();

        for(String id: ids){
            UserInfo contact = getContactById(id);
            contacts.add(contact);
        }

        return contacts;
    }

    //保存单个联系人
    public void saveContact(UserInfo user, boolean isContact){
        if (user == null){
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_USERID, user.getUserid());
        values.put(ContactTable.COL_NICK, user.getNick());
        values.put(ContactTable.COL_PHOTO, user.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT, isContact ? 1 : 0);

        db.replace(ContactTable.TAB_NAME, null, values);
    }

    //保存多个联系人
    public void saveContacts(List<UserInfo> contacts, boolean isContact){
        if (contacts == null || contacts.size() <= 0){
            return;
        }

        for(UserInfo contact: contacts){
            saveContact(contact, isContact);
        }
    }

    //删除联系人信息
    public void deleteContactById(String id){
        if(id == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME, ContactTable.COL_USERID+"=?", new String[]{id});
    }
}
