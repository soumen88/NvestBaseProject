package nvest.com.nvestlibrary.databaseFiles.workers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.TimingLogger;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;

import nvest.com.nvestlibrary.BuildConfig;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public class DownloadDataWorker extends ListenableWorker {
    private static String TAG = DownloadDataWorker.class.getSimpleName();
    private SettableFuture<Result> mFuture;
    private String productId;
    private boolean runStatus;
    private TimingLogger timings = new TimingLogger("MyTag", "MyMethodName");
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public DownloadDataWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        CommonMethod.log(TAG  , "Inside start work request");
        mFuture = SettableFuture.create();
        runStatus = getInputData().getBoolean("run_status", false);
        productId = getInputData().getString(NvestLibraryConfig.PRODUCT_ID_ANNOTATION);

        //getProductIdFromChangedProductDetailsTable();
        if(runStatus){
            getBackgroundExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    getApplicationContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    String url = BuildConfig.BASE_URL+ "GetDataInZip?ProductId="+productId;
                    downloadData(url);
                }
            });
        }
        return mFuture;
    }

    private void getProductIdFromChangedProductDetailsTable(){
        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(getApplicationContext());
        Cursor cursor = nvestAssetDatabaseAccess.ExecuteQuery("select * from changedProductdetails where download = 0 and executed = 'false' LIMIT 1");
        if(cursor!= null && cursor.getCount() > 0){
            cursor.moveToFirst();
            CommonMethod.log(TAG , "ProductId " + cursor.getString(2) + " Cursor count " + cursor.getCount());
            productId = cursor.getString(2);
            if(runStatus){
                getBackgroundExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getApplicationContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        String url = BuildConfig.BASE_URL+ "GetDataInZip?ProductId="+productId;
                        downloadData(url);
                    }
                });
            }
        }
    }


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            CommonMethod.log(TAG , "Id received " + id);
            if(id > 0){
                Data outputData = new Data.Builder().putLong("id_received" , id).build();
                mFuture.set(Result.success(outputData));
            }
            else {
                Data outputData = new Data.Builder().putLong("id_received" , id).build();
                mFuture.set(Result.failure(outputData));
            }

        }
    };

    public void downloadData(String url){
        CommonMethod.createTempFolder();
        if(CommonMethod.deleteTempfilesIfPresent()){
            CommonMethod.log(TAG , "Delete temp file successfull");
        }
        CommonMethod.log(TAG , "Download manager started " + url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription("Some description");
        request.setTitle("Some title");
        request.setAllowedOverMetered(true);// Set if download is allowed on Mobile network
        request.setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, NvestLibraryConfig.NVEST_TEMP_FOLDER_NAME+"/premium-"+productId+".txt");
        final DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);

                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    CommonMethod.log(TAG , "Download progress " + dl_progress);
                    cursor.close();
                }

            }
        }).start();
    }
}
