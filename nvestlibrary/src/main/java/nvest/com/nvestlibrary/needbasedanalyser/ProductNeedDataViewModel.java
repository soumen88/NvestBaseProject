package nvest.com.nvestlibrary.needbasedanalyser;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.landing.User;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.PTPPTGoal;
import nvest.com.nvestlibrary.nvestWebModel.Productneed;

public class ProductNeedDataViewModel extends AndroidViewModel implements NeedBasedListeners{
//validateInformationDataViewModel.NeedAdvisory(31, 35, 15,1,1000000);
    private static String TAG = ProductNeedDataViewModel.class.getSimpleName();
    private HashMap<String, String> Params = new HashMap<>();
    private int inputCatTypeId = 31;
    private int inputAge = 35;
    private int inputPt = 15;
    private int Mode = 1;
    private double TargetBenefit = 1000000;
    private ArrayList<Productneed> prodneedlist = new ArrayList<>();
    private ArrayList<Integer> optList = new ArrayList<>();
    private int PTout = 0;
    private int PPTout = 0;
    private int MPout = 0 ;
    private int SAout = 0 ;
    private int MIout = 0 ;
    private int APout = 0 ;
    private int SAMFout = 0 ;
    private int ProductId = 0;
    private int PT = 0, PPT = 0, PtPptId = 0;
    private int OptionId = 0;
    private int optionlevelid = 0 ;
    private Productneed p1;
    private NeedBasedListeners needBasedListeners;
    private static final String FAILED = "Failed";
    private static final String COMPLETE = "Complete";
    private static final String ON_NEXT = "Next";
    private int PTdevup = 5;
    private int PTdevdown = 5;
    private int PPTdevup = 5;
    private int PPTdevdown = 5;
    private HashMap<String, Integer> PTValues = new HashMap<>();
    private HashMap<String, Integer> PPTValues = new HashMap<>();
    private String PTFormulalist = "";
    private String PPTFormulalist = "";
    private String PTValuelist = "";
    private String PPTValuelist = "";
    private String Errormessage = "";
    private String PPTQuery = "0";
    private String PTQuery = "0";
    private ArrayList<PTPPTGoal> ptpptgoal = new ArrayList<>();
    private int inputPPT = inputPt;
    private int singleprod = 0;
    private int OptionLevelId = 0;
    private HashMap<String, String> prodoutput = new HashMap<>();
    private double ModeDisc = 0.0;
    private int ModeFreq = 0;
    private String Options = "";
    private String unique;
    private LinkedHashMap<Integer, String> stringHashMap = new LinkedHashMap<>();
    private LinkedHashMap<Integer, Integer> saveValue = new LinkedHashMap<>();
    public ProductNeedDataViewModel(@NonNull Application application) {
        super(application);
    }
    int pTerm = 0;

    public HashMap<String, String> productneed() {
        HashMap<String, String> params = new HashMap<>();
        Params.put(NvestLibraryConfig.PR_PT_ANNOTATION, String.valueOf(inputPt));
        PTFormulalist = "";
        PPTFormulalist = "";
        PTValuelist = "";
        PPTValuelist = "";
        Errormessage = "";
        PPTQuery = "0";
        PTQuery = "0";
        Options = "";
        unique = "";
        //setObserver();
        //setOperation();
        //setStringOperation();
        getNeedList();

        return params;
    }

