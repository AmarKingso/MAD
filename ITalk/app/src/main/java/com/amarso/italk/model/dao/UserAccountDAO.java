package com.amarso.italk.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.model.db.UserAccountDB;

//账号数据库的操作类
public class UserAccountDAO {

    private final UserAccountDB mHelper;

    public UserAccountDAO(Context context) {
         mHelper = new UserAccountDB(context);
    }

    public void addAccount(UserInfo user) {
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //添加条目
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_USERID, user.getUserid());
        values.put(UserAccountTable.COL_NICK, user.getNick());
        values.put(UserAccountTable.COL_PHOTO, user.getPhoto());

        db.replace(UserAccountTable.TAB_NAME, null, values);
    }

    public UserInfo getAccountByUserid(String userid) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + UserAccountTable.COL_USERID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userid});

        UserInfo user = null;
        if(cursor.moveToNext()){
            user = new UserInfo();

            //封装对象
            user.setUserid((cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_USERID))));
            user.setNick((cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK))));
            user.setPhoto((cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO))));
        }

        cursor.close();

        return user;
    }
}
