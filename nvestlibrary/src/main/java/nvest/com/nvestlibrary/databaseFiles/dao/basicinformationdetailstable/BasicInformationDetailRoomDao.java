package nvest.com.nvestlibrary.databaseFiles.dao.basicinformationdetailstable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface BasicInformationDetailRoomDao {
    @Insert
    void insertBasicInformationDetail(List<BasicInformationDetailRoom> basicInformationDetailRoomList);


    @Query("SELECT * FROM " + NvestLibraryConfig.BASIC_INFORMATION_DETAIL_TABLE + " WHERE _id = :id")
    BasicInformationDetailRoom getFormulaById(int id);
}
