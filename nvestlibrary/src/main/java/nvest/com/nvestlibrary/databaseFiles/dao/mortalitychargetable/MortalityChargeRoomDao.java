package nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;


import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface MortalityChargeRoomDao {

    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.MORTALITY_CHARGE_TABLE)
    int getMortalityChargeCount();

    @Query("SELECT * FROM " + NvestLibraryConfig.MORTALITY_CHARGE_TABLE)
    LiveData<List<MortalityChargeRoom>> getMortalityCharge();

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    void insertintoMortalityChargeRoom(List<MortalityChargeRoom> mortalityChargeRoomList);

    @RawQuery(observedEntities = MortalityChargeRoom.class)
    String insertintoMortalityChargeRoomString(SupportSQLiteQuery supportSQLiteQuery);
}
