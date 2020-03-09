package com.nvest.user.databaseFiles.dao.gsvtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;

import java.util.List;

@Dao
public interface GSVRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.GSV_TABLE)
    int getGSVCount();

    @Insert
    void insertGSV(List<GSVRoom> gsvRoomList);

}
