package nvest.com.nvestlibrary.databaseFiles.dao.productMasterTable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface ProductMasterRoomDao {

    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.PRODUCT_MASTER_TABLE)
    int getProductMasterCount();

    @Insert
    void insertintoProductMasterRoom(List<ProductMasterRoom> productMasterRoomList);

}
