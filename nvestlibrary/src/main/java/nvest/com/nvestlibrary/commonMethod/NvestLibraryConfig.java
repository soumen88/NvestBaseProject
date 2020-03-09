package nvest.com.nvestlibrary.commonMethod;

import android.os.Environment;

public class NvestLibraryConfig {
    public static final String DB_NAME = "nvest_new.db";
    public static final String DB_NAME_WITH_FORMULAS_SQLITE = "nvest_with_formulas_sqlite.db";
    public static final int DB_VERSION = 1;
    public static final int TIMEOUT_SECONDS = 60;
    public static final String STORAGE_DIRECTORY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+ NvestLibraryConfig.NVEST_TEMP_FOLDER_NAME).getAbsolutePath();
    public static final String NVEST_TEMP_FOLDER_NAME = "/NvestTemp";


    public static final String DB_NAME_GREENDAO = "nvestGreenDao.db";


    public static final String PRODUCT_MODIFIED = "Modified";
    public static final String PRODUCT_DELETED = "Delete";


    //Spinner Names
    public static final String PT_SPINNER = "PT_SPINNER";
    public static final String PPT_SPINNER = "PPT_SPINNER";
    public static final String MODE_SPINNER= "MODE_SPINNER";
    public static final String EMR_SPINNER= "EMR_SPINNER";
    public static final String FLAT_EXTRA_SPINNER= "FLAT_EXTRA_SPINNER";




    //Table Names
    public static final String BASIC_INFORMATION_DETAIL_TABLE = "BasicInformationDetail";
    public static final String CHANGED_PRODUCT_LOGMASTER = "ChangedProductLogmaster";
    public static final String CHANGED_PRODUCT_DETAILS= "ChangedProductDetails";
    public static final String FUND_STRATEGY_TABLE = "FundStrategyMaster";
    public static final String FORMULA_TABLE = "Formulas";
    public static final String BONUS_GUARANTEE_TABLE = "BonusGuarantee";
    public static final String PREMIUM_RATES_TABLE = "PremiumRates";
    public static final String PRODUCT_CATEGORY_MAP_TABLE = "ProductCateryMap";
    public static final String TEMP_BI_TABLE = "TEMPBI";
    public static final String BI_QUOTATION_TABLE = "BIQuotation";
    public static final String TEMP_BI_TABLE_PRIMARY_KEY = "_id";
    public static final String GSV_TABLE = "GSV";
    public static final String TAX_STRUCTURE_TABLE = "TaxStructure";
    public static final String FORMULAS_SQLITE_TABLE = "FormulasSqlite";
    public static final String SV_TABLE = "SV";
    public static final String PRODUCT_MODE_TABLE = "ProductMode";
    public static final String LSAD_MASTER_TABLE = "LSADMaster";
    public static final String MODE_MASTER_TABLE = "ModeMaster";
    public static final String BONUS_SCR_TABLE = "BonusScr";
    public static final String KEY_VALUE_STORE_TABLE = "KeyValueStore";
    public static final String DUMMY_KEY = "DUMMY_KEY";
    public static final String TEST_TEMP_BI_TABLE = "aksjdclkdsj";
    public static final String T0_TABLE = "t0";
    public static final String SYS_DIAGRAMS_TABLE = "sysdiagrams";
    public static final String SQLITE_SEQUENCE_TABLE = "sqlite_sequence";
    public static final String UNIT_STATEMENT_TABLE = "UnitStatement";
    public static final String UNIT_S_TAX_STRUCTURE_TABLE = "UnitSTaxStructure";
    public static final String UNIT_SFMC_MASTER_TABLE = "UnitSFMCMaster";
    public static final String ULIP_SV_TABLE = "UlipSV";
    public static final String ULIP_SA_MASTER_TABLE = "UlipSAMaster";
    public static final String ULIP_REC_POLICY_DATA_TABLE = "UlipRecPolicyData";
    public static final String ULIP_REC_OUTPUT_TABLE = "UlipRecOutput";
    public static final String TYPE_MASTER_TABLE = "TypeMaster";
    public static final String TRANSACTION_TRACKING_TABLE = "TransactionTracking";
    public static final String TRANSACTION_MASTER_TABLE = "TransactionMaster";
    public static final String TEST_TEMP_TABLE = "Test_Temp";
    public static final String TEST_OUTPUT_KEYWORDS_TABLE = "TestOutputKeywords";
    public static final String TEST_CITY_TABLE = "TestCity";
    public static final String TEST_CASES_DYNAMIC_TABLE = "TestCasesDynamic";
    public static final String TEST_CASES_TABLE = "TestCases";
    public static final String TEMP_BI_ULIP_TABLE = "TempBiUlip";
    public static final String TEMP_BI_ULIP_UNIT_TABLE = "TempBIUlipUnit";
    public static final String SURRENDER_PREM_SCHEDULE_TABLE = "SurrenderPremSchedule";
    public static final String SURRENDER_POLICY_DETAILS_TABLE = "SurrenderPolicyDetails";
    public static final String SURRENDER_OUTPUT_TABLE = "SurrenderOutput";
    public static final String SURRENDER_CLOSING_FV_TABLE = "SurrenderClosingFV";
    public static final String STATE_MASTER_TABLE = "StateMaster";
    public static final String SOLUTION_MASTER_TABLE = "SolutionMaster";
    public static final String SOLUTION_PRODUCTS_TABLE = "SolutionProducts";
    public static final String SITE_ADMIN_TABLE = "SiteAdmin";
    public static final String SIMPLIFY_DOC_PATH_TABLE = "SimplyDocPath";
    public static final String SUD_QUOTATION_TABLE = "SUDQuotation";
    public static final String SA_MASTER_TABLE = "SAMaster";
    public static final String ROLE_USER_MAP_TABLE = "RoleUserMap";
    public static final String ROLE_MASTER_TABLE = "RoleMaster";
    public static final String RIDER_RULE_MASTER_TABLE = "RiderRuleMaster";
    public static final String RIDER_RULE_MAP_TABLE = "RiderRuleMap";
    public static final String RIDER_INPUT_TABLE = "RiderInput";
    public static final String RATE_TABLE1_TABLE = "RateTable1";
    public static final String QUOTATION_TABLE = "Quotation";
    public static final String PT_PPT_MASTER_TABLE = "PtPptMaster";
    public static final String PROPOSAL_SECTION_TABLE = "ProposalSection";
    public static final String PRODUCT_ROUNDS_TABLE = "ProductRounds";
    public static final String PRODUCT_RIDER_MAP_TABLE = "ProductRiderMap";
    public static final String PRODUCT_PAYMENT_TYPE_TABLE = "ProductPaymentType";
    public static final String PRODUCT_OTHER_INFO_TABLE = "ProductOtherInfo";
    public static final String PRODUCT_MASTER_TABLE = "ProductMaster";
    public static final String PRODUCT_INPUT_MAPPING_TABLE = "ProductInputMapping";
    public static final String PRODUCT_COMPANY_CATERIES_TABLE = "ProductCompanyCateries";
    public static final String PRODUCT_COMMISSION_TABLE = "ProductCommission";
    public static final String PRODUCT_CHANNEL_MAPPING_TABLE = "ProductChannelMapping";
    public static final String PRODUCT_CATERY_MAP_TABLE = "ProductCateryMap";
    public static final String PREMIUM_MASTER_TABLE = "PremiumMaster";
    public static final String POL_UNIT_HISTORY_TABLE = "PolUnitHistory";
    public static final String POL_STATUS_MASTER_TABLE = "PolStatusMaster";
    public static final String POL_STATUS_HISTORY_TABLE = "PolStatusHistory";
    public static final String OUTPUT_SUMMARY_TABLE = "OutputSummary";
    public static final String OPTION_VALIDATION_TABLE = "OptionValidation";
    public static final String OPTION_MASTER_TABLE = "OptionMaster";
    public static final String OPTION_LEVEL_MASTER_TABLE = "OptionLevelMaster";
    public static final String MORTALITY_CHARGE_TABLE = "MortalityCharge";
    public static final String MORTALITY_CHARGE_TABLE_NEW = "MortalityChargeNew";
    public static final String MI_MASTER_TABLE = "MIMaster";
    public static final String INPUT_VALIDATION_FORMULA_TABLE = "InputValidationFormula";
    public static final String INPUT_VALIDATION_TABLE = "InputValidation";
    public static final String INPUT_OUTPUT_MASTER_TABLE = "InputOutputMaster";
    public static final String INPUT_KEYWORDS_TABLE = "InputKeywords";
    public static final String INPUT_KEYWORD_VALUES_TABLE = "InputKeywordValues";
    public static final String INPUT_KEYWORD_DEFAULT_VALUES_TABLE = "InputKeywordDefaultValues";
    public static final String FUND_MASTER_TABLE = "FundMaster";
    public static final String FUND_MAPPING_TABLE = "FundMapping";
    public static final String FUND_ALLOCATION_RULE_TABLE = "FundAllocationRule";
    public static final String COMPANY_DEFINED_CATEGORIES_TABLE = "CompanyDefinedCategories";
    public static final String CITY_MASTER_TABLE = "CityMaster";
    public static final String ALLOC_CHARGE_TABLE = "AllocCharge";
    public static final String AGE_MASTER_TABLE = "AgeMaster";


    public static final String MANDATORY_FIELD = "Please select*";
    //Product annotations
    public static final String LI_FIRST_NAME_ANNOTATION = "@LI_FIRST_NAME";
    public static final String LI_LAST_NAME_ANNOTATION = "@LI_LAST_NAME";
    public static final String LI_NAME_ANNOTATION = "@LI_NAME";
    public static final String LI_DOB_ANNOTATION = "@LI_DOB";
    public static final String LI_AGE_ANNOTATION = "@LI_AGE";

    public static final String PR_FIRST_NAME_ANNOTATION = "@PR_FIRST_NAME";
    public static final String PR_LAST_NAME_ANNOTATION = "@PR_LAST_NAME";
    public static final String PR_NAME_ANNOTATION = "@PR_NAME";
    public static final String PR_DOB_ANNOTATION = "@PR_DOB";
    public static final String PR_AGE_ANNOTATION = "@PR_AGE";
    public static final String PR_GENDER_ANNOTATION = "@PR_GENDER";
    public static final String EMAIL_ID_ANNOTATION = "@EMAIL_ID";
    public static final String STATE_DETAIL_ANNOTATION = "@STATE_DETAIL";
    public static final String CITY_DETAIL_ANNOTATION = "@CITY_DETAIL";
    public static final String NVEST_ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String NVEST_ERROR_STATUS = "ERROR_STATUS";

    public static final String PRODUCT_ID_ANNOTATION = "@PR_ID";
    public static final String OPTIONS_ANNOTATION = "@OPTIONS";
    public static final String MODAL_PREM_ANNOTATION = "@PR_MODALPREM";
    public static final String LA_AGE_ANNOTATION = "@LI_ENTRY_AGE";
    public static final String GENDER_ANNOTATION = "@LI_GENDER";
    public static final String SMOKING_ANNOTATION = "@LI_SMOKE";
    public static final String PR_PT_ANNOTATION = "@PR_PT";

    public static final String PR_PPT_ANNOTATION = "@PR_PPT";
    public static final String TAX_GROUP_ANNOTATION = "@TAXGROUP";
    public static final String PREMIUM_RATE_ANNOTATION = "@PREM_RATE";
    public static final String INPUT_MODE_ANNOTATION = "@INPUT_MODE";
    public static final String PRODUCT_SUM_ASSURED_ANNOTATION = "@PR_SA";
    public static final String NSAP_FLAG_ANNOTATION = "@NSAP_FLAG";
    public static final String RD_PT_ANNOTATION = "@RD_PT";
    public static final String BONUS_SCR_ANNOTATION = "@BONUS_SCR";
    public static final String PR_OPTION_ANNOTATION = "@PR_OPTION_";
    public static final String OPTION_VALUE_ANNOTATION = "@OPTION_VALUE_";
    public static final String PR_OPTION_VALUE_ANNOTATION = "@PR_OPTION_VALUE_";

    public static final String PR_OPTION_1_ANNOTATION = "@PR_OPTION_1";
    public static final String PR_OPTION_2_ANNOTATION = "@PR_OPTION_2";
    public static final String OPTION_VALUE_3_ANNOTATION = "@OPTION_VALUE_3";
    public static final String FUND_STRATEGY_ID_ANNOTATION = "@FUNDSTRATEGYID";
    public static final String PR_ANN_PREMIUM_ANNOTATION = "@PR_ANNPREM";
    public static final String PR_MONTHLY_INCOME_ANNOTATION = "@PR_MI";
    public static final String PR_SAMF_ANNOTATION = "@PR_SAMF";
    public static final String FUND_ID_ANNOTATION = "@FUNDID_";
    public static final String EXTRA_MORTALITY_RATE_ANNOTATION = "@PR_EMRID";
    public static final String PR_FLAT_EXTRA_ANNOTATION = "@PR_FLATEXTRAID";
    public static final String RIDER_ID_ANNOTATION = "@RD_ID_";
    public static final String RIDER_SUM_ASSURED_ANNOTATION = "@RD_SA_";

    //WorkManger Messages
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String FAILURE_MESSAGE = "failure";
    //Gender
    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";

    public static String PRODUCT_HANDLER_TAG  = "productHandler";
    public static String GET_PRODUCT_CHANGE_LIST_TAG  = "GetProductChangeListWorker";


    public static final String CUSTOM_BROADCAST_ACTION = "nvest.com.nvestlibrary.nvestsync.broadcast.activity.CUSTOM_BROADCAST";

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

    //Fragment names
    public static final String LANDING_FRAGMENT_NAME = "Landing";
    public static final String BASIC_INFORMATION_FRAGMENT_NAME = "basic_information";
    public static final String PRODUCT_INFORMATION_FRAGMENT_NAME = "product_information";
    public static final String RIDER_FRAGMENT_NAME = "riders";
    public static final String SELECT_OPTION = "Select";
    public static final String SUMMARY_FRAGMENT_NAME = "summary";

    // permission request codes
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 123;

    // pdf configs
    public static final String PDF_FILE_NAME = "%s.pdf";
    public static final String PRODUCT_FILE_NAME = "OutputForms/%s.txt";
    public static final String PRODUCT_DISCLAIMER_FILE_NAME = "OutputForms/%sDisclaimer.txt";

    // fragment titles
    public static final String TITLE_SELECT_PRODUCT = "Select Product";
    public static final String TITLE_BASIC_INFORMATION = "Basic Information";
    public static final String TITLE_PRODUCT_INFORMATION = "Product Information";
    public static final String TITLE_RIDER_INFORMATION = "Rider Information";
    public static final String TITLE_SUMMARY = "Summary";


    public static final String VALIDATION_IP = "VALIDATION_IP";
    public static final String LIST_VALIDATION_IP = "LIST_VALIDATION_IP";
    public static final String IS_ULIP = "IS_ULIP";
    public static final String UNSELECTED_LEVEL_MINUS_999 = "(-999)";


    public static enum WorkerName {
        PREMIUM_RATES_WORKER, OTHER_TABLES_WORKER, PRODUCT_TABLES_WORKER, DUMMY_WORKER, DELETE_TABLES_WORKER;
    }

    public enum KeywordName {
        LI_ENTRY_AGE, NSAP_FLAG, PR_PPT, PR_PT, INPUT_MODE, LI_NAME, LI_GENDER, PR_CHANNEL, PR_SA,
        PR_MI, PR_ANNPREM, PR_MODALPREM, PR_OPTION, PR_SAMF, PR_EMRID,
        PR_FLATEXTRAID, BONUS_SCR, PR_LOAN_AMT, IS_STAFF, CHILD_AGE_1, LI_SMOKE, LI_SMOKING, SPOUSE_AGE,
        PROPOSER_GENDER, PROPOSER_AGE, PROPOSER_RELATION
    }

    public enum FieldType {
        DOB, List, Number, String, Disabled, Output, CheckBox, Input
    }

    public enum IncomingDataTypes {
        Boolean,
        Char,
        Byte,
        Short,
        Int,
        Long,
        Float,
        Double
    }



    public static final String CHILD_AGE = "child_age";
    public static final String RETURN_ON_INVESTMENT = "return_on_investment";
    public static final String CURRENT_AGE= "What Is Your Current Age?";
    public static final String CHILD_AGE_CAPTION = "How old is your Child?";
    public static final String EXPECTED_USAGE_OF_FUNDS_CAPTION= "At What Age Do You Expect Child To Use This Fund?";
    public static final String RETURN_ON_INVESTMNET_CAPTION = "Expected Return on Investment";
    public static final String INFALTION_CAPTION= "Inflation";
    public static final int MIN_CURRENT_AGE = 25;
    public static final int MIN_CHILD_AGE = 0;
    public static final int MIN_EXPECTED_USAGE_OF_FUNDS= 16;
    public static final int MIN_RETURN_ON_INVESTMENT = 3;
    public static final int MIN_INFLATION= 2;
    public static final int MAX_CURRENT_AGE = 61;
    public static final int MAX_CHILD_AGE = 19;
    public static final int MAX_EXPECTED_USAGE_OF_FUNDS= 26;
    public static final int MAX_RETURN_ON_INVESTMENT = 16;
    public static final int MAX_INFLATION = 11;

    public enum educationFragmentTag{
        CURRENT_AGE_TAG, CHILD_AGE_TAG, EXPECTED_USAGE_OF_FUNDS_TAG  ,RETURN_ON_INVESTMENT_TAG, INFLATION_TAG
        , SELECT_FREQUENCY_TAG ,TEXT_AMT_TAG,ALREADY_TEXT_AMT_TAG
    }

    public static final String RETIREMENT_CURRENT_AGE_CAPTION = "What is your current Age?";
    public static final String RETIREMENT_WHEN_TO_RETIRE_AGE_CAPTION = "When Do you want to retire?";
    public static final String LIFE_EXPECTANCY_CAPTION = "Life Expectancy";
    public static final String PRE_RETIREMENT_CAPTION = "Pre Retirement Investment Return";
    public static final String POST_RETIREMENT_CAPTION = "Post Retirement Investment Return";
    public static final String RETIREMENT_INFLATION_CAPTION = "Inflation";
    public static final String EDUCATION_CAPTION = "Education";
    public static final String MARRIAGE_CAPTION = "Marriage";
    public static final String RETIREMENT_CAPTION = "Retirement";
    public static final String HOUSE_CAPTION = "House";
    public static final String ACCUMULATE_WEALTH_CAPTION = "Accumulating Wealth";
    public static final String INCOME_REPLACEMENT_CAPTION = "Income Replacement";
    public static final int MIN_RETIREMENT_AGE = 25;
    public static final int MAX_RETIREMENT_AGE = 60;
    public static final int MIN_RETIREMENT_AGE_WHEN = 50;
    public static final int MAX_RETIREMENT_AGE_WHEN = 80;
    public static final int MIN_LIFE_EXPECTANCY = 65;
    public static final int MAX_LIFE_EXPECTANCY = 101;
    public static final int MIN_PRE_RETIREMENT_RETURN = 3;
    public static final int MAX_PRE_RETIREMENT_RETURN = 15;
    public static final int MIN_POST_RETIREMENT_RETURN = 2;
    public static final int MAX_POST_RETIREMENT_RETURN = 15;
    public static final int MIN_RETIREMENT_INFLATION = 2;
    public static final int MAX_RETIREMENT_INFLATION = 10;


    public enum retirementFragmentTag{
        CURRENT_AGE_TAG,  RETIREMENT_AGE_TAG, LIFE_EXPECTANCY_TAG, PRE_RETIREMENT_INVESTMENT_RETURN_TAG ,
        POST_RETIREMENT_INVESTMENT_RETURN_TAG, RETIREMENT_INFLATION_TAG,RETIREMENT_FREQUENCY_TAG

    }

    public static final String WEALTH_CURRENT_AGE_CAPTION = "What is your current Age?";
    public static final String WEALTH_YEARS_WANT_THIS_SAVING_CAPTION = "In How Many Years Do You Want This Saving?";
    public static final String WEALTH_EXPECTED_RETURN_CAPTION = "Expected Return On Investment";
    public static final int WEALTH_MIN_AGE_VALUE = 25;
    public static final int WEALTH_MAX_AGE_VALUE = 60;
    public static final int WEALTH_MIN_YEARS_SAVING_VALUE = 2;
    public static final int WEALTH_MAX_YEARS_SAVING_VALUE = 40;
    public static final int WEALTH_MIN_ROI_VALUE = 3;
    public static final int WEALTH_MAX_ROI_VALUE = 15;

    public enum WealthFragmentTag{
        CURRENT_AGE_TAG ,  YEARS_WANT_THIS_SAVING_TAG , EXPECTED_RETURN_ON_INVESTMENT_TAG,WEALTH_FREQUENCY_TAG

    }

}
