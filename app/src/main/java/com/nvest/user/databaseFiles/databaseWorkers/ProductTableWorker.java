package com.nvest.user.databaseFiles.databaseWorkers;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.databaseFiles.DatabaseAccess;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.dao.bonusscrtable.BonusScrRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.productmodetable.ProductModeRoom;
import com.nvest.user.databaseFiles.dao.taxstructure.TaxStructureRoom;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ProductTableWorker extends Worker {

    private static String TAG = ProductTableWorker.class.getSimpleName();

    public ProductTableWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {

        List<ProductModeRoom> productModeRoomList = new ArrayList<ProductModeRoom>();
        List<TaxStructureRoom> taxStructureRoomList= new ArrayList<TaxStructureRoom>();
        List<FormulaRoom> formulasList = new ArrayList<FormulaRoom>();
        List<BonusScrRoom> bonusScrRoomList = new ArrayList<BonusScrRoom>();


        int productModeCount = RoomDatabaseSingleton.getRoomDatabaseSession().productModeRoomDao().getProductModeCount();
        LogUtils.log(TAG , "product mode count " + productModeCount);
        if(productModeCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getProductModeCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product mode cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            ProductModeRoom productModeRoom = new ProductModeRoom();
                            productModeRoom.setProductId(cursor.getInt(0));
                            productModeRoom.setModelId(cursor.getInt(1));
                            productModeRoom.setMultiplier(cursor.getDouble(2));
                            productModeRoomList.add(productModeRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into product mode");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().productModeRoomDao().insertProductMode(productModeRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing product mode cursor " + e.toString());
                    }
                }
            }
        }


        int taxStructureCount = RoomDatabaseSingleton.getRoomDatabaseSession().taxStructureRoomDao().getTaxStructureCount();
        LogUtils.log(TAG , "tax structure count " + taxStructureCount);

        if(taxStructureCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getTaxStructureCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            TaxStructureRoom taxStructureRoom = new TaxStructureRoom();
                            taxStructureRoom.setTaxID(cursor.getInt(0));
                            taxStructureRoom.setTaxGroup(cursor.getInt(1));
                            taxStructureRoom.setTaxGroupName(cursor.getString(2));
                            taxStructureRoom.setTaxKeyword(cursor.getString(3).toUpperCase());
                            taxStructureRoom.setTaxName(cursor.getString(4));
                            taxStructureRoom.setFromYear(cursor.getInt(5));
                            taxStructureRoom.setToYear(cursor.getInt(6));
                            taxStructureRoom.setFromDate(cursor.getString(7));
                            taxStructureRoom.setToDate(cursor.getString(8));
                            taxStructureRoom.setRate(cursor.getDouble(9));

                            taxStructureRoomList.add(taxStructureRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into tax structure");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().taxStructureRoomDao().insertTaxStructure(taxStructureRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing tax structure cursor " + e.toString());
                    }
                }
            }
        }
        int bonusScrCount = RoomDatabaseSingleton.getRoomDatabaseSession().bonusScrRoomDao().getBonusScrCount();
        LogUtils.log(TAG , "Bonus scr count " + bonusScrCount);

        if(bonusScrCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getBonusSCRCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Bonus scr cursor count " + cursor.getCount());
                    LogUtils.log(TAG , "Bonus scr column count " + cursor.getColumnCount());
                    if (cursor.moveToFirst()) {
                        do {
                            BonusScrRoom bonusScrRoom = new BonusScrRoom();
                            bonusScrRoom.setProductId(cursor.getInt(0));
                            bonusScrRoom.setBonusScrId(cursor.getInt(1));
                            bonusScrRoom.setBonusName(cursor.getString(2));
                            bonusScrRoom.setBonusValue(cursor.getDouble(3));
                            bonusScrRoomList.add(bonusScrRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into bonus scr");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().bonusScrRoomDao().insertIntoBonusScr(bonusScrRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing bonus scr cursor " + e.toString());
                    }
                }
            }
        }


        int formulasCount = RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().getFormulaCount();
        LogUtils.log(TAG , "Formulas count " + formulasCount);
        if(formulasCount == 0){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getFormulaCursor();
            if(cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            FormulaRoom formulas = new FormulaRoom();
                            formulas.setFormulaId(cursor.getInt(0));
                            formulas.setProductId(cursor.getInt(1));
                            formulas.setFormulaKeyword(cursor.getString(2));
                            String formulaWithFunction = cursor.getString(3);
                            if(formulaWithFunction.charAt(0) == '(' && formulaWithFunction.charAt(formulaWithFunction.length()-1) == ')') //str.matches("\\[.*\\]")
                            {
                                formulaWithFunction = formulaWithFunction.substring(1, formulaWithFunction.length() - 1);
                            }
                            formulas.setFormulawithfunction(formulaWithFunction);
                            String formulabasic = cursor.getString(4);
                            if(formulabasic.charAt(0) == '(' && formulabasic.charAt(formulabasic.length()-1) == ')') //str.matches("\\[.*\\]")
                            {
                                formulabasic = formulabasic.substring(1, formulabasic.length() - 1);
                            }
                            formulas.setFormulabasic(formulabasic);
                            String formulaExtended = cursor.getString(5);
                            if(formulaExtended.charAt(0) == '(' && formulaExtended.charAt(formulaExtended.length()-1) == ')') //str.matches("\\[.*\\]")
                            {
                                formulaExtended = formulaExtended.substring(1, formulaExtended.length() - 1);
                            }
                            formulas.setFormulaExtended(formulaExtended);
                            formulas.setOutput(cursor.getInt(6) > 0);
                            formulas.setInterimKe(cursor.getInt(7) > 0);
                            formulas.setOutputLoop(cursor.getInt(8) > 0);
                            formulas.setSumOrYrEnd(cursor.getInt(9) > 0);
                            formulas.setDescription(cursor.getString(10));
                            formulasList.add(formulas);
                        } while (cursor.moveToNext());
                    }

                }
                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into formula dao");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().insertFormulas(formulasList);
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