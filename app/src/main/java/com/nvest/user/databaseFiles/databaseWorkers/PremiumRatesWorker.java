package com.nvest.user.databaseFiles.databaseWorkers;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.databaseFiles.DatabaseAccess;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulasRoomDao;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.databaseFiles.dao.gsvtable.GSVRoom;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PremiumRatesWorker extends Worker {

    private static String TAG = PremiumRatesWorker.class.getSimpleName();

    public PremiumRatesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        LogUtils.log(TAG , "Inside do work");
        List<PremiumRatesRoom> premiumRatesRoomList = new ArrayList<PremiumRatesRoom>();
        int premratecount = RoomDatabaseSingleton.getRoomDatabaseSession().premiumRatesRoomDao().getPremiumRatesCount();
        LogUtils.log(TAG , "Premium rate count " + premratecount);
        if(premratecount == 0){
            LogUtils.log(TAG , "Inserting all prem rates");
            Cursor cursor = DatabaseAccess.getSingletonInstance().getPremiumCursor();
            if(cursor != null){
                try {
                    // looping through all rows and adding to list
                    if (cursor.moveToFirst()) {
                        do {
                            PremiumRatesRoom premiumRates = new PremiumRatesRoom();
                            premiumRates.setPremiumRateId(cursor.getInt(0));
                            premiumRates.setProductId(cursor.getInt(1));
                            premiumRates.setLaAge(cursor.getInt(2));
                            premiumRates.setSpouseAge(cursor.getInt(3));
                            premiumRates.setPT(cursor.getInt(4));
                            premiumRates.setPPT(cursor.getInt(5));
                            premiumRates.setOptionId(cursor.getInt(6));
                            premiumRates.setGender(cursor.getString(7));
                            premiumRates.setSaLower(cursor.getDouble(8));
                            premiumRates.setSaUpper(cursor.getDouble(9));
                            premiumRates.setSmoking(cursor.getInt(10));
                            premiumRates.setField1(cursor.getString(11));
                            premiumRates.setRate(cursor.getDouble(12));
                            premiumRatesRoomList.add(premiumRates);
                        } while (cursor.moveToNext());
                    }

                } finally {
                    try {
                        cursor.close();
                        LogUtils.log(TAG, "Closing cursor and inserting");
                        RoomDatabaseSingleton.getRoomDatabaseSession().premiumRatesRoomDao().insertPremiumRates(premiumRatesRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                    }
                }
            }
        }
        LogUtils.log(TAG , "Returning back");
        return Result.success();
    }
}
