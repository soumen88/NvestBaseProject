package com.nvest.user.databaseFiles.databaseWorkers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.home.APIClient;
import com.nvest.user.home.APIInterface;
import com.nvest.user.home.Product;
import com.nvest.user.home.ProductContents;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DummyWorker  extends Worker {

    private static String TAG = DummyWorker.class.getSimpleName();
    private APIInterface apiInterface;
    private Context context;

    public DummyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {

        LogUtils.log(TAG , "Inside do work");
        List<ProductContents> fundStrategyMasterList = new ArrayList<ProductContents>();
        hitserver();
        return Result.success();
    }

    public void hitserver(){
        LogUtils.log(TAG , "Hit server for getting details");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Product> call2 = (Call< Product>) apiInterface.doGetUserList();
        call2.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                LogUtils.log(TAG , "Inside success " + response.isSuccessful());
                if(response.isSuccessful()){
                    Product product = response.body();
                    if(product != null){
                        LogUtils.log(TAG , "Product Size " + product.getProduct().size());
                    }
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                LogUtils.log(TAG , "Inside failure " + t.toString());
            }
        });
    }
}
