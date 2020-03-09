package nvest.com.nvestlibrary.nvestDatabaseAccess;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class NvestAssetDbOpenHelper  extends SQLiteAssetHelper {
    //private static final String DATABASE_NAME = NvestLibraryConfig.DB_NAME;
    private static final String DATABASE_NAME = NvestLibraryConfig.DB_NAME_WITH_FORMULAS_SQLITE;
    private static final int DATABASE_VERSION = NvestLibraryConfig.DB_VERSION;

    public NvestAssetDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

