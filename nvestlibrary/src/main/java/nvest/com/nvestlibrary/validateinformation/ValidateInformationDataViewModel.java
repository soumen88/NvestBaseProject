package nvest.com.nvestlibrary.validateinformation;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.database.DatabaseUtils;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.PTPPTGoal;
import nvest.com.nvestlibrary.nvestWebModel.Productneed;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;


public class ValidateInformationDataViewModel extends AndroidViewModel {
    private static final String TAG = ValidateInformationDataViewModel.class.getSimpleName();
    private ValidateInformationDataListener validateInformationDataListener;
    private MutableLiveData<ValidationIP> validationIPMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ValidationIP>> validIpListLiveData = new MutableLiveData<>();
    private long startTime, endTime, duration;
    private LinkedHashMap<Integer, Integer> saveValue = new LinkedHashMap<>();
    public ValidateInformationDataViewModel(@NonNull Application application) {
        super(application);
    }

    public interface ValidateInformationDataListener {
        void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData);

        void onCompleteRidersValidation(MutableLiveData<List<ValidationIP>> validIpListLiveData);

        void onBiGenerated(LinkedHashMap<Integer, HashMap<String, String>> biData);

        void onBiUlipGenerated(LinkedHashMap<Integer, HashMap<String, String>>[] biData);
    }

    public void setValidateInformationDataListener(ValidateInformationDataListener validateInformationDataListener) {
        this.validateInformationDataListener = validateInformationDataListener;
    }

    public ValidationIP ValidateInput(HashMap<String, String> Param) {
        boolean isrider = false;
        int FailedCount = 0;
        int ProductId = 0;
        int addmutable = 1;
        if (Param.containsKey("NeedAdvisory")) {
            addmutable = 0;
        }
        if (Param.containsKey("@RD_ID")) {
            isrider = true;
        }
        ValidationIP valid = new ValidationIP();
        HashMap<String, String> ErrorMessage = new HashMap<String, String>();
        try {
            if (isrider) {
                ProductId = Integer.parseInt(Param.get("@RD_ID"));
            } else {
                ProductId = Integer.parseInt(Param.get("@PR_ID"));
            }
            int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));
            int ModeId = Integer.parseInt(Param.get("@INPUT_MODE"));
            String Platform = "";
            String ProductName = "";
            String Query = "select * from productmaster where productid =" + ProductId + " ORDER BY ROWID ASC LIMIT 1";
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Platform = cursor.getInt(cursor.getColumnIndex("Platform")) == 4 ? "ULIP" : "TRAD";
                ProductName = cursor.getString(cursor.getColumnIndex("ProductName"));
            }

            valid.setProductName(ProductName);
            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            int PTinForm = 0, PPTinForm = 0, SAinForm = 0, APinForm = 0, MPinForm = 0, MIinForm = 0, SAMFinForm = 0, PT = 0, PPT = 0, PtPptId = 0;
            float ModalPremium = 0, AnnualPremium = 0, SumAssured = 0, MonthlyIncome = 0, SAMF = 0, LoadAnnPrems = 0, Tax = 0, TaxYr2 = 0;
            boolean ageOk = true;
            HashMap<Integer, Float> OpVals = new HashMap<Integer, Float>();
            String Options = "", PremiumRate = "0";
            Query = "Select * from InputOutputMaster where ProductId = " + ProductId + " ORDER BY ROWID ASC LIMIT 1";
            Cursor cursorIPOP = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursorIPOP.moveToFirst();
            int PTout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("PT"));
            int PPTout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("PPT"));
            int SAout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("SA"));
            int APout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("AnnualPremium"));
            int MPout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("ModalPremium"));
            int MIout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("MI"));
            int SAMFout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("SAMF"));

            //Calculation of output values if product is not a rider
            if (isrider) {
                PT = Integer.parseInt(Param.get("@RD_PT"));
                PPT = Integer.parseInt(Param.get("@RD_PPT"));
                SumAssured = Float.parseFloat(Param.get("@RD_SA"));
                ModalPremium = Float.parseFloat(Param.get("@RD_MODALPREM"));
                AnnualPremium = Float.parseFloat(Param.get("@RD_ANNPREM"));
                LoadAnnPrems = Float.parseFloat(Param.get("@RD_LOADEDPREM"));
                Tax = Float.parseFloat(Param.get("@RD_TAX"));
                TaxYr2 = Float.parseFloat(Param.get("@RD_TAXYR2"));
                PtPptId = Integer.parseInt(Param.get("@RD_PTPPTID"));
            } else {
                Query = "select * from " + FormulaTable + " where productid = " + ProductId;
                Cursor cursorFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                String TaxMPQuery = "0", PTQuery = "0", PPTQuery = "0", SAQuery = "0", APQuery = "0", MPQuery = "0", MIQuery = "0", SAMFQuery = "0", LoadAPQuery = "0";
                for (cursorFormulas.moveToFirst(); !cursorFormulas.isAfterLast(); cursorFormulas.moveToNext()) {
                    String Formulakeyword = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaKeyword"));
                    String FormulaExtended = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaExtended"));
                    if (Formulakeyword.toUpperCase().equals("[PR_PT]")) {
                        PTinForm = 1;
                        PTQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[PR_PPT]")) {
                        PPTinForm = 1;
                        PPTQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[SA]")) {
                        SAinForm = 1;
                        SAQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[ANN_PREM]")) {
                        APinForm = 1;
                        APQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[MODAL_PREM]")) {
                        MPinForm = 1;
                        MPQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[MI]")) {
                        MIinForm = 1;
                        MIQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[SAMF]")) {
                        SAMFinForm = 1;
                        SAMFQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[PREMIUMRATE]")) {
                        PremiumRate = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[TAX_MP]")) {
                        TaxMPQuery = "Select (" + FormulaExtended + ")";
                    } else if (Formulakeyword.toUpperCase().equals("[LOAD_ANN_PREM]")) {
                        LoadAPQuery = "Select (" + FormulaExtended + ")";
                    } else {
                    }
                }

                //Creating Comma Separated Options
                for (Map.Entry<String, String> e : Param.entrySet()) {
                    if (e.getKey().startsWith("@PR_OPTION")) {
                        Options += e.getValue() + ",";
                    }
                }
                if (!Options.equals("")) {
                    Options = Options.substring(0, Options.length() - 1);
                }

                // Mode Frequency and Mode Discount added as Param.
                Query = "select * from ModeMaster where ModeId = " + ModeId + " ORDER BY ROWID ASC LIMIT 1";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                int freq = cursor.getInt(cursor.getColumnIndex("Frequency"));
                valid.setModeFreq(freq == 0 ? 1 : freq);

                Query = "select  * from ProductMode where productid = " + ProductId + " and modeid = " + ModeId + " ORDER BY ROWID ASC LIMIT 1";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                float disc = cursor.getFloat(cursor.getColumnIndex("Multiplier"));
                CommonMethod.log(TAG, "Dump " + DatabaseUtils.dumpCursorToString(cursor));
                valid.setModeDisc(disc == 0 ? 1 : disc);

                Param.put("@MODE_FREQ", String.valueOf(freq));
                Param.put("@MODE_DISC", String.valueOf(disc));

                String PTFormula = "0";
                String PPTFormula = "0";
                if (!PTQuery.equals("0")) {
                    PTFormula = PTQuery;
                } else {
                    if (Param.containsKey("@PR_PT")) {
                        PTFormula = Param.get("@PR_PT");
                    }
                }
                if (!PPTQuery.equals("0")) {
                    PPTFormula = PPTQuery;
                } else {
                    if (Param.containsKey("@PR_PPT")) {
                        PPTFormula = Param.get("@PR_PPT");
                    }
                }

                try {
                    PT = Integer.parseInt(PTFormula);
                } catch (Exception e) {
                    Query = CommonMethod.ReplaceParams(PTFormula, Param, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        PT = cursor.getInt(0);
                    }
                }
                try {
                    PPT = Integer.parseInt(PPTFormula);
                } catch (Exception e) {
                    Query = CommonMethod.ReplaceParams(PPTFormula, Param, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        PPT = cursor.getInt(0);
                    }
                }


                Param.remove("@PR_PT");
                Param.remove("@PR_PPT");
                Param.put("@PR_PT", String.valueOf(PT));
                Param.put("@PR_PPT", String.valueOf(PPT));

                int TaxGroup = 0;
                Query = "Select TaxGroup from ProductCategoryMap where ProductId = " + ProductId + " and (OptionId is null OR OptionId in (" + Options + "))  ORDER BY ROWID ASC LIMIT 1";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    TaxGroup = cursor.getInt(0);
                }
                Param.put("@TAXGROUP", String.valueOf(TaxGroup));

                //Check if any of the variable is output and its formula is not defined
                if (PTout == 2 && PTinForm == 0) {
                    FailedCount++;
                }
                if (PPTout == 2 && PPTinForm == 0) {
                    FailedCount++;
                }
                if (SAout == 2 && SAinForm == 0) {
                    FailedCount++;
                }
                if (APout == 2 && APinForm == 0) {
                    FailedCount++;
                }
                if (MPout == 2 && MPinForm == 0) {
                    FailedCount++;
                }
                if (MIout == 2 && MIinForm == 0) {
                    FailedCount++;
                }
                if (SAMFout == 2 && SAMFinForm == 0) {
                    FailedCount++;
                }
                if (FailedCount > 0) {
                    if (valid.getErrorMessage().size() == 0) {
                        valid.FailedCount = 1;
                        ErrorMessage.put("General", "Validation error. Please check the inputs again. Or error setting product");
                        valid.setErrorMessage(ErrorMessage);
                        if (addmutable == 1) {
                            validationIPMutableLiveData.setValue(valid);
                            validateInformationDataListener.onCompleteValidation(validationIPMutableLiveData);
                        }
                        return valid;
                    }
                }
                ModalPremium = Param.containsKey("@PR_MODALPREM") == true ? (Param.get("@PR_MODALPREM").equals("") ? 0 : Float.parseFloat(Param.get("@PR_MODALPREM"))) : 0;
                AnnualPremium = Param.containsKey("@PR_ANNPREM") == true ? (Param.get("@PR_ANNPREM").equals("") ? 0 : Float.parseFloat(Param.get("@PR_ANNPREM"))) : 0;
                SumAssured = Param.containsKey("@PR_SA") == true ? (Param.get("@PR_SA").equals("") ? 0 : Float.parseFloat(Param.get("@PR_SA"))) : 0;
                MonthlyIncome = Param.containsKey("@PR_MI") == true ? (Param.get("@PR_MI").equals("") ? 0 : Float.parseFloat(Param.get("@PR_MI"))) : 0;
                SAMF = Param.containsKey("@PR_SAMF") == true ? (Param.get("@PR_SAMF").equals("") ? 0 : Float.parseFloat(Param.get("@PR_SAMF"))) : 0;

                ModalPremium = MPout == 2 ? 0 : ModalPremium;
                AnnualPremium = APout == 2 ? 0 : AnnualPremium;
                SumAssured = SAout == 2 ? 0 : SumAssured;
                MonthlyIncome = MIout == 2 ? 0 : MonthlyIncome;
                SAMF = SAMFout == 2 ? 0 : SAMF;

                //PTPPTID calculation
                Query = "select * from PtPptMaster where productid = " + ProductId + " and (PTFormula = '" + PTFormula + "' OR PTFormula is null OR PTFormula = '') and (PPTFormula = '" + PPTFormula + "' OR PPTFormula is null OR PPTFormula = '') ORDER BY ROWID ASC LIMIT 1";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor == null || cursor.getCount() == 0) {
                    FailedCount++;
                    ErrorMessage.put("PR_PPT", "Invalid PT-PPT combination");
                    valid.setErrorMessage(ErrorMessage);
                    valid.setFailedCount(valid.getErrorMessage().size());
                    if (addmutable == 1) {
                        validationIPMutableLiveData.setValue(valid);
                        validateInformationDataListener.onCompleteValidation(validationIPMutableLiveData);
                    }
                    return valid;
                }
                cursor.moveToFirst();
                PtPptId = cursor.getInt(cursor.getColumnIndex("PtPptId"));

                //Premium Rate Calculation
                float pRate = 0;
                try {
                    Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        pRate = cursor.getFloat(0);
                    }
                } catch (Exception e) {
                    pRate = 0;
                }
                Param.put("@PREM_RATE", Float.toString(pRate));


                try {
                    Query = CommonMethod.ReplaceParams(TaxMPQuery, Param, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        Tax = cursor.getFloat(0);
                    }
                } catch (Exception e) {
                }

                try {
                    if (SAMFout == 2) {
                        Query = CommonMethod.ReplaceParams(SAMFQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            SAMF = cursor.getFloat(0);
                        }
                    }
                } catch (Exception e) {
                    FailedCount++;
                }

                if (Platform.equals("ULIP")) {
                    try {
                        if (SAMFout == 2) {
                            Query = CommonMethod.ReplaceParams(LoadAPQuery, Param, true);
                            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                SAMF = cursor.getFloat(0);
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (APout == 2 && ModalPremium > 0) {
                        AnnualPremium = ModalPremium * freq;
                    }
                    if (MPout == 2 && AnnualPremium > 0) {
                        ModalPremium = AnnualPremium / freq;
                    }
                    if (SAout == 2) {
                        try {
                            Query = CommonMethod.ReplaceParams(SAQuery, Param, true);
                            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                SumAssured = cursor.getFloat(0);
                            }
                        } catch (Exception e) {
                            SumAssured = 0;
                        }
                    }
                } else {
                    //Annual Premium Calculation
                    try {
                        Query = CommonMethod.ReplaceParams(APQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            AnnualPremium = cursor.getFloat(0);
                        }
                    } catch (Exception e) {
                        FailedCount++;
                    }
                    //Modal Premium Calculation
                    try {
                        Query = CommonMethod.ReplaceParams(MPQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            ModalPremium = cursor.getFloat(0);
                        }
                    } catch (Exception e) {
                        FailedCount++;
                    }
                }
                if (SumAssured == 0 && !Platform.equals("ULIP")) {
                    if (SAout == 2) {
                        try {
                            Query = CommonMethod.ReplaceParams(SAQuery, Param, true);
                            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                SumAssured = cursor.getFloat(0);
                            }
                        } catch (Exception e) {
                            FailedCount++;
                        }
                    }
                }

                if (!Param.containsKey("NeedAdvisory")) {
                    //Adding calculated parameters to Generic DTO
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_PT", String.valueOf(PT), CommonMethod.getfieldValueFromGenericDTO("@PR_PT"), "", "List", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_PPT", String.valueOf(PPT), CommonMethod.getfieldValueFromGenericDTO("@PR_PPT"), "", "List", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@MODE_FREQ", String.valueOf(freq), String.valueOf(freq), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@MODE_DISC", String.valueOf(disc), String.valueOf(disc), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_MODALPREM", String.valueOf(ModalPremium), String.valueOf(ModalPremium), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_ANNPREM", String.valueOf(AnnualPremium), String.valueOf(AnnualPremium), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_SA", String.valueOf(SumAssured), String.valueOf(SumAssured), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PR_LOADANNPREM", String.valueOf(LoadAnnPrems), String.valueOf(LoadAnnPrems), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@TAXGROUP", String.valueOf(TaxGroup), String.valueOf(TaxGroup), "", "Number", "Validate");
                    CommonMethod.addDynamicKeyWordToGenericDTO("@PREM_RATE", String.valueOf(PremiumRate), String.valueOf(PremiumRate), "", "Number", "Validate");
                }
            }
            //Setting Values in validation object
            valid.setProductId(ProductId);
            valid.setAnnualPremium(AnnualPremium);
            valid.setModalPremium(ModalPremium);
            valid.setLoadAnnPrems(LoadAnnPrems);
            valid.setSA(SumAssured);
            valid.setTax(Tax);
            valid.setTaxYr2(TaxYr2);
            valid.setPT(PT);
            valid.setPPT(PPT);

            //Validation for both product and rider starts

            //InputValidationFormula Table Validation
            Query = "select * from InputValidationFormula where productid = " + ProductId;
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            int counter = 0;
            cursor.moveToFirst();
            Query = "Select ";
            while (!cursor.isAfterLast()) {
                String FormulaExtended = cursor.getString(cursor.getColumnIndex("FormulaExtended"));
                String FormulaKeyword = cursor.getString(cursor.getColumnIndex("FormulaKeyword")).replace("[", "").replace("]", "");
                String FailedMessage = cursor.getString(cursor.getColumnIndex("ErrorMessage"));
                Query += "(CASE WHEN (" + FormulaExtended + ") THEN 0 ELSE 1 END) as [" + FormulaKeyword + "],'" + FailedMessage + "' as [" + FormulaKeyword + "_FM],";
                cursor.moveToNext();
            }
            if (cursor.getCount() > 0) {
                Query = Query.substring(0, Query.length() - 1);
                Query = CommonMethod.ReplaceParams(Query, Param, true);
                Cursor cursorOutput = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursorOutput.moveToFirst();
                for (int i = 0; i < cursorOutput.getColumnCount(); i = i + 2) {
                    if (cursorOutput.getInt(i) == 1) {
                        FailedCount++;
                        String column_name = cursorOutput.getColumnName(i);
                        ErrorMessage.put(column_name + "_" + i, cursorOutput.getString(i + 1));
                    }
                }
            }

            //Age Master Validation
            String ageqry = "SELECT * FROM [AgeMaster]  where productid=" + ProductId + " and ((";
            ageqry = ageqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            ageqry = ageqry + PPT + ">=FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + ")) and ((";
            ageqry = ageqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and ((";
            ageqry = ageqry + SumAssured + ">= FromSA and " + SumAssured + "<= ToSA ) or FromSA isnull) and (( ";
            ageqry = ageqry + AnnualPremium + ">=FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium ) or FromAnnualPremium isnull)";

            Cursor agemaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(ageqry);
            if (agemaster.getCount() == 0 || agemaster == null) {
                ageOk = false;
                FailedCount++;
            } else if (agemaster.getCount() > 1) {
                ageOk = false;
                FailedCount++;
            }
            agemaster.moveToFirst();

            String saqry = "SELECT * FROM [SAMaster] where productid=" + ProductId + " and ((";
            saqry = saqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            saqry = saqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in (" + Options + ")) and ((";
            saqry = saqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            saqry = saqry + AnnualPremium + ">=FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium ) or FromAnnualPremium isnull) and ((";
            saqry = saqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (";
            saqry = saqry + ModeId + "=ModeId or ModeId isnull)";
            Cursor samaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(saqry);
            if (samaster.getCount() == 0 || samaster == null) {
                FailedCount++;
            } else if (samaster.getCount() > 1) {
                FailedCount++;
            }
            samaster.moveToFirst();

            String prqry = " SELECT * FROM [PremiumMaster] where productid=" + ProductId + " and ((";
            prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
            prqry = prqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            prqry = prqry + SumAssured + ">=FromSA and " + SumAssured + "<= ToSA ) or FromSA isnull) and ((";
            prqry = prqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (";
            prqry = prqry + ModeId + "=ModeId or ModeId isnull)";
            Cursor premiummaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
            if (premiummaster.getCount() == 0 || premiummaster == null) {
                FailedCount++;
            } else if (premiummaster.getCount() > 1) {
                FailedCount++;
            }
            premiummaster.moveToFirst();

            String miqry = "SELECT * FROM [MIMaster] where Productid=" + ProductId + " and((";
            miqry = miqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            miqry = miqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + ")) and ((";
            miqry = miqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (( ";
            miqry = miqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            miqry = miqry + AnnualPremium + ">=FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium ) or FromAnnualPremium isnull)";
            Cursor MImaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(miqry);
            if (MImaster.getCount() == 0 || MImaster == null) {
                FailedCount++;
            } else if (MImaster.getCount() > 1) {
                FailedCount++;
            }
            MImaster.moveToFirst();

            //AgeMasterValidation
            int MinEntryAge = 0, MaxEntryAge = 0, MinMaturityAge = 0, MaxMaturityAge = 0;
            String MinEntryReference = "";
            try {
                MinEntryAge = agemaster.getInt(agemaster.getColumnIndex("MinEntryAge"));
                MinEntryReference = agemaster.getString(agemaster.getColumnIndex("MinEntryReference"));
                MaxEntryAge = agemaster.getInt(agemaster.getColumnIndex("MaxEntryAge"));
                MinMaturityAge = agemaster.getInt(agemaster.getColumnIndex("MinMaturityAge"));
                MaxMaturityAge = agemaster.getInt(agemaster.getColumnIndex("MaxMaturityAge"));
            } catch (Exception e) {
            }

            if (agemaster.getCount() == 0) {
                // Error already recorded added above
            } else if (MinEntryAge > Age && MinEntryReference.toUpperCase().equals("YEAR")) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Minimum entry age for " + ProductName + " is " + MinEntryAge + " years. Kindly revise the age.");
            } else if (MinEntryAge > Age * 365 && MinEntryReference.toUpperCase().equals("DAY")) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Minimum entry age for " + ProductName + " is " + MinEntryAge + " days. Kindly revise the age.");
            } else if (MinEntryAge > Age * 12 && MinEntryReference.toUpperCase().equals("MONTH")) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Minimum entry age for " + ProductName + " is " + MinEntryAge + " months. Kindly revise the age.");
            } else if (MaxEntryAge < Age) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Maximum entry age for " + ProductName + " is " + MaxEntryAge + " years. Kindly revise the age.");
            } else if (MinMaturityAge > Age + PT && MinMaturityAge != 0) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Minimum Maturity age for " + ProductName + " is " + MinMaturityAge + " years. Kindly revise the age or PT.");
            } else if (MaxMaturityAge < Age + PT && MaxMaturityAge != 0) {
                ageOk = false;
                FailedCount++;
                ErrorMessage.put("LI_ENTRY_AGE", "Maximum Maturity age for " + ProductName + " is " + MaxMaturityAge + " years. Kindly revise the age or PT.");
            }
            //SA Validation
            int MinSA = 0;
            int MaxSA = 0;
            int Interval = 0;
            try {
                MinSA = samaster.getInt(samaster.getColumnIndex("MinSA"));
                MaxSA = samaster.getInt(samaster.getColumnIndex("MaxSA"));
                Interval = samaster.getInt(samaster.getColumnIndex("Interval"));
            } catch (Exception e) {
                CommonMethod.log(TAG, "Exception in min sa " + e.toString());
            }

            if (samaster.getCount() == 0 || (!ageOk && SAout == 2)) {
                // Error already recorded added above
            } else if (MinSA > SumAssured && MinSA != 0) {
                if (SAout == 1) {
                    FailedCount++;
                    ErrorMessage.put("PR_SA", "Minimum Sum Assured for " + ProductName + " is " + MinSA + ".Kindly revise the sum assured.");
                } else {
                    FailedCount++;
                    ErrorMessage.put("PR_SA", "Minimum Sum Assured for " + ProductName + " is " + MinSA + ".Kindly revise the Premium.");
                }
            } else if (MaxSA < SumAssured && MaxSA != 0) {
                if (SAout == 1) {
                    FailedCount++;
                    ErrorMessage.put("PR_SA", "Maximum Sum Assured for " + ProductName + " is " + MaxSA + ".Kindly revise the sum assured.");
                } else {
                    FailedCount++;
                    ErrorMessage.put("PR_SA", "Maximum Sum Assured for " + ProductName + " is " + MaxSA + ".Kindly revise the Premium.");
                }
            } else if (Interval != 0) {
                if ((SumAssured - MinSA) % Interval != 0) {
                    FailedCount++;
                    ErrorMessage.put("PR_SA", "Value of Sum Assured for " + ProductName + " should be in incremental of " + Interval + " starting from " + MinSA + ". Kindly revise the Sum Assured");
                }
            }

            //Premium Validation
            int MinPremium = 0, MaxPremium = 0;
            Interval = 0;
            try {
                MinPremium = premiummaster.getInt(premiummaster.getColumnIndex("MinPremium"));
                MaxPremium = premiummaster.getInt(premiummaster.getColumnIndex("MaxPremium"));
                Interval = premiummaster.getInt(premiummaster.getColumnIndex("Interval"));
            } catch (Exception e) {
            }
            if (premiummaster.getCount() == 0 || (!ageOk && MPout == 2)) {
                // Error already recorded added above
            } else if (MinPremium > ModalPremium && MinPremium != 0) {
                if (MPout == 1) {
                    FailedCount++;
                    ErrorMessage.put("PR_MODALPREM", "Minimum Premium for " + ProductName + " is " + MinPremium + ". Kindly revise the Premium.");
                } else {
                    FailedCount++;
                    ErrorMessage.put("PR_MODALPREM", "Minimum Premium for " + ProductName + " is " + MinPremium + ". Kindly revise the Sum Assured.");
                }
            } else if (MaxPremium < ModalPremium && MaxPremium != 0) {
                if (MPout == 1) {
                    FailedCount++;
                    ErrorMessage.put("PR_MODALPREM", "Maximum Premium for " + ProductName + " is " + MaxPremium + ". Kindly revise the Premium.");
                } else {
                    FailedCount++;
                    ErrorMessage.put("PR_MODALPREM", "Maximum Premium for " + ProductName + " is " + MaxPremium + ". Kindly revise the Sum Assured.");
                }
            } else if (Interval != 0) {
                if ((ModalPremium - MinPremium) % Interval != 0) {
                    FailedCount++;
                    ErrorMessage.put("PR_MODALPREM", "Value of Premium for " + ProductName + " should be in incremental of " + Interval + " starting from " + MinPremium + ". Kindly revise the Premium");
                }
            }

            //Monthly Income Validation
            int MinMI = 0, MaxMI = 0;
            Interval = 0;
            try {
                MinMI = MImaster.getInt(MImaster.getColumnIndex("MinMI"));
                MaxMI = MImaster.getInt(MImaster.getColumnIndex("MaxMI"));
                Interval = MImaster.getInt(MImaster.getColumnIndex("Interval"));
            } catch (Exception e) {
            }
            if (MImaster.getCount() == 0 || (!ageOk && MPout == 2)) {
                // Error already recorded added above
            } else if (MinMI > MonthlyIncome && MinMI != 0) {
                FailedCount++;
                ErrorMessage.put("PR_MI", "Minimum Monthly Income for " + ProductName + " is " + MinMI + ". Kindly revise it.");
            } else if (MaxMI < MonthlyIncome && MaxMI != 0) {
                FailedCount++;
                ErrorMessage.put("PR_MI", "Maximum Monthly Income for " + ProductName + " is " + MaxMI + ". Kindly revise it.");
            } else if (Interval != 0) {
                if ((MonthlyIncome - MinMI) % Interval != 0) {
                    FailedCount++;
                    ErrorMessage.put("PR_MI", "Value of Monthly Income for " + ProductName + " should be in incremental of " + Interval + " starting from " + MinMI + ". Kindly revise the Monthly Income");
                }
            }

            // IF PRODUCT IS ULIP
            if (Platform.equals("ULIP")) {
                if (SAMFout == 1) {
                    SAMF = Float.parseFloat(Param.get("@PR_SAMF"));
                } else if (SAMFout != 2) {
                    SAMF = SumAssured / AnnualPremium;
                }

                String UlipSAqry = "SELECT * FROM [UlipSAMaster] where ProductId=" + ProductId + " and ((";
                UlipSAqry = UlipSAqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull) and ((";
                UlipSAqry = UlipSAqry + PPT + ">= FromPPt and " + PT + "<= ToPPt) or FromPPt isnull) and (";
                UlipSAqry = UlipSAqry + "OptionId isnull Or OptionId in (" + Options + ")) and ((";
                UlipSAqry = UlipSAqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull)";
                Cursor SAMFmaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(UlipSAqry);
                SAMFmaster.moveToFirst();

                float SAMultiLow = SAMFmaster.getInt(SAMFmaster.getColumnIndex("SAMultiLow"));
                float SAMultiHigh = SAMFmaster.getInt(SAMFmaster.getColumnIndex("SAMultiHigh"));
                float PTMultiLow = SAMFmaster.getInt(SAMFmaster.getColumnIndex("PTMultiLow"));

                float v1 = SAMultiLow > (PTMultiLow * PT) ? SAMultiLow : (PTMultiLow * PT);
                if (SAMF < v1) {
                    FailedCount++;
                    ErrorMessage.put("PR_SAMF", "Minimum Sum Assured Multiple for " + ProductName + " is " + v1);
                } else if (SAMF > SAMultiHigh) {
                    FailedCount++;
                    ErrorMessage.put("PR_SAMF", "Maximum Sum Assured Multiple for " + ProductName + " is " + SAMultiHigh);
                }
            }

            int i1 = 0;
            for (Map.Entry<Integer, Float> item : OpVals.entrySet()) {
                Integer key = item.getKey();
                Float optionvalue = item.getValue();

                //Option Value Validation
                String opValidationqry = "SELECT *FROM [OptionValidation] ";
                opValidationqry = opValidationqry + "Where ProductId = " + ProductId + " and optionId = " + key + " and ((";
                opValidationqry = opValidationqry + PT + " >= FromPT and  " + PT + "<=ToPT) or FromPT isNull ) and ((";
                opValidationqry = opValidationqry + PPT + " >=FromPPT and " + PPT + " <= ToPPT ) or FromPPT isNull ) and ((";
                opValidationqry = opValidationqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (( ";
                opValidationqry = opValidationqry + SumAssured + ">=FromsSA and " + SumAssured + " <= ToSA ) or FromsSa is null) and ((";
                opValidationqry = opValidationqry + AnnualPremium + " >= FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium) or FromAnnualPremium isnull) and((";
                opValidationqry = opValidationqry + Age + ">= FromAge and " + Age + "<=ToAge ) or FromAge isnull)";
                Cursor opValidation = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(opValidationqry);
                opValidation.moveToFirst();

                float MaxValue = opValidation.getInt(opValidation.getColumnIndex("MaxValue"));
                float MinValue = opValidation.getInt(opValidation.getColumnIndex("MinValue"));
                Interval = opValidation.getInt(opValidation.getColumnIndex("Interval"));

                if (opValidation.getCount() == 0 || opValidation == null) {
                    FailedCount++;
                    i1++;
                    continue;
                }
                Query = "Select inputfieldname from optionmaster where productid = " + ProductId + " and optionid = " + key;
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                String FieldName = cursor.getString(cursor.getColumnIndex("InputFieldName"));

                if (MaxValue < optionvalue && MaxValue != 0) {
                    FailedCount++;
                    ErrorMessage.put("OPTION_VALUE" + i1, "Maximum value of " + FieldName + " must be " + MaxValue + ". Kindly revise the value");
                } else if (MinValue > optionvalue && MinValue != 0) {
                    FailedCount++;
                    ErrorMessage.put("OPTION_VALUE" + i1, "Minimum value of " + FieldName + " must be " + MinValue + ". Kindly revise the value");
                } else if (Interval != 0) {
                    if ((optionvalue - MinValue) % Interval != 0) {
                        FailedCount++;
                        ErrorMessage.put("OPTION_VALUE" + i1, FieldName + " must be in multiples of " + Interval + ". Kindly revise.");
                    }
                }
                i1++;
            }

            //Input Validation
            String ipValsqry = "SELECT * FROM [InputValidation]";
            ipValsqry = ipValsqry + " Where productId= " + ProductId + " and ((" + PT + ">=FromPT and " + PT + "<=ToPT )  or FromPT isnull) and ((";
            ipValsqry = ipValsqry + PPT + ">=FromPPT and " + PPT + "<=ToPPT )  or FromPPT isnull) and ((";
            ipValsqry = ipValsqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId is null) and (( ";
            ipValsqry = ipValsqry + SumAssured + ">=FromSA and " + SumAssured + " <= ToSA ) or FromSa is null) and (";
            ipValsqry = ipValsqry + "OptionId isnull or OptionId in (" + Options + ")) and ((";
            ipValsqry = ipValsqry + AnnualPremium + " >= FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium) or FromAnnualPremium isnull) and(";
            ipValsqry = ipValsqry + ModeId + "=ModeId or ModeId is null) and ((";
            ipValsqry = ipValsqry + Age + ">=FromAge and " + Age + "<=ToAge ) or FromAge isnull)";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(ipValsqry);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String KeywordName = cursor.getString(cursor.getColumnIndex("KeywordName"));
                String KeywordLabel = cursor.getString(cursor.getColumnIndex("KeywordLabel"));
                int MaxValue = cursor.getInt(cursor.getColumnIndex("MaxValue"));
                int MinValue = cursor.getInt(cursor.getColumnIndex("MinValue"));
                Interval = cursor.getInt(cursor.getColumnIndex("Interval"));

                float Value = 0;
                String OutputFormula = "";
                if (KeywordName.contains("[")) {
                    try {
                        Query = "Select FormulaExtended from " + FormulaTable + " where ProductId= " + ProductId + " and FormulaKeyword='" + KeywordName + "'";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            OutputFormula = cursor.getString(0);
                            Query = "Select (" + CommonMethod.ReplaceParams(OutputFormula, Param, true) + ")";
                            Cursor cursorOutput = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                            if (cursorOutput != null && cursorOutput.getCount() > 0) {
                                cursorOutput.moveToFirst();
                                Value = cursorOutput.getFloat(0);
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    if (Param.containsKey(KeywordName)) {
                        Value = Float.parseFloat(Param.get(KeywordName));
                    } else {
                        Value = -777;
                    }
                }
                if (Value == -999 && KeywordName.toLowerCase().contains("@option_value"))
                    continue;
                if (KeywordName.contains("[") && !ageOk) {
                    // Do not show output keywords error if age is not ok
                } else if (Value == -777) {
                    FailedCount++;
                    ErrorMessage.put(KeywordName, "No value received for " + KeywordLabel);
                    continue;
                } else if (MaxValue < Value && MaxValue != 0) {
                    FailedCount++;
                    ErrorMessage.put(KeywordName, "Maximum value of " + KeywordLabel + " must be " + MaxValue + " . Kindly revise the value");
                } else if (MinValue > Value && MinValue != 0) {
                    FailedCount++;
                    ErrorMessage.put(KeywordName, "Minimum value of " + KeywordLabel + " must be " + MinValue + " . Kindly revise the value");
                } else if (Interval != 0) {
                    if ((Value - MinValue) % Interval != 0) {
                        FailedCount++;
                        ErrorMessage.put(KeywordName, "Value of " + KeywordLabel + " must be in multiples of " + Interval + ". Kindly revise the value.");
                    }
                } else {
                    //valid.ErrorMessage.Add(new KeyValuePair<string, string>(KeywordName);
                    //,"");
                }
            }
            valid.setFailedCount(FailedCount);
            valid.setErrorMessage(ErrorMessage);
        } catch (Exception e) {
            CommonMethod.log(TAG, "Exception in raw query " + e.toString());
            CommonMethod.log(TAG, "Line Number " + (e.getStackTrace()[0].getLineNumber()));

        }
        if (addmutable == 1) {
            validationIPMutableLiveData.setValue(valid);
            validateInformationDataListener.onCompleteValidation(validationIPMutableLiveData);
        }
        return valid;
    }

    public List<ValidationIP> ValidateAllRiders() {
        List<ValidationIP> valid = new ArrayList<>();
        HashMap<String, String> Param = new HashMap<String, String>();
/*        Param.put("@LI_ENTRY_AGE".toUpperCase(), "40");
        Param.put("@LI_GENDER".toUpperCase(), "M");
        Param.put("@INPUT_MODE".toUpperCase(), "1");
        Param.put("@PR_ID".toUpperCase(), "1401");
        Param.put("@OPTIONS".toUpperCase(), "");
        Param.put("@OptionValues".toUpperCase(), "");
        Param.put("@PR_PT".toUpperCase(), "60");
        Param.put("@PR_PPT".toUpperCase(), "15");
        Param.put("@PR_PT".toUpperCase(), "60");
        Param.put("@Mode_Freq".toUpperCase(), "1");
        Param.put("@Mode_Disc".toUpperCase(), "1");
        Param.put("@PR_ANNPREM".toUpperCase(), "64935");
        Param.put("@PR_MonthlyIncome".toUpperCase(), "0");
        Param.put("@PR_SA".toUpperCase(), "500000");
        Param.put("@PR_SAMF".toUpperCase(), "0");
        Param.put("@PR_ModalPrem".toUpperCase(), "64935");
        Param.put("@RD_SA_2003".toUpperCase(), "500000");
        Param.put("@RD_SA_2004".toUpperCase(), "300000");
        Param.put("@RD_ID_2003".toUpperCase(), "1");
        Param.put("@RD_ID_2004".toUpperCase(), "1");
        Param.put("@RD_ID_2005".toUpperCase(), "1");*/
        Param = CommonMethod.getAllParamsFromGenericDTO();
        float SumAnnualPremium = 0, SumModalPremium = 0, SumLoadAnnPrems = 0, SumSA = 0;
        int ProductId = Integer.parseInt(Param.get("@PR_ID"));
        String Query = "select * from ProductRiderMap where ProductId = " + ProductId + " order by EvalSequence asc";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int Rd_Id = (cursor.getInt(cursor.getColumnIndex("RiderId")));
            if (!Param.containsKey("@RD_ID_" + Rd_Id)) {
                Param.put("@RD_Flag_" + Rd_Id, "0");
                // cursor.moveToNext();
                continue;
            }
            ValidationIP val = new ValidationIP();
            HashMap<String, String> ErrorMessage = new HashMap<String, String>();

            try {
                Query = "select upper(TypeName) from TypeMaster where  TypeId = (Select platform from productmaster where productid = " + Rd_Id + ")";
                Cursor cursorWOP = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursorWOP.moveToFirst();
                String RiderType = cursorWOP.getString(0);
                int isWOP = RiderType.equals("WOP") ? 1 : 0;
                val.ProductId = Rd_Id;
                Param.put("@RD_Flag_" + Rd_Id, "1");
                Param.remove("@RD_ID");
                Param.remove("@RD_SA");
                Param.put("@RD_ID", String.valueOf(Rd_Id));
                if (isWOP == 0) {
                    Param.put("@RD_SA", Param.get("@RD_SA_" + Rd_Id));
                    val = ValidateRider(Param, true, false);
                } else {
                    float WOPSA = 0, WOPAP = 0, WOPMP = 0, WOPLOADANNPREM = 0, WOPSATotal = 0, WOPTAX = 0, WOPTAXYr2 = 0;
                    int WOPPT = 0;
                    int WOPCalcIndividual = (cursor.getInt(cursor.getColumnIndex("WOPCalcIndividual")));
                    int WOPValIndividual = (cursor.getInt(cursor.getColumnIndex("WOPValIndividual")));
                    String WOPProductsList = (cursor.getString(cursor.getColumnIndex("WOPProducts")));
                    List<String> WOPProducts = Arrays.asList(WOPProductsList.split(","));
                    List<ValidationIP> ValWOPProducts = new ArrayList<>();

                    //Combined calculation for WOP Adding premium of all riders and products
                    if (WOPCalcIndividual == 0) {
                        for (String wp : WOPProducts) {
                            if (ProductId == Integer.parseInt(wp)) {
                                WOPSATotal = WOPSATotal + Float.parseFloat(Param.get("@PR_ANNPREM"));
                            } else {
                                WOPSATotal = WOPSATotal + Float.parseFloat(Param.get("@RD_ANNPREM_" + wp));
                            }
                        }
                        WOPPT = Integer.parseInt(Param.get("@PR_PPT"));
                    }
                    //Individual calculation for WOP Adding premium of all riders and products
                    else {
                        for (String wp : WOPProducts) {
                            if (ProductId == Integer.parseInt(wp)) {
                                WOPSA = Float.parseFloat(Param.get("@PR_ANNPREM"));
                                WOPPT = Integer.parseInt(Param.get("@PR_PPT"));
                            } else {
                                WOPSA = Float.parseFloat(Param.get("@RD_ANNPREM_" + wp));
                                WOPPT = Integer.parseInt(Param.get("@RD_PPT_" + wp));
                            }
                            Param.remove("@RD_SA");
                            Param.remove("@RD_PT");
                            Param.remove("@RD_PPT");
                            Param.put("@RD_SA", String.valueOf(WOPSA));
                            Param.put("@RD_PT", String.valueOf(WOPPT));
                            Param.put("@RD_PPT", String.valueOf(WOPPT));
                            ValidationIP valIndividualRider = new ValidationIP();
                            //Individual WOP Premium Validation
                            if (WOPValIndividual == 1) {
                                valIndividualRider = ValidateRider(Param, true, false);

                            } else {
                                valIndividualRider = ValidateRider(Param, false, false);
                            }
                            valIndividualRider.setProductId(Integer.parseInt(wp));
                            ValWOPProducts.add(valIndividualRider);
                            WOPAP = WOPAP + valIndividualRider.getAnnualPremium();
                            WOPMP = WOPMP + valIndividualRider.getModalPremium();
                            WOPLOADANNPREM = WOPLOADANNPREM + valIndividualRider.getLoadAnnPrems();
                            WOPTAX = WOPTAX + valIndividualRider.getTax();
                            WOPTAXYr2 = WOPTAXYr2 + valIndividualRider.getTaxYr2();
                            WOPSATotal = WOPSATotal + WOPSA;

                            Param.remove("@RD_ANNPREM");
                            Param.remove("@RD_MODALPREM");
                            Param.remove("@RD_LOADEDPREM");
                            Param.remove("@RD_TAX");
                            Param.remove("@RD_TAXYR2");
                            Param.put("@RD_ANNPREM", String.valueOf(WOPAP));
                            Param.put("@RD_MODALPREM", String.valueOf(WOPMP));
                            Param.put("@RD_LOADEDPREM", String.valueOf(WOPLOADANNPREM));
                            Param.put("@RD_TAX", String.valueOf(WOPTAX));
                            Param.put("@RD_TAXYR2", String.valueOf(WOPTAXYr2));
                        }
                        val.setWOProducts(ValWOPProducts);
                    }
                    if (WOPValIndividual == 0) {
                        List<ValidationIP> WOPIndProdPrem = val.getWOProducts();
                        Param.remove("@RD_SA");
                        Param.remove("@RD_PT");
                        Param.remove("@RD_PPT");
                        Param.put("@RD_SA", String.valueOf(WOPSATotal));
                        Param.put("@RD_PT", String.valueOf(WOPPT));
                        Param.put("@RD_PPT", String.valueOf(WOPPT));
                        if (WOPCalcIndividual == 0) {
                            val = ValidateRider(Param, true, false);
                        } else {
                            val = ValidateRider(Param, true, true);
                        }

                        val.setWOProducts(WOPIndProdPrem);
                    } else {
                        val.setProductId(Rd_Id);
                        val.setAnnualPremium(WOPAP);
                        val.setModalPremium(WOPMP);
                        val.setLoadAnnPrems(WOPLOADANNPREM);
                        val.setSA(WOPSATotal);
                    }
                }
                //Adding Other parameters PT, PPT, Annual Premium, Modal Premium from calculated values
                Param.remove("@RD_PT_" + Rd_Id);
                Param.remove("@RD_PPT_" + Rd_Id);
                Param.remove("@RD_ANNPREM_" + Rd_Id);
                Param.remove("@RD_MODALPREM_" + Rd_Id);
                Param.remove("@RD_LOADEDPREM_" + Rd_Id);
                Param.put("@RD_PT_" + Rd_Id, String.valueOf(val.getPT()));
                Param.put("@RD_PPT_" + Rd_Id, String.valueOf(val.getPPT()));
                Param.put("@RD_ANNPREM_" + Rd_Id, String.valueOf(val.getAnnualPremium()));
                Param.put("@RD_MODALPREM_" + Rd_Id, String.valueOf(val.getModalPremium()));
                Param.put("@RD_LOADEDPREM_" + Rd_Id, String.valueOf(val.getLoadAnnPrems()));
                valid.add(val);

                SumAnnualPremium = SumAnnualPremium + val.getAnnualPremium();
                SumModalPremium = SumModalPremium + val.getModalPremium();
                SumLoadAnnPrems = SumLoadAnnPrems + val.getLoadAnnPrems();
                SumSA = SumSA + val.getSA();
            } catch (Exception ex) {
                val.FailedCount++;
                ErrorMessage.put("RiderValidation", "Error validating Rider");
                valid.add(val);
            }
        }

        //Product Rider Validation
        Param.put("@Rider_prem_total".toUpperCase(), String.valueOf(SumAnnualPremium));
        Param.put("@Rider_modal_total".toUpperCase(), String.valueOf(SumModalPremium));
        Param.put("@Rider_loaded_total".toUpperCase(), String.valueOf(SumLoadAnnPrems));
        Param.put("@Rider_sa_total".toUpperCase(), String.valueOf(SumSA));
        String qry = "";
        String error = "";
        String result = "";
        String RiderRuleQuery = "select RiderRuleMaster.RiderRuleId,RuleName,RuleQuery,ErrorMessage from RiderRuleMap inner join RiderRuleMaster on RiderRuleMap.RiderRuleId = RiderRuleMaster.RiderRuleId and ProductId = " + ProductId;
        Query = CommonMethod.ReplaceParams(RiderRuleQuery, Param, true);
        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        int i = 0;
        int FailedCount = valid.get(0).getFailedCount();
        HashMap<String, String> ErrorMessage = new HashMap<>();
        ErrorMessage.putAll(valid.get(0).getErrorMessage());
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String rulequery = cursor.getString(cursor.getColumnIndex("RuleQuery"));
            String errorrider = cursor.getString(cursor.getColumnIndex("ErrorMessage"));
            Query = "SELECT (CASE WHEN (" + rulequery + ") THEN '' ELSE 'FALSE' END)";
            Query = CommonMethod.ReplaceParams(Query, Param, true);
            Cursor cursorOutput = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursorOutput.moveToFirst();
            result = cursorOutput.getString(0);
            if (!result.equals("")) {
                FailedCount++;
                ErrorMessage.put("ProductRiderValidationError_" + "_" + i, errorrider);
            }
            i++;
        }
        valid.get(0).setFailedCount(FailedCount);
        valid.get(0).setErrorMessage(ErrorMessage);
        validIpListLiveData.setValue(valid);
        validateInformationDataListener.onCompleteRidersValidation(validIpListLiveData);

        return valid;
    }

    public ValidationIP ValidateRider(HashMap<String, String> Param, boolean WOPValInd, boolean WOPCalInd) {
        // UpdateFormula(ProductId);
        int ProductId = Integer.parseInt(Param.get("@PR_ID"));
        int RiderId = Integer.parseInt(Param.get("@RD_ID"));
        String Options = Param.get("@OPTIONS");
        String Query = "";
        ValidationIP v = new ValidationIP();
        int FailedCount = 0, RiderPT = 0, RiderPPT = 0;
        HashMap<String, String> ErrorMessage = new HashMap<String, String>();
        Query = "select * from ProductRiderMap where productid = " + ProductId + " and RiderId = " + RiderId;
        Cursor cursorPRMap = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

        //Skipping premiumcalculation if premium for WOP is sum of individual premiums
        if (!WOPCalInd) {
            float RiderSA = Float.parseFloat(Param.get("@RD_SA"));
            int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));
            String Gender = Param.get("@LI_GENDER");
            int inputMode = Integer.parseInt(Param.get("@INPUT_MODE"));
            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            Query = "select * from " + FormulaTable + " where productid = " + RiderId;
            Cursor cursorRiderFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            int PTout = 0;
            int PPTout = 0;
            int SAout = 0;
            int APout = 0;
            int MPout = 0;
            int MIout = 0;
            int SAMFout = 0;
            String PremiumRate = "0", TaxMPQuery = "0", PTQuery = "0", PPTQuery = "0", SAQuery = "0", APQuery = "0", MPQuery = "0", MIQuery = "0", SAMFQuery = "0", LoadAPQuery = "0";

            for (cursorRiderFormulas.moveToFirst(); !cursorRiderFormulas.isAfterLast(); cursorRiderFormulas.moveToNext()) {
                String Formulakeyword = cursorRiderFormulas.getString(cursorRiderFormulas.getColumnIndex("FormulaKeyword"));
                String FormulaExtended = cursorRiderFormulas.getString(cursorRiderFormulas.getColumnIndex("FormulaExtended"));
                if (Formulakeyword.toUpperCase().equals("[RD_PT]")) {
                    PTout = 1;
                    PTQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[RD_PPT]")) {
                    PPTout = 1;
                    PPTQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[ANN_PREM]")) {
                    APout = 1;
                    APQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[MODAL_PREM]")) {
                    MPout = 1;
                    MPQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[PREMIUMRATE]")) {
                    PremiumRate = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[TAX_MP]")) {
                    TaxMPQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[LOAD_ANN_PREM]")) {
                    LoadAPQuery = "Select (" + FormulaExtended + ")";
                } else {
                }
            }

            Query = CommonMethod.ReplaceParams(PTQuery, Param, true);
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                RiderPT = cursor.getInt(0);
            }
            Param.remove("@RD_PT");
            Param.put("@RD_PT", String.valueOf(RiderPT));

            Query = CommonMethod.ReplaceParams(PPTQuery, Param, true);
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                RiderPPT = cursor.getInt(0);
            }

            Param.remove("@RD_PPT");
            Param.put("@RD_PPT", String.valueOf(RiderPPT));

            // Mode Frequency and Mode Discount added as Param.
            int freq = Integer.parseInt(Param.get("@MODE_FREQ"));
            v.setModeFreq(freq == 0 ? 1 : freq);

            //ModeDisc
            Query = "select * from ProductMode where modeid = " + inputMode + " and productid = (CASE WHEN (select useridermode from productridermap where productid = " + ProductId + " and riderid = " + RiderId + ") = 0 THEN " + ProductId + " else " + RiderId + " end)";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursor.moveToFirst();
            float disc = cursor.getFloat(cursor.getColumnIndex("Multiplier"));
            CommonMethod.log(TAG, "Dump " + DatabaseUtils.dumpCursorToString(cursor));
            v.setModeDisc(disc == 0 ? 1 : disc);

            Param.remove("@MODE_DISC");
            Param.put("@MODE_DISC", String.valueOf(disc));

            float pRate = 0;
            try {
                Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    pRate = cursor.getFloat(0);
                }
            } catch (Exception e) {
                pRate = 0;
            }
            Param.remove("@PREM_RATE");
            Param.put("@PREM_RATE", Float.toString(pRate));

            //Annual Premium Calculation
            String AnnualPremium = "0", ModalPremium = "0", SumAssured = "0", LoadAnnPrem = "0";
            try {
                Query = CommonMethod.ReplaceParams(APQuery, Param, true);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    AnnualPremium = cursor.getString(0);
                }
            } catch (Exception e) {
                AnnualPremium = "0";
            }

            //Modal Premium Calculation
            try {
                Query = CommonMethod.ReplaceParams(MPQuery, Param, true);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    ModalPremium = cursor.getString(0);
                }
            } catch (Exception e) {
                ModalPremium = "0";
            }

            //Load Annual Premium Calculation
            try {
                Query = CommonMethod.ReplaceParams(LoadAPQuery, Param, true);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    LoadAnnPrem = cursor.getString(0);
                }
            } catch (Exception e) {
                LoadAnnPrem = "0";
            }

            int TaxGroup = 0;
            Query = "Select TaxGroup from ProductCategoryMap where ProductId = " + RiderId + " and (OptionId is null OR OptionId in (" + Options + "))  ORDER BY ROWID ASC LIMIT 1";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                TaxGroup = cursor.getInt(0);
            }
            Param.remove("@TAXGROUP");
            Param.put("@TAXGROUP", String.valueOf(TaxGroup));

            //Year 1 tax calculation
            float tax = 0;
            try {
                Query = CommonMethod.ReplaceParams(TaxMPQuery, Param, true);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    tax = cursor.getFloat(0);
                }
            } catch (Exception e) {
            }
            v.setTax(tax);

            //Year 2 tax calculation
            float taxYr2 = 0;
            try {
                TaxMPQuery = TaxMPQuery.toUpperCase().replace("POLICYYEAR", "2").replace("POLICYDURATION", "1").replace("POLICYMONTH", "1");
                Query = CommonMethod.ReplaceParams(TaxMPQuery, Param, true);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    taxYr2 = cursor.getFloat(0);
                }
            } catch (Exception e) {
            }
            v.setTaxYr2(taxYr2);

            //Adding Other parameters PT, PPT, Annual Premium, Modal Premium from calculated values
            Param.remove("@RD_ANNPREM");
            Param.remove("@RD_MODALPREM");
            Param.remove("@RD_LOADEDPREM");
            Param.remove("@RD_TAX");
            Param.remove("@RD_TAXYR2");
            Param.put("@RD_ANNPREM", ModalPremium);
            Param.put("@RD_MODALPREM", AnnualPremium);
            Param.put("@RD_LOADEDPREM", LoadAnnPrem);
            Param.put("@RD_TAX", String.valueOf(tax));
            Param.put("@RD_TAXYR2", String.valueOf(taxYr2));

            v.setPT(RiderPT);
            v.setPPT(RiderPPT);

            //If we have to do individual WOP rider validation instead of combined validation for WOP
            if (WOPValInd == false) {
                v.ProductId = RiderId;
                v.setAnnualPremium(Float.parseFloat(AnnualPremium));
                v.setModalPremium(Float.parseFloat(ModalPremium));
                v.setLoadAnnPrems(Float.parseFloat(LoadAnnPrem));
                v.setTax(tax);
                v.setTaxYr2(taxYr2);
                v.setSA(RiderSA);
                v.setFailedCount(FailedCount);
                v.setErrorMessage(ErrorMessage);
                return v;
            }
        } else {
            RiderPT = Integer.parseInt(Param.get("@RD_PT"));
            RiderPPT = Integer.parseInt(Param.get("@RD_PPT"));
        }
        Query = "select * from PtPptMaster where productid = " + RiderId + " and (PTFormula = '" + RiderPT + "' OR PTFormula is null OR PTFormula = '') and (PPTFormula = '" + RiderPPT + "' OR PPTFormula is null OR PPTFormula = '') ORDER BY ROWID ASC LIMIT 1";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if (cursor == null || cursor.getCount() == 0) {
            FailedCount++;
            ErrorMessage.put("PR_PPT", "Invalid PT-PPT combination");
            v.setErrorMessage(ErrorMessage);
            v.setFailedCount(FailedCount);
            return v;
        }
        cursor.moveToFirst();

        int PtPptId = cursor.getInt(cursor.getColumnIndex("PtPptId"));
        Param.remove("@RD_PTPPTID");
        Param.put("@RD_PTPPTID", String.valueOf(PtPptId));

        v = ValidateInput(Param);
        FailedCount += v.getFailedCount();
        ErrorMessage.putAll(v.getErrorMessage());

