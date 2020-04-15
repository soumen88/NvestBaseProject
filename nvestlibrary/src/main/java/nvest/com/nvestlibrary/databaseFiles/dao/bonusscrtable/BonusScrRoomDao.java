package nvest.com.nvestlibrary.databaseFiles.dao.bonusscrtable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
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
