package com.nvest.user.databaseFiles.dao.bonusscrtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

@Dao
public interface BonusScrRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.BONUS_SCR_TABLE)
    int getBonusScrCount();

    @Insert
    void insertIntoBonusScr(List<BonusScrRoom> bonusScrRoomList);

    @Query("SELECT * FROM " + Config.BONUS_SCR_TABLE + " WHERE ProductId = :productId")
    Maybe<List<BonusScrRoom>> getBonusByProductId(String productId);

}
