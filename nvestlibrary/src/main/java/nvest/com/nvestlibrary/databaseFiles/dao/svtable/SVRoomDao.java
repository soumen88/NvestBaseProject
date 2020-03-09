package nvest.com.nvestlibrary.databaseFiles.dao.svtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface SVRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.SV_TABLE)
    int getSVCount();

    @Insert
    void insertSV(List<SVRoom> gsvRoomList);
}
