package com.nvest.user.databaseFiles.dao.productcategorymap;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import com.nvest.user.appConfig.Config;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ProductCategoryMapRoomDao {


    @Query("SELECT COUNT(*) FROM " + Config.PRODUCT_CATEGORY_MAP_TABLE)
    int getProductCategoryCount();


    @Insert
    void insertProductCategories(List<ProductCategoryMapRoom> productCategoryMapRoomList);

    @Query("Select * from " + Config.PRODUCT_CATEGORY_MAP_TABLE + " WHERE ProductId = :productId")
    Single<ProductCategoryMapRoom> getProductCategoryMapByProductId(String productId);


}
