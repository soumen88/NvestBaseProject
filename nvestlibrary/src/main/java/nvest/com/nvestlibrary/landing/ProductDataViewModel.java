package nvest.com.nvestlibrary.landing;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.database.Cursor;
import androidx.annotation.NonNull;

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
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;


public class ProductDataViewModel extends AndroidViewModel {
    private static String TAG = ProductDataViewModel.class.getSimpleName();
    private MutableLiveData<List<Products>> mutableProductList = new MutableLiveData<>();
    private MutableLiveData<LinkedHashMap<String, ArrayList<Products>>> mutableSegregatedList = new MutableLiveData<>();
    private MutableLiveData<Products> productLiveData = new MutableLiveData<>();
    private ProductDataListener productDataListener;
    public ProductDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void setProductDataListener(ProductDataListener productDataListener) {
        this.productDataListener = productDataListener;
    }

    public interface ProductDataListener{
        void completestatus(int productsize);
        void onProductLiveDataReceived(boolean complete);
    }

    public void getProductById(int productId){
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getProductById(1,productId);
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getProduct(productId);
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select DISTINCT CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.* from CompanyDefinedCategories INNER JOIN ProductCompanyCategories ON  CompanyDefinedCategories.CompCat = ProductCompanyCategories.CompCat INNER JOIN (Select * From ProductMaster where islive = 1 AND ProductMaster.ProductId = "+productId+" ) AS ProductMaster ON ProductCompanyCategories.ProductId = ProductMaster.ProductId ORDER BY CompanyDefinedCategories.CompCat" );
        if(cursor!=null){
            if(cursor.moveToFirst()){
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
                productLiveData.setValue(product);
                productDataListener.onProductLiveDataReceived(true);
            }
        }
    }

    public void loadProducts(){
        LinkedHashMap<String, ArrayList<Products>> segregatedCategoryList = new LinkedHashMap<>();
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getProductsWithoutCompanyId();
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select DISTINCT CompanyDefinedCategories.CompCat AS CompCat, CompanyDefinedCategories.CompCatName AS CompCatName, ProductMaster.* from CompanyDefinedCategories INNER JOIN ProductCompanyCategories ON  CompanyDefinedCategories.CompCat = ProductCompanyCategories.CompCat INNER JOIN (Select * From ProductMaster where islive = 1 ) AS ProductMaster ON ProductCompanyCategories.ProductId = ProductMaster.ProductId ORDER BY CompanyDefinedCategories.CompCat");
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getProductById(1,21033);
        mutableSegregatedList = new MutableLiveData<>();
        if(cursor != null){
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    CommonMethod.log(TAG , "Fund strategy cursor count " + cursor.getCount());
                    ArrayList<Products> productsArrayList = new ArrayList<>();
                    String categoryName = "";
                    if (cursor.moveToFirst()) {
                        do {
                            Products product = new Products();
                            if(!categoryName.isEmpty() && !categoryName.equals(cursor.getString(1))){
                                CommonMethod.log(TAG , "Category Name 2 " + categoryName + " Product list size " + productsArrayList.size());
                                segregatedCategoryList.put(categoryName, productsArrayList);
                                productsArrayList = new ArrayList<>();
                                //CommonMethod.log(TAG,"check2" +segregatedCategoryList);
                            }
                            categoryName = cursor.getString(1);
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
                            productsArrayList.add(product);
                        } while (cursor.moveToNext());
                        //Adding the last element in cursor
                        segregatedCategoryList.put(categoryName, productsArrayList);
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
                            /*
                                Uncomment this code to see segregated products list
                                for (Map.Entry<String, ArrayList<Products>> entry: segregatedCategoryList.entrySet()){
                                CommonMethod.log(TAG , "Key " + entry.getKey());
                                ArrayList<Products> productsArrayList1 = entry.getValue();
                                CommonMethod.log(TAG , "Key size " + productsArrayList1.size());
                                for (Products products: productsArrayList1){
                                    CommonMethod.log(TAG , "Product name " + products.getProductName());
                                    CommonMethod.log(TAG , "Product category name " + products.getCompCatName());
                                }
                            }
                             */
                            mutableSegregatedList.setValue(segregatedCategoryList);
                            if(productDataListener != null){
                                productDataListener.completestatus(cursor.getCount());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonMethod.log(TAG, "Error " + e.toString());
                        }
                    });
        }

    }

    public MutableLiveData<List<Products>> setMutableLiveData() {
        CommonMethod.log(TAG , "Inside set mutable live data");
        return mutableProductList;
    }

    public MutableLiveData<LinkedHashMap<String, ArrayList<Products>>> setSegregatedCategoryLiveData(){
        return mutableSegregatedList;
    }

    public MutableLiveData<Products> setSingleProductLiveData(){
        return productLiveData;
    }


}
