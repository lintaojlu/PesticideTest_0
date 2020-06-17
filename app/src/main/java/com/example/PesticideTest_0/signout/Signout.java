package com.example.PesticideTest_0.signout;

import android.app.Activity;
import java.util.ArrayList;

public class Signout {
    public static ArrayList<Activity> mActivityList = new ArrayList<Activity>();


    //onCreate()时添加
    public static void addActivity(Activity activity){
        //判断集合中是否已经添加，添加过的则不再添加
        if (!mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }

    //关闭所有Activity
    public static void finishAllActivity(){
        for (Activity activity : mActivityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
