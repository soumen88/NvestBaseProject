package com.nvest.user.databaseFiles.dao.taxstructure;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;

import java.util.List;

@Dao
public interface TaxStructureRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.TAX_STRUCTURE_TABLE)
    int getTaxStructureCount();

    @Insert
    void insertTaxStructure(List<TaxStructureRoom> gsvRoomList);

}
