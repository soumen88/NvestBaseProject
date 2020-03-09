package com.nvest.user.databaseFiles.dao.lsadmaster;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.gsvtable.GSVRoom;

import java.util.List;

@Dao
public interface LSADMasterRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.LSAD_MASTER_TABLE)
    int getLSADMasterCount();

    @Insert
    void insertLSAD(List<LSADMasterRoom> lsadMasterRoomList);
}
