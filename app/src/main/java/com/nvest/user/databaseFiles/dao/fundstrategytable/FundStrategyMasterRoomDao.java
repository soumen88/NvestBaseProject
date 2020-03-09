package com.nvest.user.databaseFiles.dao.fundstrategytable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface FundStrategyMasterRoomDao {
    @Query("SELECT * FROM " + Config.FUND_STRATEGY_TABLE)
    Maybe<List<FundStrategyMasterRoom>> getAllRoomFuncMasters();


    @Query("SELECT * FROM " + Config.FUND_STRATEGY_TABLE)
    List<FundStrategyMasterRoom> getAllRoomFuncMastersNormal();

    @Insert
    void insertListFundStrategy(List<FundStrategyMasterRoom> fundStrategyMasterRoomList);
}
