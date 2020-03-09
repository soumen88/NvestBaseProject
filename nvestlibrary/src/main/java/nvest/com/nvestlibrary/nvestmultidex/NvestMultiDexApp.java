package nvest.com.nvestlibrary.nvestmultidex;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public class NvestMultiDexApp extends MultiDexApplication {
    private static String TAG = NvestMultiDexApp.class.getSimpleName();
    public static NvestMultiDexApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        CommonMethod.log(TAG , "Nvest lib multi dex has started");
        MultiDex.install(this);
        mInstance = this;

    }


    public static NvestMultiDexApp getmInstance(){
        return mInstance;
    }
}
