package nvest.com.nvestlibrary.commonMethod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

/**
 * @author Soumen Das.
 */

public class DeviceUtils {

    private static final String TAG = "DeviceUtils";


    @SuppressLint("HardwareIds")
    public static String getDeviceAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getUserAppVersion(Context context) {
        String appVersion = "" ;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //LogUtil.e(TAG, " Name of application not found : " + e.getMessage());
        }
        return appVersion;
    }
}
