package com.amarso.italk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.amarso.italk.ITalkApplication;

public class SpUtils {
    public static final String IS_NEW_INVITE = "is_new_invite";
    private static SpUtils instance = new SpUtils();
    private static SharedPreferences mSp;

    private SpUtils() {

    }

    public static SpUtils getInstance() {

        if(mSp == null){
            mSp = ITalkApplication.getGlobalApplication().getSharedPreferences("italk", Context.MODE_PRIVATE);
        }

        return instance;
    }

    //保存
    public void save(String key, Object value){
        if(value instanceof String){
            mSp.edit().putString(key, (String) value).commit();
        }
        else if(value instanceof Boolean){
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        }
        else if(value instanceof Integer){
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }
}