/*        if (ErrorMessage.size() != 0) {
            v.setErrorMessage(ErrorMessage);
        }*/
        cursorPRMap.moveToFirst();
        List<String> rrId = Arrays.asList((cursorPRMap.getString(cursorPRMap.getColumnIndex("Validation"))).split(","));
        List<Integer> RuleId = new ArrayList<>();
        String RuleIdList = "";
        for (String id : rrId) {
            if (id.equals(""))
                continue;
            RuleId.add(Integer.parseInt(id));
            RuleIdList = RuleIdList + "," + id;
        }
        RuleIdList = RuleIdList.substring(1);
        Query = "select * from RiderRuleMaster where RiderRuleId in (" + RuleIdList + ")";
        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if (cursor == null || cursor.getCount() == 0) {
            v.setFailedCount(FailedCount);
            v.setErrorMessage(ErrorMessage);
            return v;
        }
        String result = "";
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String rulequery = cursor.getString(cursor.getColumnIndex("RuleQuery"));
            String errorrider = cursor.getString(cursor.getColumnIndex("ErrorMessage"));
            Query = "SELECT (CASE WHEN (" + rulequery + ") THEN '' ELSE 'FALSE' END)";
            Query = CommonMethod.ReplaceParams(Query, Param, true);
            Cursor cursorOutput = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursorOutput.moveToFirst();
            result = cursorOutput.getString(0);
            if (!result.equals("")) {
                FailedCount++;
                ErrorMessage.put("RD_SA_" + RiderId + "_" + i, errorrider);
            }
            i++;
        }


        v.setFailedCount(FailedCount);
        v.setErrorMessage(ErrorMessage);
        return v;
    }

    public LinkedHashMap<Integer, HashMap<String, String>>[] GenerateBIULIP(HashMap<String, String> Param) {
        try {
            startTime = System.currentTimeMillis();
            long startime = System.currentTimeMillis();
            CommonMethod.log(TAG,"Bi started " + startime + " ms");
            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            int FundStrategyId = Param.containsKey("@FUNDSTRATEGYID") ? Integer.parseInt(Param.get("@FUNDSTRATEGYID")) : 1;
            int ProductId = Integer.parseInt(Param.get("@PR_ID"));
            int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));
            int needadv = 0;
            double bonus = 0;
            if (Param.containsKey("NeedAdvisory")) {
                needadv = 1;
                bonus = 0.08;
            }
            String Query = "select * from " + FormulaTable + " where productid = " + ProductId + " and (outputloop=0 or outputloop is null)";
            Cursor lstFormula = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

            Query = "select * from " + FormulaTable + " where productid = " + ProductId + " and OutputLoop = 1 order by FormulaId";
            Cursor dynamicFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

            //PolicyTerm Calculation
            int pTerm = 0;
            try {
                Query = "select FormulaExtended from FormulasSqlite where productid = " + ProductId + " and formulakeyword = '[PolicyTerm]'";
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                String PolicyTermQuery = cursor.getString(0);

                Query = CommonMethod.ReplaceParams("SELECT (" + PolicyTermQuery + ")", Param, false);
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                pTerm = cursor.getInt(0);
            } catch (Exception e) {
                CommonMethod.log(TAG, "Policy Term cannot be calculated for gove inputs");
            }
            //CompanyId
            Query = "select CompanyId from ProductMaster where productid = " + ProductId;
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursor.moveToFirst();
            int Comp_ID = cursor.getInt(0);

            Query = "Select IsInput from fundstrategymaster where productid = " + ProductId + " and strategyid = " + FundStrategyId;
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

            //FMC calculation if funds come as input from fromt end
            float WA = 0;
            int str = 0;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                str = cursor.getInt(0);
            } else {
                str = 1;
            }
            HashMap<Integer, Float> Fund = new HashMap<>();
            Query = "select * from FundMaster where fundid in (select fundid from FundMapping where productid = " + ProductId + " )";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Integer FundId = cursor.getInt(cursor.getColumnIndex("FundId"));
                Float FMC = cursor.getFloat(cursor.getColumnIndex("FMCBaseCharge")) +
                        cursor.getFloat(cursor.getColumnIndex("GuaranteeCharge"));
                Fund.put(FundId, FMC);
                if (str == 1) {
                    if (Param.containsKey("@FUNDID_" + FundId)) {
                        int fper = Integer.parseInt(Param.get("@FUNDID_" + FundId));
                        WA = WA + fper * FMC;
                    }
                }
            }
            WA = WA / 100;

            //TempBIULip Creation
            String unique = UUID.randomUUID().toString().toUpperCase();
            int m = 1;
            m = 1;
            Query = "Insert into TempBiUlip ";
            for (int j = 1; j <= pTerm; j++) {
                for (int z = 1; z <= 12; z++) {
                    int LI_ATTAINED_AGE = Age + j - 1;
                    int PolicyYear = j;
                    int PolicyMonth = z;
                    int PolicyDuration = m;
                    float FMC = WA != 0 ? WA : 0;
                    float PW = 0;
                    m++;
                    Query = Query + " select 1," + ProductId + "," + PolicyYear + "," + PolicyMonth + "," + PolicyDuration + "," + LI_ATTAINED_AGE + "," + PW + "," + FMC + ",'" + unique + "'";
                    if (z + 1 <= 12)
                        Query = Query + " UNION ";
                }
                if (j + 1 <= pTerm) {
                    Query = Query + " UNION ";
                }
            }
            try {
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query.toUpperCase());
            } catch (Exception ex) {
                CommonMethod.log(TAG, "Error in generating TempBIULIP");
            }
            Param.put("@UNIQUEKEY", unique);
