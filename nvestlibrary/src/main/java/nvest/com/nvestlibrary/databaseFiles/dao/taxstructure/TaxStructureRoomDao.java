package nvest.com.nvestlibrary.databaseFiles.dao.taxstructure;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface TaxStructureRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.TAX_STRUCTURE_TABLE)
    int getTaxStructureCount();

    @Insert
    void insertTaxStructure(List<TaxStructureRoom> gsvRoomList);

}
