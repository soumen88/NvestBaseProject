package com.nvest.user.home;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.util.StringUtil;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nvest.user.LogUtils.GenericDTO;
import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.LogUtils.TestPojo;
import com.nvest.user.appConfig.Config;

import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.RoomRawQueryBuilder;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import com.nvest.user.databaseFiles.dao.bonusscrtable.BonusScrRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;
import com.nvest.user.databaseFiles.dao.tempbitable.TempBIRoom;
import com.nvest.user.databaseFiles.dao.tempbitable.TempBITableSqliteHelper;
import com.nvest.user.databaseFiles.dao.tempbitable.TestPojoSUD;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TraditionalAsyncTask  extends AsyncTask<Void, Void, LinkedHashMap<String, FormulaRoom>>{

    private static String TAG = TraditionalAsyncTask.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LinkedHashMap<String, FormulaRoom> formulaRoomLinkedHashMap = new LinkedHashMap<>();
    private ArrayList<FormulaRoom> formulaRoomArrayList = new ArrayList<>();
    private ArrayList<BonusScrRoom> bonusScrRoomArrayList = new ArrayList<>();
    private Context context;
    public TraditionalAsyncTask(Context context) {
        this.context = context;
    }


        @Override
        protected LinkedHashMap<String, FormulaRoom> doInBackground(Void... voids) {
            LogUtils.log(TAG , "Traditional async task started");

            //queryTesting();
            stepOneGetFormulaFromProductId();
            return null;
        }

        @Override
        protected void onPostExecute(LinkedHashMap<String, FormulaRoom> aVoid) {
            super.onPostExecute(aVoid);
            //testing();
            //stepFourGenerateTempBITable();
            LogUtils.log(TAG , "On post execute started");
        }



    public FormulaRoom stepOneGetFormulaFromProductId(){
        try {

            Disposable disposable = RoomDatabaseSingleton
                    .getRoomDatabaseSession()
                    .formulasRoomDao().getFormulaByProductId((String) GenericDTO.getAttributeValue(Config.PRODUCT_ID_ANNOTATION))
                    .observeOn(Schedulers.io())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.log(TAG , "Throwable " + throwable.toString());
                        }
                    })
                    .subscribe(new Consumer<List<FormulaRoom>>() {
                        @Override
                        public void accept(List<FormulaRoom> formulaRoomsList) throws Exception {
                            for (FormulaRoom formulaRoom: formulaRoomsList){
                                formulaRoomLinkedHashMap.put(formulaRoom.getFormulaKeyword(),formulaRoom);
                            }
                            formulaRoomArrayList.addAll(formulaRoomsList);
                            LogUtils.log(TAG , "Formula list size " + formulaRoomsList.size());
                            //LogUtils.log(TAG , "Formula id " + formulaRoomLinkedHashMap.get(Config.PREMIUM_RATE).getFormulaId());
                        }
                    });
            compositeDisposable.add(disposable);
            return formulaRoomLinkedHashMap.get(Config.PREMIUM_RATE);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in step one " + e.toString());
        }
        return null;
    }

    public void stepTwoGetPremiumRate(FormulaRoom formulaRoom){
        try {
            LogUtils.log(TAG , "Formula basic " + formulaRoom.getFormulabasic().toUpperCase());
            RoomRawQueryBuilder roomRawQueryBuilder = new RoomRawQueryBuilder(formulaRoom.getFormulabasic().toUpperCase());
            String query = roomRawQueryBuilder.getSql();
            roomRawQueryBuilder.getArgCount();
            SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);

            Disposable disposable =
                    RoomDatabaseSingleton
                            .getRoomDatabaseSession()
                            .premiumRatesRoomDao().getPremiumRateRoom(simpleSQLiteQuery)
                            .observeOn(Schedulers.io())
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    LogUtils.log(TAG ,"Error " + throwable.toString());
                                }
                            })
                            .subscribe(new Consumer<Double>() {
                                @Override
                                public void accept(Double aDouble) throws Exception {
                                    LogUtils.log(TAG , "Value xxx yes " + aDouble);
                                    GenericDTO.addAttribute(Config.PREMIUM_RATE_ANNOTATION, aDouble.toString());
                                    stepThreeGetTaxGroupFromProductCategoryMap();
                                }
                            });
            compositeDisposable.add(disposable);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in step two " + e.toString());
        }

    }

    public void stepThreeGetTaxGroupFromProductCategoryMap(){
        try {
            LogUtils.log(TAG , "Step 3 started...");
            Disposable disposable =
                    RoomDatabaseSingleton
                            .getRoomDatabaseSession()
                            .productCategoryMapRoomDao().getProductCategoryMapByProductId((String)GenericDTO.getAttributeValue(Config.PRODUCT_ID_ANNOTATION))
                            .observeOn(Schedulers.io())
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    LogUtils.log(TAG ,"Error " + throwable.toString());
                                }
                            })
                            .subscribe(new Consumer<ProductCategoryMapRoom>() {
                                @Override
                                public void accept(ProductCategoryMapRoom productCategoryMapRoom) throws Exception {
                                   LogUtils.log(TAG , "Product category maps " + productCategoryMapRoom.getTaxGroup() + " catery map id " + productCategoryMapRoom.getProductCateryMapId());
                                   GenericDTO.addAttribute(Config.TAX_GROUP_ANNOTATION,String.valueOf(productCategoryMapRoom.getTaxGroup()));
                                   //stepFourGenerateTempBITable();
                                    stepFivePrepareFinalQuery();
                                }
                            });
            compositeDisposable.add(disposable);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in product category map " + e.toString());
        }
    }

    public void stepFourGenerateTempBITable(){
        try {
            LogUtils.log(TAG  , "Step 4 started...");
            String uuid =  "1003_1552631383654";
            LogUtils.log(TAG , "Generate uuid " + uuid);
            GenericDTO.addAttribute(Config.TEMP_BI_TABLE,"LI_ATTAINED_AGE");
            GenericDTO.addAttribute(Config.TEMP_BI_TABLE,"POLICYYEAR");
            GenericDTO.addAttribute(Config.TEMP_BI_TABLE,"PRODUCTID");
            GenericDTO.addAttribute(Config.TEMP_BI_TABLE,"CURRENTAGE");
            String createTable = "CREATE TABLE " + Config.TEMP_BI_TABLE + " (" + Config.TEMP_BI_TABLE_PRIMARY_KEY + " INT PRIMARY KEY , " ;
            LogUtils.log(TAG ,"Create table " + createTable);
            List<Object> templist = GenericDTO.getAttributeValues(Config.TEMP_BI_TABLE);
            LogUtils.log(TAG ,"Size " + GenericDTO.getAttributeValues(Config.TEMP_BI_TABLE).size());
            for (int i =0 ; i < GenericDTO.getAttributeValues(Config.TEMP_BI_TABLE).size(); i++){
                createTable = createTable.concat(GenericDTO.getAttributeValues(Config.TEMP_BI_TABLE).get(i) +  " TEXT NOT NULL, " );
                //LogUtils.log(TAG , "Create string " + createTable);
            }
            createTable = createTable.concat("unique_key TEXT NOT NULL)");
            LogUtils.log(TAG , "Final Create string " + createTable);
            TempBITableSqliteHelper tempBITableSqliteHelper = new TempBITableSqliteHelper(context);
            tempBITableSqliteHelper.createTempBiTable(createTable);
            int policyTerm =Integer.parseInt((String) GenericDTO.getAttributeValue(Config.PT_ANNOTATION));
            LogUtils.log(TAG ,"Policy term " + policyTerm);
            int age = Integer.parseInt((String) GenericDTO.getAttributeValue(Config.LA_AGE_ANNOTATION));
            List<TempBIRoom> tempBIRoomList = new ArrayList<>();
            for (int i = 0 ; i < policyTerm; i++ ){
                TempBIRoom tempBIRoom = new TempBIRoom();
                tempBIRoom.setUniqueKey(uuid);
                tempBIRoom.setProductId(Integer.parseInt((String) GenericDTO.getAttributeValue(Config.PRODUCT_ID_ANNOTATION)));
                tempBIRoom.setPolicyYear(i+1);
                tempBIRoom.setLiAttainedAge(age++);
                tempBIRoom.setAge(age);
                tempBIRoomList.add(tempBIRoom);
                LogUtils.log(TAG , "tempbi table insert started from task");
                tempBITableSqliteHelper.insertIntoTempTable(tempBIRoom);
            }

        }
        catch (Exception e){
            LogUtils.log(TAG  ,"Exception in generating temp bi table " + e.toString());
        }
    }


    public void stepFivePrepareFinalQuery(){
        try {
            //Get formula extended
            String finalQuery = "select [LI_ATTAINED_AGE],[POLICYYEAR], " +
                    GenericDTO.getAttributeValue(Config.PREMIUM_RATE_ANNOTATION) + " as [PREMIUMRATE]";
            LogUtils.log(TAG , "Before final query " + finalQuery + " Formula room list size " + formulaRoomArrayList.size());
            for (int i = 0; i < formulaRoomArrayList.size(); i++) {
                FormulaRoom formulaRow = formulaRoomArrayList.get(i);
                if(formulaRow != null){
                    if(formulaRow.getFormulaExtended().contains(Config.BONUS_SCR_ANNOTATION)){
                        getbonusdetails();
                    }
                    finalQuery = finalQuery.concat(" , ( " + formulaRow.getFormulaExtended() + " ) as " + formulaRow.getFormulaKeyword())  ;
                }
                else {
                    LogUtils.log(TAG ,  "row is null" );
                }

            }


            RoomRawQueryBuilder roomRawQueryBuilder = new RoomRawQueryBuilder(finalQuery.toUpperCase());
            LogUtils.logQuery(TAG ,  "After Final query " + roomRawQueryBuilder.getSql());
            /*String test1 =roomRawQueryBuilder.getSql().replaceAll((String)GenericDTO.getAttributeValue(Config.IS_NULL), (String)GenericDTO.getAttributeValue(Config.IF_NULL));
            LogUtils.logQuery(TAG , "Test 1 " + test1);*/
            //String test2 = test1.replaceAll("@TaxGroup", "1");
            //LogUtils.logQuery(TAG , "Test 2 " +test2);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception occurred in step 5 " + e.toString());
        }


    }


    public void dismiss(){
        LogUtils.log(TAG , "Disposing all");
        compositeDisposable.dispose();
    }

    public void getbonusdetails(){
        try {

            Disposable disposable = RoomDatabaseSingleton.getRoomDatabaseSession().bonusScrRoomDao()
                    .getBonusByProductId((String) GenericDTO.getAttributeValue(Config.PRODUCT_ID_ANNOTATION))
                    .observeOn(Schedulers.io())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                         public void accept(Throwable throwable) throws Exception {
                            LogUtils.log(TAG , "Exception purpose " + throwable.toString());
                        }
                    })
                    .subscribe(new Consumer<List<BonusScrRoom>>() {
                        @Override
                        public void accept(List<BonusScrRoom> bonusScrRoomList) throws Exception {
                            bonusScrRoomArrayList.addAll(bonusScrRoomList);
                        }
                    });

            compositeDisposable.add(disposable);
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in getting bonus details " + e.toString());
        }

    }



    public void testing(){
        try {
            LogUtils.log(TAG , "Testing method started...");
            //String query = "Select [FormulaId] as FormulaIdTestColumn, ProductId as ProductIdTestColumn, 90.62000000 as [PREMIUMRATE] from Formulas where ProductId = 1003 and FormulaKeyword = '[PremiumRate]'";
            String query = "SELECT a.ProductId, a.MinPremium, a.MaxPremium, i.ModalPremium, a.Interval FROM [PremiumMaster] a join inputoutputmaster i on a.productid=i.productid where a.productid=1003 and ((10> FromPt and 10< ToPt) or FromPt isnull ) and ((10> FromPpt and 10< ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in( ))and ((24>= FromAge and 24<=ToAge) or FromAge isnull ) and ((500000>=FromSA and 500000<= ToSA ) or FromSA isnull) and ((1> FromPtPptId and 1< ToPtPptId) or FromPtPptId isnull) and (1=ModeId or ModeId isnull)";
            //String query = "Select * from Formulas where ProductId = 1003 and FormulaKeyword = '[PremiumRate]'";
            // String example = "United Arab Emirates Dirham (AED)";
            //Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(example);
            String example = "SELECT [LI_ATTAINED_AGE],[POLICYYEAR], NULL AS [PREMIUMRATE] , ( SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1 ) AS [MODE_FREQ] , ( SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003 ) AS [MODE_DISC] , ( 29 + POLICYYEAR ) AS [LI_AGE] , ( SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA ) AS [HSAD_RATE] , ( ((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000 ) AS [ANN_PREM] , ( ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1) ) AS [NSAP_VAL] , ( ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1) ) AS [MODAL_PREM_WNSAP] , ( ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0) ) AS [MODAL_PREM] , ( ROUND(((ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) ) AS [TAX_MP] , ( (ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0))) + (ROUND(((ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) ) AS [MODAL_PREM_TAX] , ( ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)) ) AS [LOAD_ANN_PREM] , ( (ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)) ) AS [LOAD_ANN_PREM_NSAP] , ( CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END ) AS [ANN_PREM_YEARLY] , ( CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN (((ROUND(((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 5000000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2,1))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END ) AS [ANN_PREM_YEARLY_NSAP] , ( ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END) ) AS [CUM_LOAD_PREM] , ( CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [GUAR_ADD] , ( ((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR ) AS [ACCR_GUAR_ADD] , ( 5000000 + (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR) ) AS [DB_G] , ( CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END ) AS [MBACK] , ( CASE WHEN POLICYYEAR  = (10) THEN 5000000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END ) AS [MB_G] , ( (CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END))+((CASE WHEN POLICYYEAR  = (10) THEN 5000000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END) ) AS [SB_G] , ( CASE WHEN POLICYYEAR >= 3 THEN  ((SELECT RATE FROM SV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 10 AND AGE = 29) * (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.69 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 5000000 BETWEEN FROMSA AND TOSA))) * (5000000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2,1)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ELSE 0 END ) AS [GUAR_ADD_SV] , ( SELECT ROUND(RATE,2) FROM PREMIUMRATES WHERE PRODUCTID = 1003 AND LAAGE = 29 Afinal ND PT = 10 ) AS [PREMIUMRATE] , ( 10 ) AS [POLICYTERM]";

            Matcher m = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])").matcher(example);
            while(m.find()) {
                LogUtils.log("Testing" , "Matching " + m.group(1));
            }


            String reactNative = "SELECT [LI_ATTAINED_AGE],[POLICYYEAR],90.73 AS [PREMIUMRATE] , ( (SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1) ) AS [MODE_FREQ] , ( (SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003) ) AS [MODE_DISC] , ( (30 + POLICYYEAR) ) AS [LI_AGE] , ( (SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA) ) AS [HSAD_RATE] , ( (((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000) ) AS [ANN_PREM] , ( ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2) ) AS [NSAP_VAL] , ( ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2) ) AS [MODAL_PREM_WNSAP] , ( ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0) ) AS [MODAL_PREM] , ( ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) ) AS [TAX_MP] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0))) + (ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0)) ) AS [MODAL_PREM_TAX] , ( (ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM_NSAP] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN (((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY_NSAP] , ( (((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END)) ) AS [CUM_LOAD_PREM] , ( (CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ) AS [GUAR_ADD] , ( ((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR ) AS [ACCR_GUAR_ADD] , ( (500000 + (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ) AS [DB_G] , ( (CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [MBACK] , ( (CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END) ) AS [MB_G] , ( ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END))+((CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END)) ) AS [SB_G] , ( (CASE WHEN POLICYYEAR >= 3 THEN  ((SELECT RATE FROM SV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 10 AND AGE = 30) * (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ELSE 0 END) ) AS [GUAR_ADD_SV] , ( SELECT (SELECT ROUND(RATE,2) FROM PREMIUMRATES WHERE PRODUCTID = 1003 AND LAAGE = 30 AND PT = 10) AS RATE ) AS [PREMIUMRATE] , ( SELECT 10 AS PTERM ) AS [POLICYTERM] FROM TEMPBI WHERE PRODUCTID=1003 AND UNIQUEKEY='1003_1552631383654' ORDER BY [POLICYYEAR]";
            Matcher m1 = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])").matcher(reactNative);

            while(m1.find()) {
                LogUtils.log(TAG , "Matching needed " + m1.group(1));
            }

        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in testing " + e.toString());
        }

    }


    public void queryTesting(){
        try {
            TempBITableSqliteHelper tempBITableSqliteHelper= new TempBITableSqliteHelper(context);
            tempBITableSqliteHelper.open();
            String query = "SELECT [LI_ATTAINED_AGE],[POLICYYEAR],90.73 AS [PREMIUMRATE] , ( (SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1) ) AS [MODE_FREQ] , ( (SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003) ) AS [MODE_DISC] , ( (30 + POLICYYEAR) ) AS [LI_AGE] , ( (SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA) ) AS [HSAD_RATE] , ( (((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000) ) AS [ANN_PREM] , ( ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2) ) AS [NSAP_VAL] , ( ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2) ) AS [MODAL_PREM_WNSAP] , ( ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0) ) AS [MODAL_PREM] , ( ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) ) AS [TAX_MP] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0))) + (ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0)) ) AS [MODAL_PREM_TAX] , ( (ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM_NSAP] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN (((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY_NSAP] , ( (((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END)) ) AS [CUM_LOAD_PREM] , ( (CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ) AS [GUAR_ADD] , ( ((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR ) AS [ACCR_GUAR_ADD] , ( (500000 + (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ) AS [DB_G] , ( (CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [MBACK] , ( (CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END) ) AS [MB_G] , ( ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END))+((CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END)) ) AS [SB_G] , ( (CASE WHEN POLICYYEAR >= 3 THEN  ((SELECT RATE FROM SV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 10 AND AGE = 30) * (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ELSE 0 END) ) AS [GUAR_ADD_SV] ,  ( SELECT 10 AS PTERM ) AS [POLICYTERM] FROM TEMPBI WHERE PRODUCTID=1003 AND UNIQUEKEY='1003_1552631383654' ORDER BY [POLICYYEAR] LIMIT 2";
            SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);
            List<TestPojoSUD> testPojoSUD  = RoomDatabaseSingleton.getRoomDatabaseSession().bonusGuaranteeRoomDao()
            .runtimequerytestPojo(simpleSQLiteQuery);
            if(testPojoSUD != null){
                LogUtils.log(TAG, "Query not null");
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in query Testing " + e.toString());
        }
    }

}
