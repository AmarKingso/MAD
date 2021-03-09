package com.amarso.italk.model.dao;

public class UserAccountTable {

    public static final String TAB_NAME = "tab_account";
    public static final String COL_NICK = "nick";
    public static final String COL_USERID = "userid";
    public static final String COL_PHOTO = "photo";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_USERID + " text primary key,"
            + COL_NICK + " text,"
            + COL_PHOTO + " text);";
}
