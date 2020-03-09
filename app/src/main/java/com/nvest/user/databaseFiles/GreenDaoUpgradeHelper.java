package com.nvest.user.databaseFiles;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.appConfig.Config;
import com.nvest.user.models.DaoMaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GreenDaoUpgradeHelper extends DaoMaster.OpenHelper {

    private static String TAG = GreenDaoUpgradeHelper.class.getSimpleName();
    public GreenDaoUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            updateDatabase(newVersion, db);
        }
    }

    private void updateDatabase(int newVersion, SQLiteDatabase db) {
        switch (newVersion) {
            case 1:
                //updateDatabaseVersion1(db);
                break;
            case 2:
                // updateDatabaseVersion2(db);
                break;
            case 3:
                // updateDatabaseVersion3(db);
                break;
            default:
                break;
        }
    }


}
