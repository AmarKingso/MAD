package com.amarso.italk.model.dao;

//邀请信息表
public class InviteTable {

    public static final String TAB_NAME = "tab_invite";
    public static final String COL_USERID = "userid";
    public static final String COL_NICK = "nick";
    public static final String COL_REASON = "reason";
    public static final String COL_STATUS = "status";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_USERID + " text primary key,"
            + COL_NICK + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " integer);";
}
