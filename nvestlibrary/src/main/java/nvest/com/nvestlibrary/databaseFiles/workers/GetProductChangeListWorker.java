package nvest.com.nvestlibrary.databaseFiles.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.connectionDetector.ConnectionDetector;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebClient.NvestWebApiClient;
import nvest.com.nvestlibrary.nvestWebClient.TempClient;
import nvest.com.nvestlibrary.nvestWebInterface.NvestWebApiInterface;
import nvest.com.nvestlibrary.nvestWebInterface.TempInterface;
import nvest.com.nvestlibrary.nvestWebModel.ChangeProductDetailsModel;
import nvest.com.nvestlibrary.nvestWebModel.DeletedProductDetails;
import nvest.com.nvestlibrary.nvestWebModel.MasterProductDetailModel;
import nvest.com.nvestlibrary.nvestWebModel.ModifiedProductDetailsModel;

public class GetProductChangeListWorker extends ListenableWorker implements WorkerMessageInterface{

    private static String TAG = GetProductChangeListWorker.class.getSimpleName();
    private SettableFuture<Result> mFuture;
    private NvestWebApiInterface nvestWebApiInterface;
    private TempInterface tempInterface;
    private TempClient tempClient;
    private long masterId;
    private final CompositeDisposable disposables = new CompositeDisposable();
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public GetProductChangeListWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        mFuture = SettableFuture.create();
        CommonMethod.log(TAG , "Worker started ..");
        CommonMethod.log(TAG , "Data " + getInputData().getBoolean("run_status", false));
        nvestWebApiInterface = NvestWebApiClient.getClient().create(NvestWebApiInterface.class);
        tempInterface = TempClient.getClient().create(TempInterface.class);
        if(new ConnectionDetector(getApplicationContext()).isNetworkConnectionAvailable()){
            getBackgroundExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //hitServerForChangedProductList();
                    hitServerForGettingMasterData();
                }
            });
        }
        else {
            sendFailureMessage("No internet connection");
        }
        return mFuture;
    }

    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message + " Time " + getCurrentTime())
                .setSmallIcon(R.drawable.ic_action_home);

        notificationManager.notify(1, notification.build());
    }

    private void hitServerForChangedProductList(){
        CommonMethod.log(TAG , "Get change product details " + getCurrentDate());
        disposables.add(
                nvestWebApiInterface.getChangeProductDetails(getCurrentDate()).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ChangeProductDetailsModel>() {
                    @Override
                    public void onSuccess(ChangeProductDetailsModel changeProductDetailsModel) {
                        CommonMethod.log(TAG , "On success");
                        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(getApplicationContext());
                        masterId = nvestAssetDatabaseAccess.insertIntoChangedProductLogmaster("GetChangedProductList",getCurrentTime(), getCurrentTime(),"false");
                        CommonMethod.log(TAG , "Received id " + masterId);
                        if(changeProductDetailsModel.getStatus()){
                            for (ModifiedProductDetailsModel modifiedProductDetailsModel: changeProductDetailsModel.getModified()){
                                String unformattedDate = modifiedProductDetailsModel.getLastModify();
                                Matcher m = Pattern.compile("\\((.*?)\\)").matcher(unformattedDate);
                                String[] dateSplit = null;
                                while(m.find()) {
                                    //System.out.println(m.group(1));
                                    //CommonMethod.log(TAG , "Extracted " + m.group(1));
                                    dateSplit = m.group(1).split("-");
                                    for (String d : dateSplit) {
                                        CommonMethod.log(TAG , "Split " + d);
                                    }
                                    //CommonMethod.log(TAG , "Date Split " + dateSplit[0]);
                                }
                                CommonMethod.log(TAG , "Product id " + modifiedProductDetailsModel.getProductId());

                                String formattedDate = convertToTimestamp(dateSplit[0]);
                                //insertIntoChangedProductDetails(String actionType , String masterid,String productid,String filetype,String isRateMaster, String toBeDownloaded,String tobeExecuted, String lastModifiedAt)
                                nvestAssetDatabaseAccess.insertIntoChangedProductDetails(NvestLibraryConfig.PRODUCT_MODIFIED, String.valueOf(masterId),String.valueOf(modifiedProductDetailsModel.getProductId()),"Input",String.valueOf(modifiedProductDetailsModel.getIsRateMaster()),"0", "0", dateSplit[0], formattedDate);
                            }
                            for (DeletedProductDetails deletedProductDetails: changeProductDetailsModel.getDeleted()){
                                CommonMethod.log(TAG , "To be delete Product id " + deletedProductDetails.getProductId());
                                nvestAssetDatabaseAccess.insertIntoChangedProductDetails(NvestLibraryConfig.PRODUCT_DELETED, String.valueOf(masterId),String.valueOf(deletedProductDetails.getProductId()),"Input","No","0", "0", getCurrentDate(), getCurrentDate());
                            }

                            sendSuccessMessage(NvestLibraryConfig.SUCCESS_MESSAGE);

                        }
                        else {
                            sendFailureMessage("Received status as false");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "On error " + e.toString());
                        sendFailureMessage(e.toString());
                    }
                }));

    }

    public void hitServerForGettingMasterData(){
        CommonMethod.log(TAG , "Getting master data from server");
        disposables.add(
                tempInterface.getMasterProductChangeDetails().
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MasterProductDetailModel>() {
                    @Override
                    public void onSuccess(MasterProductDetailModel masterProductDetailModel) {
                        CommonMethod.log(TAG , "Master data Inside success ");
                        insert(masterProductDetailModel.getQuery());
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "Master data Error occurred " + e.toString() );
                    }
                }));
    }

    private void insert(String line){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    StringBuilder asd = new StringBuilder();
                    CommonMethod.log(TAG , "Input query " + line);
                    String[] querySplit = line.split("\\|");
                    for (String d : querySplit) {
                        CommonMethod.log(TAG , "Line " + d);
                        if(d != null && !d.isEmpty()){
                            NvestAssetDatabaseAccess.getSingletonInstance().bulkInsert(d);
                        }
                        //queryList.add(d);
                    }
                }
                catch (Exception e){
                    CommonMethod.log(TAG , "Error occurred in inserting " + e.toString());
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
                        CommonMethod.log(TAG,"insert queries are complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "Error occurred while inserting " + e.toString());
                    }
                });
    }


    public String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public void deleteQuery(){
        NvestAssetDatabaseAccess nvestAssetDatabaseAccess = NvestAssetDatabaseAccess.getInstance(getApplicationContext());
        nvestAssetDatabaseAccess.open();
        List<String> deleteTableList = new ArrayList<>();
        deleteTableList.add("AgeMaster");
        deleteTableList.add("AllocCharge");
        for (String deleteTableName : deleteTableList){
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Delete From "+deleteTableName+" where Productid = 1011");
            if(cursor != null){
                CommonMethod.log(TAG , "Delete successful");
            }
        }
    }

    public String getCurrentDate(){
        Calendar now = Calendar.getInstance();
        CommonMethod.log(TAG , "Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(now.getTime());
        CommonMethod.log(TAG , "Month name " + month_name);
        now = Calendar.getInstance();
        now.add(Calendar.DATE,10);
        now.add(Calendar.YEAR, 0);
        String requiredDate =  now.get(Calendar.DATE) + "-" + month_name.toUpperCase() + "-" +now.get(Calendar.YEAR);
        CommonMethod.log(TAG , "date Required : " +  now.get(Calendar.DATE) + "-" + month_name.toUpperCase() + "-" +now.get(Calendar.YEAR));
        return requiredDate;
    }

    private String convertToTimestamp(String timePassed){
        long unixSeconds = Long.parseLong(timePassed);
// convert seconds to milliseconds|1549483800083 , 1372339860
        //Date date = new java.util.Date(unixSeconds*1000L);
        Date date = new java.util.Date(unixSeconds);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date).toUpperCase();
        CommonMethod.log(TAG  , "Java Formatted " + formattedDate);
        return formattedDate;
    }

    @Override
    public void sendSuccessMessage(String message) {
        disposables.clear();
        Data outputData = new Data.Builder().putLong("id_received" , masterId).putString("message", message).build();
        CommonMethod.log(TAG , "Setting result to success");
        mFuture.set(Result.success(outputData));
    }

    @Override
    public void sendFailureMessage(String message) {
        disposables.clear();
        Data outputData = new Data.Builder().putLong("id_received" , -1).putString("message",message).build();
        CommonMethod.log(TAG , "Setting result to failure");
        mFuture.set(Result.failure(outputData));
    }
}