/*
            Query = "Select * from TempBIULIP where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY order by [PolicyDURATION]";
            Query=CommonMethod.ReplaceParams(Query,Param,false);
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                CommonMethod.log(TAG,"TempBiCount :"+cursor.getCount());
            }
*/
            //For strategyid 2 update FMC
            if (str != 1) {
                Query = " update TempBiUlip set FMC = (select tbl.FMC from  (select sum(percentage * FMCBaseCharge)  as FMC,FromAge,ToAge,FromPM,ToPM,FromRemainingPM,ToRemainingPM " +
                        " from FundAllocationRule  inner join FundMaster on  FundAllocationRule.FundId = FundMaster.FundId  inner join FundRuleMap " +
                        " on FundAllocationRule.FundRuleId = FundRuleMap.FundRuleId  inner join FundStrategyMaster on FundRuleMap.StrategyId =" +
                        " FundStrategyMaster.StrategyId  where FundStrategyMaster.StrategyId =" + str + " and FundStrategyMaster.ProductId = " + ProductId + " and" +
                        " FundAllocationRule.ProductId = " + ProductId + " and FundRuleMap.ProductId = " + ProductId + "  group by FromAge,ToAge,FromPM,ToPM," +
                        " FromRemainingPM,ToRemainingPM) tbl where   " +
                        " (((LI_ATTAINED_AGE  between FromAge and ToAge) or FromAge is null) and " +
                        " ((PolicyMonth  between FromPM and ToPM) or FromPM is null) and " +
                        " ((" + pTerm + "*12-PolicyDuration  between FromRemainingPM and ToRemainingPM) or FromRemainingPM is null) )) where  " +
                        " UniqueKey = '" + unique + "'";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            }

            //Tax Group
