package nvest.com.nvestlibrary.databaseFiles.dao.productcategorymap;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;


import java.util.List;

import io.reactivex.Single;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface ProductCategoryMapRoomDao {


    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.PRODUCT_CATEGORY_MAP_TABLE)
    int getProductCategoryCount();


    @Insert
    void insertProductCategories(List<ProductCategoryMapRoom> productCategoryMapRoomList);

    @Query("Select * from " + NvestLibraryConfig.PRODUCT_CATEGORY_MAP_TABLE + " WHERE ProductId = :productId")
    Single<ProductCategoryMapRoom> getProductCategoryMapByProductId(String productId);


}
