package com.amarso.italk.model.bean;

import com.amarso.italk.controller.adapter.InviteAdapter;

public class InvitationInfo {
    private UserInfo user;
    private String reason;
    private InvitationStatus status;

    public enum InvitationStatus{
        //新邀请
        NEW_INVITE,
        //接受邀请
        INVITE_ACCEPT,
        //邀请被接受
        INVITE_ACCEPT_BT_PEER
    }

    public InvitationInfo(){

    }

    public InvitationInfo(UserInfo user, String reason, InvitationStatus status){
        this.user = user;
        this.reason = reason;
        this.status = status;
    }

    public UserInfo getUser() {
        return user;
    }

    public String getReason() {
        return reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
}
