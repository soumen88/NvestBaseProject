package nvest.com.nvestlibrary.nvestsync;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.TimingLogger;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.databaseFiles.dao.RoomDatabaseSingleton;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;

public class SyncDataViewModel extends AndroidViewModel {

    private static String TAG = SyncDataViewModel.class.getSimpleName();
    private LiveData<List<MortalityChargeRoom>> mutableLiveData = new MutableLiveData<>();
    private SyncDataViewModelListener syncDataViewModelListener;
    public SyncDataViewModel(@NonNull Application application) {
        super(application);
    }

    public interface SyncDataViewModelListener{
        void complete(boolean flag);
    }

    public SyncDataViewModelListener getSyncDataViewModelListener() {
        return syncDataViewModelListener;
    }

    public void setSyncDataViewModelListener(SyncDataViewModelListener syncDataViewModelListener) {
        this.syncDataViewModelListener = syncDataViewModelListener;
    }

    public void getMortalityList(){
        TimingLogger timings = new TimingLogger("MyTag", "MyMethodName");
        mutableLiveData = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mutableLiveData = RoomDatabaseSingleton.getRoomDatabaseSession().mortalityChargeRoomDao().getMortalityCharge();
            }
        })
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        CommonMethod.log(TAG, "Inside on subscribe ");

                    }

                    @Override
                    public void onComplete() {

                        timings.addSplit("work A complete");
                        timings.dumpToLog();
                        syncDataViewModelListener.complete(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }

    public LiveData<List<MortalityChargeRoom>> setData(){
        return mutableLiveData;
    }

}
