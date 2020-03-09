package nvest.com.nvestlibrary.commonMethod;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.databaseFiles.dao.RoomDatabaseSingleton;
import nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;

public class KeyValueStoreDataViewModel extends AndroidViewModel {

    private final MutableLiveData<List<KeyValuePair>> mutableKeyValueList = new MutableLiveData<>();
    public KeyValueStoreDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void insertIntoKeyValueStore(String TAG  , String key, String value, String tagPassed, String fieldName){
        CommonMethod.log(TAG , "Insert into key value store started....");
        KeyValueStoreRoom keyValueStoreRoom = new KeyValueStoreRoom();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                keyValueStoreRoom.setKeyName(key);
                keyValueStoreRoom.setKeyValue(value);
                keyValueStoreRoom.setClassName(tagPassed);
                keyValueStoreRoom.setClassName(fieldName);

            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG ,"Inside complete Inserting into key value store");
                        RoomDatabaseSingleton.getRoomDatabaseSession().keyValueStoreRoomDao().insertKeyValueVariable(keyValueStoreRoom);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }
}
