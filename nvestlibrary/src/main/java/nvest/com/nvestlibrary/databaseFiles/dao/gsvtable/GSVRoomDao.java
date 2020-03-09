package nvest.com.nvestlibrary.databaseFiles.dao.gsvtable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface GSVRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.GSV_TABLE)
    int getGSVCount();

    @Insert
    void insertGSV(List<GSVRoom> gsvRoomList);

}
