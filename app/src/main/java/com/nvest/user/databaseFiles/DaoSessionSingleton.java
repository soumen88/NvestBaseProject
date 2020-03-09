package com.nvest.user.databaseFiles;


import android.content.Context;

import com.nvest.user.appConfig.Config;
import com.nvest.user.models.DaoMaster;
import com.nvest.user.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DaoSessionSingleton {
    private static DaoSession daoSession;

    private DaoSessionSingleton(Context context) {
        GreenDaoUpgradeHelper helper = new GreenDaoUpgradeHelper(context, Config.DB_NAME_GREENDAO);
        //GreenDaoUpgradeHelper helper = new GreenDaoUpgradeHelper(context, Config.DB_NAME_GREENDAO, null);
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }


    public static synchronized void initialize(Context context) {
        if (daoSession == null) {
            new DaoSessionSingleton(context);
        }
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            throw new RuntimeException("Dao session not initialized");
        }
        return daoSession;
    }


}