/*            int TaxGroup = 0;
            Query = "Select TaxGroup from ProductCategoryMap where ProductId = " + ProductId + " and (OptionId is null OR OptionId in (" + Options + "))  ORDER BY ROWID ASC LIMIT 1";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                TaxGroup = cursor.getInt(0);
            }
            Param.put("@TAXGROUP", String.valueOf(TaxGroup));*/

            //Interest Rate List
            Query = "select * from BonusScr where productid = " + ProductId + " and (" + bonus + "=0 or bonusvalue=" + bonus + ")";
            Cursor IntRateList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            LinkedHashMap<Integer, Float> IntRate = new LinkedHashMap<>();
            if (IntRateList.getCount() == 0) {
                CommonMethod.log(TAG, "Cannot find interest rate scenarios");
            } else {
                int counter = 0;
                for (IntRateList.moveToFirst(); !IntRateList.isAfterLast(); IntRateList.moveToNext()) {

                    Float BonusValue = IntRateList.getFloat(IntRateList.getColumnIndex("BonusValue"));
                    IntRate.put(counter, BonusValue);
                    counter++;
                }
            }

            HashMap<String, Double> paramValues = new HashMap<>();
            List<String> paramKeys = new ArrayList<>();
            paramValues.put("POLICYYEAR", 0.0);
            paramValues.put("LI_ATTAINED_AGE", 0.0);
            paramValues.put("POLICYMONTH", 0.0);
            paramValues.put("POLICYDURATION", 0.0);
            paramValues.put("PW", 0.0);
            paramValues.put("FMC", 0.0);

            //FinalQuery Creation
            Query = "Select [POLICYYEAR], [LI_ATTAINED_AGE], [POLICYMONTH], [POLICYDURATION], [PW], [FMC] ";
            for (lstFormula.moveToFirst(); !lstFormula.isAfterLast(); lstFormula.moveToNext()) {
                String Formulaextended = lstFormula.getString(lstFormula.getColumnIndex("FormulaExtended"));
                String FormulaKeyword = lstFormula.getString(lstFormula.getColumnIndex("FormulaKeyword")).toUpperCase();
                Query += ", " + Formulaextended + " as " + FormulaKeyword;
                paramValues.put(FormulaKeyword.replace("[", "").replace("]", ""), 0.0);
                paramKeys.add(FormulaKeyword.replace("[", "").replace("]", ""));
            }
            Query += " from TempBIUlip where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY order by [PolicyDURATION]";
            LinkedHashMap<Integer, LinkedHashMap<Integer, HashMap<String, String>>> dtData = new LinkedHashMap<>(IntRate.size() * 2);

            //Final Query Execution for each interest rate
            for (int no = 0; no < IntRate.size() * 2; no = no + 2) {
                LinkedHashMap<Integer, HashMap<String, String>> dt = new LinkedHashMap<>(pTerm * 12);
                LinkedHashMap<Integer, HashMap<String, String>> dtRIY = new LinkedHashMap<>(pTerm * 12);
                Param.remove("@INT_RATE");
                Param.put("@INT_RATE", String.valueOf(IntRate.get(no / 2)));
                try {
                    String FinalQuery = CommonMethod.ReplaceParams(Query, Param, false);
                    //cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(FinalQuery);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery(FinalQuery);
                    startime = System.currentTimeMillis();
                    CommonMethod.log(TAG,"TempBI Bi query execution complete " + startime + " ms");
                    int numcolumns = cursor.getColumnCount();
                    int counter = 0;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        LinkedHashMap<String, String> row = new LinkedHashMap<>(numcolumns);
                        LinkedHashMap<String, String> rowRIY = new LinkedHashMap<>(numcolumns);
                        for (int i = 0; i < numcolumns; i++) {
                            String columnname = cursor.getColumnName(i).toUpperCase();
                            String columnval = cursor.getString(i);
                            if (cursor.getString(i) == null) {
                                row.put(columnname, ("0"));
                                rowRIY.put(columnname, ("0"));
                            } else if (columnname.equals("MORT_RATE") || columnname.equals("ST_RATE")) {
                                rowRIY.put(columnname, ("0"));
                                row.put(columnname, columnval);
                            } else {
                                row.put(columnname, columnval);
                                rowRIY.put(columnname, columnval);
                            }
                        }
                        dt.put(counter, row);
                        dtRIY.put(counter, rowRIY);
                        counter++;
                    }
                    startime = System.currentTimeMillis();
                    CommonMethod.log(TAG,"TempBI Bi adding items to list complete " + startime + " ms");

                } catch (Exception ex) {
                    CommonMethod.log(TAG, "Error in Generating TempBI with values");
                }
                dtData.put(no, dt);
                if (needadv == 0) {
                    dtData.put(no + 1, dtRIY);
                }
            }

            //Delete from TempBi once records are executed
            Query = "delete from TempBIULIP where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY";
            Query = CommonMethod.ReplaceParams(Query, Param, false);
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

            startime = System.currentTimeMillis();
            CommonMethod.log(TAG,"TempBiFinish " + startime + " ms");

            int numdatasets = dtData.size();
            int rows = pTerm * 12;

            List<String> paramdynamicKeys = new ArrayList<>();
            dynamicFormulas.moveToFirst();
            while (!dynamicFormulas.isAfterLast()) {
                //  for(int k=0;k<=11;k++){
                String kw = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaKeyword")).toUpperCase().replace("]", "").replace("[", "");
                paramValues.put(kw.replace("[", "").replace("]", ""), 0.0);
                paramdynamicKeys.add(kw.replace("[", "").replace("]", ""));
                dynamicFormulas.moveToNext();
            }
            //HardCodedUlip Calcualation
            for (int no = 0; no < numdatasets; no++) {
                LinkedHashMap<Integer, HashMap<String, String>> dtRef = dtData.get(no);
                for (int i = 0; i < rows; i++) {
                    HashMap<String, String> temprow = new HashMap<>();
                    temprow = dtRef.get(i);
                    double ALL_CHRG = Double.valueOf(temprow.get("ALL_CHRG"));
                    double ADMIN_CHRG = Double.valueOf(temprow.get("ADMIN_CHRG"));
                    double ST_RATE = Double.valueOf(temprow.get("ST_RATE"));
                    double ST_VALUE_1 = (ALL_CHRG + ADMIN_CHRG) * (ST_RATE);
                    temprow.put(("ST_VALUE_1"), String.valueOf(ST_VALUE_1));
                    double FINAL_FV_PREV = 0;
                    if (i == 0) {
                        temprow.put("FINAL_FV_PREV", "0");
                    } else {
                        FINAL_FV_PREV = Double.valueOf(dtRef.get(i - 1).get("FINAL_FV"));
                        temprow.put(("FINAL_FV_PREV"), String.valueOf(FINAL_FV_PREV));
                    }

                    double MODAL_PREM = Double.valueOf(temprow.get("MODAL_PREM"));
                    double FV_1 = (FINAL_FV_PREV + MODAL_PREM - ALL_CHRG - ST_VALUE_1 - ADMIN_CHRG);
                    temprow.put(("FV_1"), String.valueOf(FV_1));

                    double INVESTED_AMT = (MODAL_PREM - ALL_CHRG);
                    temprow.put(("INVESTED_AMT"), String.valueOf(INVESTED_AMT));

                    double SA = Double.valueOf(temprow.get("SA"));
                    double CumPrem = Double.valueOf(temprow.get("CUM_PREM")) * 1.05;
                    double DB_G = Math.max(CumPrem, SA);
                    double DEATHBEN = Math.max(FV_1, CumPrem);
                    temprow.put(("DEATHBEN"), String.valueOf(DEATHBEN));

                    double MORT_RATE = Double.valueOf(temprow.get("MORT_RATE"));
                    double MORT_CHRG = (DEATHBEN - FV_1) * MORT_RATE;
                    temprow.put(("MORT_CHRG"), String.valueOf(MORT_CHRG));

                    double ST_VALUE_2 = MORT_CHRG * ST_RATE;
                    temprow.put(("ST_VALUE_2"), String.valueOf(ST_VALUE_2));

                    double INV_RATE = Double.valueOf(temprow.get("INV_RATE"));
                    double INV_RET = (FV_1 - MORT_CHRG - ST_VALUE_2) * INV_RATE;
                    temprow.put(("INV_RET"), String.valueOf(INV_RET));

                    double FV_2 = (FV_1 - MORT_CHRG - ST_VALUE_2 + INV_RET);
                    temprow.put(("FV_2"), String.valueOf(FV_2));

                    double FMC_RATE = Double.valueOf(temprow.get("FMC_RATE"));
                    double FMC_CHRG = (FV_2) * FMC_RATE;
                    temprow.put(("FMC_CHRG"), String.valueOf(FMC_CHRG));

                    double ST_VALUE_3 = (FMC_CHRG) * ST_RATE;
                    temprow.put(("ST_VALUE_3"), String.valueOf(ST_VALUE_3));

                    double ST_TOTAL = ST_VALUE_1 + ST_VALUE_2 + ST_VALUE_3;
                    temprow.put(("ST_TOTAL"), String.valueOf(ST_TOTAL));

                    temprow.put(("GA_TOTAL"), "0");
                    temprow.put(("OTHER_CHRG"), "0");
                    double TOTAL_CHRG = ST_TOTAL + ALL_CHRG + ADMIN_CHRG + MORT_CHRG + FMC_CHRG;
                    temprow.put(("TOTAL_CHRG"), String.valueOf(TOTAL_CHRG));

                    double FINAL_FV = FV_2 - FMC_CHRG - ST_VALUE_3;
                    temprow.put(("FINAL_FV"), String.valueOf(FINAL_FV));

                    double TOTAL_DB = Math.max(FINAL_FV, DEATHBEN);
                    temprow.put(("TOTAL_DB"), String.valueOf(TOTAL_DB));
                    temprow.put(("DB_G"), String.valueOf(DB_G));

                    double DB_NG = DEATHBEN - DB_G;
                    temprow.put(("DB_NG"), String.valueOf(DB_NG));

                    temprow.put(("TOTAL_SB"), temprow.get("FINAL_FV"));
                    temprow.put(("SB_NG"), temprow.get("FINAL_FV"));
                    temprow.put(("SB_G"), "0");

                    double ANN_PREM = Double.valueOf(temprow.get("ANN_PREM"));
                    double MIN_GSV_1 = Math.min(FINAL_FV, ANN_PREM);
                    temprow.put(("MIN_GSV_1"), String.valueOf(MIN_GSV_1));

                    double SV_RATE = 0;
                    try {
                        SV_RATE = Double.valueOf(temprow.get("SV_RATE"));
                    } catch (Exception ex) {
                    }
                    double MIN_GSV_2 = SV_RATE * MIN_GSV_1;
                    temprow.put(("MIN_GSV_2"), String.valueOf(MIN_GSV_2));

                    double SV_MAX = 0.0;
                    try {
                        SV_MAX = Double.valueOf(temprow.get("SV_MAX"));
                    } catch (Exception ex) {
                    }
                    double MIN_GSV_3 = Math.min(SV_MAX, MIN_GSV_2);
                    temprow.put(("MIN_GSV_3"), String.valueOf(MIN_GSV_3));

                    double GSV = FINAL_FV - MIN_GSV_3;
                    temprow.put(("GSV"), String.valueOf(GSV));
                }
            }
            startime = System.currentTimeMillis();
            CommonMethod.log(TAG,"Monthly Calc Finish " + startime + " ms");

            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=- CALCULATION OF Monthly Calculations DONE. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= \\
            int u = 0;
            int red = 1;
            LinkedHashMap<Integer, HashMap<String, String>>[] dtDataYearly = new LinkedHashMap[IntRate.size()];
            int init = 1;
            if (needadv == 1) {
                init = 0;
            }
            for (int no = init; no < dtData.size(); no = no + 2, u++, red += 2) {
                LinkedHashMap<Integer, HashMap<String, String>> dtRef = dtData.get(no);
                Param.put("@INT_RATE", String.valueOf(IntRate.get(no / 2)));
                float INPUT_IntRate = IntRate.get(no / 2);
                int Rows_Count = dtRef.size();

                //Financial IRR Calculation Starts
                double redinyield = 0;
                double IRRValue_Tot_Ben = 0.0;
                if (needadv == 0) {
                    double[] IRR_Tot_Ben = new double[Rows_Count + 1];
                    for (int k = 0; k < Rows_Count; k++) {
                        IRR_Tot_Ben[k] = Double.valueOf(dtRef.get(k).get("MODAL_PREM")) * (-1.00);
                    }
                    IRR_Tot_Ben[Rows_Count] = Double.valueOf(dtRef.get(Rows_Count - 1).get("TOTAL_SB"));
                    double Guess = (INPUT_IntRate - 0.025) / 12;
                    IRRValue_Tot_Ben = 0;
                    redinyield = 0;
                    double TargetKeywordCurrentValue = 0;
                    double result = 0.0;
                    double TargetDiff = 0;
                    double currentdistance = 0;
                    double previousdistance = 0;
                    double ChangingKeywordValue = 0.001;
                    double delta = 0.001;
                    double TargetValue = 0;
                    int loopcnt = 0;
                    try {
                        do {
                            TargetKeywordCurrentValue = 0;
                            for (int i = 0; i < IRR_Tot_Ben.length; i++) {
                                TargetKeywordCurrentValue += IRR_Tot_Ben[i] / Math.pow((1 + ChangingKeywordValue), i);
                            }

                            TargetDiff = Math.abs(TargetValue - TargetKeywordCurrentValue);
                            if ((TargetDiff < 0.0001 * Math.abs(IRR_Tot_Ben[Rows_Count]) && TargetValue < TargetKeywordCurrentValue) || loopcnt > 20) {
                                TargetDiff = 0;
                            } else {
                                currentdistance = TargetValue - TargetKeywordCurrentValue;
                                if ((previousdistance - currentdistance) != 0) {
                                    delta = ((currentdistance * (delta / (previousdistance - currentdistance))));
                                }
                                ChangingKeywordValue = ((ChangingKeywordValue + delta));
                                previousdistance = currentdistance;
                                loopcnt += 1;
                            }

                        } while (TargetDiff != 0);
                        if (loopcnt < 20 || (TargetValue == 0 && TargetDiff <= 1))
                            result = (ChangingKeywordValue);

                        IRRValue_Tot_Ben = (Math.pow(1 + result, 12) - 1) * 100;
                        redinyield = INPUT_IntRate - (IRRValue_Tot_Ben / 100);
                    } catch (Exception e) {
                        IRRValue_Tot_Ben = 0.00;
                        redinyield = 0;
                    }

                }

                LinkedHashMap<Integer, HashMap<String, String>> dt = new LinkedHashMap<Integer, HashMap<String, String>>();
                //Conversion of Monthly Values to Yearly Values
                Query = "select * from " + FormulaTable + " where productid = " + ProductId + " and isoutput = 1";
                Cursor OutputList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                for (int i = 0; i < dtRef.size() / 12; i++) {
                    HashMap<String, String> dr = new HashMap<>();
                    OutputList.moveToFirst();
                    while (!OutputList.isAfterLast()) {
                        String FormulaKeyword = OutputList.getString(OutputList.getColumnIndex("FormulaKeyword")).toUpperCase().replace("[", "").replace("]", "");
                        Double value = 0.0;
                        String SumOrYE = OutputList.getString(OutputList.getColumnIndex("SumOrYrEnd"));
                        OutputList.moveToNext();
                        if (SumOrYE.equals("1")) {
                            for (int z = 0; z < 12; z++) {
                                try {
                                    value = value + Double.valueOf(dtRef.get(i * 12 + z).get(FormulaKeyword));
                                } catch (Exception e) {
                                    CommonMethod.log(TAG, FormulaKeyword + "not found in monthly BI or error in fetching value of this keyword");
                                }
                            }
                        } else {
                            try {
                                value = Double.valueOf(dtRef.get(i * 12 + 11).get(FormulaKeyword));
                            } catch (Exception e) {
                                CommonMethod.log(TAG, FormulaKeyword + "not found in monthly BI or error in fetching value of this keyword");
                            }
                        }
                        dr.put(FormulaKeyword.toUpperCase(), String.valueOf(value));
                    }
                    dr.put("PolicyYear".toUpperCase(), String.valueOf(i + 1));
                    dr.put("Red_in_Yield", String.valueOf(redinyield));
                    dr.put("Net_Yield", String.valueOf(IRRValue_Tot_Ben / 100));
                    dt.put(i, dr);
                }
                dtDataYearly[u] = dt;
            }

            startime = System.currentTimeMillis();
            CommonMethod.log(TAG,"Yearly Calc and IRR Finish " + startime + " ms");
            endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            CommonMethod.log(TAG,String.format("%s: %s %s %s", "DURATION:", "Bi ulip generated in", duration, "ms"));
            validateInformationDataListener.onBiUlipGenerated(dtDataYearly);
            return dtDataYearly;
        } 
        catch (Exception ex) {
            CommonMethod.log(TAG, "Exception in raw query " + ex.toString());
            CommonMethod.log(TAG, "Line Number " + (ex.getStackTrace()[0].getLineNumber()));
            return null;
        }
    }

    public LinkedHashMap<Integer, HashMap<String, String>> GenerateBI(HashMap<String, String> Param) {
        try {
            startTime = System.currentTimeMillis();
            int queryCounter = 0 ;
            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            int ProductId = Integer.parseInt(Param.get("@PR_ID"));
            int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));

            String Query = "select * from " + FormulaTable + " where productid = " + ProductId + " and (isoutput=1)";
            Cursor lstFormula = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

            //PolicyTerm Calculation
            int pTerm = 0;
            try {
                Query = "select FormulaExtended from " + FormulaTable + " where productid = " + ProductId + " and formulakeyword = '[PolicyTerm]'";
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                cursor.moveToFirst();
                String PolicyTermQuery = cursor.getString(0);

                Query = CommonMethod.ReplaceParams("SELECT (" + PolicyTermQuery + ") as PolicyTerm", Param, false);
                Cursor PTermcursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                PTermcursor.moveToFirst();
                pTerm = PTermcursor.getInt(PTermcursor.getColumnIndex("POLICYTERM"));
            } catch (Exception e) {
                CommonMethod.log(TAG, "Policy Term cannot be calculated for gove inputs");
            }

            //Premium Rate Calculation if not present in params
            if (!Param.containsKey("@PREM_RATE")) {
                float pRate = 0;
                try {

                    Query = "select FormulaExtended from " + FormulaTable + " where productid = " + ProductId + " and formulakeyword = '[PremiumRate]'";
                    Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    cursor.moveToFirst();
                    String PremiumRate = cursor.getString(0);

                    Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        pRate = cursor.getFloat(0);
                    }
                } catch (Exception e) {
                    pRate = 0;
                }
                Param.put("@PREM_RATE", Float.toString(pRate));
            }

            //TempBI Creation
            String unique = UUID.randomUUID().toString().toUpperCase();
            int m = 1;
            m = 1;
            Query = "Insert into TempBI ";
            for (int i = 0; i < pTerm; i++) {
                int LI_ATTAINED_AGE = Age + i - 1;
                int PolicyYear = i + 1;
                Query = Query + " select 1," + ProductId + "," + PolicyYear + "," + LI_ATTAINED_AGE + ",'" + unique + "'";
                if (i + 1 < pTerm) {
                    Query = Query + " UNION ";
                }
            }
            try {
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query.toUpperCase());
            } catch (Exception ex) {
                CommonMethod.log(TAG, "Error in generating TempBI");
            }
            Param.put("@UNIQUEKEY", unique);

            //Interest Rate List
            Query = "select * from BonusScr where productid = " + ProductId;
            Cursor IntRateList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            LinkedHashMap<Integer, Float> IntRate = new LinkedHashMap<>();
            if (IntRateList.getCount() == 0) {
                CommonMethod.log(TAG, "Cannot find interest rate scenarios");
            } else {
                int counter = 0;
                for (IntRateList.moveToFirst(); !IntRateList.isAfterLast(); IntRateList.moveToNext()) {
                    Float BonusValue = IntRateList.getFloat(IntRateList.getColumnIndex("BonusValue"));
                    int BonusScrId = IntRateList.getInt(IntRateList.getColumnIndex("BonusScrId"));
                    IntRate.put(BonusScrId, BonusValue);
                    counter++;
                }
            }
            //FinalQuery Creation
            Query = "Select [LI_ATTAINED_AGE],[POLICYYEAR] ";
            for (lstFormula.moveToFirst(); !lstFormula.isAfterLast(); lstFormula.moveToNext()) {
                String Formulaextended = lstFormula.getString(lstFormula.getColumnIndex("FormulaExtended"));
                String FormulaKeyword = lstFormula.getString(lstFormula.getColumnIndex("FormulaKeyword")).toUpperCase();
                if (Formulaextended.toUpperCase().contains("@BONUS_SCR")) {
                    int i = 1;
                    for (Map.Entry<Integer, Float> entry : IntRate.entrySet()) {
                        String BonusScrId = String.valueOf(entry.getKey());
                        String BonusValue = String.valueOf(entry.getValue());
                        Query += ", (" + Formulaextended.toUpperCase().replace("@BONUS_SCR", BonusScrId) + ") as " + FormulaKeyword.replace("]", "").toUpperCase() + "_BS_" + i + "]";
                        i++;
                    }
                } else {
                    Query += ", " + Formulaextended + " as " + FormulaKeyword;
                }
            }
            Query += " from TempBI where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY order by [POLICYYEAR]";
            List<HashMap<String, Float>> dtData = new ArrayList<>();

            //Final Query Execution
            LinkedHashMap<Integer, HashMap<String, String>> dt = new LinkedHashMap<>();
            try {
                String FinalQuery = CommonMethod.ReplaceParams(Query, Param, false);
                //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(FinalQuery);
                Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery(FinalQuery);
                int counter = 0;
                if (cursor != null) {
                    CommonMethod.log(TAG , "Final query cursor count " + cursor.getCount());
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        LinkedHashMap<String, String> row = new LinkedHashMap<>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            row.put(cursor.getColumnName(i), cursor.getString(i));
                            if (cursor.getColumnName(i).equals("") || cursor.getColumnName(i) == null) {
                                row.put(cursor.getColumnName(i), ("0"));
                            }
                        }
                        dt.put(counter, row);
                        counter++;
                    }
                }
            } catch (Exception ex) {
                CommonMethod.log(TAG, "Error in Generating TempBI with values");
            }


            /*String Query2 = "Select [LI_ATTAINED_AGE],[POLICYYEAR] ";

            for (lstFormula.moveToFirst(); !lstFormula.isAfterLast(); lstFormula.moveToNext()) {
                String Formulaextended2 = lstFormula.getString(lstFormula.getColumnIndex("FormulaExtended"));
                String FormulaKeyword = lstFormula.getString(lstFormula.getColumnIndex("FormulaKeyword")).toUpperCase();
                //CommonMethod.log(TAG , "Formula extended " + Formulaextended);
                Pattern p = Pattern.compile("\\((.*?)\\)");
                if(Pattern.compile(Pattern.quote("Select"), Pattern.CASE_INSENSITIVE).matcher(Formulaextended2).find() && !Formulaextended2.contains("POLICYYEAR")){
                    Matcher m1 = p.matcher(Formulaextended2);
                    while(m1.find()) {
                        int a = m1.group().indexOf("SELECT");
                        if( a > 0){
                            String query123 = m1.group().substring(a, m1.group().lastIndexOf(")"));
                            //CommonMethod.log(TAG , "Output "  + query123 );

                            if(!saveValue.containsKey(query123.hashCode())){
                                String subQuery = CommonMethod.ReplaceParams(query123, Param, false);
                                //CommonMethod.log(TAG , "Sub query " + subQuery);
                                Cursor c = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(subQuery);
                                if(c != null && c.getCount() > 0){
                                    c.moveToFirst();
                                    //CommonMethod.log(TAG , "Value fetched from sub query " + c.getInt(0));
                                    saveValue.put(query123.hashCode(), c.getInt(0));
                                    Formulaextended2 = Formulaextended2.replaceAll(query123, String.valueOf(c.getInt(0)));
                                }
                            }
                            else{
                                // CommonMethod.log(TAG  , "Hash map already has " + query123);
                                queryCounter++;
                                int value  = saveValue.get(query123.hashCode());
                                //CommonMethod.log(TAG , "Value " + value);
                                Formulaextended2 = Formulaextended2.replaceAll(query123, String.valueOf(value));
                            }
                            //System.out.println("Last index of " + m1.group().lastIndexOf(")")) ;
                        }
//                    System.out.println("Index " + a);
//                    System.out.println(m1.group(1));

                    }
                }

                if (Formulaextended2.toUpperCase().contains("@BONUS_SCR")) {
                    int i = 1;
                    for (Map.Entry<Integer, Float> entry : IntRate.entrySet()) {
                        String BonusScrId = String.valueOf(entry.getKey());
                        String BonusValue = String.valueOf(entry.getValue());
                        Query2 += ", (" + Formulaextended2.toUpperCase().replace("@BONUS_SCR", BonusScrId) + ") as " + FormulaKeyword.replace("]", "").toUpperCase() + "_BS_" + i + "]";
                        i++;
                    }
                } else {
                    Query2 += ", " + Formulaextended2 + " as " + FormulaKeyword;
                }
            }
            CommonMethod.log(TAG , "Query counter " + queryCounter);
            Query2 += " from TempBI where ProductID = @PR_ID and UniqueKey = @UNIQUEKEY order by [POLICYYEAR]";
            String finalQuery2 = CommonMethod.ReplaceParams(Query2, Param, false);
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery(finalQuery2);
            int counter = 0;
            if (cursor != null) {
                CommonMethod.log(TAG, "Final query cursor count " + cursor.getCount());
            }*/
            endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            CommonMethod.log(TAG,String.format("%s: %s %s %s", "DURATION:", "Bi generated in", duration, "ms"));
            if(validateInformationDataListener != null){
                validateInformationDataListener.onBiGenerated(dt);
            }

            return dt;
        } catch (Exception ex) {
            CommonMethod.log(TAG, "Exception in raw query " + ex.toString());
            CommonMethod.log(TAG, "Line Number " + (ex.getStackTrace()[0].getLineNumber()));
            return null;
        }
    }

    public HashMap<String, Double> ReverseCalculate(HashMap<String, String> Param) {
        Param.put("@LI_ENTRY_AGE".toUpperCase(), "35");
        Param.put("@LI_GENDER".toUpperCase(), "M");
        Param.put("@INPUT_MODE".toUpperCase(), "1");
        Param.put("@PR_ID".toUpperCase(), "1011");
        Param.put("@OPTIONS".toUpperCase(), "");
        Param.put("@OptionValues".toUpperCase(), "");
        Param.put("@PR_PT".toUpperCase(), "15");
        Param.put("@PR_PPT".toUpperCase(), "10");
        Param.put("@PR_ANNPREM".toUpperCase(), "");
        Param.put("@PR_SA".toUpperCase(), "0");
        Param.put("@PR_MI".toUpperCase(), "0");
        Param.put("@PR_ModalPrem".toUpperCase(), "950000");
        Param.put("@PR_SAMF".toUpperCase(), "10");
        Param.put("@NSAP_FLAG".toUpperCase(), "1");
        Param.put("@PR_CHANNEL".toUpperCase(), "1");
        Param.put("@PR_EMRID".toUpperCase(), "0");
        Param.put("@PR_FLATEXTRAID".toUpperCase(), "0");

        if (Param == null)
            Param = new HashMap<String, String>();

        int Age = Integer.parseInt(Param.get("@LI_ENTRY_AGE"));
        int ProductId = Integer.parseInt(Param.get("@PR_ID"));
        int ModeId = Integer.parseInt(Param.get("@INPUT_MODE"));

        // Mode Frequency and Mode Discount added as Param.
        String Query = "select * from ModeMaster where ModeId = " + ModeId + " ORDER BY ROWID ASC LIMIT 1";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        cursor.moveToFirst();
        int freq = cursor.getInt(cursor.getColumnIndex("Frequency"));

        Query = "select  * from ProductMode where productid = " + ProductId + " and modeid = " + ModeId + " ORDER BY ROWID ASC LIMIT 1";
        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        cursor.moveToFirst();
        float disc = cursor.getFloat(cursor.getColumnIndex("Multiplier"));
        CommonMethod.log(TAG, "Dump " + DatabaseUtils.dumpCursorToString(cursor));

        Param.put("@MODE_FREQ", String.valueOf(freq));
        Param.put("@MODE_DISC", String.valueOf(disc));

        String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
        Query = "select * from " + FormulaTable + " where productid = " + ProductId;
        Cursor cursorFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        String TaxMPQuery = "0", PTQuery = "0", PPTQuery = "0", SAQuery = "0", APQuery = "0", MPQuery = "0", MIQuery = "0", SAMFQuery = "0", LoadAPQuery = "0", PremiumRate = "0";
        for (cursorFormulas.moveToFirst(); !cursorFormulas.isAfterLast(); cursorFormulas.moveToNext()) {
            String Formulakeyword = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaKeyword"));
            String FormulaExtended = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaExtended"));
            if (Formulakeyword.toUpperCase().equals("[PR_PT]")) {
                PTQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[PR_PPT]")) {
                PPTQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[SA]")) {
                SAQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[ANN_PREM]")) {
                APQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[MODAL_PREM]")) {
                MPQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[MI]")) {
                MIQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[SAMF]")) {
                SAMFQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[PREMIUMRATE]")) {
                PremiumRate = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[TAX_MP]")) {
                TaxMPQuery = "Select (" + FormulaExtended + ")";
            } else if (Formulakeyword.toUpperCase().equals("[LOAD_ANN_PREM]")) {
                LoadAPQuery = "Select (" + FormulaExtended + ")";
            } else {
            }
        }
        int PT = 0, PPT = 0;
        String PTFormula = "0", PPTFormula = "0";
        if (!PTQuery.equals("0")) {
            PTFormula = PTQuery;
        } else {
            if (Param.containsKey("@PR_PT")) {
                PTFormula = Param.get("@PR_PT");
            }
        }
        if (!PPTQuery.equals("0")) {
            PPTFormula = PPTQuery;
        } else {
            if (Param.containsKey("@PR_PPT")) {
                PPTFormula = Param.get("@PR_PPT");
            }
        }
        try {
            PT = Integer.parseInt(PTFormula);
        } catch (Exception e) {
            Query = CommonMethod.ReplaceParams(PTFormula, Param, true);
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                PT = cursor.getInt(0);
            }
        }
        try {
            PPT = Integer.parseInt(PPTFormula);
        } catch (Exception e) {
            Query = CommonMethod.ReplaceParams(PPTFormula, Param, true);
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                PPT = cursor.getInt(0);
            }
        }
        Param.remove("@PR_PT");
        Param.remove("@PR_PPT");
        Param.put("@PR_PT", String.valueOf(PT));
        Param.put("@PR_PPT", String.valueOf(PPT));

        //Storing Options in variables
        List<Float> optvalList = new ArrayList<>();
        List<Integer> optList = new ArrayList<>();
        HashMap<Integer, Float> OpVals = new HashMap<Integer, Float>();
        String Options = Param.get("@OPTIONS");
        String OptionText = Param.get("@OPTIONVALUES");
        List<String> op = Arrays.asList(Options.split(","));
        List<String> tx = Arrays.asList(OptionText.split(","));
        if (Options.length() > 0 || OptionText.length() > 0) {
            int lp = 0;
            for (String st : op) {
                lp++;
                try {
                    optList.add(Integer.parseInt(st));
                    Param.put(("@PR_OPTION_" + lp), (st.equals("") ? "0" : st));
                } catch (Exception e) {
                    Param.put(("@PR_OPTION_" + lp), ("0"));
                    optList.add(0);
                }
            }
            lp = 0;
            for (String st : tx) {
                lp++;
                if (st.equals("")) {
                    Param.put(("@OPTION_VALUE_" + lp), ("-999"));
                } else {
                    Param.put(("@OPTION_VALUE_" + lp), (st));
                    try {
                        OpVals.put(optList.get(lp - 1), Float.parseFloat(st));
                    } catch (Exception e) {

                    }
                }
            }
        }

        Double ModalPremium = Param.containsKey("@PR_MODALPREM") == true ? (Param.get("@PR_MODALPREM").equals("") ? 0 : Double.valueOf(Param.get("@PR_MODALPREM"))) : 0;
        Double AnnualPremium = Param.containsKey("@PR_ANNPREM") == true ? (Param.get("@PR_ANNPREM").equals("") ? 0 : Double.valueOf(Param.get("@PR_ANNPREM"))) : 0;
        Double SumAssured = Param.containsKey("@PR_SA") == true ? (Param.get("@PR_SA").equals("") ? 0 : Double.valueOf(Param.get("@PR_SA"))) : 0;
        Double MonthlyIncome = Param.containsKey("@PR_MI") == true ? (Param.get("@PR_MI").equals("") ? 0 : Double.valueOf(Param.get("@PR_MI"))) : 0;

        //PTPPTID calculation
        Query = "select * from PtPptMaster where productid = " + ProductId + " and (PTFormula = '" + PTFormula + "' OR PTFormula is null OR PTFormula = '') and (PPTFormula = '" + PPTFormula + "' OR PPTFormula is null OR PPTFormula = '') ORDER BY ROWID ASC LIMIT 1";
        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("Invalid PT-PPT combination. Please enter valid values");
        }
        cursor.moveToFirst();
        int PtPptId = cursor.getInt(cursor.getColumnIndex("PtPptId"));

        //Premium Rate Calculation
        double pRate = 0;
        try {
            Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                pRate = cursor.getDouble(0);
            }
        } catch (Exception e) {
            pRate = 0;
        }
        Param.put("@PREM_RATE", String.valueOf(pRate));

        Query = "Select * from InputOutputMaster where ProductId = " + ProductId + " ORDER BY ROWID ASC LIMIT 1";
        Cursor cursorIPOP = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if (cursor == null || cursor.getCount() == 0) {
            CommonMethod.log(TAG, "Invalid Input Output record");
            // throw new IllegalArgumentException("Invalid Input Output record");
        }
        cursorIPOP.moveToFirst();
        int PTout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("PT"));
        int PPTout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("PPT"));
        int SAout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("SA"));
        int APout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("AnnualPremium"));
        int MPout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("ModalPremium"));
        int MIout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("MI"));
        int SAMFout = cursorIPOP.getInt(cursorIPOP.getColumnIndex("SAMF"));

        boolean gotdata = false;
        double sa = 0;
        if (MPout == 1 && ModalPremium == 0) {
            // Op Premium
            String prqry = " SELECT * FROM [PremiumMaster] where productid=" + ProductId + " and ((";
            prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
            prqry = prqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            prqry = prqry + SumAssured + ">=FromSA and " + SumAssured + "<= ToSA ) or FromSA isnull) and ((";
            prqry = prqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (";
            prqry = prqry + ModeId + "=ModeId or ModeId isnull)";
            Cursor premiummaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
            premiummaster.moveToFirst();
            double MinPremium = 0, MaxPremium = 0, Interval = 0;
            try {
                MinPremium = premiummaster.getDouble(premiummaster.getColumnIndex("MinPremium"));
                MaxPremium = premiummaster.getDouble(premiummaster.getColumnIndex("MaxPremium"));
                Interval = premiummaster.getDouble(premiummaster.getColumnIndex("Interval"));
            } catch (Exception e) {
            }

            double minValue = Math.max(SumAssured / (freq * PT), MinPremium);
            while ((minValue <= MaxPremium || MaxPremium == 0) && (minValue >= MinPremium || MinPremium == 0)) {
                Param.remove("@PR_MODALPREM");
                Param.put("@PR_MODALPREM", String.valueOf(minValue));

                //Premium Rate Calculation
                pRate = 0;
                try {
                    Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        pRate = cursor.getDouble(0);
                    }
                } catch (Exception e) {
                    pRate = 0;
                }
                Param.put("@PREM_RATE", String.valueOf(pRate));

                //SA calculation
                try {
                    Query = CommonMethod.ReplaceParams(SAQuery, Param, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        sa = cursor.getDouble(0);
                    }
                } catch (Exception e) {
                    sa = 0.0;
                }

                if (sa >= SumAssured * 0.9995 && sa <= SumAssured * 1.0005) {
                    ModalPremium = minValue;
                    gotdata = true;
                    ModalPremium = Interval == 0 ? ModalPremium : Math.round(ModalPremium / Interval) * Interval;
                    break;
                }
                minValue = minValue * SumAssured / sa;
            }
            if (!gotdata) {
                CommonMethod.log(TAG, "Invalid Premium Amount");
                // throw new IllegalArgumentException ("Invalid Premium Amount");
            }
        }

        gotdata = false;
        double prem = 0.0;
        if (SAout == 1 && SumAssured == 0) {
            // SAMaster
            String saqry = "SELECT * FROM [SAMaster] where productid=" + ProductId + " and ((";
            saqry = saqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            saqry = saqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in (" + Options + ")) and ((";
            saqry = saqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            saqry = saqry + AnnualPremium + ">=FromAnnualPremium and " + AnnualPremium + "<= ToAnnualPremium ) or FromAnnualPremium isnull) and ((";
            saqry = saqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (";
            saqry = saqry + ModeId + "=ModeId or ModeId isnull)";
            Cursor samaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(saqry);
            samaster.moveToFirst();
            double MinSA = 0, MaxSA = 0, Interval = 0;
            try {
                MinSA = samaster.getInt(samaster.getColumnIndex("MinSA"));
                MaxSA = samaster.getInt(samaster.getColumnIndex("MaxSA"));
                Interval = samaster.getInt(samaster.getColumnIndex("Interval"));
            } catch (Exception e) {
                CommonMethod.log(TAG, "Exception in min sa " + e.toString());
            }
            if (ModalPremium != 0) {
                double minValue = Math.max(ModalPremium * freq * PT, MinSA);
                while ((minValue <= MaxSA || MaxSA == 0) && (minValue >= MinSA || MinSA == 0)) {
                    Param.remove("@PR_SA");
                    Param.put("@PR_SA", String.valueOf(minValue));
                    //Premium Rate Calculation
                    pRate = 0;
                    try {
                        Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            pRate = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        pRate = 0;
                    }
                    Param.put("@PREM_RATE", String.valueOf(pRate));

                    //Modal Premium calculation
                    try {
                        Query = CommonMethod.ReplaceParams(MPQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            prem = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        prem = 0.0;
                    }

                    if (prem >= ModalPremium * 0.9995 && prem <= ModalPremium * 1.0005) {
                        gotdata = true;
                        SumAssured = minValue;
                        SumAssured = Interval == 0 ? SumAssured : Math.round(SumAssured / Interval) * Interval;
                        break;
                    }
                    minValue = minValue * ModalPremium / prem;
                }
            } else if (AnnualPremium != 0) {
                double minValue = Math.max(AnnualPremium * freq * PT, MinSA);

                while ((minValue <= MaxSA || MaxSA == 0) && (minValue >= MinSA || MinSA == 0)) {
                    Param.remove("@PR_SA");
                    Param.put("@PR_SA", String.valueOf(minValue));

                    //Premium Rate Calculation
                    pRate = 0;
                    try {
                        Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            pRate = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        pRate = 0;
                    }
                    Param.put("@PREM_RATE", String.valueOf(pRate));

                    //Annual Premium calculation
                    try {
                        Query = CommonMethod.ReplaceParams(APQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            prem = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        prem = 0.0;
                    }

                    if (prem >= AnnualPremium * 0.9995 && prem <= AnnualPremium * 1.0005) {
                        gotdata = true;
                        SumAssured = minValue;
                        SumAssured = Interval == 0 ? SumAssured : Math.round(SumAssured / Interval) * Interval;
                        break;
                    }
                    minValue = minValue * AnnualPremium / prem;
                }
                if (!gotdata) {
                    CommonMethod.log(TAG, "Invalid Premium Amount");
                    //  throw new IllegalArgumentException ("Invalid Premium Amount");
                }

            }
        }

        if (APout == 1 && AnnualPremium == 0) {
            // Op Premium
            String prqry = " SELECT * FROM [PremiumMaster] where productid=" + ProductId + " and ((";
            prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
            prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
            prqry = prqry + Age + ">= FromAge and " + Age + "<=ToAge) or FromAge isnull ) and ((";
            prqry = prqry + SumAssured + ">=FromSA and " + SumAssured + "<= ToSA ) or FromSA isnull) and ((";
            prqry = prqry + PtPptId + ">= FromPtPptId and " + PtPptId + "<= ToPtPptId) or FromPtPptId isnull) and (";
            prqry = prqry + ModeId + "=ModeId or ModeId isnull)";
            Cursor premiummaster = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
            premiummaster.moveToFirst();
            double MinPremium = 0, MaxPremium = 0, Interval = 0;
            try {
                MinPremium = premiummaster.getDouble(premiummaster.getColumnIndex("MinPremium"));
                MaxPremium = premiummaster.getDouble(premiummaster.getColumnIndex("MaxPremium"));
                Interval = premiummaster.getDouble(premiummaster.getColumnIndex("Interval"));
            } catch (Exception e) {
            }

            double MinAnnPrem = freq * MinPremium;
            double MaxAnnPrem = freq * MaxPremium;

            if (SumAssured != 0) {
                double minValue = Math.max(SumAssured / PT, MinAnnPrem);

                while ((minValue <= MaxAnnPrem || MaxAnnPrem == 0) && (minValue >= MinAnnPrem || MinAnnPrem == 0)) {
                    Param.remove("@PR_ANNPREM");
                    Param.put("@PR_ANNPREM", String.valueOf(minValue));

                    //Premium Rate Calculation
                    pRate = 0;
                    try {
                        Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            pRate = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        pRate = 0;
                    }
                    Param.put("@PREM_RATE", String.valueOf(pRate));

                    //SA calculation
                    try {
                        Query = CommonMethod.ReplaceParams(SAQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            sa = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        sa = 0.0;
                    }
                    if (sa >= SumAssured * 0.9995 && sa <= SumAssured * 1.0005) {
                        AnnualPremium = minValue;
                        gotdata = true;
                        AnnualPremium = Interval == 0 ? AnnualPremium : Math.round(AnnualPremium / Interval) * Interval;
                        break;
                    }
                    minValue = minValue * SumAssured / sa;
                }
            } else if (ModalPremium != 0) {
                double minValue = Math.max(ModalPremium, MinAnnPrem);
                while ((minValue <= MaxAnnPrem || MaxAnnPrem == 0) && (minValue >= MinAnnPrem || MinAnnPrem == 0)) {
                    Param.remove("@PR_ANNPREM");
                    Param.put("@PR_ANNPREM", String.valueOf(minValue));

                    //Premium Rate Calculation
                    pRate = 0;
                    try {
                        Query = "Select (" + CommonMethod.ReplaceParams(PremiumRate, Param, true) + ")";
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            pRate = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        pRate = 0;
                    }
                    Param.put("@PREM_RATE", String.valueOf(pRate));

                    //SA calculation
                    try {
                        Query = CommonMethod.ReplaceParams(SAQuery, Param, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            sa = cursor.getDouble(0);
                        }
                    } catch (Exception e) {
                        sa = 0.0;
                    }
                    if (sa >= ModalPremium * 0.9995 && sa <= ModalPremium * 1.0005) {
                        AnnualPremium = minValue;
                        gotdata = true;
                        AnnualPremium = Interval == 0 ? AnnualPremium : Math.round(AnnualPremium / Interval) * Interval;
                        break;
                    }
                    minValue = minValue * ModalPremium / sa;
                }
            }
            if (!gotdata) {
                CommonMethod.log(TAG, "Invalid Premium Amount");
                //throw new IllegalArgumentException ("Invalid Premium Amount");
            }


        }

        HashMap<String, Double> lst = new HashMap<>();
        lst.put("AnnualPremium", (AnnualPremium));
        lst.put("ModalPremium", (ModalPremium));
        lst.put("SumAssured", (SumAssured));
        return lst;
    }

    public HashMap<String, Object> ClearBIOP(HashMap<Integer, Map<String, DynamicParams>> allComboProducts) {
        // HashMap<Integer, Map<String, DynamicParams>> allComboProducts
        //Map<Integer, Map<String, DynamicParams>> CombinedProds, int SolutionId
        // HashMap<Integer, Map<String, DynamicParams>> CombinedProds = allComboProducts;
        HashMap<Integer, Map<String, DynamicParams>> CombinedProds = new HashMap<>();
        String ProductIdList = "";
        List<Integer> ProductIds = new ArrayList<>();
        for (Integer key : CombinedProds.keySet()) {
            ProductIdList = ProductIdList + "," + (key);
            ProductIds.add(key);
            CommonMethod.log(TAG, key.toString());
        }
        /*ProductIds.add(1003);
        ProductIds.add(1011);
        ProductIdList=",1003,1011";*/

        ProductIdList = ProductIdList.substring(1);
        LinkedHashMap<Integer, HashMap<String, String>> SumDt = new LinkedHashMap<>();
        LinkedHashMap<Integer, HashMap<String, String>> SumDt8 = new LinkedHashMap<>();
        List<LinkedHashMap<Integer, List<HashMap<String, String>>>> Params = new ArrayList<>();

        String Query = "select * from PFSectionDetailsBI where ProductId in( " + ProductIdList + ") order by SectionId, Sequence";
        Cursor PFSectionDetailcursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        Query = "select * from PFSectionMasterBI";
        Cursor PFSectionMastercursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);

        HashMap<String, Object> BIOP = new HashMap<>();
        BIOP.put("SumDt", SumDt);
        BIOP.put("SumDt8", SumDt8);
        BIOP.put("BIMaster", PFSectionMastercursor);
        BIOP.put("AllDetails", PFSectionDetailcursor);
        BIOP.put("GenerateCount", 0);

        /*HashMap<String, String> Product1Params = new HashMap<>();
        Product1Params.put("@LI_NAME","Sweta Jain");
        Product1Params.put("@LI_ENTRY_AGE","30");
        Product1Params.put("@LI_GENDER","F");
        Product1Params.put("@PR_ID","1003");
        Product1Params.put("@PR_PT","15");
        Product1Params.put("@PR_PPT","10");
        Product1Params.put("@INPUT_MODE","1");
        Product1Params.put("@PR_ANNPREM","0");
        Product1Params.put("@PR_MI","0");
        Product1Params.put("@PR_SA","626000");
        Product1Params.put("@PR_SAMF","0");
        Product1Params.put("@PR_MODALPREM","50036");
        Product1Params.put("@MODE_DISC","1");
        Product1Params.put("@MODE_FREQ","1");
        Product1Params.put("@NSAP_FLAG","1");
        Product1Params.put("@PR_CHANNEL","1");
        Product1Params.put("@TAXGROUP","1");
        Product1Params.put("@PREM_RATE","79.93");
        BIOP = GenerateBICombo(Product1Params,BIOP);

        HashMap<String, String> Product2Params = new HashMap<>();
        Product2Params.put("@LI_NAME","Sweta Jain");
        Product2Params.put("@LI_ENTRY_AGE","30");
        Product2Params.put("@LI_GENDER","F");
        Product2Params.put("@PR_ID","1011");
        Product2Params.put("@PR_PT","15");
        Product2Params.put("@PR_PPT","15");
        Product2Params.put("@INPUT_MODE","1");
        Product2Params.put("@PR_ANNPREM","50000");
        Product2Params.put("@PR_MI","0");
        Product2Params.put("@PR_SA","0");
        Product2Params.put("@PR_SAMF","0");
        Product2Params.put("@PR_MODALPREM","50000");
        Product2Params.put("@MODE_DISC","1");
        Product2Params.put("@MODE_FREQ","1");
        Product2Params.put("@NSAP_FLAG","1");
        Product2Params.put("@PR_CHANNEL","1");
        Product2Params.put("@PR_EMRID","0");
        Product2Params.put("@PR_FLATEXTRAID","0");
        Product2Params.put("@FUNDID_10001","100");
        Product2Params.put("@FUNDID_10002","0");
        Product2Params.put("@FUNDID_10003","0");
        Product2Params.put("@FUNDID_10004","0");
        Product1Params.put("@TAXGROUP","2");

        BIOP = GenerateBICombo(Product2Params,BIOP);*/


        for (Map.Entry<Integer, Map<String, DynamicParams>> entry : CombinedProds.entrySet()) {
            HashMap<String, String> singleprodparams = CommonMethod.getAllParamsFromDynamicParms(entry.getValue());
            BIOP = GenerateBICombo(singleprodparams, BIOP);
        }
        return BIOP;
    }

    public HashMap<String, Object> GenerateBICombo(HashMap<String, String> Params, HashMap<String, Object> BIOP) {
        int GenCount = (int) (BIOP.get("GenerateCount")) + 1;
        BIOP.put("GenerateCount", GenCount);
        HashMap<String, String> FundList = new HashMap<>();
        int ProductId = Integer.parseInt(Params.get("@PR_ID"));
        String Platform = "";
        String Query = "select * from productmaster where productid =" + ProductId + " ORDER BY ROWID ASC LIMIT 1";
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Platform = cursor.getInt(cursor.getColumnIndex("Platform")) == 4 ? "ULIP" : "TRAD";
        }

        int tbl1 = -1, tbl2 = -1;
        if (Platform.equals("ULIP")) {
            LinkedHashMap<Integer, HashMap<String, String>>[] dtDataYearly = new LinkedHashMap[2];
            dtDataYearly = GenerateBIULIP(Params);
            //Interest Rate List
            Query = "select * from BonusScr where productid = " + ProductId;
            Cursor IntRateList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            LinkedHashMap<Integer, String> IntRate = new LinkedHashMap<>();
            if (IntRateList.getCount() == 0) {
                CommonMethod.log(TAG, "Cannot find interest rate scenarios");
            } else {
                int counter = 0;
                for (IntRateList.moveToFirst(); !IntRateList.isAfterLast(); IntRateList.moveToNext()) {
                    String BonusValue = IntRateList.getString(IntRateList.getColumnIndex("BonusValue"));
                    IntRate.put(counter, BonusValue);
                    counter++;
                }
            }
            for (int num = 0; num < IntRate.size(); num++) {
                if (IntRate.get(num).contains("4")) {
                    tbl1 = num;
                } else if (IntRate.get(num).contains("8")) {
                    tbl2 = num;
                }
            }
            BIOP = CombineResult(ProductId, dtDataYearly[tbl1], BIOP);
            BIOP = CombineResult8(ProductId, dtDataYearly[tbl2], BIOP);
        } else {
            LinkedHashMap<Integer, HashMap<String, String>> result = new LinkedHashMap<>();
            LinkedHashMap<Integer, HashMap<String, String>>[] dtDataYearly = new LinkedHashMap[2];
            result = GenerateBI(Params);
            if (Platform.equals("3")) {
                //Interest Rate List
                Query = "select * from BonusScr where productid = " + ProductId;
                Cursor IntRateList = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                LinkedHashMap<Integer, String> IntRate = new LinkedHashMap<>();
                if (IntRateList.getCount() == 0) {
                    CommonMethod.log(TAG, "Cannot find interest rate scenarios");
                } else {
                    int counter = 0;
                    for (IntRateList.moveToFirst(); !IntRateList.isAfterLast(); IntRateList.moveToNext()) {
                        String BonusValue = IntRateList.getString(IntRateList.getColumnIndex("BonusValue"));
                        IntRate.put(counter, BonusValue);
                        counter++;
                    }
                }
                for (int num = 0; num < IntRate.size(); num++) {
                    if (IntRate.get(num).contains("4")) {
                        tbl1 = num;
                    } else if (IntRate.get(num).contains("8")) {
                        tbl2 = num;
                    }
                }
                for (int i = 0; i < result.size(); i++) {
                    for (String key : result.get(i).keySet()) {
                        if (result.get(i).get(key).contains("_BS_1")) {
                            String Newkey = key.substring(0, key.length() - 5);
                            HashMap<String, String> row = new HashMap<>();
                            row.put(Newkey, result.get(i).get(key));
                            dtDataYearly[tbl1].put(i, row);
                        } else if (result.get(i).get(key).contains("_BS_2")) {
                            String Newkey = key.substring(0, key.length() - 5);
                            HashMap<String, String> row = new HashMap<>();
                            row.put(Newkey, result.get(i).get(key));
                            dtDataYearly[tbl2].put(i, row);
                        } else {
                            dtDataYearly[tbl1].put(i, result.get(i));
                            dtDataYearly[tbl2].put(i, result.get(i));
                        }
                    }
                }
                BIOP = CombineResult(ProductId, dtDataYearly[tbl1], BIOP);
                BIOP = CombineResult8(ProductId, dtDataYearly[tbl2], BIOP);
            } else {
                LinkedHashMap<Integer, HashMap<String, String>> result2 = new LinkedHashMap<>();
                result2.putAll(result);
                BIOP = CombineResult(ProductId, result, BIOP);
                BIOP = CombineResult8(ProductId, result2, BIOP);
            }
        }
        return BIOP;
    }

    public HashMap<String, Object> CombineResult(int ProductId, LinkedHashMap<Integer, HashMap<String, String>> dt, HashMap<String, Object> BIOP) {

        LinkedHashMap<Integer, HashMap<String, String>> SumDt = (LinkedHashMap<Integer, HashMap<String, String>>) BIOP.get("SumDt");
        LinkedHashMap<Integer, HashMap<String, String>> SumDt4 = new LinkedHashMap<>();
        SumDt4.putAll(SumDt);


        if (!BIOP.containsKey("SumDt") || SumDt4.size() == 0) {
            BIOP.put("SumDt", dt);
            return BIOP;
        }

        Cursor PFSectionDetailcursor = (Cursor) BIOP.get("AllDetails");
        LinkedHashMap<String, Integer> det = new LinkedHashMap<>();
        PFSectionDetailcursor.moveToFirst();

        while (!PFSectionDetailcursor.isAfterLast()) {
            String columnname = PFSectionDetailcursor.getString(PFSectionDetailcursor.getColumnIndex("OutputKeyword"));
            Integer IsSum = PFSectionDetailcursor.getInt(PFSectionDetailcursor.getColumnIndex("IsSum"));
            Integer pid = PFSectionDetailcursor.getInt(PFSectionDetailcursor.getColumnIndex("ProductId"));
            if (pid == ProductId) {
                det.put(columnname, IsSum);
            }
            PFSectionDetailcursor.moveToNext();
        }

        for (String dc : dt.get(0).keySet()) {
            CommonMethod.log(TAG,dc);
            if (det.containsKey(dc)) {
                if (det.get(dc) == 1) {
                    for (int i = 0; i < dt.size(); i++) {
                        double val = 0;
                        try {
                            val = Double.valueOf(SumDt4.get(i).get(dc));
                        } catch (Exception e) {
                        }
                        SumDt4.get(i).put(dc, String.valueOf(val + Double.valueOf(dt.get(i).get(dc))));
                    }
                } else {
                    for (int i = 0; i < dt.size(); i++) {
                        double val = 0.0;
                        try {
                            val = Double.valueOf(SumDt4.get(i).get(dc));
                        } catch (Exception e) {
                            val = Double.valueOf(dt.get(i).get(dc));
                        }
                        try {
                            SumDt4.get(i).put(dc, String.valueOf(val));
                        } catch (Exception ex) {
                            int x = 1;
                        }
                    }
                }
            }
        }
        BIOP.put("SumDt", SumDt4);
        return BIOP;
    }

    public HashMap<String, Object> CombineResult8(int ProductId, LinkedHashMap<Integer, HashMap<String, String>> dt8, HashMap<String, Object> BIOP) {

        LinkedHashMap<Integer, HashMap<String, String>> SumDt = (LinkedHashMap<Integer, HashMap<String, String>>) BIOP.get("SumDt8");
        LinkedHashMap<Integer, HashMap<String, String>> SumDt8 = new LinkedHashMap<>();
        SumDt8.putAll(SumDt);

        if (!BIOP.containsKey("SumDt8") || SumDt8.size() == 0) {
            BIOP.put("SumDt8", dt8);
            return BIOP;
        }

        Cursor PFSectionDetailcursor = (Cursor) BIOP.get("AllDetails");
        LinkedHashMap<String, Integer> det8 = new LinkedHashMap<>();
        PFSectionDetailcursor.moveToFirst();

        while (!PFSectionDetailcursor.isAfterLast()) {
            String columnname = PFSectionDetailcursor.getString(PFSectionDetailcursor.getColumnIndex("OutputKeyword"));
            Integer IsSum = PFSectionDetailcursor.getInt(PFSectionDetailcursor.getColumnIndex("IsSum"));
            Integer pid = PFSectionDetailcursor.getInt(PFSectionDetailcursor.getColumnIndex("ProductId"));
            if (pid == ProductId) {
                det8.put(columnname, IsSum);
            }
            PFSectionDetailcursor.moveToNext();
        }

        for (String dc : dt8.get(0).keySet()) {
            CommonMethod.log(TAG,dc);
            if (det8.containsKey(dc)) {
                if (det8.get(dc) == 1) {
                    for (int i = 0; i < dt8.size(); i++) {
                        double val = 0;
                        try {
                            val = Double.valueOf(SumDt8.get(i).get(dc));
                        } catch (Exception e) {
                        }
                        SumDt8.get(i).put(dc, String.valueOf(val + Double.valueOf(dt8.get(i).get(dc))));
                    }
                } else {
                    for (int i = 0; i < dt8.size(); i++) {
                        double val = 0.0;
                        try {
                            val = Double.valueOf(SumDt8.get(i).get(dc));
                        } catch (Exception e) {
                            val = Double.valueOf(dt8.get(i).get(dc));
                        }
                        SumDt8.get(i).put(dc, String.valueOf(val));
                    }
                }
            }
        }
        BIOP.put("SumDt8", SumDt8);
        return BIOP;
    }

    public LinkedHashMap<Integer, HashMap<String, String>> NeedAdvisory(int inputCatTypeId, int inputAge, int inputPt, int Mode, double TargetBenefit) {
        String Query = "select productid,optionlevelid,optionid from prodneedmap where needid=" + inputCatTypeId;
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
        LinkedHashMap<Integer, HashMap<String, String>> AllParams = new LinkedHashMap<>();
        if (cursor == null) {
            HashMap<String, String> prodoutput = new HashMap<>();
            prodoutput.put("Remarks", "This product doesnot satisfy the given criteria");
            AllParams.put(1, prodoutput);
            return AllParams;
        }

        ArrayList<Productneed> prodneedlist = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int productid = cursor.getInt(0);
            int optionlevelid = cursor.getInt(1);
            int optionid = cursor.getInt(2);
            Productneed p1 = new Productneed();
            p1.setProductId(productid);
            p1.setOptionLevelId(optionlevelid);
            p1.setOptionId(optionid);
            prodneedlist.add(p1);
        }
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                int singleprod = prodneedlist.get(0).getProductId();
                int OptionLevelId = prodneedlist.get(0).getOptionLevelId();
                int OptionId = prodneedlist.get(0).getOptionId();
                HashMap<String, String> prodoutput = new HashMap<>();
                prodoutput = productneed(singleprod, OptionId, OptionLevelId, inputCatTypeId, inputAge, inputPt, Mode, TargetBenefit);
                AllParams.put(singleprod, prodoutput);

            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        CommonMethod.log(TAG , "Bi gene rated");
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonMethod.log(TAG, "Error " + e.toString());
                    }
                });

        /*ExecutorService exec = Executors.newFixedThreadPool(cursor.getCount());
        try {
            for (final Productneed prodneed : prodneedlist) {
                exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        int singleprod = prodneed.getProductId();
                        int OptionLevelId = prodneed.getOptionLevelId();
                        int OptionId = prodneed.getOptionId();
                        HashMap<String, String> prodoutput = new HashMap<>();
                        prodoutput = productneed(singleprod, OptionId, OptionLevelId, inputCatTypeId, inputAge, inputPt, Mode, TargetBenefit);
                        AllParams.put(singleprod, prodoutput);
                    }
                });
            }
        } finally {
            exec.shutdown();
        }

        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            CommonMethod.log(TAG , "Exception exec " + e.toString());
        }*/

        return AllParams;
    }

    public HashMap<String, String> productneed(int ProductId, int OptionId, int OptionLevelId, int inputCatTypeId, int inputAge, int inputPt, int Mode, double TargetBenefit) {
        HashMap<String, String> Params = new HashMap<>();
        Params.put("NeedAdvisory", "1");
        HashMap<Integer, Double> Fund = new HashMap();
        ArrayList<PTPPTGoal> ptpptgoal = new ArrayList<>();
        HashMap<String, Integer> PTValues = new HashMap<>();
        HashMap<String, Integer> PPTValues = new HashMap<>();
        ArrayList<Integer> optList = new ArrayList<>();
        String PTFormulalist = "";
        String PPTFormulalist = "";
        String PTValuelist = "";
        String PPTValuelist = "";
        String Errormessage = "";
        try {
            int PTdevup = 5;
            int PTdevdown = 5;
            int PPTdevup = 5;
            int PPTdevdown = 5;
            int inputPPT = inputPt;
            //GoalSeek for TargetBenefit
            boolean gotdata = false;
            double ModalPremium = 0, AnnualPremium = 0, SumAssured = 0, SBG = 0, SBNG = 0, SBTotal = 0;
            Params.put("@PR_ID", String.valueOf(ProductId));
            Params.put("@LI_ENTRY_AGE", String.valueOf(inputAge));
            Params.put("@INPUT_MODE", String.valueOf(Mode));
            String Query = "select  Multiplier,Frequency from productmode inner join ModeMaster on productmode.ModeId= ModeMaster.ModeId where productid=" + ProductId + " and productmode.ModeId=" + Mode;
            Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor == null) {
                Params.put("Remarks", "This product doesnot satisfy the given criteria");
                return Params;
            }
            cursor.moveToFirst();
            double ModeDisc = cursor.getDouble(0);
            int ModeFreq = cursor.getInt(1);
            Params.put("@MODE_FREQ", String.valueOf(ModeFreq));
            Params.put("@MODE_DISC", String.valueOf(ModeDisc));
            String Options = "";
            if (OptionId > 0) {
                Params.put("@PR_OPTION_" + OptionLevelId, String.valueOf(OptionId));
                optList.add(OptionId);
                Options = Options + "," + OptionId;
            }
            if (!Options.equals("")) {
                Options = Options.substring(1);
            }

            Query = "select PName,PValue from ProdNeedDefaultInputs where ProductId = " + ProductId;
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            if (cursor != null && cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String pname = cursor.getString(0);
                    String pvalue = cursor.getString(1);
                    if (!Params.containsKey(pname)) {
                        Params.put(pname.toUpperCase(), pvalue);
                        if (pname.toUpperCase().contains("@PR_OPTION")) {
                            optList.add(Integer.valueOf(pvalue));
                            Options = Options + "," + Integer.valueOf(pvalue);
                        }
                    }
                }
            }
            if (!Options.equals("")) {
                Options = Options.substring(1);
            }
            Query = "Select * from InputOutputMaster where ProductId = " + ProductId + " ORDER BY ROWID ASC LIMIT 1";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursor.moveToFirst();
            int PTout = cursor.getInt(cursor.getColumnIndex("PT"));
            int PPTout = cursor.getInt(cursor.getColumnIndex("PPT"));
            int SAout = cursor.getInt(cursor.getColumnIndex("SA"));
            int APout = cursor.getInt(cursor.getColumnIndex("AnnualPremium"));
            int MPout = cursor.getInt(cursor.getColumnIndex("ModalPremium"));
            int MIout = cursor.getInt(cursor.getColumnIndex("MI"));
            int SAMFout = cursor.getInt(cursor.getColumnIndex("SAMF"));

            String FormulaTable = NvestLibraryConfig.FORMULAS_SQLITE_TABLE;
            int PTinForm = 0, PPTinForm = 0, SAinForm = 0, APinForm = 0, MPinForm = 0, MIinForm = 0, SAMFinForm = 0, PT = 0, PPT = 0, PtPptId = 0;

            Query = "select * from " + FormulaTable + " where productid = " + ProductId;
            Cursor cursorFormulas = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            String TaxMPQuery = "0", PTQuery = "0", PPTQuery = "0", SAQuery = "0", APQuery = "0", MPQuery = "0", MIQuery = "0", SAMFQuery = "0", LoadAPQuery = "0", PremiumRate = "0";
            for (cursorFormulas.moveToFirst(); !cursorFormulas.isAfterLast(); cursorFormulas.moveToNext()) {
                String Formulakeyword = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaKeyword"));
                String FormulaExtended = cursorFormulas.getString(cursorFormulas.getColumnIndex("FormulaExtended"));
                if (Formulakeyword.toUpperCase().equals("[PR_PT]")) {
                    PTinForm = 1;
                    PTQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[PR_PPT]")) {
                    PPTinForm = 1;
                    PPTQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[SA]")) {
                    SAinForm = 1;
                    SAQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[ANN_PREM]")) {
                    APinForm = 1;
                    APQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[MODAL_PREM]")) {
                    MPinForm = 1;
                    MPQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[MI]")) {
                    MIinForm = 1;
                    MIQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[SAMF]")) {
                    SAMFinForm = 1;
                    SAMFQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[PREMIUMRATE]")) {
                    PremiumRate = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[TAX_MP]")) {
                    TaxMPQuery = "Select (" + FormulaExtended + ")";
                } else if (Formulakeyword.toUpperCase().equals("[LOAD_ANN_PREM]")) {
                    LoadAPQuery = "Select (" + FormulaExtended + ")";
                } else {
                }
            }

            if (PTout == 2) {
                try {
                    Query = CommonMethod.ReplaceParams(PTQuery, Params, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        PT = cursor.getInt(0);
                    }
                } catch (Exception ex) {
                }
                if (PT >= inputPt - PTdevdown && PT <= inputPt + PTdevup) {
                    PTValues.put("", PT);
                } else {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    return Params;
                }
            } else if (PTout == 0) {
                PT = 0;
                PTValues.put("", PT);
            } else if (PTout == 1) {
                Query = "SELECT distinct ptformula FROM ptpptmaster WHERE productid = " + ProductId + " AND (optionid = " + OptionId + " or " + OptionId + " <= 0 or optionid = null) ";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String PTFormula = cursor.getString(0);
                    try {
                        PT = Integer.parseInt(PTFormula);
                    } catch (Exception e) {
                        Query = CommonMethod.ReplaceParams(PTFormula, Params, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            PT = cursor.getInt(0);
                        }
                    }
                    if (PT >= inputPt - PTdevdown && PT <= inputPt + PTdevup) {
                        PTValues.put(PTFormula, PT);
                        PTFormulalist = PTFormulalist + "," + PTFormula;
                    }
                }
            }
            if (!PTFormulalist.equals("")) {
                PTFormulalist = PTFormulalist.substring(1);
            }

            if (PPTout == 2) {
                try {
                    Query = CommonMethod.ReplaceParams(PPTQuery, Params, true);
                    cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        PPT = cursor.getInt(0);
                    }
                } catch (Exception ex) {
                }
                if (PPT >= inputPPT - PPTdevdown && PPT <= inputPPT + PPTdevup) {
                    PPTValues.put("", PPT);
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    return Params;
                }
            } else if (PPTout == 0) {
                PPT = 0;
                PPTValues.put("", PPT);
            } else if (PPTout == 1) {
                Query = "SELECT distinct pptformula FROM ptpptmaster WHERE productid = " + ProductId + " AND (optionid = " + OptionId + " or " + OptionId + " <= 0 or optionid = null) AND (PTFormula= null or PTFormula='' or PTFormula in (" + PTFormulalist + ")) ";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String PPTFormula = cursor.getString(0);

                    try {
                        PPT = Integer.parseInt(PPTFormula);
                    } catch (Exception e) {
                        Query = CommonMethod.ReplaceParams(PPTFormula, Params, true);
                        cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            PPT = cursor.getInt(0);
                        }
                    }
                    if (PPT >= inputPPT - PPTdevdown && PPT <= inputPPT + PPTdevup) {
                        PPTValues.put(PPTFormula, PPT);
                        PPTFormulalist = PPTFormulalist + "," + PPTFormula;
                    }
                }
            }
            if (!PPTFormulalist.equals("")) {
                PPTFormulalist = PPTFormulalist.substring(1);
            }

            Query = "select distinct * from PtPptMaster where productid = " + ProductId + " and (PTFormula=null or PTFormula='' or PTFormula in (" + PTFormulalist + ")) and (PPTFormula=null or PPTFormula='' or PPTFormula in (" + PPTFormulalist + "))";
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String PPTFormula = cursor.getString(cursor.getColumnIndex("PPTFormula"));
                String PTFormula = cursor.getString(cursor.getColumnIndex("PTFormula"));
                String PPTDisplay = cursor.getString(cursor.getColumnIndex("PPTDisplay"));
                String PTDisplay = cursor.getString(cursor.getColumnIndex("PTDisplay"));
                PTPPTGoal ptpptobj = new PTPPTGoal();
                ptpptobj.ProductId = ProductId;
                ptpptobj.PPTDisplay = PPTDisplay;
                ptpptobj.PPTValue = PPTValues.get(PPTFormula);
                ptpptobj.PTDisplay = PTDisplay;
                ptpptobj.PTValue = PTValues.get(PTFormula);
                ptpptgoal.add(ptpptobj);
            }

            ArrayList<PTPPTGoal> ptpptvalidage = new ArrayList<>();
            for (int i = 0; i < ptpptgoal.size(); i++) {
                int ptval = ptpptgoal.get(i).PTValue;
                int pptval = ptpptgoal.get(i).PPTValue;
                //Age Master Validation

                String ageqry = "SELECT * FROM agemaster where productid=" + ProductId + " and ((";
                ageqry = ageqry + ptval + ">= FromPt and " + ptval + "<= ToPt) or FromPt isnull ) and ((";
                ageqry = ageqry + pptval + ">=FromPpt and " + pptval + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + ")) and ((";
                ageqry = ageqry + inputAge + ">=MinEntryAge ) or MinEntryAge isnull ) and ((";
                ageqry = ageqry + inputAge + "<=MaxEntryAge ) or MaxEntryAge isnull ) and ((";
                ageqry = ageqry + (inputAge + ptval) + ">=MinMaturityAge ) or MinMaturityAge isnull ) and ((";
                ageqry = ageqry + (inputAge + ptval) + "<=MaxMaturityAge ) or MaxMaturityAge isnull )";

                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(ageqry);
                if (cursor != null && cursor.getCount() > 0) {
                    ptpptvalidage.add(ptpptgoal.get(i));
                }
            }
            if (ptpptvalidage.size() == 0) {
                Params.put("Remarks", "This product doesnot satisfy the given criteria");
                return Params;
            }

            //Closest PT
            int distance = Math.abs(ptpptvalidage.get(0).PTValue - inputPt);
            int idx = 0;
            for (int i = 1; i < ptpptvalidage.size(); i++) {
                int cdistance = Math.abs(ptpptvalidage.get(i).PTValue - inputPt);
                if (cdistance < distance) {
                    idx = i;
                    distance = cdistance;
                }
            }
            PT = ptpptvalidage.get(idx).PTValue;
            String PTDisplay = ptpptvalidage.get(idx).PTDisplay;

            //Closest PPT
            idx = 0;
            int cdistance = 100;
            for (int i = 0; i < ptpptvalidage.size(); i++) {
                int ptval = ptpptvalidage.get(i).PTValue;
                if (ptval == PT) {
                    int pptval = ptpptvalidage.get(i).PPTValue;
                    distance = Math.abs(pptval - inputPPT);
                    if (distance < cdistance) {
                        idx = i;
                        distance = cdistance;
                    }
                }
            }
            PPT = ptpptvalidage.get(idx).PPTValue;
            String PPTDisplay = ptpptvalidage.get(idx).PPTDisplay;
            Params.put("@PR_PT", String.valueOf(PT));
            Params.put("@PR_PPT", String.valueOf(PPT));
            Params.put("@DISPLAY_PR_PT", PTDisplay);
            Params.put("@DISPLAY_PR_PPT", PPTDisplay);

            Query = "select Platform from  ProductMaster where productid = " + ProductId;
            cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(Query);
            cursor.moveToFirst();
            String Platform = cursor.getString(0);

            if (MPout == 1) {
                // Find min and max Modal Premium
                String prqry = " SELECT Interval,MinPremium,MaxPremium FROM [PremiumMaster] where productid=" + ProductId + " and ((";
                prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
                prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
                prqry = prqry + inputAge + ">= FromAge and " + inputAge + "<=ToAge) or FromAge isnull ) and ((";
                prqry = prqry + SumAssured + ">=FromSA and " + SumAssured + "<= ToSA ) or FromSA isnull) and ((";
                prqry = prqry + Mode + "=ModeId) or ModeId isnull)  ORDER BY ROWID ASC LIMIT 1";

                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
                if (cursor.getCount() == 0 || cursor == null) {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    return Params;
                }
                cursor.moveToFirst();
                double Interval = cursor.getDouble(0);
                double MinPremium = cursor.getDouble(1);
                double MaxPremium = cursor.getDouble(2);

                double minValue = Math.max(Math.round(TargetBenefit / (ModeFreq * PT)), MinPremium);
                int cntloop = 0;
                int maxloopcnt = 10;
                while (cntloop < maxloopcnt && (minValue <= MaxPremium || MaxPremium == 0) && (minValue >= MinPremium || MinPremium == 0)) {
                    Params.remove("@PR_MODALPREM");
                    Params.put("@PR_MODALPREM", String.valueOf(minValue));
                    if (!Platform.equals("4")) {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBI(Params);
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            if (Platform.equals(3)) {
                                SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG_BS_1"));
                            }
                        }
                    } else {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBIULIP(Params)[0];
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG"));
                        }
                    }
                    SBTotal = SBG + SBNG;
                    if (SBTotal >= TargetBenefit * 0.9995 && SBTotal <= TargetBenefit * 1.0005) {
                        ModalPremium = minValue;
                        gotdata = true;
                        ModalPremium = Interval == 0 ? ModalPremium : Math.round(ModalPremium / Interval) * Interval;
                        Params.remove("@PR_MODALPREM");
                        Params.put("@PR_MODALPREM", String.valueOf(ModalPremium));
                        break;
                    }
                    minValue = Math.round(minValue * TargetBenefit / SBTotal);
                    cntloop++;
                }
                if (!gotdata)
                    Errormessage = "Premium validation failed for the given target amount";
            } else if (SAout == 1) {
                //Find min and max SA
                String saqry = "SELECT Interval,MinSA,MaxSA FROM [SAMaster] where productid=" + ProductId + " and ((";
                saqry = saqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
                saqry = saqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in (" + Options + ")) and ((";
                saqry = saqry + inputAge + ">= FromAge and " + inputAge + "<=ToAge) or FromAge isnull ) and ((";
                saqry = saqry + Mode + "=ModeId or ModeId isnull))";
                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(saqry);
                if (cursor.getCount() == 0 || cursor == null) {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    return Params;
                }
                cursor.moveToFirst();
                double Interval = cursor.getDouble(0);
                double MinSA = cursor.getDouble(1);
                double MaxSA = cursor.getDouble(2);
                double minValue = Math.max(TargetBenefit, Double.valueOf(MinSA));
                int cntloop = 0;
                int maxloopcnt = 10;
                while (cntloop < maxloopcnt && (minValue >= MinSA || MinSA == 0)) {
                    Params.remove("@PR_SA");
                    Params.put("@PR_SA", String.valueOf(minValue));
                    if (!Platform.equals("4")) {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBI(Params);
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            if (Platform.equals(3)) {
                                SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG_BS_1"));
                            }
                        }
                    } else {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBIULIP(Params)[0];
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG"));
                        }
                    }
                    SBTotal = SBG + SBNG;
                    if (SBTotal >= TargetBenefit * 0.9995 && SBTotal <= TargetBenefit * 1.0005) {
                        SumAssured = minValue;
                        gotdata = true;
                        SumAssured = Interval == 0 ? SumAssured : Math.round(SumAssured / Interval) * Interval;
                        Params.remove("@PR_SA");
                        Params.put("@PR_SA", String.valueOf(SumAssured));
                        break;
                    }
                    minValue = Math.round(minValue * TargetBenefit / SBTotal);
                }
                if (!gotdata)
                    Errormessage = "Sum Assured validation failed for the given target amount";
            } else if (APout == 1) {
                // Find min and max Annual Premium
                int annualmodeid = 1;
                // Find min and max Modal Premium
                String prqry = " SELECT Interval,MinPremium,MaxPremium FROM [PremiumMaster] where productid=" + ProductId + " and ((";
                prqry = prqry + PT + ">= FromPt and " + PT + "<= ToPt) or FromPt isnull ) and ((";
                prqry = prqry + PPT + ">= FromPpt and " + PPT + "<= ToPpt) or FromPpt isnull ) and ( optionId isnull or optionId in(" + Options + " )) and ((";
                prqry = prqry + inputAge + ">= FromAge and " + inputAge + "<=ToAge) or FromAge isnull ) and ((";
                prqry = prqry + annualmodeid + "=ModeId) or ModeId isnull)  ORDER BY ROWID ASC LIMIT 1";

                cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery(prqry);
                if (cursor.getCount() == 0 || cursor == null) {
                    Params.put("Remarks", "This product doesnot satisfy the given criteria");
                    return Params;
                }
                cursor.moveToFirst();
                double Interval = cursor.getDouble(0);
                double MinPremium = cursor.getDouble(1);
                double MaxPremium = cursor.getDouble(2);

                double minValue = Math.max(Math.round(TargetBenefit / (ModeFreq * PT)), MinPremium);

                int cntloop = 0;
                int maxloopcnt = 10;
                while (cntloop < maxloopcnt && (minValue <= MaxPremium || MaxPremium == 0) && (minValue >= MinPremium || MinPremium == 0)) {

                    Params.remove("@PR_ANNPREM");
                    Params.put("@PR_ANNPREM", String.valueOf(minValue));
                    if (!Platform.equals("4")) {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBI(Params);
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            if (Platform.equals(3)) {
                                SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG_BS_1"));
                            }
                        }
                    } else {
                        LinkedHashMap<Integer, HashMap<String, String>> DtBiOutput = GenerateBIULIP(Params)[0];
                        SBG = 0;
                        SBNG = 0;
                        for (int i = 0; i < DtBiOutput.size(); i++) {
                            SBG = SBG + Double.valueOf(DtBiOutput.get(i).get("SB_G"));
                            SBNG = SBNG + Double.valueOf(DtBiOutput.get(i).get("SB_NG"));
                        }
                    }
                    SBTotal = SBG + SBNG;
                    if (SBTotal >= TargetBenefit * 0.9995 && SBTotal <= TargetBenefit * 1.0005) {
                        AnnualPremium = minValue;
                        gotdata = true;
                        AnnualPremium = Interval == 0 ? AnnualPremium : Math.round(AnnualPremium / Interval) * Interval;
                        Params.remove("@PR_ANNPREM");
                        Params.put("@PR_ANNPREM", String.valueOf(AnnualPremium));
                        break;
                    }
                    minValue = Math.round(minValue * TargetBenefit / SBTotal);
                }
                if (!gotdata)
                    Errormessage = "Annual Premium Validation failed for the given target amount";
            }
            //Validation
            if (gotdata) {
                ValidationIP valid = new ValidationIP();
                valid = ValidateInput(Params);
                if (valid.FailedCount > 0) {
                    for (int i = 0; i <= valid.getErrorMessage().size(); i++) {
                        Errormessage = valid.getErrorMessage().get(i) + ";";
                    }
                }
            }
            Params.put("Remarks", Errormessage);
            Params.put("TotalBenefit", String.valueOf(SBTotal));
        } catch (Exception ex) {
            Errormessage = "This product doesnot satisfy the given criteria";
            Params.put("Remarks", Errormessage);
        }
        return Params;
    }
}

