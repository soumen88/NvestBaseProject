package com.nvest.user.databaseFiles.dao.bonusguaranteetable;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;


import com.nvest.user.LogUtils.TestPojo;
import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import com.nvest.user.databaseFiles.dao.tempbitable.TestPojoSUD;


import java.util.List;



@Dao
public interface BonusGuaranteeRoomDao {
    @Query("SELECT COUNT(*) FROM " + Config.BONUS_GUARANTEE_TABLE)
    int getBonusCount();


    @Insert
    void insertBonuses(List<BonusGuaranteeRoom> bonusGuaranteeRoomList);

    @RawQuery
    BonusGuaranteeRoom runtimequery(SupportSQLiteQuery query);

    /*@RawQuery
    PremiumRatesRoom runtimequerytest(SupportSQLiteQuery query);*/

    @RawQuery
    TestPojo runtimequerytest(SupportSQLiteQuery query);

    @RawQuery
    List<TestPojoSUD> runtimequerytestPojo(SupportSQLiteQuery query);
}
