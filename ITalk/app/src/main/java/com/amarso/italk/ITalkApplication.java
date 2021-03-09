package com.amarso.italk;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.amarso.italk.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

public class ITalkApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化SDK
        initSDK();

        //初始化数据模型层
        Model.getInstance().init(this);

        mContext = this;
    }

    //初始化SDK
    private void initSDK(){
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways((false));     //设置不自动接受好友请求
        options.setAutoAcceptGroupInvitation(false);    //设置不自动接受群邀请

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("service", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    //获取全局上下文
    public static Context getGlobalApplication() {
        return mContext;
    }
}