// Method - 1    ULIP Calculation By Replacement
/*
            for (int i = 0; i < dtData[0].size(); i++)
            {
                dynamicFormulas.moveToFirst();
                while ( !dynamicFormulas.isAfterLast()) {
                    String kw = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaKeyword")).toUpperCase().replace("]","").replace("[","");
                    String Formula = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaExtended")).toUpperCase();
                    String CombinedQuery = "";
                    for (int no = 0; no < dtData.length; no++)
                    {
                        String Qry=CommonMethod.ReplaceParamsUlip(Formula,dtData[no],i);
                        CombinedQuery += "(" + Qry + ")+','+";
                    }
                    dynamicFormulas.moveToNext();
                    if (CombinedQuery.equals(""))
                        continue;

                    CombinedQuery = CombinedQuery.substring(0, CombinedQuery.length() - 5);
                    String cResult = CommonMethod.evaluateExpression(CombinedQuery);
                    List<String> cResultout = Arrays.asList(cResult.split(","));
                    for (int no = 0; no < dtData.length; no++)
                    {   dtData[no].get(i).put(kw,cResultout.get(no));
                    }
                }
            }*/


/*
    // Method - 2    ULIP Calculation using runtime compilation generation of string
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("js");
            engine.put("paramKeys", paramKeys);
                    engine.put("paramValues", paramValues);
                    engine.put("dtData", dtData);
                    String code = "importPackage(java.util);\n" +
                    "for (let no = 0; no <dtData.length; no++)\n" +
                    "{ for (let i = 0; i <dtData[0].size(); i++)\n" +
                    "{\n"+
                    "for ( let z = 0; z < paramKeys.size(); z++) {\n"+
                    "var key = paramKeys.get(z);\n"+
                    //       "println( key);\n"+
                    "var keyval = dtData[no].get(i).get(key);\n"+
                    //        "println( keyval);\n"+
                    "if(keyval)\n"+
                    "{\n"+
                    "  paramValues.put(key,parseFloat(keyval));\n"+
                    "}\n"+
                    "else\n"+
                    "{\n"+
                    "    paramValues.put(key,0);\n"+
                    "}\n"+
                    //        "println( paramValues.get(key));\n"+
                    "}\n";
                    dynamicFormulas.moveToFirst();
                    while ( !dynamicFormulas.isAfterLast()) {
                    //for(int k=0;k<=1;k++){
                    String kw = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaKeyword")).toUpperCase().replace("]", "").replace("[", "");
                    String Formula = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaExtended")).toUpperCase();
                    String Qry = CommonMethod.ReplaceParamsUlip(Formula);
                    // code=code+"println(paramValues.get(\"ALL_CHRG\"));\n";
                    // code=code+"println(paramValues.get(\"ADMIN_CHRG\"));\n";
                    //   Qry= "parseFloat(paramValues.get(\"ALL_CHRG\"))+parseFloat(paramValues.get(\"ADMIN_CHRG\"))";
                    // code=code+"println(('"+ kw + "'));\n";
                    //  code=code+"println(('" + Qry + "'));\n";
                    //     code = code+"var value1 = (" + Qry + ");\n";
                    //     code=code+"println(value1);";
                    code = code+"var value = eval(" + Qry + ");\n";
                    //    code=code+"println((" + Qry + ").toString());";
                    code=code+"paramValues.put(\"" + kw + "\",value);";
                    code = code + "dtData[no].get(i).put(\"" + kw + "\",value.toString()); \n";
                    dynamicFormulas.moveToNext();
                    }
                    code = code+" }\n" +
                    "    }\n" ;
                    engine.eval(code);*/


