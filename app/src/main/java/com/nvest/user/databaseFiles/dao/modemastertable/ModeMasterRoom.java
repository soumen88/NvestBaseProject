package com.nvest.user.databaseFiles.dao.modemastertable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.nvest.user.appConfig.Config;

@Entity(tableName = Config.MODE_MASTER_TABLE)
public class ModeMasterRoom {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "MODEID")
    private int modeId;

    @ColumnInfo(name = "MODENAME")
    private String modeName;

    @ColumnInfo(name = "FREQUENCY")
    private int frequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
