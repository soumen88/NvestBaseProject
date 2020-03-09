package nvest.com.nvestlibrary.productinformation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.databaseFiles.dao.RoomRawQueryBuilder;
import nvest.com.nvestlibrary.databaseFiles.dao.optionmaster.OptionMasterRoom;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.FundStrategyModel;
import nvest.com.nvestlibrary.nvestWebModel.FundsModel;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;
import nvest.com.nvestlibrary.nvestWebModel.MasterOption;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;

public class ProductInformationDataViewModel extends AndroidViewModel {
    private static String TAG = ProductInformationDataViewModel.class.getSimpleName();
    private MutableLiveData<Map<String,String>> mutablePtPptKeyValuePairList = new MutableLiveData<>();
    private MutableLiveData<Map<String,String>> mutablePtKeyValuePairList = new MutableLiveData<>();
    private MutableLiveData<String> mutablePt = new MutableLiveData<>();
    private MutableLiveData<List<FundsModel>> mutableFundsList = new MutableLiveData<>();
    private MutableLiveData<List<FundStrategyModel>> mutableFundStrategyModelList = new MutableLiveData<>();

    private ProductInformationDataListener productInformationDataListener;

    public ProductInformationDataListener getProductInformationDataListener() {
        return productInformationDataListener;
    }

    public void setProductInformationDataListener(ProductInformationDataListener productInformationDataListener) {
        this.productInformationDataListener = productInformationDataListener;
    }

    public ProductInformationDataViewModel(@NonNull Application application) {
        super(application);
    }

    public interface ProductInformationDataListener{
        void getPtlistcompleted(boolean complete);
        void getOutputPtlistcompleted(boolean complete);
        void getRiderlist(boolean complete);
        void getFundStrategyList(boolean complete);
        void getPtPptmasterlist(boolean complete);

    }

    public List<FundsModel> getFunds(int productid) {
        List<FundsModel> fundsModelList = new ArrayList<>();
        mutableFundsList = new MutableLiveData<>();
        String Query = "Select * from FundMaster where FundId in (Select FundId FROM FundMapping where Productid = " + productid + ")";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        CommonMethod.log(TAG, "Keywords cursor count " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                FundsModel fundsModel = new FundsModel();
                fundsModel.setFundId(cursor.getInt(0));
                fundsModel.setCompanyId(cursor.getInt(1));
                fundsModel.setFundName(cursor.getString(2));
                fundsModel.setFundType(cursor.getString(3));
                fundsModel.setFundUin(cursor.getString(4));
                fundsModel.setFmcBaseCharge(cursor.getDouble(5));
                fundsModel.setGuaranteeCharge(cursor.getDouble(6));
                fundsModelList.add(fundsModel);
            } while (cursor.moveToNext());
        }

        mutableFundsList.setValue(fundsModelList);
        productInformationDataListener.getRiderlist(true);

