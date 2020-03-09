package com.nvest.user.home;

import android.content.Context;
import android.os.AsyncTask;

import com.nvest.user.LogUtils.GenericDTO;
import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UlipAsyncTask extends AsyncTask<Void, Void, Void> {

    private static String TAG = UlipAsyncTask.class.getSimpleName();
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LinkedHashMap<String, FormulaRoom> formulaRoomLinkedHashMap = new LinkedHashMap<>();
    private ArrayList<FormulaRoom> formulaRoomArrayList = new ArrayList<>();
    public UlipAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public void stepOneGetFormulaFromProductIdWithOutputloopFalse(){
        try {

            Disposable disposable = RoomDatabaseSingleton
                    .getRoomDatabaseSession()
                    .formulasRoomDao().getFormulaByProductId((String) GenericDTO.getAttributeValue(Config.PRODUCT_ID_ANNOTATION))
                    .observeOn(Schedulers.io())
                    .onErrorReturn((Throwable ex) -> {
                        LogUtils.log(TAG,"Error in try "+ex.toString()); //examine error here
                        List<FormulaRoom> formulaRoomsList = null;
                        return formulaRoomsList; //empty object of the datatype
                    })
                    .doOnSuccess(new Consumer<List<FormulaRoom>>() {
                        @Override
                        public void accept(List<FormulaRoom> formulaRoomsList) throws Exception {
                            for (FormulaRoom formulaRoom: formulaRoomsList){
                                formulaRoomLinkedHashMap.put(formulaRoom.getFormulaKeyword(),formulaRoom);
                            }
                            formulaRoomArrayList.addAll(formulaRoomsList);
                            LogUtils.log(TAG , "Formula list size " + formulaRoomsList.size());
                            //LogUtils.log(TAG , "Formula id " + formulaRoomLinkedHashMap.get(Config.PREMIUM_RATE).getFormulaId());
                        }
                    })
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            LogUtils.log(TAG , "Inside do on complete");
                        }
                    })
                    .subscribe();
            compositeDisposable.add(disposable);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in step one " + e.toString());
        }

    }

    public void stepTwoGetFormulaFromProductIdWithOutputloopTrue(){

    }

    public void stepThreeGetCompanyIdFromProductMasters(){

    }

    public void stepFourCalculateFundValue(){

    }

    public void stepFiveCreateTempBIUlipTable(){

    }

    public void stepSixGetTaxGroup(){
        //Fills @TaxGroup annotation here

    }


}
