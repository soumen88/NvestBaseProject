package com.nvest.user.databaseFiles.databaseWorkers;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.databaseFiles.DatabaseAccess;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.databaseFiles.dao.gsvtable.GSVRoom;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import com.nvest.user.databaseFiles.dao.lsadmaster.LSADMasterRoom;
import com.nvest.user.databaseFiles.dao.modemastertable.ModeMasterRoom;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;
import com.nvest.user.databaseFiles.dao.productmodetable.ProductModeRoom;
import com.nvest.user.databaseFiles.dao.taxstructure.TaxStructureRoom;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class OtherTablesWorker extends Worker {

    private static String TAG = OtherTablesWorker.class.getSimpleName();

    public OtherTablesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        LogUtils.log(TAG , "Inside do work");
        List<FundStrategyMasterRoom> fundStrategyMasterList = new ArrayList<FundStrategyMasterRoom>();
        List<BonusGuaranteeRoom> bonusGuaranteeList = new ArrayList<BonusGuaranteeRoom>();
        List<ProductCategoryMapRoom> productCategoryMapRoomList = new ArrayList<ProductCategoryMapRoom>();
        List<GSVRoom> gsvRoomList = new ArrayList<GSVRoom>();
        List<ModeMasterRoom> modeMasterRoomList = new ArrayList<ModeMasterRoom>();
        List<LSADMasterRoom> lsadMasterRoomsList = new ArrayList<LSADMasterRoom>();

        int fundStrategyCount = RoomDatabaseSingleton.getRoomDatabaseSession().fundStrategyMasterRoomDao().getAllRoomFuncMastersNormal().size();
        LogUtils.log(TAG , "Fund strategy count " + fundStrategyCount);
        if(fundStrategyCount == 0){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getFundStrategyCursor();
            if(cursor != null){
                try {
                    LogUtils.log(TAG , "Fund strategy cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            FundStrategyMasterRoom fundStrategyMaster = new FundStrategyMasterRoom();
                            fundStrategyMaster.setParentId(cursor.getInt(0));
                            fundStrategyMaster.setProductId(cursor.getInt(1));
                            fundStrategyMaster.setStrategyName(cursor.getString(2));
                            fundStrategyMaster.setParentId(cursor.getInt(3));
                            fundStrategyMasterList.add(fundStrategyMaster);
                        } while (cursor.moveToNext());
                    }

                } finally {
                    try {
                        LogUtils.log(TAG, "Closing cursor and inserting fund strategy");
                        cursor.close();
                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                LogUtils.log(TAG , "Inside Completable for fund master");
                                RoomDatabaseSingleton.getRoomDatabaseSession().fundStrategyMasterRoomDao().insertListFundStrategy(fundStrategyMasterList);
                            }
                        }).observeOn(Schedulers.newThread())
                                .subscribeOn(Schedulers.newThread()).subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                LogUtils.log(TAG , "Inside on subscribe for fund masters ");
                            }

                            @Override
                            public void onComplete() {
                                LogUtils.log(TAG , "Inside on complete for fund masters");
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.log(TAG , "Inside on error for fund masters ");
                            }
                        });


                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                    }
                }


            }
        }


        int bonusGuranteeCount = RoomDatabaseSingleton.getRoomDatabaseSession().bonusGuaranteeRoomDao().getBonusCount();
        LogUtils.log(TAG , "Bonus guarantee count " + bonusGuranteeCount);
        if(bonusGuranteeCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getBonusCursor();
            if(cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            BonusGuaranteeRoom bonusGuarantee = new BonusGuaranteeRoom();
                            bonusGuarantee.setBonusgId(cursor.getInt(0));
                            bonusGuarantee.setProductId(cursor.getInt(1));
                            bonusGuarantee.setPt(cursor.getInt(2));
                            bonusGuarantee.setPpt(cursor.getInt(3));
                            bonusGuarantee.setOptionId(cursor.getInt(4));
                            bonusGuarantee.setFromSA(cursor.getDouble(5));
                            bonusGuarantee.setToSA(cursor.getDouble(6));
                            bonusGuarantee.setFromAge(cursor.getInt(7));
                            bonusGuarantee.setToAge(cursor.getInt(8));
                            bonusGuarantee.setFrompolicyyear(cursor.getInt(9));
                            bonusGuarantee.setTopolicyyear(cursor.getInt(10));
                            bonusGuarantee.setRate(cursor.getDouble(11));
                            bonusGuaranteeList.add(bonusGuarantee);
                        } while (cursor.moveToNext());
                    }

                }
                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into bonus dao");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().bonusGuaranteeRoomDao().insertBonuses(bonusGuaranteeList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                    }
                }
            }
        }

        int productCategoryCount = RoomDatabaseSingleton.getRoomDatabaseSession().productCategoryMapRoomDao().getProductCategoryCount();
        LogUtils.log(TAG , "Product category count " + productCategoryCount);
        if(productCategoryCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getProductCategoryMapCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            ProductCategoryMapRoom productCategoryMapRoom = new ProductCategoryMapRoom();
                            productCategoryMapRoom.setProductCateryMapId(cursor.getInt(0));
                            productCategoryMapRoom.setProductId(cursor.getInt(1));
                            productCategoryMapRoom.setOptionId(cursor.getInt(2));
                            productCategoryMapRoom.setTypeId(cursor.getInt(3));
                            productCategoryMapRoom.setTaxGroup(cursor.getInt(4));
                            productCategoryMapRoomList.add(productCategoryMapRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into product category dao");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().productCategoryMapRoomDao().insertProductCategories(productCategoryMapRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing product category cursor " + e.toString());
                    }
                }
            }
            else {
                LogUtils.log(TAG , "Cursor is null");
            }
        }
        /*int temptableCount = RoomDatabaseSingleton.getRoomDatabaseSession().tempBIRoomDao().getTempBICount();
        LogUtils.log(TAG , "Temp table count " + temptableCount);*/


        /*int gsvCount = RoomDatabaseSingleton.getRoomDatabaseSession().gsvRoomDao().getGSVCount();
        LogUtils.log(TAG , "GSV count " + gsvCount);

        if(gsvCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getGSVRoomCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            GSVRoom gsvRoom = new GSVRoom();
                            gsvRoom.setGsvId(cursor.getInt(0));
                            gsvRoom.setProductId(cursor.getInt(1));
                            gsvRoom.setFromYear(cursor.getInt(2));
                            gsvRoom.setToYear(cursor.getInt(3));
                            gsvRoom.setAge(cursor.getInt(4));
                            gsvRoom.setPT(cursor.getInt(5));
                            gsvRoom.setPPT(cursor.getInt(6));
                            gsvRoom.setOptionId(cursor.getInt(7));
                            gsvRoom.setRefYear(cursor.getInt(8));
                            gsvRoom.setRate(cursor.getDouble(9));
                            gsvRoomList.add(gsvRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into gsv room dao");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().gsvRoomDao().insertGSV(gsvRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing gsv cursor " + e.toString());
                    }
                }
            }
        }

        int modeMasterCount = RoomDatabaseSingleton.getRoomDatabaseSession().modeMasterRoomDao().getModeMasterCount();
        LogUtils.log(TAG , "mode master count " + modeMasterCount);
        if(modeMasterCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getModeMasterRoomCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            ModeMasterRoom modeMasterRoom = new ModeMasterRoom();
                            modeMasterRoom.setModeId(cursor.getInt(0));
                            modeMasterRoom.setModeName(cursor.getString(1));
                            modeMasterRoom.setFrequency(cursor.getInt(2));
                            modeMasterRoomList.add(modeMasterRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into mode master");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().modeMasterRoomDao().insertModeMaster(modeMasterRoomList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing mode master cursor " + e.toString());
                    }
                }
            }
        }

        int lsadMasterCount = RoomDatabaseSingleton.getRoomDatabaseSession().lsadMasterRoomDao().getLSADMasterCount();
        LogUtils.log(TAG , "Lsad count count " + lsadMasterCount);
        if(lsadMasterCount == 0L){
            Cursor cursor = DatabaseAccess.getSingletonInstance().getLSADMasterCursor();
            if(cursor != null) {
                try {
                    LogUtils.log(TAG , "Product cursor count " + cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            LSADMasterRoom lsadMasterRoom = new LSADMasterRoom();
                            lsadMasterRoom.setLsadId(cursor.getInt(0));
                            lsadMasterRoom.setProductId(cursor.getInt(1));
                            lsadMasterRoom.setPT(cursor.getInt(2));
                            lsadMasterRoom.setPPT(cursor.getInt(3));
                            lsadMasterRoom.setOptionId(cursor.getInt(4));
                            lsadMasterRoom.setFromSa(cursor.getDouble(5));
                            lsadMasterRoom.setToSa(cursor.getDouble(6));
                            lsadMasterRoom.setSaInterval(cursor.getDouble(7));
                            lsadMasterRoom.setDiscountRate(cursor.getDouble(8));
                            lsadMasterRoom.setField1(cursor.getDouble(9));
                            lsadMasterRoomsList.add(lsadMasterRoom);
                        } while (cursor.moveToNext());
                    }

                }

                finally {
                    try {
                        LogUtils.log(TAG , "Closing cursor and inserting into lsad master");
                        cursor.close();
                        RoomDatabaseSingleton.getRoomDatabaseSession().lsadMasterRoomDao().insertLSAD(lsadMasterRoomsList);
                    }
                    catch (Exception e) {
                        LogUtils.log(TAG , "Exception occurred in closing lsad master cursor " + e.toString());
                    }
                }
            }
        }
*/

        /*int productModeCount = RoomDatabaseSingleton.getRoomDatabaseSession().productModeRoomDao().getProductModeCount();
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
        }*/


        /*int taxStructureCount = RoomDatabaseSingleton.getRoomDatabaseSession().taxStructureRoomDao().getTaxStructureCount();
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
        }*/

        /*int formulasCount = RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().getFormulaCount();
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


        }*/

        LogUtils.log(TAG , "Returning back");
        return Result.success();
    }
}
