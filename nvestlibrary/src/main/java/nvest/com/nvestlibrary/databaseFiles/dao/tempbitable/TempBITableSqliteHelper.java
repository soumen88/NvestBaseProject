package nvest.com.nvestlibrary.databaseFiles.dao.tempbitable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class TempBITableSqliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = NvestLibraryConfig.DATABASE_VERSION;

    // Database Name
    private static final String DATABASE_NAME = NvestLibraryConfig.DB_NAME_GREENDAO;

    private static String TAG = TempBITableSqliteHelper.class.getSimpleName();

    //Temp table
    //Icons and information table


    public TempBITableSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getProductCategoryMapCursor() {

        // Select All Query
        String selectQuery = "SELECT  * FROM ProductCateryMap"  ;
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            CommonMethod.log(TAG , "Count " + cursor.getCount());
            if (cursor.moveToFirst()) {
                return cursor;
            }

        }
        catch (Exception e){
            CommonMethod.log(TAG , "Cursor exception " + e.toString());
        }
        return null;
    }

    public void insertIntoTempTable(TempBIRoom tempBIRoom){
        CommonMethod.log(TAG , "Insert address started");
        String COLUMN_UID = "_id";
        //String COLUMN_AGE =(String) GenericDTO.getAttributeValue("age");
        String COLUMN_AGE ="LI_ATTAINED_AGE";
        //String COLUMN_POLICY_TERM = (String)  GenericDTO.getAttributeValue("policyTerm");
        String COLUMN_POLICY_TERM = "POLICYYEAR";
        String COLUMN_PRODUCT_ID = "PRODUCTID";
        String COLUMN_CURRENT_AGE ="CURRENTAGE";
        String COLUMN_UNIQUE_KEY = "unique_key";
        CommonMethod.log(TAG , "Age column " + COLUMN_AGE);
        CommonMethod.log(TAG , "Policy term column " + COLUMN_POLICY_TERM);
        CommonMethod.log(TAG , "Product id column " + COLUMN_PRODUCT_ID);
        CommonMethod.log(TAG , "Current age column " + COLUMN_CURRENT_AGE);
        CommonMethod.log(TAG , "Unique key column " + COLUMN_UNIQUE_KEY);
        SQLiteDatabase db = this.getWritableDatabase();
        if(tempBIRoom != null){
            CommonMethod.log(TAG, "Not null Policy year" + tempBIRoom.getPolicyYear() + " - Attainted age " + tempBIRoom.getLiAttainedAge()+ " - unique key " + " - product id " + tempBIRoom.getProductId());
            ContentValues values = new ContentValues();
            values.put(COLUMN_POLICY_TERM, tempBIRoom.getPolicyYear());
            values.put(COLUMN_CURRENT_AGE, tempBIRoom.getLiAttainedAge());
            values.put(COLUMN_UNIQUE_KEY, tempBIRoom.getUniqueKey());
            values.put(COLUMN_PRODUCT_ID, tempBIRoom.getProductId());
            values.put(COLUMN_AGE, tempBIRoom.getAge());
            CommonMethod.log(TAG , "Policy term " + tempBIRoom.getPolicyYear() + " Age " +tempBIRoom.getLiAttainedAge() + " Unique " + tempBIRoom.getUniqueKey() + " Product id " + tempBIRoom.getProductId() + " ");
            db.insert(NvestLibraryConfig.TEMP_BI_TABLE, null, values);
        }
        else {
            CommonMethod.log(TAG, "Temp bi is null");
        }
        db.close(); // Closing database connection
        CommonMethod.log(TAG , "Saving address is success");
    }

    public void createTempBiTable(String createTableString){
        try {
            CommonMethod.log(TAG , "Create table string " + createTableString);
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL(createTableString);
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in create temp bi table " + e.toString());
        }
    }

    public TestPojoSUD open(){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in create temp bi table " + e.toString());
        }
        return null;
    }


    public void getTempData(){
        String selectQuery = "Select ROUND(90.73-(SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA)) AS TEMPDATA from TempBI";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        CommonMethod.log(TAG , "Count "+ cursor.getCount());
        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            CommonMethod.log(TAG , "Temp Data dump " + DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {
                do {
                    CommonMethod.log(TAG , "dump at 0 " + cursor.getInt(0));
                } while (cursor.moveToNext());
            }
        }
    }
    public List<TestPojoSUD> getTempTablePOJO(String selectQuery){
        List<TestPojoSUD> testPojoSUDList = new ArrayList<>();
        CommonMethod.log(TAG, "Getting temp table details ");
        //List<TestPojoSUD> testPojoSUDList = new ArrayList<>()
        //String selectQuery = "SELECT  * mFROM " + Config.TEMP_BI_TABLE +  " WHERE POLICYYEAR = '1'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        CommonMethod.log(TAG , "Count "+ cursor.getCount());

        // looping through all rows and adding to list
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                CommonMethod.log(TAG , "Looading a cursor");
                CommonMethod.log(TAG , "Current dump " +DatabaseUtils.dumpCurrentRowToString(cursor));
                do {
                    TestPojoSUD testPojoSUD = new TestPojoSUD();
                    //CommonMethod.log(TAG , "Li attained age " + cursor.getString(cursor.getInt(0)));
                    CommonMethod.log(TAG , "Li attained age " + cursor.getInt(0) + " some " + cursor.getDouble(6));
                    testPojoSUD.setLI_ATTAINED_AGE(cursor.getInt(0));
                    testPojoSUD.setPOLICYYEAR(cursor.getInt(1));
                    testPojoSUD.setPREMIUMRATE(cursor.getDouble(2));
                    testPojoSUD.setMODE_FREQ(cursor.getInt(3));
                    testPojoSUD.setMODE_DISC(cursor.getInt(4));
                    testPojoSUD.setLI_AGE(cursor.getInt(5));
                    testPojoSUD.setHSAD_RATE(cursor.getInt(6));
                    testPojoSUD.setANN_PREM(cursor.getDouble(7));
                    testPojoSUD.setNSAP_VAL(cursor.getDouble(8));
                    testPojoSUD.setMODAL_PREM(cursor.getDouble(9));
                    testPojoSUD.setTAX_MP(cursor.getDouble(10));
                    testPojoSUD.setMODAL_PREM_TAX(cursor.getDouble(11));
                    testPojoSUD.setLOAD_ANN_PREM(cursor.getDouble(12));
                    testPojoSUD.setLOAD_ANN_PREM_NSAP(cursor.getDouble(13));
                    testPojoSUD.setANN_PREM_YEARLY(cursor.getDouble(14));
                    testPojoSUD.setCUM_LOAD_PREM(cursor.getDouble(15));
                    testPojoSUD.setGUAR_ADD(cursor.getDouble(16));
                    testPojoSUD.setACCR_GUAR_ADD(cursor.getDouble(17));
                    testPojoSUD.setDB_G(cursor.getDouble(20));
                    testPojoSUDList.add(testPojoSUD);
                    /*CommonMethod.log(TAG , "Pincode " + cursor.getString(4));
                    CommonMethod.log(TAG , "City " + cursor.getString(5));*/
                } while (cursor.moveToNext());
            }
            return  testPojoSUDList;
        }

        // close db connection
        db.close();

        return null;
    }

}