// Method - 4    ULIP Calculation using runtime compilation - beanshell

/*    Interpreter bsh = new Interpreter();  // Construct an interpreter
            try {
                    bsh.set("paramKeys", paramKeys);                    // Set variables
                    bsh.set("paramValues", paramValues);
                    bsh.set("dtData", dtData);
                    bsh.set("numdatasets", numdatasets);
                    bsh.set("rows", rows);
                    // Eval a statement and get the result
                    String code="";
                    code = code+"import java.util.*;\n";
                    code = code+ "for (int no = 0; no < numdatasets; no++)\n" +
                    "{ for (int i = 0; i <rows; i++)\n" +
                    "{\n"+
                    "for ( int z = 0; z < paramKeys.size(); z++) {\n"+
                    "String key = paramKeys.get(z);\n"+
                    "String keyval = dtData.get(no).get(i).get(key);\n"+
                    // "println( key);\n"+
                    // "if(keyval)\n"+
                    //  "{\n"+
                    "  paramValues.put(key,Double.valueOf(keyval));\n"+
                    //    "}\n"+
                    //    "else\n"+
                    //    "{\n"+
                    //    "    paramValues.put(key,'0');\n"+
                    //    "}\n"+
                    //  "println( paramValues.get(key));\n"+
                    "}\n"+
                    "String value =\"\";\n";
                    dynamicFormulas.moveToFirst();
                    while ( !dynamicFormulas.isAfterLast()) {
                    //   for(int k=0;k<=1;k++){
                    String kw = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaKeyword")).toUpperCase().replace("]", "").replace("[", "");
                    String Formula = dynamicFormulas.getString(dynamicFormulas.getColumnIndex("FormulaExtended")).toUpperCase();
                    if(Formula.toUpperCase().contains("MAX("))
                    {
                    Formula="100000";
                    }
                    if(Formula.toUpperCase().contains("MIN("))
                    {
                    Formula="0";
                    }
                    String Qry = CommonMethod.ReplaceParamsUlip(Formula);
                    //  code=code+"println(('"+ kw + "'));\n";
                    //  code=code+"println(('" + Qry + "'));\n";
                    code = code+"try{\n";
                    code = code+"value =String.valueOf((" + Qry + "));}\n";
                    code = code+"catch(Exception e){\n";
                    code = code+"value =\"0\";\n}\n";
                    //  code=code+"println(eval(" + Qry + ").toString());";
                    code=code+"paramValues.put(\"" + kw + "\",Double.valueOf(value)); \n";
                    code = code + "dtData.get(no).get(i).put(\"" + kw + "\",value); \n";
                    dynamicFormulas.moveToNext();
                    }
                    code = code+" }\n" +
                    "    }\n" ;
                    bsh.eval(code);
                    }
                    catch(Exception e){
                    CommonMethod.log(TAG, "Exception in raw query " + e.toString());
                    CommonMethod.log(TAG, "Line Number " + (e.getStackTrace()[0].getLineNumber()));
                    }*/
