package nvest.com.nvestlibrary.nvestmultidex;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;

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
