package nvest.com.nvestlibrary.databaseFiles.dao.lsadmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface LSADMasterRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.LSAD_MASTER_TABLE)
    int getLSADMasterCount();

    @Insert
    void insertLSAD(List<LSADMasterRoom> lsadMasterRoomList);
}
