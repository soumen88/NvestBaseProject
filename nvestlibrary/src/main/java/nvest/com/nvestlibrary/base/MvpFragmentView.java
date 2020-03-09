package nvest.com.nvestlibrary.base;

public interface MvpFragmentView {

    void init();

    boolean isNetworkConnected();

    void hideKeyboard();

    void showLoading();

    void hideLoading();

}

