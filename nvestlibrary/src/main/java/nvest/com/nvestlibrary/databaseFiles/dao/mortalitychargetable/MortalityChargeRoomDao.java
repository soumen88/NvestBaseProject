package nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
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
