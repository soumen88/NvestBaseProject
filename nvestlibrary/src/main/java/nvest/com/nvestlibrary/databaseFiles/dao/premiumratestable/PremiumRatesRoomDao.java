package nvest.com.nvestlibrary.databaseFiles.dao.premiumratestable;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

import io.reactivex.Single;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface PremiumRatesRoomDao {

    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.PREMIUM_RATES_TABLE)
    int getPremiumRatesCount();

    @Insert
    void insertPremiumRates(List<PremiumRatesRoom> premiumRatesRoomList);

    @Query("SELECT * FROM " + NvestLibraryConfig.PREMIUM_RATES_TABLE + " WHERE PPT= :ppt AND PT= :pt and ProductId= :productId and LaAge= :laage")
    PremiumRatesRoom getPremiumById(int ppt, int pt, int productId, int laage);

    @RawQuery(observedEntities = PremiumRatesRoom.class)
    Single<Double> getPremiumRateRoom(SupportSQLiteQuery query);

    @Query("SELECT Round(RATE,2) FROM Premiumrates where ProductId = 1003 AND LAAGE = 29 AND PT = 10")
    Double getValue();

}
