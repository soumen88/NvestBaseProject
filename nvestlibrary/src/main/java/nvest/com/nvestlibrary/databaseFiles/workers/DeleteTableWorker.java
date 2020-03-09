package nvest.com.nvestlibrary.databaseFiles.workers;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public class DeleteTableWorker extends ListenableWorker {

    private static String TAG = DeleteTableWorker.class.getSimpleName();
    private SettableFuture<Result> mFuture;
    private ArrayList<String> deleteProductList;
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public DeleteTableWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        mFuture = SettableFuture.create();
        deleteProductList = new ArrayList<>();
        deleteProductList.addAll(CommonMethod.getDeleteProductList());
        deleteAllTables();
        return mFuture;
    }

    public void deleteAllTables(){
        CommonMethod.log(TAG , "Delete table list size " + deleteProductList.size());
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (int i=0;i<deleteProductList.size();i++){
                    CommonMethod.log(TAG,"list" +deleteProductList.get(i));
                    String Query = "Delete from  "+deleteProductList.get(i);
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    CommonMethod.log(TAG , " cursor count " + cursor.getCount());
                }

            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        CommonMethod.log(TAG, "Inside on subscribe ");
                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG,"Delete table complete");
                        mFuture.set(Result.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFuture.set(Result.failure());
                    }
                });
    }

}
