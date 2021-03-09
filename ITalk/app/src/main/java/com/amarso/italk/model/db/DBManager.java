package com.amarso.italk.model.db;

import android.content.Context;

import com.amarso.italk.model.dao.ContactTableDAO;
import com.amarso.italk.model.dao.InviteTableDAO;

public class DBManager {

    private final DBHelper dbHelper;
    private final ContactTableDAO contactTableDAO;
    private final InviteTableDAO inviteTableDAO;

    public DBManager(Context context, String tab_name){
        dbHelper = new DBHelper(context, tab_name);

        //创建两张表的操作类
        contactTableDAO = new ContactTableDAO(dbHelper);
        inviteTableDAO = new InviteTableDAO(dbHelper);
    }

    public ContactTableDAO getContactTableDAO(){
        return contactTableDAO;
    }

    public InviteTableDAO getInviteTableDAO() {
        return inviteTableDAO;
    }

    //关闭数据库
    public void close() {
        dbHelper.close();
    }
}
