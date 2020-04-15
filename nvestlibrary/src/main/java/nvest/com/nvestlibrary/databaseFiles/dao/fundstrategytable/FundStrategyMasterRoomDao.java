package nvest.com.nvestlibrary.databaseFiles.dao.fundstrategytable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface FundStrategyMasterRoomDao {
    @Query("SELECT * FROM " + NvestLibraryConfig.FUND_STRATEGY_TABLE)
    Maybe<List<FundStrategyMasterRoom>> getAllRoomFuncMasters();


    @Query("SELECT * FROM " + NvestLibraryConfig.FUND_STRATEGY_TABLE)
    List<FundStrategyMasterRoom> getAllRoomFuncMastersNormal();

    @Insert
    void insertListFundStrategy(List<FundStrategyMasterRoom> fundStrategyMasterRoomList);
}
