package nvest.com.nvestlibrary.summary;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;


public class SummaryVieModel extends AndroidViewModel {
    private static final String TAG = SummaryVieModel.class.getSimpleName();
    private SummaryListener summaryListener;
    private NvestAssetDatabaseAccess nvestDb;

    public SummaryVieModel(@NonNull Application application) {
        super(application);
    }

    public void setSummaryListener(SummaryListener summaryListener) {
        this.summaryListener = summaryListener;
        nvestDb = NvestAssetDatabaseAccess.getSingletonInstance();
    }

    public interface SummaryListener {
        void biQuotationGenerated();
    }

    public void generateBiQuotation(Products product) {
        // get all generic dto key-value pairs
        HashMap<String, String> allDynamicParams = CommonMethod.getAllParamsFromGenericDTO();

        // create json array which holds input string for bi quotation
        JsonArray inputString = new JsonArray();

        // add data in input string array
        for (Map.Entry<String, String> entry : allDynamicParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Key", key);
            jsonObject.addProperty("Value", value);

            inputString.add(jsonObject);
        }
/*

        // get db object
        SQLiteDatabase db = null;
        //SQLiteDatabase db = nvestDb.getDatabase();

        // insert data in db
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductId", product.getProductId());
        contentValues.put("InputString", inputString.toString());
        contentValues.put("LoginId", "Dummy test id");

        long dataInserted = db.insert(NvestLibraryConfig.BI_QUOTATION_TABLE, null, contentValues);
*/

        NvestAssetDatabaseAccess.getSingletonInstance().insertIntoBIQuotationTable(product, inputString.toString());
        // close db
        // nvestDb.close();


        // if data is inserted, fire up the listener
        /*if (dataInserted != -1) {
            summaryListener.biQuotationGenerated();
        }*/
    }
}
