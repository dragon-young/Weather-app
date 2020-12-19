package com.example.dragon.app.base;

import android.app.Application;

import com.example.dragon.app.db.DBManger;

import org.xutils.x;

/**
 * Created by yl177 on 2020/12/15.
 */

/**
 *   需要权限 <uses-permission android:name="android.permission.INTERNET" />
 *  1. 新建一个类MyApplication继承Application
 *  2. 在Manifest文件中注册MyApplication
 */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 全局声明
        x.Ext.init(this);

        // 当项目工程被创建的时候， 数据库也会被初始化
        DBManger.initDB(this);
    }
}
