package com.nvest.user.databaseFiles;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import com.nvest.user.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoomDao;
import com.nvest.user.databaseFiles.dao.bonusscrtable.BonusScrRoom;
import com.nvest.user.databaseFiles.dao.bonusscrtable.BonusScrRoomDao;
import com.nvest.user.databaseFiles.dao.formulatable.FormulaRoom;
import com.nvest.user.databaseFiles.dao.formulatable.FormulasRoomDao;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import com.nvest.user.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoomDao;
import com.nvest.user.databaseFiles.dao.gsvtable.GSVRoom;
import com.nvest.user.databaseFiles.dao.gsvtable.GSVRoomDao;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoomDao;
import com.nvest.user.databaseFiles.dao.lsadmaster.LSADMasterRoom;
import com.nvest.user.databaseFiles.dao.lsadmaster.LSADMasterRoomDao;
import com.nvest.user.databaseFiles.dao.modemastertable.ModeMasterRoom;
import com.nvest.user.databaseFiles.dao.modemastertable.ModeMasterRoomDao;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import com.nvest.user.databaseFiles.dao.premiumratestable.PremiumRatesRoomDao;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;
import com.nvest.user.databaseFiles.dao.productcategorymap.ProductCategoryMapRoomDao;
import com.nvest.user.databaseFiles.dao.productmodetable.ProductModeRoom;
import com.nvest.user.databaseFiles.dao.productmodetable.ProductModeRoomDao;
import com.nvest.user.databaseFiles.dao.svtable.SVRoom;
import com.nvest.user.databaseFiles.dao.svtable.SVRoomDao;
import com.nvest.user.databaseFiles.dao.taxstructure.TaxStructureRoom;
import com.nvest.user.databaseFiles.dao.taxstructure.TaxStructureRoomDao;

import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;


@Database(entities = {
            FundStrategyMasterRoom.class, FormulaRoom.class, BonusGuaranteeRoom.class,
            PremiumRatesRoom.class, ProductCategoryMapRoom.class, GSVRoom.class, TaxStructureRoom.class,
            SVRoom.class, ProductModeRoom.class, ModeMasterRoom.class, LSADMasterRoom.class, BonusScrRoom.class,
            KeyValueStoreRoom.class, MortalityChargeRoom.class
        },
        version = Config.DATABASE_VERSION,  exportSchema = false)

public abstract class RoomAppDatabase extends RoomDatabase {
    public abstract FundStrategyMasterRoomDao fundStrategyMasterRoomDao();
    public abstract FormulasRoomDao formulasRoomDao();
    public abstract BonusGuaranteeRoomDao bonusGuaranteeRoomDao();
    public abstract PremiumRatesRoomDao premiumRatesRoomDao();
    public abstract ProductCategoryMapRoomDao productCategoryMapRoomDao();

    public abstract GSVRoomDao gsvRoomDao();
    public abstract TaxStructureRoomDao taxStructureRoomDao();
    public abstract SVRoomDao svRoomDao();
    public abstract ProductModeRoomDao productModeRoomDao();
    public abstract LSADMasterRoomDao lsadMasterRoomDao();
    public abstract ModeMasterRoomDao modeMasterRoomDao();
    public abstract BonusScrRoomDao bonusScrRoomDao();
    public abstract KeyValueStoreRoomDao keyValueStoreRoomDao();

}