        return fundsModelList;
    }
    public List<FundStrategyModel> getFundStrategyByProductId(int productid){
        List<FundStrategyModel> fundStrategyModelList = new ArrayList<>();
        mutableFundStrategyModelList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //String Query = "Select * from FundStrategyMaster where Productid  = '"+productid+"' and IsInput = 1";
                String Query = "Select * from FundStrategyMaster where Productid  = " + productid;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                CommonMethod.log(TAG , "Fund strategy cursor count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            FundStrategyModel fundStrategyModel = new FundStrategyModel();
                            fundStrategyModel.setProductId(cursor.getInt(0));
                            fundStrategyModel.setStrategyId(cursor.getInt(1));
                            fundStrategyModel.setStrategyName(cursor.getString(2));
                            fundStrategyModel.setParentId(cursor.getString(3));
                            fundStrategyModel.setInput(cursor.getInt(4) > 0);
                            fundStrategyModelList.add(fundStrategyModel);
                        } while (cursor.moveToNext());
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
                        CommonMethod.log(TAG , "Fund strategy list size  " + fundStrategyModelList.size());
                        mutableFundStrategyModelList.setValue(fundStrategyModelList);
                        productInformationDataListener.getFundStrategyList(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
        return fundStrategyModelList;
    }

    public Map<String, String> getPtList(int productid){
        Map<String, String> stringKeyValuePairList = new HashMap<>();
        mutablePtKeyValuePairList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String Query = "Select distinct PTDisplay, Ptformula from PtPptMaster where productid = "+productid;
                //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getPPTListFromPtPPTMaster(productid);
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                CommonMethod.log(TAG , "Keywords cursor count " + cursor.getCount());
                if(cursor != null){
                    if (cursor.moveToFirst()) {
                        do {
                            stringKeyValuePairList.put(cursor.getString(0), cursor.getString(1));
                        } while (cursor.moveToNext());
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
                        CommonMethod.log(TAG , "Keywords completed size " + stringKeyValuePairList.size());
                        mutablePtKeyValuePairList.setValue(stringKeyValuePairList);
                        productInformationDataListener.getPtlistcompleted(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
        return stringKeyValuePairList;
    }

    public void getCountFromPtPPtMaster(int productid, int optionlevelid){
        ///Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getCountFromPtPPtMaster(productid, optionlevelid);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select count(*) from PtPptMaster where productid = "+productid+" and optionid not null and (Select optionid from OptionMaster where productid = "+productid+" and optionlevelid = "+optionlevelid+")");
        CommonMethod.log(TAG , "Keywords cursor count " + cursor.getCount());
        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {

                } while (cursor.moveToNext());
            }
        }
    }

    public Map<String, String> getPtPPTMaster(int productid, String ptformula){
        Map<String, String> stringKeyValuePairList = new HashMap<>();
        mutablePtPptKeyValuePairList = new MutableLiveData<>();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String Query = "Select distinct PPTFormula, PPTDisplay from PtPptMaster where productid = '"+productid+"' and (PTFormula = '"+ptformula+"' or (PTFormula is null or PTFormula = ''))";
                //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getPPTListFromPtPPTMaster(productid);
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                CommonMethod.log(TAG , "PT PPT master cursor count " + cursor.getCount());
                if(cursor.getCount() > 0){
                    if (cursor.moveToFirst()) {
                        do {
                            if(cursor.getString(0) != null && cursor.getString(1) != null){
                                stringKeyValuePairList.put(cursor.getString(0), cursor.getString(1));
                            }
                        } while (cursor.moveToNext());
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
                        CommonMethod.log(TAG , "Keywords completed size " + stringKeyValuePairList.size());
                        mutablePtPptKeyValuePairList.setValue(stringKeyValuePairList);
                        productInformationDataListener.getPtPptmasterlist(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });
        return stringKeyValuePairList;
    }

    public ArrayList<MasterOption> getOptions(int productid){
        ArrayList<MasterOption> masterOptionList = new ArrayList<>();
        LinkedHashMap<Integer, String> currentMasterList = new LinkedHashMap<>();
        CommonMethod.log(TAG , "Product id " + productid);
        String query = "Select OptionLevelId, OptionLevelName from OptionLevelMaster where productid = " + productid+" order by OptionLevelId asc";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(query);
        CommonMethod.log(TAG , "Option cursor count " + cursor.getCount());
        if(cursor!= null){
            if (cursor.moveToFirst()) {
                do {
                    currentMasterList.put(cursor.getInt(0), cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        CommonMethod.log(TAG , "Current master option list size " + currentMasterList.size());
        for (Map.Entry<Integer, String> entry: currentMasterList.entrySet()){
            MasterOption masterOption = new MasterOption();
            masterOption.setLevel(entry.getKey());
            masterOption.setLevelName(entry.getValue());
            Map<Integer, KeyValuePair> test123 = loadOptions(productid,entry.getKey(),"", "0","0",  "Default", "0");
            masterOption.setOptions(test123);
            if(test123 != null){
                if(test123.size() > 0){
                    for (Map.Entry<Integer, KeyValuePair> temp: test123.entrySet()){
                        CommonMethod.log(TAG , "Keyword name final obtained " + temp.getKey());
                        CommonMethod.log(TAG , "Keyword value final obtained " + temp.getValue().getValue());
                        StringKeyValuePair temp2 = optionInputField(productid, temp.getValue().getKey());
                        masterOption.setInputType(temp2.getValue());
                        CommonMethod.log(TAG , "Setting master option field as " + temp2.getKey());
                        if(temp2 != null){
                            masterOption.setOptionField(temp2.getKey());
                        }

                    }
                }
            }
            else {
                //masterOption.setOptions(null);
                masterOption.setOptionField(null);
            }
            masterOptionList.add(masterOption);
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        //JsonArray myCustomArray = gson.toJsonTree(masterOptionList).getAsJsonArray();
        //JsonArray myCustomArray = gson.toJsonTree(masterOptionList).getAsJsonArray();
        CommonMethod.log(TAG , "Final master option list size " + masterOptionList.size());
        CommonMethod.log(TAG , "Final master option list size " + gson.toJson(masterOptionList));
        return masterOptionList;
    }


    public Map<Integer, KeyValuePair> loadOptions(int productid, int Level, String ParentId, String PTFormula, String PPTFormula, String Sender, String ChangeLevel){
        Map<Integer, KeyValuePair> loadOptionsList = new HashMap<>();
        boolean parentSelected = false;
        List<String> parId = Arrays.asList(ParentId.split("\\s*,\\s*"));
        List<Integer> notSelectedLevels = new ArrayList<Integer>();
        int no = Level - 1;
        for (String pi : parId) {
            try {
                int iii = Integer.parseInt(pi);
                parentSelected = true;
            }
            catch(Exception e)  {
                notSelectedLevels.add(no);
            }
            no=no-1;
        }
        List<Integer> optionIdlist = new ArrayList<>();
        //Cursor optioncursor = NvestAssetDatabaseAccess.getSingletonInstance().getOptionIdFromOptionMaster(productid,Level);
        Cursor optioncursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select optionid from OptionMaster where productid  = "+productid+" and optionlevelid = " + Level);
        if(optioncursor != null){
            if(optioncursor.moveToFirst()){
                do{
                    optionIdlist.add(optioncursor.getInt(0));
                }
                while (optioncursor.moveToNext());
            }
        }
        CommonMethod.log(TAG , "Product id " + productid + " Level " + Level + " option id list size " + optionIdlist.size());
        String OptionMasterQuery="Select * from OptionMaster where productid = " + productid + " and OptionLevelId = " + Level + "";
        if(Sender.toLowerCase().equals("default")){
            for (int i = 0 ; i < optionIdlist.size();i++){
                String selectQuery = "Select count(*) from PtPptMaster where productid = '"+productid+"' and optionid = " +optionIdlist.get(i) ;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(selectQuery);
                CommonMethod.log(TAG , "Returning cursor count " + cursor.getCount() + " option id " + optionIdlist.get(i));
                if( cursor != null){
                    if(cursor.moveToFirst()){
                        CommonMethod.log(TAG , "Returning count " + cursor.getInt(0));
                        int count = cursor.getInt(0);
                        if(count > 0){
                            CommonMethod.log(TAG , "Count found hence returning");
                            return null;
                        }
                    }
                }
                if(!parentSelected){
                    LinkedHashMap<Integer, OptionMasterRoom> tempOption = getOptionMasters(productid, Level);
                    CommonMethod.log(TAG , "Option masters size " + tempOption.size());
                    int counter = 0 ;
                    for (Map.Entry<Integer, OptionMasterRoom> entry: tempOption.entrySet()){
                        CommonMethod.log(TAG , "Keyword name " + entry.getValue().getOptionName());
                        CommonMethod.log(TAG , "Keyword option id " + entry.getValue().getOptionId());
                        KeyValuePair keyValuePair = new KeyValuePair();
                        keyValuePair.setKey(entry.getValue().getOptionId());
                        keyValuePair.setValue(entry.getValue().getOptionName());
                        int key = counter++;
                        loadOptionsList.put(key, keyValuePair);
                    }
                }
            }


        }
        else if(Sender.toLowerCase().equals("option")){
            //String Query = "Select * from OptionMaster where productid = "+productid+" and OptionLevelID = "+Level+" and optionparent is not null and optionparent <> ''";
            String Query = "Select * from OptionMaster where productid = "+productid+" and OptionLevelID = "+Level+" and optionparent is not null  and optionparent <> ''";
            String countQuery = "Select count(*) from OptionMaster where productid = "+productid+" and OptionLevelID = "+Level+" and optionparent is not null and optionparent <> ''";
            //String countQuery = "Select count(*) from OptionMaster where productid = "+productid+" and OptionLevelID = "+Level+" and optionparent is not null";
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            Cursor countCursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(countQuery);
            if(countCursor!=null ){
                countCursor.moveToFirst();
                int count = countCursor.getInt(0);
                if (count == 0 && parentSelected == true) {
                    CommonMethod.log(TAG, "Count is 0 hence returning");
                    return null;
                }
            }
            else {
                return null;
            }
            CommonMethod.log(TAG , "Inside option cursor count "+ cursor.getCount());
            ArrayList<String> ParentList = new ArrayList<String>();
            cursor.moveToFirst();
            if(cursor!= null && cursor.getCount() > 0){
                do{
                    String optionparent = cursor.getString(cursor.getColumnIndex("OptionParent"));
                    List<String> listoptionparent = Arrays.asList(optionparent.split(","));
                    List<String> temp = new ArrayList<>(listoptionparent);
                    temp.removeAll(ParentList);
                    ParentList.addAll(temp);
                }
                while (cursor.moveToNext());
                //Finding smallest parent
                String ParentListStr =ParentList.toString().replaceAll("[\\[\\]]", "");
                Query = "select distinct optionlevelid from OptionMaster where productid = "+productid+" and optionid in ("+ParentListStr+")";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() == 0 && parentSelected==true) {
                    CommonMethod.log(TAG, "Count is 0 hence returning");
                    return null;
                }
                ArrayList<String> ParentLevel = new ArrayList<String>();
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()) {
                        String levelid = cursor.getString(0);
                        ParentLevel.add(levelid);
                        cursor.moveToNext();
                    }
                }

                if(ParentLevel.get(0) != null && !ParentLevel.get(0).isEmpty() ){
                    int smallestparent = Integer.parseInt(ParentLevel.get(0));
                    for(String x : ParentLevel ){
                        if (Integer.parseInt(x) < smallestparent) {
                            smallestparent = Integer.parseInt(x);
                        }
                    }
                    boolean noElementsInCommon = Collections.disjoint(ParentList, notSelectedLevels);
                    if(smallestparent > Integer.parseInt(ChangeLevel) && noElementsInCommon==true ){
                        return  null;
                    }
                }

                if ((PTFormula == "0" || PTFormula == null) && (PPTFormula == "0" || PPTFormula == null))
                {
                    Query = "select * from ptpptmaster where productid = " + productid + " and optionid is not null and optionid in (Select optionid from OptionMaster where productid = " + productid + " and OptionLevelId = " + Level + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        CommonMethod.log(TAG, "Count found hence returning");
                        return null;
                    }
                    loadOptionsList =  OptionListInterim(OptionMasterQuery,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                    return loadOptionsList;
                }
                else
                {
                    Query = "select * from ptpptmaster where productid = " + productid + " and optionid is not null and optionid in (Select optionid from OptionMaster where productid = " + productid + " and OptionLevelId = " + Level + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        Query = "select distinct optionid from PtPptMaster where productid = "+productid+" and (ptformula = "+ PTFormula +" or ptformula is null or ptformula = 0) and (pptformula = "+PPTFormula+" or pptformula is null  or pptformula = 0)";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        cursor.moveToFirst(); String OptionIdList = "";
                        while(!cursor.isAfterLast()) {
                            String optionid = cursor.getString(0);
                            if(optionid == null || optionid.equals("")){
                                loadOptionsList =  OptionListInterim(OptionMasterQuery,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                                return loadOptionsList;
                            }
                            else
                            {
                                OptionIdList= OptionIdList +","+optionid;
                            }
                            cursor.moveToNext();
                        }
                        OptionIdList= OptionIdList.substring(1);
                        Query = "select * from optionmaster where productid = " + productid + " and OptionLevelId = " + Level + " and optionid in ("+ OptionIdList +")";
                        loadOptionsList =  OptionListInterim(Query,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                        return loadOptionsList;
                    }

                    else{
                        loadOptionsList =  OptionListInterim(OptionMasterQuery,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                        return loadOptionsList;
                    }
                }
            }

        }
        else if(Sender.equalsIgnoreCase("PT")){
            String Query = "Select * from ptpptmaster where productid = " + productid + " and optionid is not null and optionid in (Select optionid from OptionMaster where productid = " + productid + " and OptionLevelId = " + Level + ")";
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor == null || cursor.getCount() == 0) {
                CommonMethod.log(TAG, "Count found hence returning");
                return null;
            }
            String opt1Query = "Select distinct optionid from PtPptMaster where productid = '"+productid+"' and ((ptformula = '"+ PTFormula +"' or ptformula is null or ptformula = 0) and (pptformula = '"+PPTFormula+"' or pptformula is null  or pptformula = 0) or OptionId is null)";
            Cursor opt1cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(opt1Query);
            String OptionIdList = "";
            if(opt1cursor != null && opt1cursor.getCount() > 0) {
                if (opt1cursor.moveToFirst()) {
                    do {
                        String optionid = opt1cursor.getString(0);
                        OptionIdList = OptionIdList + "," + optionid;
                    }
                    while (opt1cursor.moveToNext());
                }
            }
            OptionIdList= OptionIdList.substring(1);
            if (OptionIdList.contains("null")) {
                loadOptionsList =  OptionListInterim(OptionMasterQuery,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                return loadOptionsList;
            }
            else {
                CommonMethod.log(TAG, "Executing else");
                Query = "select * from optionmaster where productid = " + productid + " and OptionLevelId = " + Level + " and optionid in ("+ OptionIdList.replace("null","") +")";
                loadOptionsList =  OptionListInterim(Query,productid, Level, ParentId, PTFormula, PTFormula, Sender, ChangeLevel,parId,parentSelected);
                return loadOptionsList;
            }
        }
        return loadOptionsList;
    }


    public LinkedHashMap<Integer, OptionMasterRoom> getOptionMasters(int productid, int levelid){
        CommonMethod.log(TAG , "Get option masters started " + productid + " levelid passed " + levelid);
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getAllColumnsFromOptionMaster(productid, levelid);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select * from optionmaster where productid = " + productid + " and optionlevelid = " + levelid + " and (optionparent = '' or optionparent is null)");
        CommonMethod.log(TAG , "Single row cursor count " + cursor.getCount());
        LinkedHashMap<Integer, OptionMasterRoom> integerOptionMasterRoomLinkedHashMap = new LinkedHashMap<>();
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    OptionMasterRoom optionMasterRoom = new OptionMasterRoom();
                    optionMasterRoom.setProductid(cursor.getInt(0));
                    optionMasterRoom.setOptionId(cursor.getInt(1));
                    int key = cursor.getInt(1);
                    optionMasterRoom.setOptionLevelId(cursor.getInt(2));
                    optionMasterRoom.setOptionName(cursor.getString(3));
                    optionMasterRoom.setOptionParent(cursor.getString(4));
                    optionMasterRoom.setInputFieldName(cursor.getString(5));
                    optionMasterRoom.setInputFieldType(cursor.getString(6));
                    optionMasterRoom.setDefault(cursor.getInt(7)>0);
                    integerOptionMasterRoomLinkedHashMap.put(key, optionMasterRoom);
                }while (cursor.moveToNext());
            }
        }

        return integerOptionMasterRoomLinkedHashMap;
    }

    public StringKeyValuePair optionInputField(int productid, int optionId){
        CommonMethod.log(TAG , "Input field product id " + productid + " option id " + optionId);
        String selectQuery = "Select InputFieldName, InputFieldType from OptionMaster where productid = '"+productid+"' and optionid = '"+optionId+"'" ;
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(selectQuery);
        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
        if(cursor != null){
            CommonMethod.log(TAG , "Option input field cursor count " + cursor.getCount());
            if (cursor.moveToFirst()) {
                if(cursor.getString(0) != null && !cursor.getString(0).isEmpty() && cursor.getString(1) != null  && !cursor.getString(1).isEmpty()){
                    CommonMethod.log(TAG , "Putting Key " + cursor.getString(0));
                    CommonMethod.log(TAG , "Putting Value" + cursor.getString(1));
                    stringKeyValuePair.setKey(cursor.getString(0));
                    stringKeyValuePair.setValue(cursor.getString(1));
                }
            }
        }
        return stringKeyValuePair;
    }

    public Map<Integer, KeyValuePair> OptionListInterim(String Query, int productid, int Level, String ParentId, String PTFormula, String PPTFormula, String Sender, String ChangeLevel,List<String> parId,boolean parentSelected ){
        Map<Integer, KeyValuePair> loadOptionsList = new LinkedHashMap<>();
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if(cursor != null){
            int counter = 0;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int key = counter++;
                String optionparent = cursor.getString(cursor.getColumnIndex("OptionParent"));
                if (optionparent == null || optionparent.equals("")) {
                    //List<String> listoptionparent = Arrays.asList(optionparent.split("\\s*,\\s*"));
                    KeyValuePair keyValuePair = new KeyValuePair();
                    keyValuePair.setKey(cursor.getInt(cursor.getColumnIndex("OptionId")));
                    keyValuePair.setValue(cursor.getString(cursor.getColumnIndex("OptionName")));
                    //List<String> temp = new ArrayList<>(listoptionparent);
                    loadOptionsList.put(key, keyValuePair);
                }
                else {
                    List<String> listoptionparent = Arrays.asList(optionparent.split("\\s*,\\s*"));
                    boolean noElementsInCommon = Collections.disjoint(parId, listoptionparent);
                    if (noElementsInCommon == false) {
                        KeyValuePair keyValuePair = new KeyValuePair();
                        keyValuePair.setKey(cursor.getInt(cursor.getColumnIndex("OptionId")));
                        keyValuePair.setValue(cursor.getString(cursor.getColumnIndex("OptionName")));
                        loadOptionsList.put(key, keyValuePair);
                    }
                }
                cursor.moveToNext();
            }
            return  loadOptionsList;
        }
        return null;
    }

    public String loadPtValue(int productid, int ppt , String optionid, String optionvalue){
        final String valueOtained = "";
        mutablePt = new MutableLiveData<>();
        String Query =  "Select * from Formulas where productid = '"+productid+"'and FormulaKeyword = '[PR_PT]'";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        cursor.moveToFirst();
        if(cursor == null || cursor.getCount() == 0) {
            CommonMethod.log(TAG , "Error in settting formulas");
            return null;
        }
        else {
            cursor.moveToFirst();
            List<String> optionIdList = Arrays.asList(optionid.split("\\s*,\\s*"));
            List<String> optionValueList = Arrays.asList(optionvalue.split("\\s*,\\s*"));
            CommonMethod.log(TAG , "Option id list size " + optionIdList.size());
            CommonMethod.log(TAG , "Option value list size " + optionValueList.size());
            GenericDTO.addAttribute(NvestLibraryConfig.PR_PPT_ANNOTATION, ppt);
            CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_PPT_ANNOTATION,NvestLibraryConfig.PR_PPT_ANNOTATION, String.valueOf(ppt),"Model", String.valueOf(NvestLibraryConfig.FieldType.Input), "");
            do {
                for (int i = 0; i < optionIdList.size();i++){
                    String st = optionIdList.get(i);
                    if(st.isEmpty()){
                        continue;
                    }
                    String identifier = NvestLibraryConfig.PR_OPTION_ANNOTATION + i;
                    CommonMethod.addDynamicKeyWordToGenericDTO(identifier,identifier, st,"Model", String.valueOf(NvestLibraryConfig.FieldType.Input),"");
                    //GenericDTO.addAttribute(identifier, st);
                }

                for (int i = 0; i < optionValueList.size();i++){
                    String st = optionIdList.get(i);
                    if(st.isEmpty()){
                        continue;
                    }
                    String identifier = NvestLibraryConfig.PR_OPTION_VALUE_ANNOTATION + i ;
                    CommonMethod.addDynamicKeyWordToGenericDTO(identifier ,identifier, st,"Model", String.valueOf(NvestLibraryConfig.FieldType.Input),"");
                    //GenericDTO.addAttribute(identifier, st);
                }
                RoomRawQueryBuilder.RoomQueryListener roomQueryListener = new RoomRawQueryBuilder.RoomQueryListener() {
                    @Override
                    public void complete(boolean flag, String query) {
                        CommonMethod.log(TAG , "Is complete " + flag);
                        CommonMethod.logQuery(TAG , "Query " + query);
                        if(flag){
                            Cursor value = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(query);
                            if(value != null && value.getCount() > 0){
                                value.moveToFirst();
                                mutablePt.setValue(value.getString(0));
                                productInformationDataListener.getOutputPtlistcompleted(flag);
                            }
                        }
                    }
                };
                String formulaExtendedQuery = " Select (" +cursor.getString(cursor.getColumnIndex("FormulaExtended"))+ ")";
                RoomRawQueryBuilder roomRawQueryBuilder = new RoomRawQueryBuilder(formulaExtendedQuery, roomQueryListener);
            }
            while (cursor.moveToNext());
        }
        return null;
    }

    public MutableLiveData<Map<String,String>> setKeyValuePairMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutablePtKeyValuePairList;
    }


    public MutableLiveData<List<FundsModel>> setFundsMutableList() {
        CommonMethod.log(TAG, "Inside set mutable live data");
        return mutableFundsList;
    }

    public MutableLiveData<Map<String,String>> setPtPptMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutablePtPptKeyValuePairList;
    }


    public MutableLiveData<List<FundStrategyModel>> setFundStrategyMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableFundStrategyModelList;
    }

    public MutableLiveData<String> setPtOutputLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutablePt;
    }

}
