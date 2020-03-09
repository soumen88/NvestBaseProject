package nvest.com.nvestlibrary.basicInformation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME =NvestLibraryConfig.DB_NAME_WITH_FORMULAS_SQLITE;
    public static final String TABLE_NAME = NvestLibraryConfig.BASIC_INFORMATION_DETAIL_TABLE;
    public static final String id = "_id";
    public static final String LI_FIRST_NAME = "LI_FIRST_NAME";
    public static final String LI_LAST_NAME = "LI_LAST_NAME";
    public static final String LI_DOB = "LI_DOB";
    public static final String LI_AGE= "LI_AGE";
    public static final String LI_GENDER= "LI_GENDER";
    public static final String PR_FIRST_NAME= "PR_FIRST_NAME";
    public static final String PR_LAST_NAME= "PR_LAST_NAME";
    public static final String PR_DOB= "PR_DOB";
    public static final String PR_AGE= "PR_AGE";
    public static final String PR_GENDER= "PR_GENDER";
    public static final String EMAIL_ID= "EMAIL_ID";
    public static final String MOBILE_NO= "MOBILE_NO";
    public static final String STATE_DETAIL= "STATE_DETAIL";
    public static final String CITY_DETAIL= "CITY_DETAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,LI_FIRST_NAME TEXT,LI_LAST_NAME TEXT,LI_DOB TEXT,LI_AGE INTEGER,LI_GENDER TEXT," +
                "PR_FIRST_NAME TEXT,PR_LAST_NAME TEXT,PR_DOB TEXT,PR_AGE INTEGER,PR_GENDER TEXT," +
                "EMAIL_ID TEXT,MOBILE_NO TEXT,STATE_DETAIL TEXT,CITY_DETAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String FirstName, String LastName, String EditDateText,String TextAge,String SelectGender,String ExtraFirstName,String ExtraLastName,String EditDateExtraText,String TextExtraAge,String SelectExtraGender,String EmailText, String SelectStateSpinner,String SelectCitySpinner,String MobileNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LI_FIRST_NAME, FirstName);
        contentValues.put(LI_LAST_NAME, LastName);
        contentValues.put(LI_DOB,EditDateText);
        contentValues.put(LI_AGE, TextAge);
        contentValues.put(LI_GENDER,SelectGender);
        contentValues.put(PR_FIRST_NAME, ExtraFirstName);
        contentValues.put(PR_LAST_NAME,ExtraLastName);
        contentValues.put(PR_DOB, EditDateExtraText);
        contentValues.put(PR_AGE, TextExtraAge);
        contentValues.put(PR_GENDER, SelectExtraGender);
        contentValues.put(EMAIL_ID, EmailText);
        contentValues.put(STATE_DETAIL, SelectStateSpinner);
        contentValues.put(CITY_DETAIL, SelectCitySpinner);
        contentValues.put(MOBILE_NO,MobileNumber);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }


    }

}
