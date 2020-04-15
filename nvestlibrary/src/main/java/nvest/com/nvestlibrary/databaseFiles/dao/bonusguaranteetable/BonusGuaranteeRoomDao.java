package nvest.com.nvestlibrary.databaseFiles.dao.bonusguaranteetable;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;


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
