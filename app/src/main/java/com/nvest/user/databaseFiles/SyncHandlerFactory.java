package com.nvest.user.databaseFiles;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.support.annotation.Nullable;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import com.nvest.user.databaseFiles.databaseWorkers.DummyWorker;
import com.nvest.user.databaseFiles.databaseWorkers.OtherTablesWorker;
import com.nvest.user.databaseFiles.databaseWorkers.PremiumRatesWorker;
import com.nvest.user.databaseFiles.databaseWorkers.ProductTableWorker;
import com.nvest.user.home.HomeScreenActivity;

import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SyncHandlerFactory {
    private static String TAG = SyncHandlerFactory.class.getSimpleName();
    private Context context;

    public SyncHandlerFactory(Context context) {
        LogUtils.log(TAG , "Handler started");
        this.context = context;
    }


    public void insertIntoKeyValueStore(){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                LogUtils.log(TAG , "Inside run");
                KeyValueStoreRoom keyValueStoreRoom = new KeyValueStoreRoom();
                keyValueStoreRoom.setKeyValue("123213");
                keyValueStoreRoom.setKeyName("test");
                RoomDatabaseSingleton.getRoomDatabaseSession().keyValueStoreRoomDao().insertKeyValueVariable(keyValueStoreRoom);
            }
        }).observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.log(TAG , "Inside on subscribe");
            }

            @Override
            public void onComplete() {
                LogUtils.log(TAG , "Inside on complete");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.log(TAG , "Inside on error");
            }
        });


    }

    public void createWorkRequest(){

        try {
           RoomDatabaseSingleton.getRoomDatabaseSession().keyValueStoreRoomDao()
                    .getValueByKey("test1")
                    .subscribeOn(Schedulers.newThread())
                    .onErrorReturn((Throwable ex) -> {
                        LogUtils.log(TAG,"Error in try "+ex.toString()); //examine error here
                        KeyValueStoreRoom keyValueStoreRoom1 = new KeyValueStoreRoom();
                        keyValueStoreRoom1.setKeyValue("234324");
                        return keyValueStoreRoom1; //empty object of the datatype
                    })
                   .doOnSuccess(new Consumer<KeyValueStoreRoom>() {
                       @Override
                       public void accept(KeyValueStoreRoom keyValueStoreRoom) throws Exception {
                           LogUtils.log(TAG , "inside do on success "+ keyValueStoreRoom.getKeyValue());
                       }
                   })
                    .subscribe();



        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception " + e.toString());
        }
        //startRequest("PREMIUM_RATES_WORKER");
        //startRequest("DUMMY_WORKER");
    }



    private void startRequest(String workerKey){
        try {
            switch (Config.WorkerName.valueOf(workerKey)){
                case PREMIUM_RATES_WORKER:
                    LogUtils.log(TAG, "Starting premium rates worker");
                    final OneTimeWorkRequest premiumRatesWorkRequest = new OneTimeWorkRequest.Builder(PremiumRatesWorker.class).build();
                    WorkManager.getInstance().enqueue(premiumRatesWorkRequest);

                    break;
                case OTHER_TABLES_WORKER:
                    LogUtils.log(TAG , "Other tables worker started");

                    break;
                case PRODUCT_TABLES_WORKER:

                    break;
                case DUMMY_WORKER:

                    break;

            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception occurred in switch case " + e.toString());
        }

    }


}
