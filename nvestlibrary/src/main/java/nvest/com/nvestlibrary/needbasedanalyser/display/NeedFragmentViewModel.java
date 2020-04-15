package nvest.com.nvestlibrary.needbasedanalyser.display;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;

public class NeedFragmentViewModel extends AndroidViewModel {
    private static String TAG  = NeedFragmentViewModel.class.getSimpleName();
    private MutableLiveData<List<KeyValuePair>> mutableNeedList = new MutableLiveData<>();
    private NeedViewModelListener needViewModelListener;
    public NeedFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public interface NeedViewModelListener{
        void complete(boolean flag);
    }

    public NeedViewModelListener getNeedViewModelListener() {
        return needViewModelListener;
    }

    public void setNeedViewModelListener(NeedViewModelListener needViewModelListener) {
        this.needViewModelListener = needViewModelListener;
    }

    public void getAllNeeds(){
        List<KeyValuePair> needList = new ArrayList<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select * from Needs");
                //CommonMethod.log(TAG , "Dump cursor " + DatabaseUtils.dumpCursorToString(cursor));
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            KeyValuePair keyValuePair = new KeyValuePair();
                            keyValuePair.setKey(cursor.getInt(0));
                            keyValuePair.setValue(cursor.getString(1));
                            needList.add(keyValuePair);
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
                        CommonMethod.log(TAG , "Needs completed size " + needList.size());
                        mutableNeedList.setValue(needList);
                        if(needViewModelListener != null){
                            needViewModelListener.complete(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }

    public MutableLiveData<List<KeyValuePair>> setMutableNeedList(){
        return mutableNeedList;
    }
}
