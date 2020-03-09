package com.nvest.user.databaseFiles.dao.formulatable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nvest.user.appConfig.Config;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface FormulasRoomDao {

    @Query("SELECT COUNT(*) FROM " + Config.FORMULA_TABLE)
    int getFormulaCount();

    @Insert
    void insertFormulas(List<FormulaRoom> formulaRoomList);


    @Query("SELECT * FROM " + Config.FORMULA_TABLE + " WHERE _id = :id")
    FormulaRoom getFormulaById(int id);

    @Query("SELECT * FROM " + Config.FORMULA_TABLE + " WHERE ProductId= :productId AND FormulaKeyword= :formulaKeyword AND IsOutput= :isOutput AND isInterimKw = :isIntermKw" )
    FormulaRoom getFormulaById2(int productId, String formulaKeyword , boolean isOutput, boolean isIntermKw);

    @Update
    public void updateFormula(FormulaRoom formulaRooms);

    @Query("SELECT * FROM " + Config.FORMULA_TABLE + " WHERE ProductId = :productId")
    Maybe<List<FormulaRoom>> getFormulaByProductId(String productId);

    @Query("SELECT * FROM " + Config.FORMULA_TABLE + " WHERE ProductId = :productId")
    Maybe<List<FormulaRoom>> getFormulaByProductIdAndOutputLoop(String productId);



}
