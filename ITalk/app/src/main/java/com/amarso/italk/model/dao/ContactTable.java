package com.amarso.italk.model.dao;

//联系人建表语句
public class ContactTable {
    public static final String TAB_NAME = "tab_contact";
    public static final String COL_USERID = "userid";
    public static final String COL_NICK = "nick";
    public static final String COL_PHOTO = "photo";

    public static final String COL_IS_CONTACT = "is_contact";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_USERID + " text primary key,"
            + COL_NICK + " text,"
            + COL_PHOTO + " text,"
            + COL_IS_CONTACT + " integer);";
}
