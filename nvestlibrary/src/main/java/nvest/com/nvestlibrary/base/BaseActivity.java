package nvest.com.nvestlibrary.base;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.connectionDetector.ConnectionDetector;
import nvest.com.nvestlibrary.databaseFiles.dao.RoomDatabaseSingleton;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {
    private static String TAG = BaseActivity.class.getSimpleName();
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }


    static {
        //System.loadLibrary("hello-jni");
        CommonMethod.log(TAG , "Loading sqlite x");
        System.loadLibrary("sqliteX");
        //System.loadLibrary("hello-jni");
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean isNetworkConnected() {
        return new ConnectionDetector(getApplicationContext()).isNetworkConnectionAvailable();
    }

    public void hideKeyboard(View view) {
        //View view = this.getCurrentFocus();
        if (view != null) {
            CommonMethod.log(TAG ,"Inside hide keyboard");
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        else {
            CommonMethod.log(TAG , "View is null");
        }
    }


    @Override
    public void accessNvestAssetDatabase(){
        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(this);
        CommonMethod.log("Database " , "Database hash code " + nvestAssetDatabaseAccess.hashCode());
        nvestAssetDatabaseAccess.open();
    }

    @Override
    public void closeNvestAssetDatabase(){
        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(this);
        nvestAssetDatabaseAccess.close();
    }

    @Override
    public void accessRoomDatabase(){
        RoomDatabaseSingleton.initialize(this);
    }


}

