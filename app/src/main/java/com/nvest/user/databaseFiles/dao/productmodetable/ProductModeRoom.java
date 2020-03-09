package com.nvest.user.databaseFiles.dao.productmodetable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.nvest.user.appConfig.Config;

@Entity(tableName = Config.PRODUCT_MODE_TABLE)
public class ProductModeRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "PRODUCTID")
    private int productId ;

    @ColumnInfo(name = "MODEID")
    private int modelId  ;


    @ColumnInfo(name = "MULTIPLIER")
    private Double multiplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }
}
