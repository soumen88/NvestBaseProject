package nvest.com.nvestlibrary.commonMethod;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import nvest.com.nvestlibrary.databaseFiles.workers.DeleteTableWorker;
import nvest.com.nvestlibrary.databaseFiles.workers.DownloadDataWorker;
import nvest.com.nvestlibrary.databaseFiles.workers.ExecuteQueryWorker;
import nvest.com.nvestlibrary.databaseFiles.workers.GetProductChangeListWorker;
import nvest.com.nvestlibrary.databaseFiles.workers.ProductHandlerWorker;
import nvest.com.nvestlibrary.databaseFiles.workers.SyncOneProductWorker;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestsync.SyncActivity;

public class SyncHandlerFactory {
    private static String TAG = SyncHandlerFactory.class.getSimpleName();
    private Context context;
    private String productId;
    private OneTimeWorkRequest insertRequest = null;
    private OneTimeWorkRequest productHandlerWorkRequest = null;
    private SyncListener syncListener;
    public SyncHandlerFactory(Context context, String productId) {
        CommonMethod.log(TAG , "Handler started");
        this.context = context;
        this.productId = productId;
    }

    public interface SyncListener{
        void showWorkInProgress();
        void showWorkFinished();
    }

    public SyncListener getSyncListener() {
        return syncListener;
    }

    public void setSyncListener(SyncListener syncListener) {
        this.syncListener = syncListener;
    }

