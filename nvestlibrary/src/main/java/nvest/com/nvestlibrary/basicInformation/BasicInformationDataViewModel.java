package nvest.com.nvestlibrary.basicInformation;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.Cities;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;
import nvest.com.nvestlibrary.nvestWebModel.SelectedProductDetails;
import nvest.com.nvestlibrary.nvestWebModel.StateCitiesModel;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.Value;

public class BasicInformationDataViewModel extends AndroidViewModel {
    private static String TAG = BasicInformationDataViewModel.class.getSimpleName();
    private MutableLiveData<LinkedHashMap<String, Keyword>> mutableKeywordList = new MutableLiveData<>();
    private LinkedHashMap<String, Keyword> keywordLinkedHashMap = new LinkedHashMap<>();

    private MutableLiveData<Map<String, ArrayList<StringKeyValuePair>>> mutableKeyValuePairList = new MutableLiveData<>();
    private Map<String, ArrayList<StringKeyValuePair>> keyValuePairHashMap = new HashMap<>();
    private MutableLiveData<List<KeyValuePair>> mutableKeyValuePairRiderList = new MutableLiveData<>();
    private MutableLiveData<LinkedHashMap<Integer, String>> mutableKeyStateList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<StateCitiesModel>> mutableKeyStateCityList = new MutableLiveData<>();
    private MutableLiveData<SelectedProductDetails> mutableKeybasicinformationdetail= new MutableLiveData<>();
    private MutableLiveData<LinkedHashMap<Integer, Cities>> mutableKeyCityList = new MutableLiveData<>();
    private BasicInformationDataListener basicInformationDataListener;

    public interface BasicInformationDataListener{
        void onStateCityListCompleted(boolean complete);
        void onStateListCompleted(boolean complete);
        void onCityListCompleted(boolean complete);
        void onKeywordsListObtained(boolean complete);
        void onKeyValuePairListObtained(boolean complete);
        void onBasicInformationDetailCompleted(boolean complete);
    }

    public BasicInformationDataListener getBasicInformationDataListener() {
        return basicInformationDataListener;
    }

    public void setBasicInformationDataListener(BasicInformationDataListener basicInformationDataListener) {
        this.basicInformationDataListener = basicInformationDataListener;
    }

