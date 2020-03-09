package com.nvest.user.appConfig;

import com.nvest.user.databaseFiles.databaseWorkers.PremiumRatesWorker;

public class Config {
    public static final String DB_NAME = "NVest";
    public static final String DB_NAME_SMALL = "nvest.db";
    public static final String DB_NAME_GREENDAO = "nvestGreenDao.db";
    public static final String DB_NAME_SMALL_1 = "databases/nvest.db";
    public static final String DB_PATH = "databases/";
    public static final String SCRIPT_ENGINE_NAME = "rhino";

    //Table Names
    public static final String FUND_STRATEGY_TABLE = "FundStrategyMaster";
    public static final String FORMULA_TABLE = "Formulas";
    public static final String BONUS_GUARANTEE_TABLE = "BonusGuarantee";
    public static final String PREMIUM_RATES_TABLE = "PremiumRates";
    public static final String PRODUCT_CATEGORY_MAP_TABLE = "ProductCateryMap";
    public static final String TEMP_BI_TABLE = "TEMPBI";
    public static final String TEMP_BI_TABLE_PRIMARY_KEY = "_id";
    public static final String GSV_TABLE = "GSV";
    public static final String TAX_STRUCTURE_TABLE = "TaxStructure";
    public static final String SV_TABLE = "SV";
    public static final String PRODUCT_MODE_TABLE = "ProductMode";
    public static final String LSAD_MASTER_TABLE = "LSADMaster";
    public static final String MODE_MASTER_TABLE = "ModeMaster";
    public static final String BONUS_SCR_TABLE = "BonusScr";
    public static final String KEY_VALUE_STORE_TABLE = "KeyValueStore";




    //Product annotations
    public static final String PRODUCT_ID_ANNOTATION = "@PR_ID";
    public static final String LA_AGE_ANNOTATION = "@LI_ENTRY_AGE";
    public static final String PR_PT_ANNOTATION = "@PR_PT";
    public static final String PT_ANNOTATION = "@PT";
    public static final String PR_PPT_ANNOTATION = "@PR_PPT";
    public static final String TAX_GROUP_ANNOTATION = "@TAXGROUP";
    public static final String PREMIUM_RATE_ANNOTATION = "@PREM_RATE";
    public static final String INPUT_MODE_ANNOTATION = "@INPUT_MODE";
    public static final String PRODUCT_SUM_ASSURED_ANNOTATION = "@PR_SA";
    public static final String NSAP_FLAG_ANNOTATION = "@NSAP_FLAG";
    public static final String RD_PT_ANNOTATION = "@RD_PT";
    public static final String BONUS_SCR_ANNOTATION = "@BONUS_SCR";
    public static final String PR_OPTION_1_ANNOTATION = "@PR_OPTION_1";
    public static final String PR_OPTION_2_ANNOTATION = "@PR_OPTION_2";
    public static final String OPTION_VALUE_3_ANNOTATION = "@OPTION_VALUE_3";
    public static final String FUND_STRATEGY_ID_ANNOTATION = "@FUNDSTRATEGYID";
    public static final String PR_ANN_PREMIUM_ANNOTATION = "@PR_ANNPREM";

    //Sql server annotations
    public static final String IS_NULL = "ISNULL";
    public static final String GET_DATE = "/GETDATE\\(\\)/";
    public static final String CEILING = "CEILING";
    public static final String POWER = "POWER";
    public static final String AS_VALUE = "AS VALUE";


    public static final String IF_NULL = "IFNULL";
    public static final String DATE_NOW = "Date('now')";
    public static final String ROUND = "ROUND";
    public static final String MIN = "MIN";

    public static final String PREMIUM_RATE = "[PremiumRate]";

    //Database
    public static final int DATABASE_VERSION = 1;

    //Worker
    public static String PREMIUM_WORKER = PremiumRatesWorker.class.getSimpleName();
    public static enum WorkerName {
        PREMIUM_RATES_WORKER , OTHER_TABLES_WORKER, PRODUCT_TABLES_WORKER, DUMMY_WORKER;
    }
}
