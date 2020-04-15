package nvest.com.nvestlibrary.databaseFiles.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.bonusguaranteetable.BonusGuaranteeRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.bonusscrtable.BonusScrRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.bonusscrtable.BonusScrRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.formulatable.FormulaRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.formulatable.FormulasRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.fundstrategytable.FundStrategyMasterRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.gsvtable.GSVRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.gsvtable.GSVRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.lsadmaster.LSADMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.lsadmaster.LSADMasterRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.modemastertable.ModeMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.modemastertable.ModeMasterRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.optionmaster.OptionMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.optionmaster.OptionMasterRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.premiumratestable.PremiumRatesRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.premiumratestable.PremiumRatesRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.productMasterTable.ProductMasterRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.productcategorymap.ProductCategoryMapRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.productcategorymap.ProductCategoryMapRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.productmodetable.ProductModeRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.productmodetable.ProductModeRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.svtable.SVRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.svtable.SVRoomDao;
import nvest.com.nvestlibrary.databaseFiles.dao.taxstructure.TaxStructureRoom;
import nvest.com.nvestlibrary.databaseFiles.dao.taxstructure.TaxStructureRoomDao;


@Database(entities = {
            FundStrategyMasterRoom.class, FormulaRoom.class, BonusGuaranteeRoom.class,
            PremiumRatesRoom.class, ProductCategoryMapRoom.class, GSVRoom.class, TaxStructureRoom.class,
            SVRoom.class, ProductModeRoom.class, ModeMasterRoom.class, LSADMasterRoom.class, BonusScrRoom.class,
            KeyValueStoreRoom.class, ProductMasterRoom.class, OptionMasterRoom.class, MortalityChargeRoom.class
        },
        version = NvestLibraryConfig.DATABASE_VERSION,  exportSchema = false)

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
    public abstract MortalityChargeRoomDao mortalityChargeRoomDao();
    public abstract OptionMasterRoomDao optionMasterRoomDao();

}
