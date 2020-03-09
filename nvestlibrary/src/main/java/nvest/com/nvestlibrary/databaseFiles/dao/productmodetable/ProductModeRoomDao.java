package nvest.com.nvestlibrary.databaseFiles.dao.productmodetable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface ProductModeRoomDao {


    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.PRODUCT_MODE_TABLE)
    int getProductModeCount();


    @Insert
    void insertProductMode(List<ProductModeRoom> productModeRoomsList);

}
