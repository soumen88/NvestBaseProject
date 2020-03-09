package nvest.com.nvestlibrary.databaseFiles.dao.formulatable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface FormulasRoomDao {

    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.FORMULA_TABLE)
    int getFormulaCount();

    @Insert
    void insertFormulas(List<FormulaRoom> formulaRoomList);


    @Query("SELECT * FROM " + NvestLibraryConfig.FORMULA_TABLE + " WHERE _id = :id")
    FormulaRoom getFormulaById(int id);

    @Query("SELECT * FROM " + NvestLibraryConfig.FORMULA_TABLE + " WHERE ProductId= :productId AND FormulaKeyword= :formulaKeyword AND IsOutput= :isOutput AND isInterimKw = :isIntermKw" )
    FormulaRoom getFormulaById2(int productId, String formulaKeyword , boolean isOutput, boolean isIntermKw);

    @Update
    public void updateFormula(FormulaRoom formulaRooms);

    @Query("SELECT * FROM " + NvestLibraryConfig.FORMULA_TABLE + " WHERE ProductId = :productId")
    Maybe<List<FormulaRoom>> getFormulaByProductId(String productId);

    @Query("SELECT * FROM " + NvestLibraryConfig.FORMULA_TABLE + " WHERE ProductId = :productId")
    Maybe<List<FormulaRoom>> getFormulaByProductIdAndOutputLoop(String productId);



}
