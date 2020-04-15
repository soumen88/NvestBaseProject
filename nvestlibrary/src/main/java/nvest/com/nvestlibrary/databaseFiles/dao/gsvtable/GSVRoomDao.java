package nvest.com.nvestlibrary.databaseFiles.dao.gsvtable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface GSVRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.GSV_TABLE)
    int getGSVCount();

    @Insert
    void insertGSV(List<GSVRoom> gsvRoomList);

}
