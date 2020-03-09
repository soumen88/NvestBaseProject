package com.nvest.user.databaseFiles.dao.modemastertable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;

import java.util.List;

@Dao
public interface ModeMasterRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.MODE_MASTER_TABLE)
    int getModeMasterCount();

    @Insert
    void insertModeMaster(List<ModeMasterRoom> modeMasterRoomList);
}
