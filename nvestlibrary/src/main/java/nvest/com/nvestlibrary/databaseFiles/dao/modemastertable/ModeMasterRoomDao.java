package nvest.com.nvestlibrary.databaseFiles.dao.modemastertable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface ModeMasterRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.MODE_MASTER_TABLE)
    int getModeMasterCount();

    @Insert
    void insertModeMaster(List<ModeMasterRoom> modeMasterRoomList);
}
