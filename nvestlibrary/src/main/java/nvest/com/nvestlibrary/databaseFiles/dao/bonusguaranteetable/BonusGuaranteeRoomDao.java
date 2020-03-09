package nvest.com.nvestlibrary.databaseFiles.dao.bonusguaranteetable;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;


import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.databaseFiles.dao.tempbitable.TestPojoSUD;


@Dao
public interface BonusGuaranteeRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.BONUS_GUARANTEE_TABLE)
    int getBonusCount();


    @Insert
    void insertBonuses(List<BonusGuaranteeRoom> bonusGuaranteeRoomList);

    @RawQuery
    BonusGuaranteeRoom runtimequery(SupportSQLiteQuery query);

    /*@RawQuery
    PremiumRatesRoom runtimequerytest(SupportSQLiteQuery query);*/

    @RawQuery
    List<TestPojoSUD> runtimequerytestPojo(SupportSQLiteQuery query);
}
