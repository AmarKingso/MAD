package com.amarso.italk.model;

import android.content.Context;

import com.amarso.italk.model.bean.UserInfo;
import com.amarso.italk.model.dao.UserAccountDAO;
import com.amarso.italk.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//数据模型层全局类
public class Model {

    //单例对象
    private static Model model = new Model();

    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();
    private UserAccountDAO userAccountDao;
    private DBManager dbManager;

    private Model(){}

    public static Model getInstance(){
        return model;
    }

    public void init(Context context){
        mContext = context;
        userAccountDao = new UserAccountDAO(mContext);
        
        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }

    //获取全局线程池
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    //用户登录成功后的处理方法
    public void loginSuccess(UserInfo user) {
        if(user == null){
            return;
        }

        if(dbManager != null){
            dbManager.close();
        }

        dbManager = new DBManager(mContext, user.getUserid());
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public UserAccountDAO getUserAccountDao() {
        return userAccountDao;
    }
}
