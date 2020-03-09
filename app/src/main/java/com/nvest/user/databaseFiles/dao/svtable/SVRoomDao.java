package com.nvest.user.databaseFiles.dao.svtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.taxstructure.TaxStructureRoom;

import java.util.List;

@Dao
public interface SVRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.SV_TABLE)
    int getSVCount();

    @Insert
    void insertSV(List<SVRoom> gsvRoomList);
}
