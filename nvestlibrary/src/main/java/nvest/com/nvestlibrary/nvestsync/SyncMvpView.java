package nvest.com.nvestlibrary.nvestsync;

import nvest.com.nvestlibrary.base.MvpView;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;
import nvest.com.nvestlibrary.nvestWebModel.NvestProductParserModel;

public interface SyncMvpView extends MvpView {
    void getProducts(String productId);
    void receivedProductDetails(NvestProductParserModel nvestProductParserModel);
    void receivedPremiumRates(NvestProductParserModel nvestProductParserModel);
    void receivedMortality(DummyMortModel1 dummyMortModel1);
}
