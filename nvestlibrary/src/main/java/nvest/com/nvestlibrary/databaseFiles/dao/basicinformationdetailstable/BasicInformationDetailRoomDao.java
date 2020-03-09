package nvest.com.nvestlibrary.databaseFiles.dao.basicinformationdetailstable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.databaseFiles.dao.formulatable.FormulaRoom;

@Dao
public interface BasicInformationDetailRoomDao {
    @Insert
    void insertBasicInformationDetail(List<BasicInformationDetailRoom> basicInformationDetailRoomList);


    @Query("SELECT * FROM " + NvestLibraryConfig.BASIC_INFORMATION_DETAIL_TABLE + " WHERE _id = :id")
    BasicInformationDetailRoom getFormulaById(int id);
}
