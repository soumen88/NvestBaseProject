package nvest.com.nvestlibrary.databaseFiles.dao.optionmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface OptionMasterRoomDao {

    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.OPTION_MASTER_TABLE)
    int getOptionMasterCount();

    @Insert
    void insertintoOptionMasterRoom(List<OptionMasterRoom> optionMasterRoomList);


}
