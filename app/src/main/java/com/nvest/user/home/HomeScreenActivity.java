package com.nvest.user.home;

import android.arch.lifecycle.Observer;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Query;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nvest.user.CheckActivity;
import com.nvest.user.LogUtils.GenericDTO;
import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.LogUtils.TestPojo;
import com.nvest.user.R;
import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.DatabaseAccess;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.RoomRawQueryBuilder;
import com.nvest.user.databaseFiles.SyncHandlerFactory;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import com.nvest.user.databaseFiles.dao.tempbitable.TempBITableSqliteHelper;
import com.nvest.user.databaseFiles.dao.tempbitable.TestPojoSUD;
import com.nvest.user.databaseFiles.databaseWorkers.OtherTablesWorker;
import com.nvest.user.databaseFiles.databaseWorkers.PremiumRatesWorker;
import com.nvest.user.databaseFiles.databaseWorkers.ProductTableWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.CentralTendency;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = HomeScreenActivity.class.getSimpleName();
    private LinkedHashMap<String, String> argumentsPassed = new LinkedHashMap<>();
    private  TraditionalAsyncTask traditionalAsyncTask;
    private Button check;
    private RecyclerView recyclerView;
    private TempBIAdapter tempBIAdapter;
    private SyncHandlerFactory syncHandlerFactory;
    private APIInterface apiInterface;
    private ProductContentsAdapter productContentsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ProductContents> productContentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.log(TAG , "On Create started...");
        check = (Button) findViewById(R.id.check);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        check.setOnClickListener(v -> startLibActivity(123));
        productContentsList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);
        //createWorkRequest();
        addAnnotations();
        //LogUtils.evaluateExpression("[(2 - 3) / (99 - 2)] * 100");
        //LogUtils.log(TAG , "Fund strategy " + DatabaseAccess.getSingletonInstance().getFundMasterCount() );

        //DatabaseAccess.getSingletonInstance().getTableNames();



        LogUtils.log(TAG , "Creating raw queries");
          // new FundStrategyMasterAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        traditionalAsyncTask = new TraditionalAsyncTask(this);
        //traditionalAsyncTask.execute();
        try {
            syncHandlerFactory = new SyncHandlerFactory(this);
            //syncHandlerFactory.createWorkRequest();
            //syncHandlerFactory.insertIntoKeyValueStore();
            //syncHandlerFactory.createWorkRequest2();
        }
        catch (Exception e){
            LogUtils.log(TAG , "Some error occurred " );
        }


        //queryTesting();
        //queryTempData();

        //tempBITableSqliteHelper.createTempBiTable("DROP TABLE IF EXISTS '" + Config.TEMP_BI_TABLE + "'");

        /*String createTable = "CREATE TABLE " + Config.TEMP_BI_TABLE + " (" + Config.TEMP_BI_TABLE_PRIMARY_KEY + " INT PRIMARY KEY, " ;
        for (int i =0 ; i < createColumnsForTempBITable.getAttributeValues("Temp").size(); i++){
            createTable = createTable.concat(createColumnsForTempBITable.getAttributeValues("Temp").get(i) +  " TEXT NOT NULL, " );
            LogUtils.log(TAG , "Create string " + createTable);
        }


        createTable = createTable.concat("unique_key TEXT NOT NULL)");
        LogUtils.log(TAG , "Final Create string " + createTable);
        //tempBITableSqliteHelper.createTempBiTable(createTable);
       String example = "select [LI_ATTAINED_AGE],[POLICYYEAR], 90.69 as [PREMIUMRATE] , ( SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE ) as [MODE_FREQ] , ( SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id ) as [MODE_DISC] , ( @LI_ENTRY_AGE + PolicyYear ) as [LI_AGE] , ( SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA ) as [HSAD_Rate] , ( ((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000 ) as [ANN_PREM] , ( ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1) ) as [NSAP_VAL] , ( ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1) ) as [MODAL_PREM_WNSAP] , ( ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0) ) as [MODAL_PREM] , ( ROUND(((ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0)) * ((select isnull(Rate,0) as Rate from TaxStructure where getdate() between FromDate and isnull(Todate, getdate()) and PolicyYear between FromYear and ToYear and TaxGroup = isnull(@TaxGroup, 0) and TaxKeyword = 'TAX_1'))),0) ) as [TAX_MP] , ( (ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0))) + (ROUND(((ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0)) * ((select isnull(Rate,0) as Rate from TaxStructure where  getdate() between FromDate and isnull(Todate, getdate()) and PolicyYear between FromYear and ToYear and TaxGroup = isnull(@TaxGroup, 0) and TaxKeyword = 'TAX_1'))),0) ) as [MODAL_PREM_TAX] , ( ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)) ) as [LOAD_ANN_PREM] , ( (ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0)))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)) ) as [LOAD_ANN_PREM_NSAP] , ( CASE  WHEN PolicyYear  > 0 AND PolicyYear  <=@Pr_PPT THEN ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)))) ELSE 0 END ) as [ANN_PREM_YEARLY] , ( CASE  WHEN PolicyYear  > 0 AND PolicyYear  <=@Pr_PPT THEN (((ROUND(((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)) + (ROUND((CASE WHEN @NSAP_FLAG = 0 THEN 1.5 * @Pr_SA /1000 ELSE 0 END) * ((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id)),2,1))),0)))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)))) ELSE 0 END ) as [ANN_PREM_YEARLY_NSAP] , ( ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))* (CASE WHEN PolicyYear  < @Pr_PPT THEN PolicyYear  else @Pr_PPT END) ) as [CUM_LOAD_PREM] , ( CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))) ) as [GUAR_ADD] , ( ((CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) * PolicyYear ) as [ACCR_GUAR_ADD] , ( @Pr_SA + (((CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) * PolicyYear) ) as [DB_G] , ( CASE WHEN ((PolicyYear % 5 = 0)  AND (PolicyYear < @Pr_PT)) THEN 2*((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)))) ELSE 0 END ) as [MBACK] , ( CASE WHEN PolicyYear  = (@Pr_PT) THEN @Pr_SA + ((((CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) * PolicyYear)) -  (((PolicyYear/5)-1)*2*((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) ELSE 0 END ) as [MB_G] , ( (CASE WHEN ((PolicyYear % 5 = 0) AND (PolicyYear < @Pr_PT)) THEN 2*((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE)))) ELSE 0 END))+((CASE WHEN PolicyYear  = (@Pr_PT) THEN @Pr_SA + ((((CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) * PolicyYear)) -  (((PolicyYear/5)-1)*2*((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000) final )*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) ELSE 0 END) ) as [SB_G] , ( CASE WHEN PolicyYear >= 3 THEN  ((SELECT RATE FROM SV where ProductId = @Pr_Id AND PolicyYear = FromYear  AND PT = @Pr_PT AND AGE = @LI_Entry_Age) * (((CASE WHEN @Pr_PT = 10 THEN 0.04 WHEN @Pr_PT = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((@PREM_RATE - ((SELECT DiscountRate From LSADMaster where ProductId = @Pr_Id and @Pr_SA between FromSa and ToSA))) * (@Pr_SA))/1000))*((SELECT Multiplier FROM ProductMode WHERE ModeId=@INPUT_MODE AND ProductId=@Pr_Id))),2,1)),0))*(((SELECT Frequency from ModeMaster where ModeId = @INPUT_MODE))))) * PolicyYear)) ELSE 0 END ) as [Guar_ADD_SV] , ( SELECT Round(RATE,2) FROM Premiumrates where ProductId = @Pr_Id AND LAAGE = @LI_Entry_Age AND PT = @Pr_PT ) as [PremiumRate] , ( @Pr_PT ) as [PolicyTerm]";
        Matcher m = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])").matcher(example);

        while(m.find()) {
            LogUtils.log(TAG , "Matching " + m.group(1));
        }


        String alterTable = "ALTER TABLE " + Config.TEMP_BI_TABLE + " ADD COLUMN  TEXT ";
        LogUtils.log(TAG , "Alter table " + alterTable);*/
        //tempBITableSqliteHelper.createTempBiTable(alterTable);
        //hitserver();
        ArrayList<Integer> numbers = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < 15; i++) {
            numbers.add(r.nextInt(7)+1);
        }

        double mean = CentralTendency.arithmeticMean(numbers).doubleValue();
        double median = CentralTendency.median(numbers);
        ArrayList<Integer> mode = CentralTendency.mode(numbers);

        Collections.sort(numbers);

        String res = String.format("Numbers:\n\n%s\nMean: %.1f\nMedian: %.1f\nMode: %s\n",
                numbers, mean, median, mode);

        LogUtils.log(TAG, "Result " + res);
        startLibActivity(123);
    }

    public void startLibActivity(int productId){
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext().getPackageName(),"nvest.com.nvestlibrary.landing.LandingActivity"));
            intent.putExtra("productId", productId);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log(TAG, "Exception in intent " + e.toString());
        }
    }

    static {
        //System.loadLibrary("hello-jni");
        CommonMethod.log(TAG , "Loading sqlite x");
        //System.loadLibrary("sqliteX");
        //System.loadLibrary("hello-jni");
    }


    public void hitserver(){
        LogUtils.log(TAG , "Hit server for getting details");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Product> call2 = (Call< Product>) apiInterface.doGetUserList();
        call2.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                LogUtils.log(TAG , "Inside success " + response.isSuccessful());
                LogUtils.log(TAG , "Inside success " + response.isSuccessful());
                if(response.isSuccessful()){
                    Product product = response.body();
                    if(product != null){
                        LogUtils.log(TAG , "Product Size " + product.getProduct().size());
                        setDataToProductContentAdapter(product.getProduct());
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

    public void queryTesting(){
        try {
            TempBITableSqliteHelper tempBITableSqliteHelper= new TempBITableSqliteHelper(this);
            String query = "SELECT [LI_ATTAINED_AGE],[POLICYYEAR],90.73 AS [PREMIUMRATE] , ( (SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1) ) AS [MODE_FREQ] , ( (SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003) ) AS [MODE_DISC] , ( (30 + POLICYYEAR) ) AS [LI_AGE] , ( (SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA) ) AS [HSAD_RATE] , ( (((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000) ) AS [ANN_PREM] , ( ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2) ) AS [NSAP_VAL] , ( ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2) ) AS [MODAL_PREM_WNSAP] , ( ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0) ) AS [MODAL_PREM] , ( ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) ) AS [TAX_MP] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0))) + (ROUND(((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0)) ) AS [MODAL_PREM_TAX] , ( (ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM] , ( ((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))) ) AS [LOAD_ANN_PREM_NSAP] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY] , ( (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN (((ROUND(((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 500000 /1000 ELSE 0 END) * ((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003)),2))),0)))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [ANN_PREM_YEARLY_NSAP] , ( (((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END)) ) AS [CUM_LOAD_PREM] , ( (CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ) AS [GUAR_ADD] , ( ((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR ) AS [ACCR_GUAR_ADD] , ( (500000 + (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ) AS [DB_G] , ( (CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END) ) AS [MBACK] , ( (CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END) ) AS [MB_G] , ( ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 10)) THEN 2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1)))) ELSE 0 END))+((CASE WHEN POLICYYEAR  = (10) THEN 500000 + ((((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) ELSE 0 END)) ) AS [SB_G] , ( (CASE WHEN POLICYYEAR >= 3 THEN  ((5) * (((CASE WHEN 10 = 10 THEN 0.04 WHEN 10 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND((((((90.73 - ((SELECT DISCOUNTRATE FROM LSADMASTER WHERE PRODUCTID = 1003 AND 500000 BETWEEN FROMSA AND TOSA))) * (500000))/1000))*((SELECT MULTIPLIER FROM PRODUCTMODE WHERE MODEID=1 AND PRODUCTID=1003))),2)),0))*(((SELECT FREQUENCY FROM MODEMASTER WHERE MODEID = 1))))) * POLICYYEAR)) ELSE 0 END) ) AS [GUAR_ADD_SV] ,  ( SELECT 10 AS PTERM ) AS [POLICYTERM] FROM TEMPBI WHERE PRODUCTID=1003 AND UNIQUE_KEY='1003_1552631383654'";
            List<TestPojoSUD> testPojoSUDList = tempBITableSqliteHelper.getTempTablePOJO(query);
            if(testPojoSUDList.size() >0){
                LogUtils.log(TAG, "Query not null " + testPojoSUDList.size());
                for (int i = 0 ; i < testPojoSUDList.size();i++){
                    LogUtils.log(TAG , "Loading details at position  " + i);
                    LogUtils.log(TAG , "Cum load prem " + testPojoSUDList.get(i).getCUM_LOAD_PREM());
                    LogUtils.log(TAG , "DB_G " + testPojoSUDList.get(i).getDB_G());
                    LogUtils.log(TAG , "Age  " + testPojoSUDList.get(i).getLI_ATTAINED_AGE());
                }
                //LogUtils.log(TAG, "Premium rate " + testPojoSUD.getPREMIUMRATE());
                setDataToAdapter(testPojoSUDList);
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in query Testing " + e.toString());
        }
    }

    public void queryTempData(){
        try {
            TempBITableSqliteHelper tempBITableSqliteHelper= new TempBITableSqliteHelper(this);
            LogUtils.log(TAG , "Starting temp data " );
            tempBITableSqliteHelper.getTempData();
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in query Testing " + e.toString());
        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Product> call2 = (Call< Product>) apiInterface.getAdditionalProductsList();
        call2.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                LogUtils.log(TAG , "Inside success " + response.isSuccessful());
                LogUtils.log(TAG , "Inside success " + response.isSuccessful());
                if(response.isSuccessful()){
                    Product product = response.body();
                    if(product != null){
                        LogUtils.log(TAG , "Product Size " + product.getProduct().size());
                        LogUtils.updateCurrentDateTime();
                        setDataToProductContentAdapter(product.getProduct());
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


    public class FundStrategyMasterAsync extends AsyncTask<Void, Void, Void> {

        public FundStrategyMasterAsync() {
            LogUtils.log(TAG , "Startting async task");
        }

        @Override
        protected Void doInBackground(Void... voids) {

           /* FormulaRoom formulaRoom = RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().getFormulaById(2);
            LogUtils.log(TAG , "Formula room " + formulaRoom.getFormulaExtended());

            formulaRoom.setFormulaExtended("Select * from BonusGuaranteeCustom where PT = @PT and BonusGId = (@PT+4+@Fourteen) and productId = (@Fourteen + 976 + @PT)");
            RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().updateFormula(formulaRoom);
*/
            /*FormulaRoom formulaRoom = RoomDatabaseSingleton.getRoomDatabaseSession().formulasRoomDao().getFormulaById2(2002, "[PolicyTerm]", true, false);
            RoomRawQueryBuilder roomRawQueryBuilder = new RoomRawQueryBuilder(formulaRoom.getFormulaExtended(), argumentsPassed);
            String query = roomRawQueryBuilder.getSql();
            roomRawQueryBuilder.getArgCount();
            LogUtils.log(TAG , "Query " + query + "Args num " + roomRawQueryBuilder.getArgCount());

            PremiumRatesRoom premiumRatesRoom = RoomDatabaseSingleton.getRoomDatabaseSession().premiumRatesRoomDao().getPremiumById(6,12,2002, 20);
            LogUtils.log(TAG , "Premium rate id " + premiumRatesRoom.getPremiumRateId());*/
            /*SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);
            BonusGuaranteeRoom bonusGuaranteeRoom = RoomDatabaseSingleton.getRoomDatabaseSession().bonusGuaranteeRoomDao().runtimequery(simpleSQLiteQuery);
            LogUtils.log(TAG , "Bonus " + bonusGuaranteeRoom.getBonusgId() + " product id " + bonusGuaranteeRoom.getProductId());*/
/*
            int temptableCount = RoomDatabaseSingleton.getRoomDatabaseSession().tempBIRoomDao().getTempBICount();
            LogUtils.log(TAG , "Temp table count " + temptableCount);
            LogUtils.log(TAG , "Value " + RoomDatabaseSingleton.getRoomDatabaseSession().premiumRatesRoomDao().getValue());*/

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LogUtils.log(TAG , "inside on post execute");
        }
    }

    private void addAnnotations(){
        GenericDTO.addAttribute(Config.PT_ANNOTATION, "16");
        LogUtils.log(TAG , "Element by key " + GenericDTO.getAttributeValue(Config.PT_ANNOTATION));
        GenericDTO.addAttribute(Config.RD_PT_ANNOTATION, "14");
        GenericDTO.addAttribute(Config.LA_AGE_ANNOTATION, "29");
        GenericDTO.addAttribute(Config.PR_PT_ANNOTATION, "11");
        GenericDTO.addAttribute(Config.PRODUCT_ID_ANNOTATION, "1011");
        GenericDTO.addAttribute(Config.INPUT_MODE_ANNOTATION,"1");
        GenericDTO.addAttribute(Config.PRODUCT_SUM_ASSURED_ANNOTATION,"5000000");
        GenericDTO.addAttribute(Config.PR_PPT_ANNOTATION,"11");
        GenericDTO.addAttribute(Config.NSAP_FLAG_ANNOTATION,"1");
        GenericDTO.addAttribute(Config.TAX_GROUP_ANNOTATION,"1");
        GenericDTO.addAttribute(Config.PR_OPTION_1_ANNOTATION,"1");
        GenericDTO.addAttribute(Config.PR_OPTION_2_ANNOTATION,"3");
        GenericDTO.addAttribute(Config.OPTION_VALUE_3_ANNOTATION,"50000");
        GenericDTO.addAttribute(Config.FUND_STRATEGY_ID_ANNOTATION,"1");
        GenericDTO.addAttribute(Config.PR_ANN_PREMIUM_ANNOTATION,"500000");
        GenericDTO.addAttribute(Config.IS_NULL,Config.IF_NULL);
        GenericDTO.addAttribute(Config.GET_DATE,Config.DATE_NOW);
        GenericDTO.addAttribute(Config.CEILING,Config.ROUND);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(traditionalAsyncTask != null && traditionalAsyncTask.getStatus() != AsyncTask.Status.RUNNING){
            LogUtils.log(TAG , "Dispose all events");
            traditionalAsyncTask.dismiss();
        }
    }

    public void setDataToAdapter(List<TestPojoSUD> testPojoSUDList){
        tempBIAdapter = new TempBIAdapter(testPojoSUDList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tempBIAdapter);
    }

    public void setDataToProductContentAdapter(List<ProductContents> productContentsListPassed){
        LogUtils.log(TAG , "Setting data to product contents");
        productContentsList.addAll(productContentsListPassed);
        productContentsAdapter= new ProductContentsAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productContentsAdapter);
        productContentsAdapter.setProductContents(productContentsList);
    }
}
