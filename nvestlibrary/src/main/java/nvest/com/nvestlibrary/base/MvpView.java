package nvest.com.nvestlibrary.base;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;

public interface MvpView {

    void init();

//    void openActivityOnTokenExpire();

//    void showSnackBar(@StringRes int resId);

//    void showSnackBar(String message, @ColorRes int color);

//    void showSnackBar(String message);

//    void showError(@StringRes int resId);

  //  void showError(String message);

//    void showSuccess(String message);

//    void showSuccess(@StringRes int resId);

//    void showToast(@StringRes int resId);

    //boolean checkNetworkAndDemoMode();

    boolean isNetworkConnected();

    void hideKeyboard(View view);

    void showLoading();

    void hideLoading();

    void accessNvestAssetDatabase();

    void closeNvestAssetDatabase();

    void accessRoomDatabase();
}