    public void startRequest(String workerKey){
        try {
            switch (NvestLibraryConfig.WorkerName.valueOf(workerKey)){
                case PREMIUM_RATES_WORKER:

                    break;
                case OTHER_TABLES_WORKER:

                    break;
                case PRODUCT_TABLES_WORKER:

                    break;
                case DELETE_TABLES_WORKER:

                    //startOneTimeRequest("5");
                    startProductHandlerWorker(true);
                    break;

            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception occurred in switch case " + e.toString());
        }

    }


    public void insertDummyData(){
        insertRequest = new OneTimeWorkRequest.Builder(GetProductChangeListWorker.class).addTag(NvestLibraryConfig.GET_PRODUCT_CHANGE_LIST_TAG).build();
        CommonMethod.log(TAG , "Insert request before " + insertRequest.getId());
        WorkManager.getInstance().enqueue(insertRequest);
        if(syncListener!= null){
            syncListener.showWorkInProgress();
        }
        WorkManager.getInstance().getWorkInfoByIdLiveData(insertRequest.getId()).observe((SyncActivity)context, workStatus ->  {
            //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
            if(workStatus != null){
                CommonMethod.log(TAG , "Periodic worker completed " +workStatus.getState().isFinished() );
                CommonMethod.log(TAG , "Periodic worker state  " +workStatus.getState() );
                if(workStatus.getState().equals(WorkInfo.State.SUCCEEDED)){
                    Toast.makeText(context, "Insert Complete" , Toast.LENGTH_SHORT).show();
                    WorkManager.getInstance().cancelWorkById(insertRequest.getId());
                    WorkManager.getInstance().cancelAllWorkByTag(NvestLibraryConfig.GET_PRODUCT_CHANGE_LIST_TAG);
                    CommonMethod.log(TAG , "Change log master id " + workStatus.getOutputData().getLong("id_received",0));
                    if(syncListener!= null){
                        syncListener.showWorkFinished();
                    }

                }
                else if(workStatus.getState().equals(WorkInfo.State.FAILED)){
                    WorkManager.getInstance().cancelWorkById(insertRequest.getId());
                    WorkManager.getInstance().cancelAllWorkByTag(NvestLibraryConfig.GET_PRODUCT_CHANGE_LIST_TAG);
                    CommonMethod.log(TAG , "Failure message " + workStatus.getOutputData().getString("message"));
                }
            }

        });
        CommonMethod.log(TAG , "Cancelling work by id " + insertRequest.getId());

        //WorkManager.getInstance().cancelAllWork();
    }

    public void startTempRequest(String productId, boolean runStatus, Context contextPassed){
        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(contextPassed);
        CommonMethod.log(TAG , "I have been called inside a worker..." + productId);
        //nvestAssetDatabaseAccess.ExecuteQuery("UPDATE changedProductdetails SET download = '1' WHERE ProductId = " + productId);
        //startOneTimeRequest(productId);
    }

    public void startProductHandlerWorker(boolean runStatus){
        long masterId = getLastSyncStatus();
        if(masterId != -1 && masterId != 0){
            Data inputData = new Data.Builder()
                    .putBoolean("run_status" , runStatus)
                    .putLong("masterId" , masterId)
                    .build();
            productHandlerWorkRequest = new OneTimeWorkRequest.Builder(ProductHandlerWorker.class).addTag(NvestLibraryConfig.PRODUCT_HANDLER_TAG).setInputData(inputData).build();
            WorkManager.getInstance().enqueue(productHandlerWorkRequest);
            //WorkManager.getInstance().enqueueUniqueWork("productHandler",Ex);
            WorkManager.getInstance().getWorkInfoByIdLiveData(productHandlerWorkRequest.getId()).observe((SyncActivity)context, workStatus ->  {
                //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
                if(workStatus != null){
                    if(workStatus.getState().equals(WorkInfo.State.SUCCEEDED)){
                        CommonMethod.log(TAG , "Cancelling workers by tag");
                        WorkManager.getInstance().cancelWorkById(productHandlerWorkRequest.getId());
                        if(syncListener != null){
                            syncListener.showWorkInProgress();
                        }
                    }
                    else if(workStatus.getState().equals(WorkInfo.State.FAILED)){
                        if(syncListener != null){
                            syncListener.showWorkFinished();
                        }
                    }
                    else if(workStatus.getState().equals(WorkInfo.State.RUNNING)){
                        CommonMethod.log(TAG , "Running...");
                    }
                }

            });

        }

    }

    public void check(){
        SyncOneProductWorker s = new SyncOneProductWorker(context, null);
        s.sayHello();
    }

    public void getStatusByTag(){
        try {

            WorkManager wm = WorkManager.getInstance();
            ListenableFuture<List<WorkInfo>> future = wm.getWorkInfosByTag(NvestLibraryConfig.PRODUCT_HANDLER_TAG);
            List<WorkInfo> list = future.get();
            CommonMethod.log(TAG , "Work info list size " + list.size());
            for(WorkInfo workInfo: list){
                CommonMethod.log(TAG , "Work info status  " + workInfo.getState() + " work info id " + workInfo.getId());

                if(workInfo.getState().equals(WorkInfo.State.RUNNING)){
                    syncListener.showWorkInProgress();
                    break;
                }
            }
        }
        catch (ExecutionException e){
            CommonMethod.log(TAG , "Execution exception " + e.toString());
        }
        catch (InterruptedException e){
            CommonMethod.log(TAG , "Interrupted exception " + e.toString());
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in listenable future " + e.toString());
        }
        // start only if no such tasks present
       /* if((list == null) || (list.size() == 0)){
            // shedule the task
            wm.enqueue(work);
        } else {
            // this periodic task has been previously scheduled
        }*/
    }

    public void killWorkByTag(){
        if(insertRequest != null){
            CommonMethod.log(TAG , "Insert request id " + insertRequest.getId());
            WorkManager.getInstance().getWorkInfoByIdLiveData(insertRequest.getId()).observe((SyncActivity)context, workStatus ->  {

                if(workStatus != null){
                    CommonMethod.log(TAG , "Periodic worker completed " +workStatus.getState().isFinished() );
                }

            });
        }
        if(productHandlerWorkRequest != null){
            CommonMethod.log(TAG, "Work status product handler id  " + productHandlerWorkRequest.getId());
            WorkManager.getInstance().cancelWorkById(productHandlerWorkRequest.getId());
            WorkManager.getInstance().getWorkInfoByIdLiveData(productHandlerWorkRequest.getId()).observe((SyncActivity)context, workStatus ->  {
                CommonMethod.log(TAG , "Observing current work state " + workStatus.getState().toString());
                if(workStatus != null){
                    CommonMethod.log(TAG , "Periodic worker completed " +workStatus.getState().isFinished() );
                }

            });
        }

    }

    public void startOneTimeRequest(String productIdPassed){
        Data inputData = new Data.Builder()
                .putString(NvestLibraryConfig.PRODUCT_ID_ANNOTATION, productIdPassed)
                .putBoolean("run_status" , true)
                .build();
        final OneTimeWorkRequest downloadDataWorkRequest = new OneTimeWorkRequest.Builder(DownloadDataWorker.class).addTag("downloaddata").setInputData(inputData).build();
        final OneTimeWorkRequest executeQueryWorkRequest = new OneTimeWorkRequest.Builder(ExecuteQueryWorker.class).setInputData(inputData).build();
        WorkManager.getInstance()
                .beginWith(downloadDataWorkRequest)
                .then(executeQueryWorkRequest)
                .enqueue();

        /*WorkManager.getInstance()
                .beginWith(downloadDataWorkRequest)
                //.then(executeQueryWorkRequest)
                .enqueue();*/

        /*WorkManager.getInstance().getWorkInfoByIdLiveData(downloadDataWorkRequest.getId()).observe((SyncActivity) context, workStatus ->  {
            //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
            if(workStatus != null){
                CommonMethod.log(TAG , "Download data worker completed " +workStatus.getState().isFinished() );
                //CommonMethod.log(TAG , "Delete tables worker completed ");
                CommonMethod.log(TAG , "Output data " + workStatus.getOutputData().getLong("id_received",0)) ;
            }

        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(executeQueryWorkRequest.getId()).observe((SyncActivity)context, workStatus ->  {
            //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
            if(workStatus != null){
                CommonMethod.log(TAG , "Execute tables worker completed " +workStatus.getState().isFinished() );
                //CommonMethod.log(TAG , "Delete tables worker completed ");
                CommonMethod.log(TAG , "Output data " + workStatus.getOutputData().getInt("result",0)) ;
            }

        });*/

    }

    public void deleteAllTablesWorker(){
        Data inputData = new Data.Builder()
                .putBoolean("run_status" , true)
                .build();
        final OneTimeWorkRequest deleteTableWorkRequest = new OneTimeWorkRequest.Builder(DeleteTableWorker.class).setInputData(inputData).build();
        WorkManager.getInstance().enqueue(deleteTableWorkRequest);
        WorkManager.getInstance().getWorkInfoByIdLiveData(deleteTableWorkRequest.getId()).observe((SyncActivity)context, workStatus ->  {
            //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
            if(workStatus != null){
                CommonMethod.log(TAG , "Delete table worker completed " +workStatus.getState().isFinished() );
                //CommonMethod.log(TAG , "Delete tables worker completed ");
                CommonMethod.log(TAG , "Output data " + workStatus.getOutputData().getLong("id_received",0)) ;
            }

        });

    }

    public void syncSingleProductWorker(String productId){

        long masterId = getLastSyncStatus();
        if(masterId != -1 && masterId != 0){
            Data inputData = new Data.Builder()
                    .putString("productId" , productId)
                    .putLong("masterId" , masterId)
                    .build();
            final OneTimeWorkRequest syncSingleProductWorkRequest = new OneTimeWorkRequest.Builder(SyncOneProductWorker.class).setInputData(inputData).build();
            WorkManager.getInstance().enqueue(syncSingleProductWorkRequest);
            WorkManager.getInstance().getWorkInfoByIdLiveData(syncSingleProductWorkRequest.getId()).observe((SyncActivity)context, workStatus ->  {
                //CommonMethod.log(TAG , "Delete tables worker completed " + workStatus.getState().isFinished());
                if(workStatus != null){
                    CommonMethod.log(TAG , "Delete table worker completed " +workStatus.getState().isFinished() );
                    //CommonMethod.log(TAG , "Delete tables worker completed ");
                    CommonMethod.log(TAG , "Output data " + workStatus.getOutputData().getLong("id_received",0)) ;
                    if(workStatus.getState().isFinished()){
                        CommonMethod.log(TAG , "Cancelling workers by tag");
                        //updateChangeProductLogMaster(1);
                        WorkManager.getInstance().cancelWorkById(syncSingleProductWorkRequest.getId());
                        Toast.makeText(context, "Sync for "+productId+" Complete" , Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }

    }

    public long getLastSyncStatus(){
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select masterid from ChangedProductLogmaster where Status = 'false'  order by masterid DESC limit 1");
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            CommonMethod.log(TAG , "Master id found " + cursor.getLong(0) );
            return cursor.getLong(0);
        }
        else {
            return -1;
        }
    }
}
