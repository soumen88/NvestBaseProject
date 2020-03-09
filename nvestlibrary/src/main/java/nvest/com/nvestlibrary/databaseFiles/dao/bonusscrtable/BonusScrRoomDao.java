package nvest.com.nvestlibrary.databaseFiles.dao.bonusscrtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface BonusScrRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.BONUS_SCR_TABLE)
    int getBonusScrCount();

    @Insert
    void insertIntoBonusScr(List<BonusScrRoom> bonusScrRoomList);

    @Query("SELECT * FROM " + NvestLibraryConfig.BONUS_SCR_TABLE + " WHERE ProductId = :productId")
    Maybe<List<BonusScrRoom>> getBonusByProductId(String productId);

}
