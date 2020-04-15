package nvest.com.nvestlibrary.databaseFiles.workers;

import android.content.Context;

import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestWebModel.InputByteStreamReader;

public class ExecuteQueryWorker extends ListenableWorker {

    private static String TAG = ExecuteQueryWorker.class.getSimpleName();
    private SettableFuture<Result> mFuture;
    private String productId;
    private Data outputData;
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public ExecuteQueryWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        mFuture = SettableFuture.create();
        boolean runStatus = getInputData().getBoolean("run_status", false);
        productId = getInputData().getString(NvestLibraryConfig.PRODUCT_ID_ANNOTATION);
        if(runStatus){
            CommonMethod.log(TAG , "Execute worker started...");
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                String inputJsonString = loadJSONFromFile();
                if(inputJsonString != null){
                    InputByteStreamReader inputByteStreamReader = gson.fromJson(inputJsonString, InputByteStreamReader.class);
                    CommonMethod.log(TAG , "Status " + inputByteStreamReader.getStatus());
                    CommonMethod.log(TAG , "Input stream " + inputByteStreamReader.getDataBytes().size());
                    //byte[]  bytes = ArrayUtils.toPrimitive(inputByteStreamReader.getDataBytes().toArray(new Byte[0]));
                    /*byte[]  bytes = new Byte[177];
                    CommonMethod.log(TAG , "Bytes size " + bytes.length);
                    String getUnzipFilePath = usingStringBuilder(bytes);
                    CommonMethod.logQuery(TAG , "Zip file path " + getUnzipFilePath );
                    if(getUnzipFilePath != null){
                        extractZipFile(getUnzipFilePath);
                    }*/
                }
            } catch (Exception e) {
                CommonMethod.log(TAG , "Exception " + e.toString());
                outputData = new Data.Builder().putString(NvestLibraryConfig.NVEST_ERROR_MESSAGE, e.toString())
                        .putBoolean(NvestLibraryConfig.NVEST_ERROR_STATUS, true)
                        .build();
                mFuture.set(Result.failure(outputData));
            }
        }

        return mFuture;
    }

    public String loadJSONFromFile() {
        File file = new File(NvestLibraryConfig.STORAGE_DIRECTORY_PATH, "premium-"+productId+".txt");
        CommonMethod.log(TAG , "File path " + file.getAbsolutePath());
        if(file.exists()){
            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                /*while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }*/
                line = br.readLine();
                br.close();
                return line;
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
                CommonMethod.log(TAG , "Io exception occurred " + e.toString());
            }
            catch (Exception e){
                CommonMethod.log(TAG , "Exception occurred " + e.toString());
            }
        }
        else {
            CommonMethod.log(TAG , "File does not exist");
        }
        return null;
    }


    private void extractZipFile(String zipFilePath ){

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String destinationFilePath = NvestLibraryConfig.STORAGE_DIRECTORY_PATH ;
                CommonMethod.log(TAG , "Destination file " + destinationFilePath);
                String fileContent = unzip(zipFilePath,destinationFilePath );
                if(fileContent != null){
                    File file = new File(fileContent);
                    CommonMethod.log(TAG , "File path " + file.getAbsolutePath());
                    if(file.exists()){
                        //Read text from file
                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            line = br.readLine();
                            CommonMethod.log(TAG , "Read line " + line);
                            br.close();
                        }
                        catch (IOException e) {
                            //You'll need to add proper error handling here
                            CommonMethod.log(TAG , "Io exception occurred " + e.toString());
                        }
                        catch (Exception e){
                            CommonMethod.log(TAG , "Exception occurred " + e.toString());
                        }
                    }
                    else {
                        CommonMethod.log(TAG , "File does not exist");
                    }

                }
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
                        outputData = new Data.Builder().putString(NvestLibraryConfig.NVEST_ERROR_MESSAGE,null)
                                .putBoolean(NvestLibraryConfig.NVEST_ERROR_STATUS, false)
                                .build();

                        CommonMethod.log(TAG , "Zipping process complete ");
                        mFuture.set(Result.success(outputData));
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                        outputData = new Data.Builder().putString(NvestLibraryConfig.NVEST_ERROR_MESSAGE,e.toString())
                                .putBoolean(NvestLibraryConfig.NVEST_ERROR_STATUS, true)
                                .build();

                        mFuture.set(Result.failure(outputData));
                    }
                });


    }

    private String unzip(String zipFilePath, String destDir) {
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
                CommonMethod.log(TAG , "File not found" + e);
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

}
