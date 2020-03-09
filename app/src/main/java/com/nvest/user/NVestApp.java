package com.nvest.user;

/*

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.DaoSessionSingleton;
import com.nvest.user.databaseFiles.DatabaseAccess;
import com.nvest.user.databaseFiles.RoomAppDatabase;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;

public class NVestApp extends MultiDexApplication {

    private static String TAG = NVestApp.class.getSimpleName();
    public static NVestApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG , "OnCreate of nvest application");
        MultiDex.install(this);
        mInstance = this;
        //DaoSessionSingleton.initialize(this);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        RoomDatabaseSingleton.initialize(this);
    }


}
*/