    private void setOperation(){
        Observable.merge( productModeObservable(), productNeedInputs(), inputOutputMasterObservable())

                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String str) {
                        CommonMethod.log(TAG , "On next value " + str);
                        switch(str)
                        {
                            case FAILED:
                                CommonMethod.log(TAG , "Execution has stopped because " + str);
                                break;
                            default:
                                System.out.println("no match");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "Error ");
                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "Insdied on complete");
                        setStringOperation();
                    }
                });
    }

    private void getNeedList(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String Query = "select productid,optionlevelid,optionid from prodneedmap where needid=" + inputCatTypeId;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                prodneedlist = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    int productid = cursor.getInt(0);
                    optionlevelid = cursor.getInt(1);
                    OptionId = cursor.getInt(2);
                    Productneed p1 = new Productneed();
                    p1.setProductId(productid);
                    p1.setOptionLevelId(optionlevelid);
                    p1.setOptionId(OptionId);
                    prodneedlist.add(p1);
                }
            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "Product Need list size " + prodneedlist.size());
                        if(prodneedlist.size() > 0){
                            startProductNeedProcess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });

    }


    private void startProductNeedProcess(){
        ProductId = prodneedlist.get(0).getProductId();
        OptionLevelId = prodneedlist.get(0).getOptionLevelId();
        OptionId = prodneedlist.get(0).getOptionId();
        CommonMethod.log(TAG , "Product id " + ProductId + " Option level " + OptionLevelId + " Option id " + OptionId);
        Params.put("@PR_ID", String.valueOf(ProductId));
        Params.put("@LI_ENTRY_AGE", String.valueOf(inputAge));
        Params.put("@INPUT_MODE", String.valueOf(Mode));
        setOperation();
    }

    private void setStringOperation(){
        CommonMethod.log(TAG , "String operation started " +  +  System.nanoTime());
        formulaObservable();
        Observable.merge(setPt(), setPPt(), calculatePolicyTermForTempBi())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        CommonMethod.log(TAG  , "Inside string on next " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG , "Error occurred " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "Inside on String on compelete");
                        tempBi();
                        readPtPPTMaster();
                        biPreProcessing();
                    }
                });
    }

    private void biPreProcessing(){
        CommonMethod.log(TAG , "Bi pre processing started");
        Observable.merge(ageCalculation() , productMaster())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        CommonMethod.log(TAG , "On next " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "On complete");
                    }
                });
    }
    private void setObserver(){
        Observable.merge(getMaleObservable(), getFemaleObservable(), getOtherObservable(),getMaleObservable())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        CommonMethod.log(TAG , "On next " + user.getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "On complete");
                    }
                });
    }

    private Observable<User> getFemaleObservable() {
        String[] names = new String[]{"Lucy", "Scarlett", "April"};

        final List<User> users = new ArrayList<>();
        for (String name : names) {
            User user = new User();
            user.setName(name);
            user.setGender("female");

            users.add(user);
        }
        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {

                        CommonMethod.log(TAG , "Female Thread name " + Thread.currentThread().getName());
                        for (User user : users) {
                            if (!emitter.isDisposed()) {
                                Thread.sleep(1000);
                                emitter.onNext(user);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }

    private Observable<User> getMaleObservable() {
        String[] names = new String[]{"Mark", "John", "Trump", "Obama"};

        final List<User> users = new ArrayList<>();

        for (String name : names) {
            User user = new User();
            user.setName(name);
            user.setGender("male");

            users.add(user);
        }
        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        CommonMethod.log(TAG , "Male Thread name " + Thread.currentThread().getName());
                        for (User user : users) {
                            if (!emitter.isDisposed()) {
                                Thread.sleep(500);
                                emitter.onNext(user);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.io());

    }

    private Observable<User> getOtherObservable() {
        String[] names = new String[]{"Person A", "Person B", "Person C", "Person D"};

        final List<User> users = new ArrayList<>();

        for (String name : names) {
            User user = new User();
            user.setName(name);
            user.setGender("male");

            users.add(user);
        }
        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        CommonMethod.log(TAG , "Other Thread name " + Thread.currentThread().getName());
                        for (User user : users) {
                            if (!emitter.isDisposed()) {
                                Thread.sleep(500);
                                emitter.onNext(user);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                })
                .subscribeOn(Schedulers.io());

    }


    private Observable<String> productModeObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , "Product mode Thread name " + Thread.currentThread().getName());
                String Query = "select  Multiplier,Frequency from productmode inner join ModeMaster on productmode.ModeId= ModeMaster.ModeId where productid= '"+ProductId+"' and productmode.ModeId=" + Mode;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor == null) {
                    Params.put("Remarks", "This product does not satisfy the given criteria");
                    emitter.onNext(FAILED + "This product does not satisfy the given criteria");
                }
                cursor.moveToFirst();
                ModeDisc = cursor.getDouble(0);
                ModeFreq = cursor.getInt(1);

                Params.put("@MODE_FREQ", String.valueOf(ModeFreq));
                Params.put("@MODE_DISC", String.valueOf(ModeDisc));
                CommonMethod.log(TAG , "Mode frequency " + ModeFreq);
                CommonMethod.log(TAG , "Mode discount " + ModeDisc);
                if (OptionId > 0) {
                    Params.put("@PR_OPTION_" + OptionLevelId, String.valueOf(OptionId));
                    optList.add(OptionId);
                    Options = Options + "," + OptionId;
                }
                /*if (!Options.isEmpty()) {
                    Options = Options.substring(1);
                }*/
                CommonMethod.log(TAG , "Options selected " + Options);
                if (!emitter.isDisposed()) {
                    emitter.onNext(ON_NEXT);
                    emitter.onComplete();
                }


            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<String> productNeedInputs() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , "Product need Thread name " + Thread.currentThread().getName());
                String Query = "select PName,PValue from ProdNeedDefaultInputs where ProductId = " + ProductId;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String pname = cursor.getString(0);
                        String pvalue = cursor.getString(1);
                        if (!Params.containsKey(pname)) {
                            Params.put(pname.toUpperCase(), pvalue);
                            if (pname.toUpperCase().contains("@PR_OPTION")) {
                                optList.add(Integer.valueOf(pvalue));
                                Options = Options + "," + Integer.valueOf(pvalue);
                            }
                        }
                    }
                    if (!Options.equals("")) {
                        Options = Options.substring(1);
                    }
                }
                else {
                    emitter.onNext(FAILED + "Product need is empty");
                }
                CommonMethod.log(TAG , "Option list size " +  optList.size());
                if(!emitter.isDisposed()){
                    emitter.onNext(ON_NEXT);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());

    }

    private Observable<String> productNeedListObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , "Product mode Thread name " + Thread.currentThread().getName());

                if(!emitter.isDisposed()){
                    emitter.onNext(ON_NEXT);
                    emitter.onComplete();
                }

            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<String> inputOutputMasterObservable() {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , "Product mode Thread name " + Thread.currentThread().getName());
                String Query = "Select * from InputOutputMaster where ProductId = " + ProductId + " ORDER BY ROWID ASC LIMIT 1";
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                PTout = cursor.getInt(cursor.getColumnIndex("PT"));
                PPTout = cursor.getInt(cursor.getColumnIndex("PPT"));
                SAout = cursor.getInt(cursor.getColumnIndex("SA"));
                APout = cursor.getInt(cursor.getColumnIndex("AnnualPremium"));
                MPout = cursor.getInt(cursor.getColumnIndex("ModalPremium"));
                MIout = cursor.getInt(cursor.getColumnIndex("MI"));
                SAMFout = cursor.getInt(cursor.getColumnIndex("SAMF"));
                CommonMethod.log(TAG , "PPT OUT " + PPTout);
                CommonMethod.log(TAG , "PT OUT " + PTout);
                CommonMethod.log(TAG , "First step is complete " + emitter.isDisposed());
                if(!emitter.isDisposed()){
                    emitter.onNext("Yes");
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    private void formulaObservable() {
        String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
        int PTinForm = 0, PPTinForm = 0, SAinForm = 0, APinForm = 0, MPinForm = 0, MIinForm = 0, SAMFinForm = 0, PT = 0, PPT = 0, PtPptId = 0;

        String Query = "select * from " + FormulaTable + " where productid = " + ProductId;
        Cursor cursorFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        String TaxMPQuery = "0", SAQuery = "0", APQuery = "0", MPQuery = "0", MIQuery = "0", SAMFQuery = "0", LoadAPQuery = "0", PremiumRate = "0";
        for (cursorFormulas.moveToFirst(); !cursorFormulas.isAfterLast(); cursorFormulas.moveToNext()) {
            String Formulakeyword = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaKeyword"));
            String FormulaExtended = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaExtended"));
            if (Formulakeyword.toUpperCase().equals("[PR_PT]")) {
                PTinForm = 1;
                PTQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[PR_PPT]")) {
                PPTinForm = 1;
                PPTQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[SA]")) {
                SAinForm = 1;
                SAQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[ANN_PREM]")) {
                APinForm = 1;
                APQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[MODAL_PREM]")) {
                MPinForm = 1;
                MPQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[MI]")) {
                MIinForm = 1;
                MIQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[SAMF]")) {
                SAMFinForm = 1;
                SAMFQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[PREMIUMRATE]")) {
                PremiumRate = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[TAX_MP]")) {
                TaxMPQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[LOAD_ANN_PREM]")) {
                LoadAPQuery = "Select (" + FormulaExtended + ")";
            } else {
            }
        }

        CommonMethod.log(TAG , "PT Query " + PTQuery);
        CommonMethod.log(TAG , "PPT Query " + PPTQuery);

        /*return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                if(!emitter.isDisposed()){
                    emitter.onNext("Yes");
                    emitter.onComplete();
                }

            }
        }).subscribeOn(Schedulers.io());*/
    }

    private Observable<String> setPPt() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , " PPT out value " + PPTout);
                if (PPTout == 2) {
                    try {
                        String Query = CommonMethod.ReplaceParams(PPTQuery, Params, true);
                        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            PPT = cursor.getInt(0);
                        }
                    } catch (Exception ex) {
                    }
                    if (PPT >= inputPPT - PPTdevdown && PPT <= inputPPT + PPTdevup) {
                        PPTValues.put("", PPT);
                        Params.put("Remarks", "This product doesnot satisfy the given criteria");
                        emitter.onNext(FAILED + "This product doesnot satisfy the given criteria");
                    }

                }
                else if (PPTout == 0) {
                    PPT = 0;
                    PPTValues.put("", PPT);
                }
                else if (PPTout == 1) {
                    String Query = "SELECT distinct pptformula FROM ptpptmaster WHERE productid = " + ProductId + " AND (optionid = " + OptionId + " or " + OptionId + " <= 0 or optionid = null) AND (PTFormula= null or PTFormula='' or PTFormula in (" + PTFormulalist + ")) ";
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String PPTFormula = cursor.getString(0);
                        try {
                            PPT = Integer.parseInt(PPTFormula);
                        } catch (Exception e) {
                            Query = CommonMethod.ReplaceParams(PPTFormula, Params, true);
                            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                PPT = cursor.getInt(0);
                            }
                        }
                        if (PPT >= inputPPT - PPTdevdown && PPT <= inputPPT + PPTdevup) {
                            PPTValues.put(PPTFormula, PPT);
                            PPTFormulalist = PPTFormulalist + "," + PPTFormula;
                        }
                    }
                }
                if (!PPTFormulalist.isEmpty()) {
                    PPTFormulalist = PPTFormulalist.substring(1);
                }
                CommonMethod.log(TAG , "PPT formula list " + PPTFormulalist);
                if(!emitter.isDisposed()) {
                    emitter.onNext(ON_NEXT);
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }

    private Observable<String> setPt() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CommonMethod.log(TAG , " PT out value " + PTout);
                if (PTout == 2) {
                    try {
                        String Query = CommonMethod.ReplaceParams(PTQuery, Params, true);
                        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            PT = cursor.getInt(0);
                        }
                    } catch (Exception ex) {
                    }
                    if (PT >= inputPt - PTdevdown && PT <= inputPt + PTdevup) {
                        PTValues.put("", PT);
                    } else {
                        Params.put("Remarks", "This product doesnot satisfy the given criteria");
                        emitter.onNext(FAILED + "This product doesnot satisfy the given criteria");
                    }
                }
                else if (PTout == 0) {
                    PT = 0;
                    PTValues.put("", PT);
                }
                else if (PTout == 1) {
                    String Query = "SELECT distinct ptformula FROM ptpptmaster WHERE productid = " + ProductId + " AND (optionid = " + OptionId + " or " + OptionId + " <= 0 or optionid = null) ";
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String PTFormula = cursor.getString(0);
                        try {
                            PT = Integer.parseInt(PTFormula);
                        } catch (Exception e) {
                            Query = CommonMethod.ReplaceParams(PTFormula, Params, true);
                            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                PT = cursor.getInt(0);
                            }
                        }
                        if (PT >= inputPt - PTdevdown && PT <= inputPt + PTdevup) {
                            PTValues.put(PTFormula, PT);
                            PTFormulalist = PTFormulalist + "," + PTFormula;
                        }

                    }
                }

                if (!PTFormulalist.isEmpty()) {
                    PTFormulalist = PTFormulalist.substring(1);
                }
                CommonMethod.log(TAG , "Set PPT formula list " + PPTFormulalist);
                CommonMethod.log(TAG , "Set PT formula list " + PTFormulalist);
                if(!emitter.isDisposed()) {
                    emitter.onNext(ON_NEXT);
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }

    private void readPtPPTMaster() {
        String Query = "select distinct * from PtPptMaster where productid = " + ProductId + " and (PTFormula=null or PTFormula='' or PTFormula in (" + PTFormulalist + ")) and (PPTFormula=null or PPTFormula='' or PPTFormula in (" + PPTFormulalist + "))";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if(cursor != null &&  cursor.getCount() != 0){
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String PPTFormula = cursor.getString(cursor.getColumnIndex("PPTFormula"));
                String PTFormula = cursor.getString(cursor.getColumnIndex("PTFormula"));
                String PPTDisplay = cursor.getString(cursor.getColumnIndex("PPTDisplay"));
                String PTDisplay = cursor.getString(cursor.getColumnIndex("PTDisplay"));
                PTPPTGoal ptpptobj = new PTPPTGoal();
                ptpptobj.ProductId = ProductId;
                ptpptobj.PPTDisplay = PPTDisplay;
                ptpptobj.PPTValue = PPTValues.get(PPTFormula);
                ptpptobj.PTDisplay = PTDisplay;
                ptpptobj.PTValue = PTValues.get(PTFormula);
                ptpptgoal.add(ptpptobj);
            }
        }
    }

    private Observable<String> ageCalculation() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                ArrayList<PTPPTGoal> ptpptvalidage = new ArrayList<>();
                for (int i = 0; i < ptpptgoal.size(); i++) {
                    int ptval = ptpptgoal.get(i).PTValue;
                    int pptval = ptpptgoal.get(i).PPTValue;
                    //Age Master Validation

                    String ageqry = "SELECT * FROM agemaster where productid=" + ProductId + " and ((";
                    ageqry = ageqry + ptval + ">= FromPt and " + ptval + "<= ToPt) or FromPt isnull ) and ((";
                    ageqry = ageqry + pptval + ">=FromPpt and " + pptval + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + ")) and ((";
                    ageqry = ageqry + inputAge + ">=MinEntryAge ) or MinEntryAge isnull ) and ((";
                    ageqry = ageqry + inputAge + "<=MaxEntryAge ) or MaxEntryAge isnull ) and ((";
                    ageqry = ageqry + (inputAge + ptval) + ">=MinMaturityAge ) or MinMaturityAge isnull ) and ((";
                    ageqry = ageqry + (inputAge + ptval) + "<=MaxMaturityAge ) or MaxMaturityAge isnull )";

                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(ageqry);
                    if (cursor != null && cursor.getCount() > 0) {
                        ptpptvalidage.add(ptpptgoal.get(i));
                    }
                }
                if (ptpptvalidage.size() == 0) {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    emitter.onNext(FAILED + "This product doesnot satisfy the given criteria");
                }

                //Closest PT
                int distance = Math.abs(ptpptvalidage.get(0).PTValue - inputPt);
                int idx = 0;
                for (int i = 1; i < ptpptvalidage.size(); i++) {
                    int cdistance = Math.abs(ptpptvalidage.get(i).PTValue - inputPt);
                    if (cdistance < distance) {
                        idx = i;
                        distance = cdistance;
                    }
                }
                PT = ptpptvalidage.get(idx).PTValue;
                String PTDisplay = ptpptvalidage.get(idx).PTDisplay;

                //Closest PPT
                idx = 0;
                int cdistance = 100;
                for (int i = 0; i < ptpptvalidage.size(); i++) {
                    int ptval = ptpptvalidage.get(i).PTValue;
                    if (ptval == PT) {
                        int pptval = ptpptvalidage.get(i).PPTValue;
                        distance = Math.abs(pptval - inputPPT);
                        if (distance < cdistance) {
                            idx = i;
                            distance = cdistance;
                        }
                    }
                }
                PPT = ptpptvalidage.get(idx).PPTValue;
                String PPTDisplay = ptpptvalidage.get(idx).PPTDisplay;
                Params.put("@PR_PT", String.valueOf(PT));
                Params.put("@PR_PPT", String.valueOf(PPT));
                Params.put("@DISPLAY_PR_PT", PTDisplay);
                Params.put("@DISPLAY_PR_PPT", PPTDisplay);
                CommonMethod.log(TAG  , "PT value " + PT);
                CommonMethod.log(TAG  , "PPT value " + PPT);
                if(!emitter.isDisposed()) {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }



    private Observable<String> productMaster() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String Query = "select Platform from  ProductMaster where productid = " + ProductId;
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                String Platform = cursor.getString(0);
                int annualmodeid = 1;
                CommonMethod.log(TAG , "Options before " + Options);
                /*if (!Options.isEmpty()) {
                    Options = Options.substring(1);
                }*/
                CommonMethod.log(TAG , "Options after " + Options);
                // Find min and max Modal Premium
                String prqry = " SELECT Interval,MinPremium,MaxPremium FROM [PremiumMaster] where productid=" + ProductId + " and ((";
                prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
                prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
                prqry = prqry + inputAge + ">= FromAge and " + inputAge + "<=ToAge) or FromAge isnull ) and ((";
                prqry = prqry + annualmodeid + "=ModeId) or ModeId isnull)  ORDER BY ROWID ASC LIMIT 1";

                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
                CommonMethod.log(TAG , "Pr query " + prqry);
                if (cursor == null || cursor.getCount() == 0) {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    emitter.onNext(FAILED + "This product doesnot satisfy the given criteria");
                }
                cursor.moveToFirst();
                double Interval = cursor.getDouble(0);
                double MinPremium = cursor.getDouble(1);
                double MaxPremium = cursor.getDouble(2);

                double minValue = Math.max(Math.round(TargetBenefit / (ModeFreq * PT)), MinPremium);
                CommonMethod.log(TAG , "Min value " + minValue);
                int cntloop = 0;
                int maxloopcnt = 10;
                while (cntloop < maxloopcnt && (minValue <= MaxPremium || MaxPremium == 0) && (minValue >= MinPremium || MinPremium == 0)) {
                    Params.remove("@PR_ANNPREM");
                    Params.put("@PR_ANNPREM", String.valueOf(minValue));
                    GenerateBI(Params);
                    cntloop = 110;
                    /*if (!Platform.equals("4")) {
                        CommonMethod.log(TAG , "Can start generate bi here... ");
                        Params.remove("@PR_MODALPREM");
                        Params.put("@PR_MODALPREM", String.valueOf(minValue));
                        if (!Platform.equals("4")) {

                        }
                        cntloop++;
                    }*/
                }
                if(!emitter.isDisposed()) {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }


    private Observable<String> calculatePolicyTermForTempBi() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    String Query = "select FormulaExtended from " + NvestLibraryConfig.FORMULAS_SQLITE_TABLE + " where productid = " + ProductId + " and formulakeyword = '[PolicyTerm]'";
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    cursor.moveToFirst();
                    String PolicyTermQuery = cursor.getString(0);
                    Query = CommonMethod.ReplaceParams("SELECT (" + PolicyTermQuery + ") as PolicyTerm", Params, false);
                    Cursor PTermcursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    PTermcursor.moveToFirst();
                    pTerm = PTermcursor.getInt(PTermcursor.getColumnIndex("POLICYTERM"));
                    CommonMethod.log(TAG , "Policy Term " + pTerm);
                } catch (Exception e) {
                    CommonMethod.log(TAG, "Policy Term cannot be calculated for gove inputs");
                }
                if(!emitter.isDisposed()) {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }


    private Observable<String> dummmyObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if(!emitter.isDisposed()) {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }    ).subscribeOn(Schedulers.io());
    }

    @Override
    public void stepOneComplete() {

    }



    public LinkedHashMap<Integer, HashMap<String, String>> GenerateBI(HashMap<String, String> Param) {
        try {
            CommonMethod.log(TAG , "Generate BI Thread name " + Thread.currentThread().getName());
            int queryCounter = 0 ;
            CommonMethod.log(TAG , "Inside generate BI");
            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            int ProductId = Integer.parseInt(Param.get("@PR_ID"));
            //int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));

            String Query = "select * from " + FormulaTable + " where productid = " + ProductId + " and (isoutput=1)";
            Cursor lstFormula = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            CommonMethod.log(TAG , "Policy Term Calculation started");
            //PolicyTerm Calculation

            CommonMethod.log(TAG , "Premium Rate calculation");
            //Premium Rate Calculation if not present in params
            if (!Param.containsKey("@PREM_RATE")) {
                float pRate = 0;
                try {

                    Query = "select FormulaExtended from " + FormulaTable + " where productid = " + ProductId + " and formulakeyword = '[PremiumRate]'";
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    cursor.moveToFirst();
                    String PremiumRate = cursor.getString(0);

                    Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        pRate = cursor.getFloat(0);
                    }
                } catch (Exception e) {
                    pRate = 0;
                }
                Param.put("@PREM_RATE", Float.toString(pRate));
            }

            //TempBI Creation
            /*String unique = UUID.randomUUID().toString().toUpperCase();
            int m = 1;
            m = 1;
            Query = "Insert into TempBI ";
            for (int i = 0; i < pTerm; i++) {
                int LI_ATTAINED_AGE = Age + i - 1;
                int PolicyYear = i + 1;
                Query = Query + " select 1," + ProductId + "," + PolicyYear + "," + LI_ATTAINED_AGE + ",'" + unique + "'";
                if (i + 1 < pTerm) {
                    Query = Query + " UNION ";
                }
            }
            try {
                NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query.toUpperCase());
            } catch (Exception ex) {
                CommonMethod.log(TAG, "Error in generating TempBI");
            }*/
            Param.put("@UNIQUEKEY", unique);
            Param.put(NvestLibraryConfig.PR_PT_ANNOTATION, String.valueOf(ProductId));
            Param.put(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, String.valueOf(TargetBenefit));
            CommonMethod.log(TAG , "Bonus scr");
            //Interest Rate List
            Query = "select * from BonusScr where productid = " + ProductId;
            Cursor IntRateList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            LinkedHashMap<Integer, Float> IntRate = new LinkedHashMap<>();
            if (IntRateList.getCount() == 0) {
                CommonMethod.log(TAG, "Cannot find interest rate scenarios");
            } else {

                for (IntRateList.moveToFirst(); !IntRateList.isAfterLast(); IntRateList.moveToNext()) {
                    Float BonusValue = IntRateList.getFloat(IntRateList.getColumnIndex("BonusValue"));
                    int BonusScrId = IntRateList.getInt(IntRateList.getColumnIndex("BonusScrId"));
                    IntRate.put(BonusScrId, BonusValue);
                }
            }
            CommonMethod.log(TAG , "Final Query Creation");
            //FinalQuery Creation
            String Query2 = "Select [LI_ATTAINED_AGE],[POLICYYEAR] ";

            for (lstFormula.moveToFirst(); !lstFormula.isAfterLast(); lstFormula.moveToNext()) {
                String Formulaextended2 = lstFormula.getString(lstFormula.getColumnIndex("FormulaExtended"));
                String FormulaKeyword = lstFormula.getString(lstFormula.getColumnIndex("FormulaKeyword")).toUpperCase();
                //CommonMethod.log(TAG , "Formula extended " + Formulaextended);
                Pattern p = Pattern.compile("\\((.*?)\\)");
                if(Pattern.compile(Pattern.quote("Select"), Pattern.CASE_INSENSITIVE).matcher(Formulaextended2).find() && !Formulaextended2.contains("POLICYYEAR")){
                    Matcher m1 = p.matcher(Formulaextended2);
                    while(m1.find()) {
                        int a = m1.group().indexOf("SELECT");
                        if( a > 0){
                            String query123 = m1.group().substring(a, m1.group().lastIndexOf(")"));
                            //CommonMethod.log(TAG , "Output "  + query123 );

                            if(!saveValue.containsKey(query123.hashCode())){
                                String subQuery = CommonMethod.ReplaceParams(query123, Param, false);
                                //CommonMethod.log(TAG , "Sub query " + subQuery);
                                Cursor c = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(subQuery);
                                if(c != null && c.getCount() > 0){
                                    c.moveToFirst();
                                    //CommonMethod.log(TAG , "Value fetched from sub query " + c.getInt(0));
                                    saveValue.put(query123.hashCode(), c.getInt(0));
                                    Formulaextended2 = Formulaextended2.replaceAll(query123, String.valueOf(c.getInt(0)));
                                }
                            }
                            else{
                               // CommonMethod.log(TAG  , "Hash map already has " + query123);
                                queryCounter++;
                                int value  = saveValue.get(query123.hashCode());
                                //CommonMethod.log(TAG , "Value " + value);
                                Formulaextended2 = Formulaextended2.replaceAll(query123, String.valueOf(value));
                            }
                            //System.out.println("Last index of " + m1.group().lastIndexOf(")")) ;
                        }
//                    System.out.println("Index " + a);
//                    System.out.println(m1.group(1));

                    }
                }

                if (Formulaextended2.toUpperCase().contains("@BONUS_SCR")) {
                    int i = 1;
                    for (Map.Entry<Integer, Float> entry : IntRate.entrySet()) {
                        String BonusScrId = String.valueOf(entry.getKey());
                        String BonusValue = String.valueOf(entry.getValue());
                        Query2 += ", (" + Formulaextended2.toUpperCase().replace("@BONUS_SCR", BonusScrId) + ") as " + FormulaKeyword.replace("]", "").toUpperCase() + "_BS_" + i + "]";
                        i++;
                    }
                } else {
                    Query2 += ", " + Formulaextended2 + " as " + FormulaKeyword;
                }
            }
            CommonMethod.log(TAG , "Query counter " + queryCounter);
            Query2 += " from TempBI where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY order by [POLICYYEAR]";
            String finalQuery2 = CommonMethod.ReplaceParams(Query2, Param, false);
            //CommonMethod.logQuery(TAG , finalQuery2 );
            List<HashMap<String, Float>> dtData = new ArrayList<>();
            //Final Query Execution
            long start = System.currentTimeMillis();
            CommonMethod.log(TAG , "Final Query Execution started " + start);
            LinkedHashMap<Integer, HashMap<String, String>> dt = new LinkedHashMap<>();
            try {
                finalQuery2 = "SELECT [LI_ATTAINED_AGE],[POLICYYEAR] , (1) AS [MODE_FREQ], (1) AS [MODE_DISC], (35 + POLICYYEAR) AS [LI_AGE], (3) AS [HSAD_RATE], (79500) AS [ANN_PREM], (0.0) AS [NSAP_VAL], (79500) AS [MODAL_PREM_WNSAP], (79500) AS [MODAL_PREM], ROUND(((ROUND(((ROUND(79500,2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 1000000.0 /1000 ELSE 0 END) * ((1)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0) AS [TAX_MP], ((ROUND(((ROUND(79500,2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 1000000.0 /1000 ELSE 0 END) * ((1)),2))),0))) + (ROUND(((ROUND(((ROUND(79500,2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 1000000.0 /1000 ELSE 0 END) * ((1)),2))),0)) * ((SELECT IFNULL(RATE,0) AS RATE FROM TAXSTRUCTURE WHERE DATE('NOW') BETWEEN FROMDATE AND IFNULL(TODATE, DATE('NOW')) AND POLICYYEAR BETWEEN FROMYEAR AND TOYEAR AND TAXGROUP = IFNULL(1, 0) AND TAXKEYWORD = 'TAX_1'))),0)) AS [MODAL_PREM_TAX], (79500) AS [LOAD_ANN_PREM], (79500) AS [LOAD_ANN_PREM_NSAP], (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN ((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END) AS [ANN_PREM_YEARLY], (CASE  WHEN POLICYYEAR  > 0 AND POLICYYEAR  <=10 THEN (((ROUND(((ROUND(79500,2)) + (ROUND((CASE WHEN 1 = 0 THEN 1.5 * 1000000.0 /1000 ELSE 0 END) * ((1)),2))),0)))*(((1)))) ELSE 0 END) AS [ANN_PREM_YEARLY_NSAP], (((ROUND((ROUND(79500,2)),0))*(((1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END)) AS [CUM_LOAD_PREM], (CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1)))) AS [GUAR_ADD], ((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR AS [ACCR_GUAR_ADD], (1000000.0 + (((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR)) AS [DB_G], (CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END) AS [MBACK], (CASE WHEN POLICYYEAR  = (1003) THEN 1000000.0 + ((((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND(79500,2)),0))*(((1))))) ELSE 0 END) AS [MB_G], ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END))+((CASE WHEN POLICYYEAR  = (1003) THEN 1000000.0 + ((((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR)) -  (((POLICYYEAR/5)-1)*2*((ROUND((ROUND(79500,2)),0))*(((1))))) ELSE 0 END)) AS [SB_G], ((SELECT SUM((((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END))))  FROM TEMPBI T1 WHERE T1.POLICYYEAR <= TEMPBI.POLICYYEAR AND T1.UNIQUEKEY = TEMPBI.UNIQUEKEY)) - ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END)) AS [ACCR_BEN], (CASE WHEN POLICYYEAR >= 3 THEN  ((SELECT RATE FROM SV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 1003 AND AGE = 35) * (((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR)) ELSE 0 END) AS [GUAR_ADD_SV], (CASE WHEN POLICYYEAR <= 1003 THEN  (((SELECT RATE FROM GSV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 1003) * ((((ROUND((ROUND(79500,2)),0))*(((1))))* (CASE WHEN POLICYYEAR  < 10 THEN POLICYYEAR  ELSE 10 END)))) + ((CASE WHEN POLICYYEAR >= 3 THEN  ((SELECT RATE FROM SV WHERE PRODUCTID = 1003 AND POLICYYEAR = FROMYEAR  AND PT = 1003 AND AGE = 35) * (((CASE WHEN 1003 = 10 THEN 0.04 WHEN 1003 = 15 THEN 0.05 ELSE 0.06 END) * ((ROUND((ROUND(79500,2)),0))*(((1))))) * POLICYYEAR)) ELSE 0 END)) - (((SELECT SUM((((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END))))  FROM TEMPBI T1 WHERE T1.POLICYYEAR <= TEMPBI.POLICYYEAR AND T1.UNIQUEKEY = TEMPBI.UNIQUEKEY)) - ((CASE WHEN ((POLICYYEAR % 5 = 0)  AND (POLICYYEAR < 1003)) THEN 2*((ROUND((ROUND(79500,2)),0))*(((1)))) ELSE 0 END))) ) ELSE 0 END) AS [GSV], (SELECT ROUND(RATE,2) FROM PREMIUMRATES WHERE PRODUCTID = 1003 AND LAAGE = 35 AND PT = 1003) AS [PREMIUMRATE], (1003) AS [POLICYTERM] FROM TEMPBI WHERE PRODUCTID = 1003 AND UNIQUEKEY = '"+unique+"' ORDER BY [POLICYYEAR]";
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(finalQuery2);
                long end = System.currentTimeMillis();
                CommonMethod.log(TAG , "Final Query Completed " +end );
                long elapsedTime = end - start;
                //double seconds = (double)elapsedTime / 1_000_000_000.0;
                CommonMethod.log(TAG,String.format("%s: %s %s %s", "DURATION:", "Bi generated in", elapsedTime, "ms"));
                int counter = 0;
                if (cursor != null) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        LinkedHashMap<String, String> row = new LinkedHashMap<>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            row.put(cursor.getColumnName(i), cursor.getString(i));
                            if (cursor.getColumnName(i).equals("") || cursor.getColumnName(i) == null) {
                                row.put(cursor.getColumnName(i), ("0"));
                            }
                        }
                        dt.put(counter, row);
                        counter++;
                    }
                }
            } catch (Exception ex) {
                CommonMethod.log(TAG, "Error in Generating TempBI with values");
            }


            //CommonMethod.log(TAG,String.format("%s: %s %s %s", "DURATION:", "Bi generated in", "ms"));
            CommonMethod.logQuery(TAG , "Values " + dt.toString());
            return null;
        } catch (Exception ex) {
            CommonMethod.log(TAG, "Exception in raw query " + ex.toString());
            CommonMethod.log(TAG, "Line Number " + (ex.getStackTrace()[0].getLineNumber()));
            return null;
        }
    }

    private void tempBi(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                int Age = Integer.parseInt(Params.get("@LI_ENTRY_AGE"));;
                unique = UUID.randomUUID().toString().toUpperCase();
                String Query = "Insert into TempBI ";
                for (int i = 0; i < pTerm; i++) {
                    int LI_ATTAINED_AGE = Age + i - 1;
                    int PolicyYear = i + 1;
                    Query = Query + " select 1," + ProductId + "," + PolicyYear + "," + LI_ATTAINED_AGE + ",'" + unique + "'";
                    if (i + 1 < pTerm) {
                        Query = Query + " UNION ";
                    }
                }
                try {
                    NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query.toUpperCase());
                } catch (Exception ex) {
                    CommonMethod.log(TAG, "Error in generating TempBI");
                }
            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "Product Need list size " + prodneedlist.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });

    }

}