    public BasicInformationDataViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("StaticFieldLeak")
    public void getKeywords(int productid){
        mutableKeywordList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                CommonMethod.log(TAG , "Product id passed " + productid);
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select * from InputKeywords where productId = " + productid );
                CommonMethod.log(TAG , "Dump cursor " + DatabaseUtils.dumpCursorToString(cursor));
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            Keyword keyword = new Keyword();
                            keyword.setID(cursor.getInt(0));
                            keyword.setProductId(cursor.getInt(1));
                            keyword.setInputRefScreen(cursor.getString(2));
                            keyword.setKeywordName(cursor.getString(3).toUpperCase());
                            keyword.setIsMapped(cursor.getInt(4) > 0);
                            keyword.setFieldType(cursor.getString(5));
                            keyword.setFieldCaption(cursor.getString(6));
                            String keywordname = cursor.getString(3).toUpperCase();
                            keywordLinkedHashMap.put(keywordname, keyword);
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
                        CommonMethod.log(TAG , "Keywords completed size " + keywordLinkedHashMap.size());
                        mutableKeywordList.setValue(keywordLinkedHashMap);
                        if(keywordLinkedHashMap.containsKey(NvestLibraryConfig.INPUT_MODE_ANNOTATION)){
                            getInputModes(NvestLibraryConfig.INPUT_MODE_ANNOTATION,productid);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    public void getInputModes(String keyname, int productid){
        ArrayList<StringKeyValuePair> keyValuePairArrayList = new ArrayList<>();
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getModeMasters(productid);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select modeid, modename from ModeMaster where modeid in (Select modeid from ProductMode where productId = "+productid+")");
        CommonMethod.log(TAG , "Mode masters cursor count " + cursor.getCount());
        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    StringKeyValuePair keyValuePair = new StringKeyValuePair();
                    keyValuePair.setKey(cursor.getString(0));
                    keyValuePair.setValue(cursor.getString(1));
                    keyValuePairArrayList.add(keyValuePair);
                } while (cursor.moveToNext());
            }
        }
        keyValuePairHashMap.put(keyname, keyValuePairArrayList);
        CommonMethod.log(TAG , "Input mode key value pair size " + keyValuePairHashMap.size());
        if(keywordLinkedHashMap.containsKey(NvestLibraryConfig.BONUS_SCR_ANNOTATION)){
            getBonusScr(NvestLibraryConfig.BONUS_SCR_ANNOTATION, productid);
        }
        else {
            for (Map.Entry<String, Keyword> entry: keywordLinkedHashMap.entrySet()){
                if(entry.getValue().getFieldType().equals("List")){
                    getOtherList(entry.getKey(), productid);
                }
            }
            mutableKeyValuePairList.setValue(keyValuePairHashMap);
            basicInformationDataListener.onKeyValuePairListObtained(true);
            basicInformationDataListener.onKeywordsListObtained(true);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getBonusScr(String keyname, int productid){
        ArrayList<StringKeyValuePair> keyValuePairArrayList = new ArrayList<>();
        mutableKeyValuePairList = new MutableLiveData<>();
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select bonusscrid, bonusname from BonusScr where productId = " + productid );
        CommonMethod.log(TAG , "bonus rates cursor count " + cursor.getCount());
        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    StringKeyValuePair keyValuePair = new StringKeyValuePair();
                    keyValuePair.setKey(cursor.getString(0));
                    keyValuePair.setValue(cursor.getString(1));
                    keyValuePairArrayList.add(keyValuePair);
                } while (cursor.moveToNext());
            }
        }
        if(keyValuePairArrayList.size() >0){
            keyValuePairHashMap.put(keyname, keyValuePairArrayList);
        }
        //CommonMethod.log(TAG , "Before bonus key value pair size " + keyValuePairHashMap.size() );
        for (Map.Entry<String, Keyword> entry: keywordLinkedHashMap.entrySet()){
            if(entry.getValue().getFieldType().equals("List")){
                getOtherList(entry.getKey(), productid);
            }
        }
        CommonMethod.log(TAG ,"Only once...");
        /*for (Map.Entry<String, Keyword> entry: keywordLinkedHashMap.entrySet()){
            if(keyValuePairHashMap.containsKey(entry.getKey())){
                CommonMethod.log(TAG , "Keyword name " + entry.getKey());
                ArrayList<KeyValuePair> keyValuePairsTemp = keyValuePairHashMap.get(entry.getValue().getKeywordName());
                CommonMethod.log(TAG , "Custom size " + keyValuePairsTemp.size());
                for (KeyValuePair keyValuePair: keyValuePairsTemp){
                    CommonMethod.log(TAG , "Key obtained " + keyValuePair.getValue() );
                }
            }
            else {
                CommonMethod.log(TAG , "Did not find anything with key " + entry.getKey());
            }
        }*/
        mutableKeyValuePairList.setValue(keyValuePairHashMap);
        basicInformationDataListener.onKeyValuePairListObtained(true);
        basicInformationDataListener.onKeywordsListObtained(true);
    }

    @SuppressLint("StaticFieldLeak")
    public void getOtherList(String keyname, int productid){
        ArrayList<StringKeyValuePair> keyValuePairArrayList = new ArrayList<>();
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getInputKeywordValues(keyname,productid);
        CommonMethod.log(TAG , "Key Name " + keyname);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select valuefield, textfield from InputKeywordValues where keywordname = '"+keyname+"' COLLATE NOCASE and productid = " + productid);
        //CommonMethod.log(TAG , "Input keywords values cursor count " + cursor.getCount() + " for keyword " + keyname);
        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    StringKeyValuePair keyValuePair = new StringKeyValuePair();
                    keyValuePair.setKey(cursor.getString(0));
                    keyValuePair.setValue(cursor.getString(1));
                    //CommonMethod.log(TAG , "Inside cursor " + cursor.getString(1));
                    keyValuePairArrayList.add(keyValuePair);

                } while (cursor.moveToNext());
            }
        }
        keyValuePairHashMap.put(keyname, keyValuePairArrayList);
    }


    @SuppressLint("StaticFieldLeak")
    public void getRiders(int productid){
        ArrayList<KeyValuePair> keyValuePairArrayList = new ArrayList<>();
        mutableKeyValuePairRiderList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select R.ProductId as RiderId, R.ProductName as RiderName from ProductRiderMap P inner join ProductMaster R on P.RiderId = R.productid and P.productid = '"+productid+"' order by R.ProductId ");
                CommonMethod.log(TAG , "Riders cursor count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            KeyValuePair keyValuePair = new KeyValuePair();
                            keyValuePair.setKey(cursor.getInt(0));
                            keyValuePair.setValue(cursor.getString(1));
                            //CommonMethod.log(TAG , "Inside cursor " + cursor.getString(1));
                            keyValuePairArrayList.add(keyValuePair);

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
                        CommonMethod.log(TAG , "inside rider complete " + keyValuePairArrayList.size());
                        mutableKeyValuePairRiderList.setValue(keyValuePairArrayList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });

    }



    public void getStateCityList(){
        ArrayList<StateCitiesModel> stateCitiesModelArrayList = new ArrayList<>();
        mutableKeyStateCityList= new MutableLiveData<>();

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select CityId, CityName , StateName from CityMaster JOIN StateMaster ON CityMaster.StateId = StateMaster.StatId ORDER by CityMaster.CityId ");
                CommonMethod.log(TAG , "StateCity count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            StateCitiesModel stateCitiesModel = new StateCitiesModel();
                            stateCitiesModel.setCityId(cursor.getInt(0));
                            stateCitiesModel.setCityName(cursor.getString(1));
                            stateCitiesModel.setStateName(cursor.getString(2));
                            stateCitiesModelArrayList.add(stateCitiesModel);
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
                        CommonMethod.log(TAG , "inside state complete " + stateCitiesModelArrayList.size());
                        mutableKeyStateCityList.setValue(stateCitiesModelArrayList);
                        basicInformationDataListener.onStateCityListCompleted(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
    }

    public void getbasicinformationdetail(){

        //ArrayList<SelectedProductDetails> selectedProductDetailArrayList = new ArrayList<>();
        SelectedProductDetails basicInformationModel = new SelectedProductDetails();;
        mutableKeybasicinformationdetail= new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("select * from basicinformationdetail order by id desc limit 1 ");
                CommonMethod.log(TAG , "Detail count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {

                            basicInformationModel.setFirstName(cursor.getString(1));
                            basicInformationModel.setLastName(cursor.getString(2));
                            basicInformationModel.setDobdate(cursor.getString(3));
                            basicInformationModel.setDob(cursor.getString(3));
                            basicInformationModel.setGender(cursor.getString(5));
                            basicInformationModel.setFirstExtraName(cursor.getString(6));
                            basicInformationModel.setLastExtraName(cursor.getString(7));
                            basicInformationModel.setDobextradate(cursor.getString(8));
                            basicInformationModel.setDobExtra(cursor.getString(8));
                            basicInformationModel.setGenderExtra(cursor.getString(10));
                            basicInformationModel.setEmail(cursor.getString(11));
                            basicInformationModel.setState(cursor.getString(12));
                            basicInformationModel.setCity(cursor.getString(13));
                            basicInformationModel.setMobileNumber(cursor.getString(14));
                            //selectedProductDetailArrayList.add(basicInformationModel);

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
                        mutableKeybasicinformationdetail.setValue(basicInformationModel);
                        basicInformationDataListener.onBasicInformationDetailCompleted(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });


    }






    @SuppressLint("StaticFieldLeak")
    public void getStateList(){
        LinkedHashMap<Integer, String> keywordLinkedStateHashMap = new LinkedHashMap<>();
        mutableKeyStateList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select * from StateMaster");
                CommonMethod.log(TAG , "States cursor count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            keywordLinkedStateHashMap.put(cursor.getInt(0),cursor.getString(1));
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
                        CommonMethod.log(TAG , "inside state complete " + keywordLinkedStateHashMap.size());
                        mutableKeyStateList.setValue(keywordLinkedStateHashMap);
                        basicInformationDataListener.onStateListCompleted(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });

    }

    @SuppressLint("StaticFieldLeak")
    public void getCityListFromStateId(int stateid){
        LinkedHashMap<Integer, Cities> keywordLinkedCityHashMap = new LinkedHashMap<>();
        mutableKeyCityList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select * from CityMaster where stateid = " + stateid);
                CommonMethod.log(TAG , "city cursor count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            Cities cities = new Cities();
                            cities.setCityId(cursor.getInt(0));
                            cities.setStatId(cursor.getInt(1));
                            cities.setCityName(cursor.getString(2));
                            keywordLinkedCityHashMap.put(cursor.getInt(0),cities);
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
                        CommonMethod.log(TAG , "inside city complete " + keywordLinkedCityHashMap.size());
                        mutableKeyCityList.setValue(keywordLinkedCityHashMap);
                        basicInformationDataListener.onCityListCompleted(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });


    }

    public MutableLiveData<LinkedHashMap<Integer, String>> setStateMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeyStateList;
    }
    public MutableLiveData<LinkedHashMap<Integer, Cities>> setCityMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeyCityList;
    }

    public MutableLiveData<LinkedHashMap<String, Keyword>> setKeyWordMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeywordList;
    }

    public MutableLiveData<ArrayList<StateCitiesModel>> setStateCityLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeyStateCityList;
    }

    public MutableLiveData<SelectedProductDetails> setBasicInformationLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeybasicinformationdetail;
    }

    public MutableLiveData<Map<String, ArrayList<StringKeyValuePair>>> setKeyValuePairMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableKeyValuePairList;
    }
}
