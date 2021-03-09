package com.amarso.italk.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amarso.italk.model.bean.InvitationInfo;
import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class InviteTableDAO {

    private DBHelper mHelper;

    public InviteTableDAO(DBHelper helper){
        mHelper = helper;
    }

    //添加邀请
    public void addInvitation(InvitationInfo invitationInfo){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_USERID, invitationInfo.getUser().getUserid());
        values.put(InviteTable.COL_NICK, invitationInfo.getUser().getNick());
        values.put(InviteTable.COL_REASON, invitationInfo.getReason());
        values.put(InviteTable.COL_STATUS, invitationInfo.getStatus().ordinal());

        db.replace(InviteTable.TAB_NAME, null, values);
    }

    //获取所有邀请信息
    public List<InvitationInfo> getInvitations(){
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "select * from " + InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvitationInfo> invitationInfos = new ArrayList<>();

        while(cursor.moveToNext()){
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invitationInfo.setStatus(int2InvitationStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            UserInfo user = new UserInfo();
            user.setUserid((cursor.getString(cursor.getColumnIndex(InviteTable.COL_USERID))));
            user.setNick((cursor.getString(cursor.getColumnIndex(InviteTable.COL_NICK))));
            invitationInfo.setUser(user);

            invitationInfos.add(invitationInfo);
        }

        cursor.close();

        return invitationInfos;
    }

    //将int转换为枚举
    private InvitationInfo.InvitationStatus int2InvitationStatus(int intStatus){
        if(intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()){
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }
        if(intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()){
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }
        if(intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BT_PEER.ordinal()){
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BT_PEER;
        }
        return null;
    }

    //删除邀请信息
    public void deleteInvitation(String id){
        if(id == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(InviteTable.TAB_NAME, InviteTable.COL_USERID+"=?", new String[]{id});
    }

    //更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String id){
        if(id == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS, invitationStatus.ordinal());
        db.update(InviteTable.TAB_NAME, values, InviteTable.COL_USERID+"=?", new String[]{id});
    }
}
