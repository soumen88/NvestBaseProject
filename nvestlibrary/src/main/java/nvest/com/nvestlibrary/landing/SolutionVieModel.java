package nvest.com.nvestlibrary.landing;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.generatePdf.models.ModeMaster;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.solutionDetails.SolutionMaster;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.solutionDetails.SolutionProducts;

public class SolutionVieModel extends AndroidViewModel {
    private static String TAG = SolutionVieModel.class.getSimpleName();
    private MutableLiveData<LinkedHashMap<String, ArrayList<SolutionMaster>>> solutionLiveDataList = new MutableLiveData<>();
    private MutableLiveData<List<ModeMaster>> modeLiveDataList = new MutableLiveData<>();
    private MutableLiveData<List<SolutionProducts>> solutionProductsLiveDataList = new MutableLiveData<>();
    private MutableLiveData<List<Products>> productLiveDataList = new MutableLiveData<>();
    private MutableLiveData<List<String>> inputKeywordsLiveDataList = new MutableLiveData<>();
    private SolutionListener solutionListener;

    public SolutionVieModel(@NonNull Application application) {
        super(application);
    }

    public void setSolutionListener(SolutionListener solutionListener) {
        this.solutionListener = solutionListener;
    }

    public interface SolutionListener {
        //void onSolutionChanged(MutableLiveData<List<SolutionMaster>> solutionLiveDataList);
        void onSolutionChanged(boolean complete);

        void onProductsChanged(boolean complete);

        void onModeRecieved(boolean modeRecieved);

        void onSolutionProductsChanged(boolean solutionProductsChanged);

        void onInputKeywordsChanged(boolean inputKeywordsChanged);
    }

