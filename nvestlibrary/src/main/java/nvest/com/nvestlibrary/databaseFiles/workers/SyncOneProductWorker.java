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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.BuildConfig;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebClient.NvestWebApiClient;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.ModifiedProductDetailsModel;
import nvest.com.nvestlibrary.nvestcheck.NvestCheckActivity;


public class SyncOneProductWorker  extends ListenableWorker {

    private static String TAG = SyncOneProductWorker.class.getSimpleName();
    private SettableFuture<Result> mFuture;
    private int startId;
    private int deletestartId;
    private String productId;
    private String deleteProductId;
    private LinkedHashMap<Integer, String> downloadProductList;
    private LinkedHashMap<Integer, ModifiedProductDetailsModel> modifiedProductLinkedHashMap;
    private LinkedHashMap<Integer, String> deleteApiProductList;
    private ArrayList<String> deleteProductList = new ArrayList<>();;
    private ArrayList<String> deleteApiProductStringList = new ArrayList<>();;
    private boolean isPremiumRate = false;
    private long masterId;
    private SyncOneListener syncOneListener;
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public SyncOneProductWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    public interface SyncOneListener{
        void syncOne(int id);
    }

    public SyncOneListener getSyncOneListener() {
        return syncOneListener;
    }

    public void setSyncOneListener(SyncOneListener syncOneListener) {
        this.syncOneListener = syncOneListener;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        mFuture = SettableFuture.create();
        deleteProductList.addAll(CommonMethod.getDeleteProductList());
        CommonMethod.log(TAG , "Product Handler worker started");
        downloadProductList = new LinkedHashMap<>();
        deleteApiProductList = new LinkedHashMap<>();
        modifiedProductLinkedHashMap = new LinkedHashMap<>();
        productId = getInputData().getString("productId");
        masterId = getInputData().getLong("masterId",0);
        CommonMethod.log(TAG , "Product id " + productId);
        CommonMethod.deleteTempfilesIfPresent();
        NvestAssetDatabaseAccess.getInstance(getApplicationContext());
        getProductIdFromChangedProductDetailsTable();
        //getDeletedProductIdFromChangedProductDetailsTable();
        //temp();
        return mFuture;
    }

    private void getDeletedProductIdFromChangedProductDetailsTable(){
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select * from ChangedProductDetails where productid = '"+productId+"' and ActionType = '"+NvestLibraryConfig.PRODUCT_DELETED +"' and ToBeExecuted = '0'");
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select * from changedProductdetails where id = '42'");
        if(cursor!= null && cursor.getCount() > 0){
            cursor.moveToFirst();
            CommonMethod.log(TAG , "ProductId " + cursor.getString(3) + " Cursor count " + cursor.getCount());
            do {
                deleteApiProductList.put(cursor.getInt(0), cursor.getString(3));

            }while (cursor.moveToNext());
        }
        CommonMethod.log(TAG , "Delete Api  product list size " + deleteApiProductList.size());
        deleteTablesReceivedFromApi();
    }


    private void getProductIdFromChangedProductDetailsTable(){

        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select * from ChangedProductDetails where ProductId = '"+productId+"' and ActionType = '"+NvestLibraryConfig.PRODUCT_MODIFIED +"' and ToBeDownloaded = 0 and ToBeExecuted = '0' and ProductId != 1018");
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select * from changedProductdetails where id = '42'");
        if(cursor!= null && cursor.getCount() > 0){
            cursor.moveToFirst();
            CommonMethod.log(TAG , "ProductId " + cursor.getString(3) + " Cursor count " + cursor.getCount());
            do {
                downloadProductList.put(cursor.getInt(0), cursor.getString(3));
                ModifiedProductDetailsModel modifiedProductDetailsModel = new ModifiedProductDetailsModel();
                modifiedProductDetailsModel.setProductId(cursor.getInt(3));
                modifiedProductDetailsModel.setIsRateMaster(cursor.getInt(5)> 0);
                modifiedProductDetailsModel.setLastModify(cursor.getString(9));
                modifiedProductLinkedHashMap.put(cursor.getInt(0),modifiedProductDetailsModel );
            }while (cursor.moveToNext());
        }
        CommonMethod.log(TAG , "Download product list size " + downloadProductList.size());
        CommonMethod.log(TAG , "Modified product list size " + modifiedProductLinkedHashMap.size());
        startProcess();
    }

    private void startDeleteProcess(){
        Map.Entry<Integer,String> entry = deleteApiProductList.entrySet().iterator().next();
        deletestartId = entry.getKey();
        deleteProductId = entry.getValue();
        CommonMethod.log(TAG , "Found " + deletestartId + " Product id " + deleteProductId);
    }


