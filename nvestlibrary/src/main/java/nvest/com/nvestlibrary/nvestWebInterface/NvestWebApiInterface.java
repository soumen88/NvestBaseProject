package nvest.com.nvestlibrary.nvestWebInterface;


import java.util.List;

import io.reactivex.Single;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;
import nvest.com.nvestlibrary.nvestWebModel.ChangeProductDetailsModel;
import nvest.com.nvestlibrary.nvestWebModel.MasterProductDetailModel;
import nvest.com.nvestlibrary.nvestWebModel.NvestProductParserModel;
import nvest.com.nvestlibrary.nvestsync.DummyMortModel1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NvestWebApiInterface {

    @GET("GetProductData")
    Call<NvestProductParserModel> getProductByProductId(@Query("ProductId") String productId);

    //@GET("GetProductData")
    @GET("1afcvs")
    Single<NvestProductParserModel> getProductByProductIdSingle(@Query("ProductId") String productId);

    @GET("1azb8w")
    Single<DummyMortModel1> getMort(@Query("ProductId") String productId);

    @GET("GetPremiumRateData")
    Single<NvestProductParserModel> getPremiumRatesByProductIdSingle(@Query("ProductId") String productId);

    @GET("GetChangedProductList")
    Single<ChangeProductDetailsModel> getChangeProductDetails(@Query("LastUpdateDate") String date);

    @GET("189ij3")
    Single<MasterProductDetailModel> getMasterProductChangeDetails();

}