    public void getSolutions() {
        // TODO remove "order by" from query
        LinkedHashMap<String, ArrayList<SolutionMaster>> segregatedCategoryList = new LinkedHashMap<>();
        String solutionQuery = String.format("select * from %s order by id desc", NvestLibraryConfig.SOLUTION_MASTER_TABLE);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(solutionQuery);
        ArrayList<SolutionMaster> solutionMasterList = new ArrayList<>();
        if (cursor != null) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    CommonMethod.log(TAG, "Fund strategy cursor count " + cursor.getCount());
                    ArrayList<Products> productsArrayList = new ArrayList<>();
                    String categoryName = "";
                    if (cursor.moveToFirst()) {
                        do {
                            SolutionMaster solutionMaster = new SolutionMaster();
                            solutionMaster.setId(cursor.getInt(0));
                            solutionMaster.setSolutionName(cursor.getString(1));
                            solutionMaster.setDescription(cursor.getString(2));
                            solutionMaster.setNeeds(cursor.getInt(3));
                            solutionMaster.setPremiumInput(cursor.getString(4));
                            solutionMaster.setValidationError(cursor.getString(5));
                            solutionMaster.setAllowEdit(cursor.getString(6));
                            solutionMasterList.add(solutionMaster);
                        } while (cursor.moveToNext());
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
                            CommonMethod.log(TAG, "Inside on complete ");
                            segregatedCategoryList.put("Solution Products", solutionMasterList);
                            solutionLiveDataList.setValue(segregatedCategoryList);
                            solutionListener.onSolutionChanged(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonMethod.log(TAG, "Error " + e.toString());
                        }
                    });
        }
    }

    public void getInputKeywords(int productId) {
        String inputKeywordsQuery = String.format("select KeywordName from %s where ProductId = %s", NvestLibraryConfig.INPUT_KEYWORDS_TABLE, productId);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(inputKeywordsQuery);
        List<String> inputKeywords = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                inputKeywords.add(cursor.getString(0).toUpperCase());
            } while (cursor.moveToNext());
        }

        inputKeywordsLiveDataList.setValue(inputKeywords);
        solutionListener.onInputKeywordsChanged(true);
    }

    public void getProducts(int... productIds) {
        StringBuilder selectQuery = new StringBuilder("Select DISTINCT CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.* from CompanyDefinedCategories INNER JOIN ProductCompanyCategories ON  CompanyDefinedCategories.CompCat = ProductCompanyCategories.CompCat INNER JOIN (Select * From ProductMaster where islive = 1 AND ");
        for (int i = 0; i < productIds.length; i++) {
            selectQuery.append("ProductMaster.ProductId = ");
            selectQuery.append(productIds[i]);
            if (i != productIds.length - 1) {
                selectQuery.append(" OR ");
            }
        }
        selectQuery.append(" ) AS ProductMaster ON ProductCompanyCategories.ProductId = ProductMaster.ProductId ORDER BY CompanyDefinedCategories.CompCat");

        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(selectQuery.toString());
        List<Products> productList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Products product = new Products();
                    product.setCompCat(cursor.getInt(0));
                    product.setCompCatName(cursor.getString(1));
                    product.setProductId(cursor.getInt(2));
                    product.setCompanyId(cursor.getInt(3));
                    product.setCategoryId(cursor.getInt(4));
                    product.setProductName(cursor.getString(5));
                    product.setProductUIN(cursor.getString(6));
                    product.setStatusFlag(cursor.getInt(7));
                    product.setPension(cursor.getInt(8) > 0);
                    product.setPlatform(cursor.getString(9));
                    product.setIslive(cursor.getInt(10) > 0);
                    product.setStartDate(cursor.getString(11));
                    product.setEndDate(cursor.getString(12));
                    product.setEntryStartDate(cursor.getString(13));
                    product.setEntryStartBy(cursor.getString(14));
                    product.setCurrentStatus(cursor.getString(15));
                    product.setLastEditBy(cursor.getString(16));
                    product.setLastEditDate(cursor.getString(17));
                    product.setDescription(cursor.getString(18));
                    product.setImageUrl(cursor.getString(19));
                    product.setSalesApp(cursor.getInt(20));
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        }
        productLiveDataList.setValue(productList);
        solutionListener.onProductsChanged(true);
    }

    public void getSolutionProducts(int solutionId) {
        String solutionProductsQuery = String.format("select * from %s where SolutionId = %s", NvestLibraryConfig.SOLUTION_PRODUCTS_TABLE, solutionId);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(solutionProductsQuery);
        List<SolutionProducts> solutionProductsList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    SolutionProducts solutionProducts = new SolutionProducts();
                    solutionProducts.setId(cursor.getInt(0));
                    solutionProducts.setSolutionId(cursor.getInt(1));
                    solutionProducts.setProductId(cursor.getInt(2));
                    solutionProducts.setProductName(cursor.getString(3));
                    solutionProducts.setInputString(cursor.getString(4));
                    solutionProductsList.add(solutionProducts);
                } while (cursor.moveToNext());
            }
        }
        solutionProductsLiveDataList.setValue(solutionProductsList);
        solutionListener.onSolutionProductsChanged(true);
    }

    public void getModes() {
        String modeQuery = String.format("select * from %s", NvestLibraryConfig.MODE_MASTER_TABLE);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(modeQuery);
        List<ModeMaster> modeMasterList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ModeMaster modeMaster = new ModeMaster();
                    modeMaster.setModeId(cursor.getInt(0));
                    modeMaster.setModeName(cursor.getString(1));
                    modeMaster.setFrequency(cursor.getInt(2));
                    modeMasterList.add(modeMaster);
                } while (cursor.moveToNext());
            }
        }

        modeLiveDataList.setValue(modeMasterList);
        solutionListener.onModeRecieved(true);
    }

    public MutableLiveData<LinkedHashMap<String, ArrayList<SolutionMaster>>> getMutableSolutionProductList() {
        return solutionLiveDataList;
    }

    public MutableLiveData<List<ModeMaster>> getModeLiveDataList() {
        return modeLiveDataList;
    }

    public MutableLiveData<List<SolutionProducts>> getSolutionProductsLiveDataList() {
        return solutionProductsLiveDataList;
    }

    public MutableLiveData<List<Products>> getProductLiveDataList() {
        return productLiveDataList;
    }

    public MutableLiveData<List<String>> getInputKeywordsLiveDataList() {
        return inputKeywordsLiveDataList;
    }
}
