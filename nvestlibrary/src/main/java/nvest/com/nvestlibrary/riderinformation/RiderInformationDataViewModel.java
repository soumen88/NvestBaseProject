package nvest.com.nvestlibrary.riderinformation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.RiderInformationModel;

public class RiderInformationDataViewModel extends AndroidViewModel {

    private static String TAG = RiderInformationDataViewModel.class.getSimpleName();
    private MutableLiveData<ArrayList<RiderInformationModel>> mutableRiderInformation = new MutableLiveData<>();
    private RiderInformationDataListener riderInformationDataListener;

    public RiderInformationDataViewModel(@NonNull Application application) {
        super(application);
    }

    public RiderInformationDataListener getRiderInformationDataListener() {
        return riderInformationDataListener;
    }

    public void setRiderInformationDataListener(RiderInformationDataListener riderInformationDataListener) {
        this.riderInformationDataListener = riderInformationDataListener;
    }

    public interface RiderInformationDataListener {
        void getRider(boolean complete);
    }

    public void getRidersFromProductId(int productId) {

        ArrayList<RiderInformationModel> riderInformationModelArrayList = new ArrayList<>();
        mutableRiderInformation = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String query = "select distinct RiderId,(select productname from ProductMaster where productid = riderid  ) as RiderName,(Select typename from typemaster where typeid = (SELECT platform from productmaster where productid = riderid)) typename from productridermap inner join productmaster on productridermap.productid = productmaster.productid where productridermap.productid = " + productId;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(query);
                CommonMethod.log(TAG, "Rider cursor count " + cursor.getCount());
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            RiderInformationModel riderInformationModel = new RiderInformationModel();
                            riderInformationModel.setRiderId(cursor.getInt(0));
                            riderInformationModel.setRiderName(cursor.getString(1));
                            riderInformationModel.setRiderTypeName(cursor.getString(2));
                            riderInformationModelArrayList.add(riderInformationModel);
                        } while (cursor.moveToNext());
                    }
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG, "Inside complete " + riderInformationModelArrayList.size());
                        mutableRiderInformation.setValue(riderInformationModelArrayList);
                        riderInformationDataListener.getRider(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }

    public MutableLiveData<ArrayList<RiderInformationModel>> setRiderInformationMutableLiveData() {
        CommonMethod.log(TAG, "Inside set mutable live data");
        return mutableRiderInformation;
    }
}
