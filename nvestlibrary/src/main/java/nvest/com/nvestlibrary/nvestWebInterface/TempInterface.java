package nvest.com.nvestlibrary.nvestWebInterface;

import io.reactivex.Single;
import nvest.com.nvestlibrary.nvestWebModel.MasterProductDetailModel;
import retrofit2.http.GET;

public interface TempInterface {

    @GET("jbga7")
    Single<MasterProductDetailModel> getMasterProductChangeDetails();

}
