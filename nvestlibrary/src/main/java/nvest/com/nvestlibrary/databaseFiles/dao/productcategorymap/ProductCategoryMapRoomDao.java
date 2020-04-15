package nvest.com.nvestlibrary.databaseFiles.dao.productcategorymap;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;


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