    private void startProcess(){

        Map.Entry<Integer,ModifiedProductDetailsModel> entry = modifiedProductLinkedHashMap.entrySet().iterator().next();
        startId = entry.getKey();
        productId = String.valueOf(entry.getValue().getProductId());
        CommonMethod.log(TAG , "Found " + startId + " Product id " + productId);
        getApplicationContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if(entry.getValue().getIsRateMasterBoolean()){
            isPremiumRate = true;
            downloadPremiumRateData();
        }
        else {
            isPremiumRate = false;
            downloadData();
        }

        //updateChangeProductDetails(entry.getKey(), entry.getValue());
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long downloadid = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            CommonMethod.log(TAG , "Id received " + downloadid);
            if(downloadid > 0){

                Data outputData = new Data.Builder().putLong("id_received" , downloadid).build();
                /*do {
                    startId++;
                    startNextProcess(startId);
                }while (downloadProductList.containsKey(startId));*/
                startUnzippingProcess();
                //sendSuccessMessage();
            }
            else {
                Data outputData = new Data.Builder().putLong("id_received" , downloadid).build();
                //mFuture.set(Result.failure(outputData));
            }


        }
    };

    private void startNextProcess(int id){
        CommonMethod.log(TAG , "Id passed " + id);
        ModifiedProductDetailsModel modifiedProductDetailsModel = modifiedProductLinkedHashMap.get(id);
        productId = String.valueOf(modifiedProductDetailsModel.getProductId());
        CommonMethod.log(TAG , "Found " + startId + " Product id " + productId);
        getApplicationContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if(modifiedProductDetailsModel.getIsRateMasterBoolean()){
            isPremiumRate = true;
            downloadPremiumRateData();
        }
        else {
            isPremiumRate = false;
            downloadData();
        }
        /*for (Map.Entry<Integer, ModifiedProductDetailsModel> entry: modifiedProductLinkedHashMap.entrySet()){
            CommonMethod.log(TAG , "Entry key " + entry.getKey());
            CommonMethod.log(TAG , "Entry Value " + entry.getValue().getProductId()
            );
        }*/
    }

    private void loop(){
        CommonMethod.log(TAG , "Looping started...");
        startId++;
        if(modifiedProductLinkedHashMap.containsKey(startId)){
            startNextProcess(startId);
        }
        updateChangeProductLogMaster(masterId);
        deleteChangeProductDetails();
        sendSuccessMessage();
    }


    public void downloadData(){
        String url = BuildConfig.BASE_URL_ZIP+ "DownloadZip.aspx?ProductId="+ productId + "&Data=ProductData";
        CommonMethod.createTempFolder();
        /*if(CommonMethod.deleteTempfilesIfPresent()){
            CommonMethod.log(TAG , "Delete temp file successfull");
        }*/
        CommonMethod.log(TAG , "Download manager started " + url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription("Some description");
        request.setTitle("Downloading " + productId);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverMetered(true);// Set if download is allowed on Mobile network
        request.setAllowedOverRoaming(false);// Set if download is allowed on roaming network

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            CommonMethod.log(TAG , "Hiding notification");
            if(CommonMethod.isLibraryDebuggable()){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            else {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

        }
        else {
            CommonMethod.log(TAG , "Not Hiding notification");
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, NvestLibraryConfig.NVEST_TEMP_FOLDER_NAME+ "/" +productId+"data.zip");
        final DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        /*new Thread(new Runnable() {

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
        }).start();*/
    }

    public void downloadPremiumRateData(){
        String url = BuildConfig.BASE_URL_ZIP+ "DownloadZip.aspx?ProductId=" + productId + "&Data=Premiumrate";
        CommonMethod.createTempFolder();
        /*if(CommonMethod.deleteTempfilesIfPresent()){
            CommonMethod.log(TAG , "Delete temp file successfull");
        }*/
        CommonMethod.log(TAG , "Download manager started " + url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription("Some description");
        request.setTitle("Downloading " + productId);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverMetered(true);// Set if download is allowed on Mobile network
        request.setAllowedOverRoaming(false);// Set if download is allowed on roaming network

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            CommonMethod.log(TAG , "Hiding notification");
            if(CommonMethod.isLibraryDebuggable()){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            else {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

        }
        else {
            CommonMethod.log(TAG , "Not Hiding notification");
        }
        //Environment.DIRECTORY_DOWNLOADS, NvestLibraryConfig.NVEST_TEMP_FOLDER_NAME+ "/" +productId+"data.zip"
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, NvestLibraryConfig.NVEST_TEMP_FOLDER_NAME+ "/"+productId+"PremiumRT.zip");
        final DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        /*new Thread(new Runnable() {

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
        }).start();*/
    }


    private void startUnzippingProcess(){
        try {
            String sourcepath = null;
            if(isPremiumRate){
                sourcepath = NvestLibraryConfig.STORAGE_DIRECTORY_PATH+"/"+productId+"PremiumRT.zip";
            }
            else {
                sourcepath = NvestLibraryConfig.STORAGE_DIRECTORY_PATH+"/"+productId+"data.zip";

            }
            String destinationFilePath = NvestLibraryConfig.STORAGE_DIRECTORY_PATH + "/" ;
            String unZipFilePath = unzip(sourcepath, destinationFilePath);
            if(unZipFilePath!= null){
                String inputJsonString = loadJSONFromFile(unZipFilePath);
                //CommonMethod.log(TAG , "Input json " + inputJsonString);
                if(inputJsonString != null){
                    if(isPremiumRate){
                        deletePremiumRateTable(inputJsonString);
                    }
                    else {
                        deleteTables(inputJsonString);
                    }
                }
                //sendSuccessMessage();
            }

        } catch (Exception e) {
            CommonMethod.log(TAG , "Exception " + e.toString());
        }
    }

    private void temp(){
        try {
            String destinationFilePath = NvestLibraryConfig.STORAGE_DIRECTORY_PATH + "/1018PremiumRT.txt";
            File file = new File(destinationFilePath);
            if(file.exists()){
                /*CommonMethod.log(TAG , "File found");
                String encoding = "UTF-8";
                int maxlines = 100;
                BufferedReader reader = null;
                BufferedWriter writer = null;

                try {
                    CommonMethod.log(TAG  , "Inside try");
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(destinationFilePath), encoding));
                    int count = 0;
                    for (String line; (line = reader.readLine()) != null;) {
                        if (count++ % maxlines == 0) {
                            //close(writer);
                            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(NvestLibraryConfig.STORAGE_DIRECTORY_PATH + "/smallfile" + (count / maxlines) + ".txt"), encoding));
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                }
                catch (Exception e){
                    sendSuccessMessage();
                    CommonMethod.log(TAG , "Exception inside " + e.toString());
                }
                finally {
                    sendSuccessMessage();
                    CommonMethod.log(TAG , "Inside finally");
                    //close(writer);
                    //close(reader);
                }*/
            }
            else {
                CommonMethod.log(TAG , "File not found");
            }
            //sendSuccessMessage();
        }
        catch (Exception e){
            //sendSuccessMessage();
            CommonMethod.log(TAG , "Exception in temp " + e.toString());
        }



    }

    public String loadJSONFromFile(String filePathReceived) {
        File file = new File(filePathReceived);
        /*if(isPremiumRate){
            file = new File(NvestLibraryConfig.STORAGE_DIRECTORY_PATH, "premium-"+productId+".txt");
        }
        else {
            file = new File(NvestLibraryConfig.STORAGE_DIRECTORY_PATH, productId+"data.txt");
        }*/

        CommonMethod.log(TAG , "File path " + file.getAbsolutePath());
        if(file.exists()){
            //Read text from file

            StringBuilder text = new StringBuilder();
            try {
                /*BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                line = br.readLine();
                br.close();
                return line;*/

                FileReader in  = new FileReader(file);
                char[] buf = new char[2048];
                int len;
                StringBuilder sbuf = new StringBuilder();
                while ((len = in.read(buf, 0, buf.length)) != -1) {
                    sbuf.append(buf, 0, len);
                }
                in.close();
                String inputjson = sbuf.toString();
                return inputjson;

                /*while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }*/

            }
            catch (IOException e) {
                //You'll need to add proper error handling here
                CommonMethod.log(TAG , "Io exception occurred " + e.toString());
            }
            catch (Exception e){
                CommonMethod.log(TAG , "Exception occurred in loading json " + e.toString());
            }
        }
        else {
            CommonMethod.log(TAG , "Text  File does not exist");
        }
        return null;
    }

    private void insert(String line){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    StringBuilder asd = new StringBuilder();
                    CommonMethod.log(TAG , "Input query " + line);
                    String[] dateSplit = line.split("\\|");
                    CommonMethod.log(TAG , "Date split length " + dateSplit.length);
                    for (String d : dateSplit) {
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
                        updateChangeProductDetails(startId , productId);
                        CommonMethod.deleteTempfilesIfPresent();

                        KeyValuePair keyValuePair = new KeyValuePair();
                        keyValuePair.setKey(34);
                        keyValuePair.setValue(productId);
                        Gson gson = new Gson();
                        Data outputData = new Data.Builder().putString("product_under_sync" , gson.toJson(keyValuePair)).build();
                        mFuture.set(Result.failure(outputData));
                        loop();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "Error occurred while inserting " + e.toString());
                    }
                });
    }

    private String unzip(String zipFilePath, String destDir) {
        File zipFile = new File(zipFilePath);
        CommonMethod.log(TAG , "Source path passed " + zipFile.getAbsolutePath());
        if(!zipFile.exists()){
            CommonMethod.log(TAG , "Returning because source file does not exist " + isPremiumRate);
            return null;
        }
        String FilePath = null;
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                CommonMethod.log(TAG , "Unzipping to "+newFile.getAbsolutePath());
                FilePath = newFile.getAbsolutePath();
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }

            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            return FilePath;

        } catch (IOException e) {
            e.printStackTrace();
            CommonMethod.log(TAG , "IO Exception while unzipping " + e.toString());
        }
        return null;
    }

    private String usingStringBuilder(byte [] data){
        if(CommonMethod.createTempFolder()){
            String path = NvestLibraryConfig.STORAGE_DIRECTORY_PATH+ "/output-"+productId+".zip";
            CommonMethod.log(TAG , "Zip file path " + path);
            File file = new File(path);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                // Writes bytes from the specified byte array to this file output stream
                fos.write(data);
            }
            catch (FileNotFoundException e) {
                CommonMethod.log(TAG , "File not found " + e.toString());
            }
            catch (IOException ioe) {
                CommonMethod.log(TAG , "Exception while writing file " + ioe.toString());
            }
            finally {
                // close the streams using close method
                try {
                    if (fos != null) {
                        fos.close();
                        return path;
                    }
                }
                catch (IOException ioe) {
                    CommonMethod.log(TAG , "Error while closing stream: " + ioe.toString());
                }
            }
        }
        return null;
    }

    public void deleteTables(String inputQueries){
        CommonMethod.log(TAG , "Delete table list size " + deleteProductList.size());
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (int i=0;i<deleteProductList.size();i++){
                    CommonMethod.log(TAG,"list" +deleteProductList.get(i));
                    String Query = "Delete from  "+deleteProductList.get(i)+" where productid = " + productId;
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
                        insert(inputQueries);
                        //extractZipFile(zipfilePath);
                        /*Intent i = new Intent(getApplicationContext(), NvestCheckActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ContextCompat.startActivity(getApplicationContext(),i,null);*/
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void sayHello(){
        CommonMethod.log(TAG , "Say hello");
    }

    public void deletePremiumRateTable(String inputQueries){
        CommonMethod.log(TAG , "Delete table list size " + deleteProductList.size());
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String Query = "Delete from  PremiumRates where productid = " + productId;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                CommonMethod.log(TAG , " cursor count " + cursor.getCount());

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
                        insert(inputQueries);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    public void deleteTablesReceivedFromApi(){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (Map.Entry<Integer, String> entry: deleteApiProductList.entrySet()){
                    CommonMethod.log(TAG , "Product id " + entry.getValue() );
                    for (int i=0;i<deleteProductList.size();i++){
                        CommonMethod.log(TAG,"list" +deleteProductList.get(i));
                        String Query = "Delete from  "+ deleteProductList.get(i)+ " where productid = " + entry.getValue();
                        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        CommonMethod.log(TAG , " cursor count " + cursor.getCount());
                    }
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
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void updateChangeProductDetails(int id , String productId){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("UPDATE ChangedProductDetails SET ToBeDownloaded = '1' ,  ToBeExecuted = '1' WHERE Id = " + id);

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
                        CommonMethod.log(TAG , "Update process complete for " + id + " in product id " + productId);
                        /*if((downloadProductList.size()+1) > startId){
                            startNextProcess(startId++);
                        }*/

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
        CommonMethod.log(TAG , "Updating " + productId + " At id " + id);

    }


    public void updateChangeProductLogMaster(long masterid){
        CommonMethod.log(TAG , "Updating change log master id " + masterid);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Update ChangedProductLogmaster SET DateTimeFinish = '" + CommonMethod.getCurrentDate()+"' , Status = 'finished' Where masterid = " + masterid );
        if(cursor != null){
            CommonMethod.log(TAG , "Update is successfull");
        }
    }

    public void deleteChangeProductDetails(){
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Delete from ChangedProductDetails");
        if(cursor != null){
            CommonMethod.log(TAG , "delete is successfull");
        }
    }

    public void sendSuccessMessage(){
        mFuture.set(Result.success());
    }

    public void sendFailureMessage(){
        mFuture.set(Result.failure());
    }


}
