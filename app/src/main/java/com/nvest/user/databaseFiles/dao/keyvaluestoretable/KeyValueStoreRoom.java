package com.nvest.user.databaseFiles.dao.keyvaluestoretable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.nvest.user.appConfig.Config;


@Entity(tableName = Config.KEY_VALUE_STORE_TABLE,
        indices = {@Index(value = {"keyName"},
        unique = true)})
public class KeyValueStoreRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "keyName")
    private String keyName;

    @ColumnInfo(name = "keyValue")
    private String keyValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
