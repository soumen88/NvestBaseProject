package nvest.com.nvestlibrary.databaseFiles.dao.svtable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface SVRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.SV_TABLE)
    int getSVCount();

    @Insert
    void insertSV(List<SVRoom> gsvRoomList);
}
