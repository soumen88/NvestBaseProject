package nvest.com.nvestlibrary.nvestsync;

import nvest.com.nvestlibrary.base.MvpPresenter;

public interface SyncMvpPresenter <V extends SyncMvpView> extends MvpPresenter<V> {
    void hitServerForGettingProducts(String productId);
    void hitServerForGettingProductsSingle(String productId);
    void hitServerForGettingMort(String productId);
    void hitServerForGettingPremiumRates(String productId);
}
