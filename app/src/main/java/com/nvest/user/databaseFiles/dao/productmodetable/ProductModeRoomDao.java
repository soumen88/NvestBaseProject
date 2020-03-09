package com.nvest.user.databaseFiles.dao.productmodetable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;

import java.util.List;

@Dao
public interface ProductModeRoomDao {


    @Query("SELECT COUNT(*) FROM " + Config.PRODUCT_MODE_TABLE)
    int getProductModeCount();


    @Insert
    void insertProductMode(List<ProductModeRoom> productModeRoomsList);

}
