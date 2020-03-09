package nvest.com.nvestlibrary.nvestsync;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.base.BasePresenter;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;
import nvest.com.nvestlibrary.nvestWebModel.NvestProductParserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncPresenter <V extends SyncMvpView> extends BasePresenter<V> implements SyncMvpPresenter<V>  {

    private static String TAG = SyncPresenter.class.getSimpleName();
    @Override
    public void hitServerForGettingProducts(String productId) {
        getApiInterface().getProductByProductId(productId).enqueue(new Callback<NvestProductParserModel>() {
            @Override
            public void onResponse(Call<NvestProductParserModel> call, Response<NvestProductParserModel> response) {
                try {
                    if(response.isSuccessful()){
                        NvestProductParserModel nvestProductParserModel = response.body();
                        getMvpView().receivedProductDetails(nvestProductParserModel);
                        getMvpView().hideLoading();
                    }
                    else {
                        CommonMethod.log(TAG , "Error occurred while fetching data");
                        getMvpView().hideLoading();

                    }
                }
                catch (Exception e){
                    CommonMethod.log(TAG , "Exception occured " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<NvestProductParserModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void hitServerForGettingProductsSingle(String productId) {
        getApiInterface().getProductByProductIdSingle(productId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NvestProductParserModel>() {
                    @Override
                    public void onSuccess(NvestProductParserModel nvestProductParserModel) {
                        CommonMethod.log(TAG , "On success");
                        getMvpView().receivedProductDetails(nvestProductParserModel);
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "On error");
                    }
                });
    }

    @Override
    public void hitServerForGettingMort(String productId) {
        getApiInterface().getMort(productId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<DummyMortModel1>() {
                    @Override
                    public void onSuccess(DummyMortModel1 dummyMortModel1) {
                        CommonMethod.log(TAG , "On success");
                        getMvpView().receivedMortality(dummyMortModel1);
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "On error");
                    }
                });
    }

    @Override
    public void hitServerForGettingPremiumRates(String productId) {
        getApiInterface().getPremiumRatesByProductIdSingle(productId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NvestProductParserModel>() {
                    @Override
                    public void onSuccess(NvestProductParserModel nvestProductParserModel) {
                        getMvpView().hideLoading();
                        CommonMethod.log(TAG , "On success");
                        getMvpView().receivedPremiumRates(nvestProductParserModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideLoading();
                        CommonMethod.log(TAG , "On error " + e.toString());
                    }
                });
    }
}
