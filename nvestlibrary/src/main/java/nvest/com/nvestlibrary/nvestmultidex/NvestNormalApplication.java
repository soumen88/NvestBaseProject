package nvest.com.nvestlibrary.nvestmultidex;

import android.app.Application;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public class NvestNormalApplication extends Application {
    private static String TAG = NvestNormalApplication.class.getSimpleName();
    private static NvestNormalApplication singleton;

    public NvestNormalApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CommonMethod.log(TAG , "Normal application started...");
        singleton = this;
        /*NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(this);
        nvestAssetDatabaseAccess.open();*/
    }


}
