package nvest.com.nvestlibrary.nvestcheck;

import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.sqlite.database.sqlite.SQLiteDatabase;

import java.io.File;
import io.reactivex.ObservableEmitter;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.base.BaseActivity;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.landing.ProductDataViewModel;
import nvest.com.nvestlibrary.needbasedanalyser.ProductNeedDataViewModel;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;

public class NvestCheckActivity extends BaseActivity implements ProductDataViewModel.ProductDataListener {
    private static String TAG = NvestCheckActivity.class.getSimpleName();
    private ProductDataViewModel productDataViewModel;
    private ProductNeedDataViewModel productNeedDataViewModel;
    private ObservableEmitter<String> emitterUp;
    private TextView test;
    private Button original;
    private ValidateInformationDataViewModel validateInformationDataViewModel;
    private File ASSET_DB_PATH;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nvest_check);
        CommonMethod.log(TAG , "Check activity  started");

        accessNvestAssetDatabase();
        accessRoomDatabase();
        test = (TextView) findViewById(R.id.test);
        original = (Button) findViewById(R.id.original);
        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CommonMethod.log(TAG , "Output " + stringFromJNI());
                testingWithAssetDatabase();
            }
        });
    }


    static {
        //System.loadLibrary("hello-jni");
        CommonMethod.log(TAG , "Loading sqlite x");
        System.loadLibrary("sqliteX");
        //System.loadLibrary("hello-jni");
    }

    public void testingWithAssetDatabase(){

        ASSET_DB_PATH = getApplicationContext().getDatabasePath(NvestAssetDatabaseAccess.getSingletonInstance().getDatabasePath());
        if(ASSET_DB_PATH.exists()){
            String res = "";
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(ASSET_DB_PATH, null);
            db.beginTransaction();
            //SQLiteStatement s = db.compileStatement("Select round(power(2,1.64),2)  from productmaster ");

            //Cursor c = db.query("productmaster",null, null, null, null, null, null);
            Cursor c = db.rawQuery("Select round(power(2,1.64),2)  from productmaster ", null);
            if(c != null){
                c.moveToFirst();
                CommonMethod.log(TAG , "Count " + c.getCount());
                do {
                    CommonMethod.log(TAG , "Output " + c.getString(0));
                }
                while (c.moveToNext());
            }
            //res = s.simpleQueryForString();
            db.setTransactionSuccessful();
            db.endTransaction();
            CommonMethod.log(TAG , "Product Master output " + res);

        }
    }


    @Override
    public void init() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void completestatus(int productsize) {
        CommonMethod.log(TAG , "Load products completed " + productsize);
        emitterUp.onNext(String.valueOf(productsize));
    }

    @Override
    public void onProductLiveDataReceived(boolean complete) {

    }

}
