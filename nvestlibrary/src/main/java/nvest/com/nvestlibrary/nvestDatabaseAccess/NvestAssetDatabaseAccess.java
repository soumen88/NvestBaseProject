package nvest.com.nvestlibrary.nvestDatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestCursorModel.Products;

public class NvestAssetDatabaseAccess {
    private static String TAG = NvestAssetDatabaseAccess.class.getSimpleName();
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static NvestAssetDatabaseAccess instance;
    public static final String Id = "id";
    public static final String ActionType = "ActionType";
    public static final String MasterId = "MasterId";
    public static final String ProductId = "ProductId";
    public static final String FileType = "FileType";
    public static final String IsRateMaster = "IsRateMaster";
    public static final String ToBeDownloaded = "ToBeDownloaded";
    public static final String ToBeExecuted = "ToBeExecuted";
    public static final String LastModifiedAt = "LastModifiedAt";
    public static final String FormattedDate = "formattedDate";
    public static final String APIName= "APIName";
    public static final String DateTimeCalled= "DateTimeCalled";
    public static final String DateTimeFinish= "DateTimeFinish";
    public static final String Status= "Status";
    private File ASSET_DB_PATH = new File("/data/data/com.nvest.user/databases/nvest_with_formulas_sqlite.db");


    private NvestAssetDatabaseAccess(Context context) {
        CommonMethod.log(TAG , "Starting database access");
        //ASSET_DB_PATH = new File(getDatabasePath());
        //getDatabasePath();
        this.openHelper = new NvestAssetDbOpenHelper(context);

    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static synchronized NvestAssetDatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new NvestAssetDatabaseAccess(context);
        }
        return instance;
    }

    public static NvestAssetDatabaseAccess getSingletonInstance() {
        if (instance == null) {
            throw new RuntimeException("Database access not instantiated");
        }
        return instance;
    }

    public void open() {
        CommonMethod.log(TAG , "Openning asset database");
        //if(database == null && ASSET_DB_PATH.exists()){
        if(database == null){
            this.database = openHelper.getWritableDatabase();
            /*this.database = SQLiteDatabase.openOrCreateDatabase(ASSET_DB_PATH, null);
            this.database.beginTransaction();*/
        }
        else {
            CommonMethod.log(TAG , "Path does not exist");
        }
    }

    public void close() {
        if (database != null) {
            CommonMethod.log(TAG , "Closing database");
            this.database.close();
        }
    }

    public void bulkInsert(String phrase){
        database.execSQL(phrase);
    }


    /*public Cursor getProducts(int companyid) {

        *//*String selectQuery = "Select CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.*\n" +
                "from CompanyDefinedCategories\n" +
                "INNER JOIN ProductCompanyCateries ON  CompanyDefinedCategories.CompCat = ProductCompanyCateries.CompCat\n" +
                "INNER JOIN (Select * From ProductMaster where islive = 1 and categoryid = 1 and companyId = "+companyid+") AS ProductMaster ON ProductCompanyCateries.ProductId = ProductMaster.ProductId\n" +
                "ORDER BY CompanyDefinedCategories.CompCat\n"  ;*//*

        String selectQuery = "Select CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.*\n" +
                "from CompanyDefinedCategories\n" +
                "INNER JOIN ProductCompanyCategories ON  CompanyDefinedCategories.CompCat = ProductCompanyCategories.CompCat\n" +
                "INNER JOIN (Select * From ProductMaster where islive = 1 and categoryid = 1 and companyId = "+companyid+") AS ProductMaster ON ProductCompanyCategories.ProductId = ProductMaster.ProductId\n" +
                "ORDER BY CompanyDefinedCategories.CompCat\n"  ;

        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            CommonMethod.log(TAG , "Product Count " + cursor.getCount());
            return cursor;
        }
        catch (Exception e) {
            CommonMethod.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }
*/

    public void insertIntoBIQuotationTable(Products product, String inputString){

        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductId", product.getProductId());
        contentValues.put("InputString", inputString);
        contentValues.put("LoginId", "Dummy test id");
        database.insert(NvestLibraryConfig.BI_QUOTATION_TABLE, null, contentValues);
    }

    public void insertIntoChangedProductDetails(String actionType , String masterid,String productid,String filetype,String isRateMaster, String toBeDownloaded,String tobeExecuted, String lastModifiedAt,String formattedDate ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActionType,actionType);
        contentValues.put(MasterId,masterid);
        contentValues.put(ProductId,productid);
        contentValues.put(FileType,filetype);
        contentValues.put(IsRateMaster,isRateMaster);
        contentValues.put(ToBeDownloaded,toBeDownloaded);
        contentValues.put(ToBeExecuted,tobeExecuted);
        contentValues.put(LastModifiedAt,lastModifiedAt);
        contentValues.put(FormattedDate,formattedDate);
        database.insert(NvestLibraryConfig.CHANGED_PRODUCT_DETAILS, null, contentValues);

    }

    public void insertIntoChangedProductDetailsTry(int primaryKey , String actionType , String masterid,String productid,String filetype,String isRateMaster, String toBeDownloaded,String tobeExecuted, String lastModifiedAt,String formattedDate ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Id,primaryKey);
        contentValues.put(ActionType,actionType);
        contentValues.put(MasterId,masterid);
        contentValues.put(ProductId,productid);
        contentValues.put(FileType,filetype);
        contentValues.put(IsRateMaster,isRateMaster);
        contentValues.put(ToBeDownloaded,toBeDownloaded);
        contentValues.put(ToBeExecuted,tobeExecuted);
        contentValues.put(LastModifiedAt,lastModifiedAt);
        contentValues.put(FormattedDate,formattedDate);
        database.replace(NvestLibraryConfig.CHANGED_PRODUCT_DETAILS, null, contentValues);

    }

    public void updateChangedProductDetails(String id , String actionType , String masterid,String productid,String filetype,String isRateMaster, String toBeDownloaded,String tobeExecuted, String lastModifiedAt,String formattedDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Id,id);
        contentValues.put(ActionType,actionType);
        contentValues.put(MasterId,masterid);
        contentValues.put(ProductId,productid);
        contentValues.put(FileType,filetype);
        contentValues.put(IsRateMaster,isRateMaster);
        contentValues.put(ToBeDownloaded,toBeDownloaded);
        contentValues.put(ToBeExecuted,tobeExecuted);
        contentValues.put(LastModifiedAt,lastModifiedAt);
        contentValues.put(FormattedDate,formattedDate);
        database.update(NvestLibraryConfig.CHANGED_PRODUCT_DETAILS, contentValues, "id = " +id , null);

    }

    //public void  insertIntoChangedProductLogmaster(String masterid,String apiname,String datetimecalled,String datetimefinish,String status) {
    public Long insertIntoChangedProductLogmaster(String apiname,String datetimecalled,String datetimefinish,String status) {
        ContentValues contentValues = new ContentValues();
        //contentValues.put(MasterId,masterid);
        contentValues.put(APIName,apiname);
        contentValues.put(DateTimeCalled,datetimecalled);
        contentValues.put(DateTimeFinish,datetimefinish);
        contentValues.put(Status,status);
        long id = database.insert(NvestLibraryConfig.CHANGED_PRODUCT_LOGMASTER, null, contentValues);
        return id;
    }

    public Cursor getProductById(int companyid, int productid) {

        String selectQuery = "Select CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.* from CompanyDefinedCategories INNER JOIN ProductCompanyCategories ON  CompanyDefinedCategories.CompCat = ProductCompanyCategories.CompCat INNER JOIN (Select * From ProductMaster where islive = 1 and categoryid = 1 and companyId = '"+companyid+"' and productid = '"+productid+"' ) AS ProductMaster ON ProductCompanyCategories.ProductId = ProductMaster.ProductId ORDER BY CompanyDefinedCategories.CompCat"  ;

        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            CommonMethod.log(TAG , "Product Count " + cursor.getCount());
            return cursor;
        }
        catch (Exception e) {
            CommonMethod.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }

    public String getDatabasePath(){
        CommonMethod.log(TAG , "Get Database Path " + database.getPath());
        return database.getPath();
    }

    public Cursor ExecuteQuery(String Query) {
        try {
            Cursor cursor = database.rawQuery(Query, null);
            CommonMethod.log(TAG , "Product Count " + cursor.getCount());
            return cursor;
        }
        catch (Exception e) {
            CommonMethod.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }

    public Cursor ExecuteBiQuery(String Query) {
        try {
            org.sqlite.database.sqlite.SQLiteDatabase db = org.sqlite.database.sqlite.SQLiteDatabase.openOrCreateDatabase(ASSET_DB_PATH, null);
            db.beginTransaction();

            //SQLiteStatement s = db.compileStatement("Select round(power(2,1.64),2)  from productmaster ");

            //Cursor c = db.query("productmaster",null, null, null, null, null, null);
            Cursor c = db.rawQuery(Query, null);
            if(c != null){
                c.moveToFirst();
                CommonMethod.log(TAG , "Get cursor column count " + c.getCount());
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return c ;

        }
        catch (Exception e) {
            CommonMethod.log(TAG , "Exception in fund cursor " + e.toString());
        }
        return null;
    }

    public void getAllTables(){
        //Cursor c = database.rawQuery("SELECT name FROM "+ NvestLibraryConfig.DB_NAME_WITH_FORMULAS_SQLITE +" WHERE type='table'", null);
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                CommonMethod.log(TAG , "Table Name=> "+c.getString(0));
                c.moveToNext();
            }
        }
    }

}
