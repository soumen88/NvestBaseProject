package nvest.com.nvestlibrary.nvestsync;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nvest.com.nvestlibrary.BuildConfig;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.base.BaseActivity;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.commonMethod.RequestPermissionHandler;
import nvest.com.nvestlibrary.commonMethod.SyncHandlerFactory;
import nvest.com.nvestlibrary.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;
import nvest.com.nvestlibrary.nvestWebInterface.NvestWebApiInterface;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;

public class SyncActivity extends BaseActivity implements SyncHandlerFactory.SyncListener {
    private static String TAG = SyncActivity.class.getSimpleName();
    private SyncPresenter syncPresenter;
    private ProgressDialog progressDialog;
    private TextView sync;
    private NvestWebApiInterface nvestWebApiInterface;
    private ArrayList<MortalityChargeRoom> mortalityChargeRoomArrayList;
    private ArrayList<FundStrategyMasterRoom> fundStrategyMasterRoomArrayList;
    private LiveData<List<MortalityChargeRoom>> mortalityLiveData;
    private Button checkStatus, dummyData, killAllWorker, removeAllDownloads, deleteDataFromTables,syncSingleProduct, sendCustomMessage ;
    private List<String> queryList;
    private RequestPermissionHandler mRequestPermissionHandler;
    private Map<String, List<String>> tempSetMap = new HashMap<>();
    private EditText productIdToBeSynced;
    private SyncHandlerFactory syncHandlerFactory;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        CommonMethod.log(TAG , "On create called...");
        syncPresenter = new SyncPresenter();
        syncPresenter.onAttach(this);
        mRequestPermissionHandler = new RequestPermissionHandler();
        accessNvestAssetDatabase();
        accessRoomDatabase();
        init();
        handlePermissions();
        CommonMethod.log(TAG , "Value from debug " + BuildConfig.LIBRARY_DEBUGABLE);
    }

    private void temp(){
        tempSetMap = new HashMap<>();
        List<String> tempList = new ArrayList<>();
        for (int i = 0 ; i < 5 ; i++){
            String abc =  "This is the value " + i;
            tempList.add(abc);

        }
        tempSetMap.put("Key", tempList);
        GenericDTO.addListAttribute("Key", tempList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonMethod.log(TAG , "Inside on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonMethod.log(TAG , "Inside on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonMethod.log(TAG , "Inside on stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonMethod.log(TAG , "Inside on destroy");
        //WorkManager.getInstance().cancelAllWork();

    }

    private void handlePermissions() {
        try {

            mRequestPermissionHandler.requestPermission(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                @Override
                public void onSuccess() {
                    CommonMethod.log(TAG, "Permission granted");

                }

                @Override
                public void onFailed() {
                    CommonMethod.log(TAG, "Permission not granted");

                    //Toast.makeText(MainActivity.this, "request permission failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            CommonMethod.log(TAG , "Exception in permission " + e.toString());
        }
    }
    @Override
    public void init() {
        progressDialog = new ProgressDialog(this);
        sync = (TextView) findViewById(R.id.sync);
        checkStatus = (Button) findViewById(R.id.checkStatus);
        dummyData = (Button) findViewById(R.id.insertDummyData);
        killAllWorker = (Button) findViewById(R.id.killAllWorker);
        deleteDataFromTables = (Button) findViewById(R.id.deleteDataFromTables);
        removeAllDownloads = (Button) findViewById(R.id.removeAllDownloads);
        productIdToBeSynced = (EditText) findViewById(R.id.productIdToBeSynced);
        syncSingleProduct = (Button) findViewById(R.id.syncSingleProduct);
        sendCustomMessage = (Button) findViewById(R.id.sendCustomMessage);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        syncHandlerFactory = new SyncHandlerFactory(this, "1011");
        syncHandlerFactory.setSyncListener(this);
        dummyData.setOnClickListener(v->{syncHandlerFactory.insertDummyData();});
        syncHandlerFactory.getStatusByTag();
        sync.setOnClickListener(view->{
            //syncHandlerFactory.startRequest(String.valueOf(NvestLibraryConfig.WorkerName.DELETE_TABLES_WORKER));
            syncHandlerFactory.startProductHandlerWorker(true);
            //IAmTryingToInsertData();
            //NvestAssetDatabaseAccess.getSingletonInstance().bulkInsert();
            //temp();
        });

        killAllWorker.setOnClickListener(view -> {
            CommonMethod.log(TAG , "Kill all workers started");
            ///WorkManager.getInstance().cancelAllWork();
            //printTempList();
            syncHandlerFactory.killWorkByTag();
        });

        removeAllDownloads.setOnClickListener(view->{
            CommonMethod.log(TAG , "Remove downloads clicked");
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus (DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PENDING|DownloadManager.STATUS_RUNNING);
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Cursor c = dm.query(query);
            while(c.moveToNext()) {
                CommonMethod.log(TAG , "Removeing from download manager");
                dm.remove(c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
            }

        });

        deleteDataFromTables.setOnClickListener(view -> {

            syncHandlerFactory.deleteAllTablesWorker();
        });

        mortalityChargeRoomArrayList = new ArrayList<>();
        mortalityLiveData = new MutableLiveData<>();
        fundStrategyMasterRoomArrayList = new ArrayList<>();
        queryList = new ArrayList<>();
        checkStatus.setOnClickListener(view->{
            syncHandlerFactory.check();
        });

        syncSingleProduct.setOnClickListener(view -> {
            if(!productIdToBeSynced.getText().toString().isEmpty()){
                syncHandlerFactory.syncSingleProductWorker(productIdToBeSynced.getText().toString());
            }
            else {
                Toast.makeText(getApplicationContext(),"Product Id is missing", Toast.LENGTH_LONG).show();
            }
        });

        sendCustomMessage.setOnClickListener(v->{
            Intent intent = new Intent(NvestLibraryConfig.CUSTOM_BROADCAST_ACTION);
            sendBroadcast(intent);
            temp1();
        });
        /*nvestSyncViewModel.getOutputWorkInfo().observe(this,listOfWorkInfo->{
            CommonMethod.log(TAG, "Inside observer");
            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }
            WorkInfo workInfo = listOfWorkInfo.get(0);

            boolean finished = workInfo.getState().isFinished();
            CommonMethod.log(TAG , "Finish status " + finished);
            if (!finished) {
                showWorkInProgress();
            } else {
                showWorkFinished();
            }


        });*/



    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    @Override
    public void showWorkInProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    @Override
    public void showWorkFinished() {
        progressBar.setVisibility(View.GONE);

    }


    private void temp1(){
        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey(34);
        keyValuePair.setValue("Message to be sent");
        Gson gson = new Gson();
        CommonMethod.log(TAG , "Gson " + gson.toJson(keyValuePair));
    }

    @Override
    public void showLoading() {
        progressDialog.setTitle("Nvest");
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    public void hideLoading() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
