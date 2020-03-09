package com.nvest.user.databaseFiles;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static String TAG = DatabaseAccess.class.getSimpleName();
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private Context context;
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        LogUtils.log(TAG , "Starting database access");
        this.openHelper = new DatabaseOpenHelper(context);
        this.context = context;
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }


    public static DatabaseAccess getSingletonInstance() {
        if (instance == null) {
            throw new RuntimeException("Database access not instantiated");
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public FundStrategyMasterRoom getQuotes() {
        try {
            Cursor cursor = database.rawQuery("Select * from FundStrategyMaster", null);
            if(cursor != null){
                cursor.moveToFirst();
                LogUtils.log(TAG , "Cursor count " + cursor.getCount());
                FundStrategyMasterRoom fundStrategyMaster = new FundStrategyMasterRoom();
                fundStrategyMaster.setParentId(cursor.getInt(0));
                fundStrategyMaster.setProductId(cursor.getInt(1));
                fundStrategyMaster.setStrategyName(cursor.getString(2));
                fundStrategyMaster.setParentId(cursor.getInt(3));
                return fundStrategyMaster;
            }
            else {
                LogUtils.log(TAG , "Cursor is null");
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Sql exception " + e.toString());
        }

        return null;
    }

    public int getFundMasterCount() {
        try {
            Cursor cursor = database.rawQuery("Select * from FundStrategyMaster", null);
            if(cursor != null){
                cursor.moveToFirst();
                LogUtils.log(TAG , "Cursor count " + cursor.getCount());
                return cursor.getCount();
            }
            else {
                LogUtils.log(TAG , "Cursor is null");
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Sql exception " + e.toString());
        }

        return 0;
    }

    public List<FundStrategyMasterRoom> getAllElements() {

        List<FundStrategyMasterRoom> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM FundStrategyMaster"  ;


        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        FundStrategyMasterRoom fundStrategyMaster = new FundStrategyMasterRoom();
                        fundStrategyMaster.setParentId(cursor.getInt(0));
                        fundStrategyMaster.setProductId(cursor.getInt(1));
                        fundStrategyMaster.setStrategyName(cursor.getString(2));
                        fundStrategyMaster.setParentId(cursor.getInt(3));
                        list.add(fundStrategyMaster);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                }
                catch (Exception e) {
                    LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                }
            }

        } finally {
            try {
                //database.close();
            }
            catch (Exception e) {
                LogUtils.log(TAG , "Exception occurred in closing db " + e.toString());
            }
        }

        return list;
    }

    public Cursor getFundStrategyCursor() {

        String selectQuery = "SELECT  * FROM FundStrategyMaster"  ;
        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        }
        catch (Exception e) {
            LogUtils.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }

    public List<PremiumRatesRoom> getAllPremiumRates() {

        List<PremiumRatesRoom> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM PremiumRates"  ;


        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        PremiumRatesRoom premiumRates = new PremiumRatesRoom();
                        premiumRates.setProductId(cursor.getInt(0));
                        premiumRates.setLaAge(cursor.getInt(1));
                        premiumRates.setPPT(cursor.getInt(2));
                        premiumRates.setPT(cursor.getInt(3));
                        list.add(premiumRates);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                }
                catch (Exception e) {
                    LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                }
            }

        } finally {
            try {
                //database.close();
            }
            catch (Exception e) {
                LogUtils.log(TAG , "Exception occurred in closing db " + e.toString());
            }
        }

        return list;
    }


    public Cursor getPremiumCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM PremiumRates"  ;


        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getBonusCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM BonusGuarantee"  ;

        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getBonusSCRCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM BonusScr"  ;

        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }


    public Cursor getProductCategoryMapCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM ProductCateryMap"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception product catery map " + e.toString());
        }
        return null;
    }

    public Cursor getGSVRoomCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM GSV"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getModeMasterRoomCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM ModeMaster"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getLSADMasterCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM LSADMASTER"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getProductModeCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM ProductMode"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public Cursor getTaxStructureCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM TaxStructure"  ;
        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    /* public Formulas getFormula() {

        Formulas formulas = new Formulas();

        // Select All Query
        String selectQuery = "SELECT  * FROM Formulas Where FormulaId = 2216"  ;


        try {

            Cursor cursor = database.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        formulas.setFormulaId(cursor.getInt(0));
                        formulas.setProductId(cursor.getInt(1));
                        formulas.setFormulaKeyword(cursor.getString(2));
                        formulas.setFormulawithfunction(cursor.getString(3));
                        formulas.setFormulabasic(cursor.getString(4));
                        formulas.setFormulaExtended(cursor.getString(5));
                        formulas.setIsOutput(cursor.getInt(6) > 0);
                        formulas.setIsInterimKe(cursor.getInt(7) > 0);
                        formulas.setOutputLoop(cursor.getInt(8) > 0);
                        formulas.setSumOrYrEnd(cursor.getInt(9) > 0);
                        formulas.setDescription(cursor.getString(10));

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                }
                catch (Exception e) {
                    LogUtils.log(TAG , "Exception occurred in closing cursor " + e.toString());
                }
            }

        } finally {
            try {
                //database.close();
            }
            catch (Exception e) {
                LogUtils.log(TAG , "Exception occurred in closing db " + e.toString());
            }
        }

        return formulas;
    }
*/
    public Cursor getFormulaCursor() {

        String selectQuery = "SELECT  * FROM Formulas"  ;
        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            return cursor;
        }
        catch (Exception e) {
            LogUtils.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }

    public void  getTableNames(){
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        //Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                LogUtils.log(TAG , "Table name "+ cursor.getString(0));
                cursor.moveToNext();
            }
        }
    }

}
