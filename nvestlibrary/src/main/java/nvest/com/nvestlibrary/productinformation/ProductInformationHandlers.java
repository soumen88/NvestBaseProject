package nvest.com.nvestlibrary.productinformation;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import nvest.com.nvestlibrary.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nvest.com.nvestlibrary.basicInformation.BasicInformationDataViewModel;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.DynamicEditTextModel;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;

public class ProductInformationHandlers {
    private static String TAG = ProductInformationHandlers.class.getSimpleName();
    private Products products;
    private ProductInformationDataViewModel productInformationDataViewModel;
    private BasicInformationDataViewModel basicInformationDataViewModel;
    public Map<String, ArrayList<StringKeyValuePair>> keyvaluePairHashMap;
    public LinkedHashMap<String, Keyword> keywordLinkedHashMap;
    private Context context;

    public ProductInformationHandlers(Products products, Map<String, ArrayList<StringKeyValuePair>> keyvaluePairHashMap, LinkedHashMap<String, Keyword> keywordLinkedHashMap, Context context) {
        this.products = products;
        this.keyvaluePairHashMap = keyvaluePairHashMap;
        this.keywordLinkedHashMap = keywordLinkedHashMap;
        this.context = context;

    }

    public void handlingSumAssuredACasesInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){
        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                ProductInformationFragment.getSingletonInstance().mSelectSpinnerSA.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);

                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }
                CommonMethod.log(TAG , "Handling SA list");

                //CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), spinner, stringList);
                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mSelectSpinnerSA, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                ProductInformationFragment.getSingletonInstance().mSAInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSA.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelSA.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().mTextSA.addTextChangedListener(watchSA);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mSAInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSA.setInputType(InputType.TYPE_CLASS_NUMBER);
                ProductInformationFragment.getSingletonInstance().mTextLabelSA.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().mTextSA.addTextChangedListener(watchSA);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
                break;
            case Output:
                //Not present
                break;
            case CheckBox:
                //Not present
                break;
            case String:
                ProductInformationFragment.getSingletonInstance().mSAInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSA.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelSA.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mSAInputLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void handlingModalPremiumInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){
        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                ProductInformationFragment.getSingletonInstance().mSelectSpinnerMP.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);

                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                //stringArrayList.add(fieldCaption);
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }

                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mSelectSpinnerMP, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.MODAL_PREM_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mMPInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextMP.setInputType(InputType.TYPE_CLASS_NUMBER);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
                //ProductInformationFragment.getSingletonInstance().mTextMP.addTextChangedListener(watchMP);
                ProductInformationFragment.getSingletonInstance().mTextLabelMP.setText(fieldCaption);
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mMPInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                ProductInformationFragment.getSingletonInstance().mMPInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextMP.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelMP.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mMPInputLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void handlingAnnualPremiumInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){
        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                ProductInformationFragment.getSingletonInstance().mSelectSpinnerAP.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mAPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);

                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                //stringArrayList.add(fieldCaption);
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }
                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mSelectSpinnerAP, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                ProductInformationFragment.getSingletonInstance().mAPInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mAPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextAP.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelAP.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mAPInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mAPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextAP.setInputType(InputType.TYPE_CLASS_NUMBER);
                ProductInformationFragment.getSingletonInstance().mTextLabelAP.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mAPInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                ProductInformationFragment.getSingletonInstance().mAPInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mAPHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextAP.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelAP.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
                break;

            case Disabled:
                ProductInformationFragment.getSingletonInstance().mAPInputLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void handlingMonthlyIncomeInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){
        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                ProductInformationFragment.getSingletonInstance().mSelectSpinnerMI.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMIHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);

                CommonMethod.log(TAG , "Handling SA list");
                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }
                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mSelectSpinnerMI, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                ProductInformationFragment.getSingletonInstance().mMIInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMIHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextMI.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelMI.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mMIInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMIHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextMI.setInputType(InputType.TYPE_CLASS_NUMBER);
                ProductInformationFragment.getSingletonInstance().mTextLabelMI.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().mTextMI.addTextChangedListener(watchMI);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mMIInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                ProductInformationFragment.getSingletonInstance().mMIInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mMIHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextMI.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelMI.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mMIInputLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void handlingSumAssuredMultiplyingFactorInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling SA list");
                ProductInformationFragment.getSingletonInstance().mSelectSpinnerSAMF.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAMFHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);

                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.PR_SAMF_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                //stringArrayList.add(fieldCaption);
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }

                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mSelectSpinnerSAMF, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_SAMF_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                ProductInformationFragment.getSingletonInstance().mSAMFInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAMFHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSAMF.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelSAMF.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_SAMF_ANNOTATION);
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mSAMFInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAMFHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSAMF.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                ProductInformationFragment.getSingletonInstance().mTextLabelSAMF.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_SAMF_ANNOTATION);
                /*ProductInformationFragment.getSingletonInstance().mTextSAMF.setHint(getSamfHint());*/
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mSAMFInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                ProductInformationFragment.getSingletonInstance().mSAMFInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mSAMFHeaderLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mBackgroundLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mTextSAMF.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelSAMF.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_SAMF_ANNOTATION);
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mSAMFInputLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void handlingPremiumTermInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){
        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling SA list");


                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                ProductInformationFragment.getSingletonInstance().mTextLabelPT.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().mTextLabelPT.setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
                CommonMethod.log(TAG , "Handling Number");

                break;
            case Output:

                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                break;

            default:
                break;
        }

    }

    public void handlingPremiumPaymentTermInStaticView(String fieldType, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling SA list");


                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");

                break;
            case Output:

                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                break;

            default:
                break;
        }
    }

    public void handlingInputModeTermInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:

                CommonMethod.log(TAG , "Getting input mode masters");
                ProductInformationFragment.getSingletonInstance().mModeSpinner.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().modeLabel.setVisibility(View.VISIBLE);

                Map<String, ArrayList<StringKeyValuePair>> modeMastersMap=ProductInformationFragment.getSingletonInstance().getInputModeMasters();
                if(modeMastersMap != null){
                    ArrayList<StringKeyValuePair> itemList =  modeMastersMap.get(NvestLibraryConfig.INPUT_MODE_ANNOTATION);
                    CommonMethod.log(TAG , "Item list size " + itemList.size());
                    //stringArrayList.add(fieldCaption);

                    List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                    StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey("-1");
                    stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                    ListSpinner.add(stringKeyValuePair);
                    for (StringKeyValuePair item : itemList){
                        //stringList.add(entry.getValue());
                        stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                        stringKeyValuePair.setValue(item.getValue());
                        ListSpinner.add(stringKeyValuePair);
                    }
                    CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mModeSpinner, ListSpinner);
                    ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.INPUT_MODE_ANNOTATION, true, ListSpinner);
                }
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");

                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mModeInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                ProductInformationFragment.getSingletonInstance().mModeInputLayout.setVisibility(View.GONE);
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mModeInputLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }

    }
    public void handlingEMRIDInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRSpinner.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().emrLabel.setVisibility(View.VISIBLE);

                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                //stringArrayList.add(fieldCaption);
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }
                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mEMRSpinner, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION, true, ListSpinner);
                break;
            case DOB:
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelEMR.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
                CommonMethod.log(TAG , "Handling dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRText.setInputType(TYPE_CLASS_NUMBER);
                ProductInformationFragment.getSingletonInstance().mTextLabelEMR.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.GONE);
                ProductInformationFragment.getSingletonInstance().mEMRInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mEMRText.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelEMR.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mEMRLayout.setVisibility(View.GONE);
                ProductInformationFragment.getSingletonInstance().mEMRInputLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void handlingFlatExtraIdInStaticView(String fieldType, String fieldCaption, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling SA list");
                ProductInformationFragment.getSingletonInstance().mFlatExtraLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraSpinner.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().flatextraLabel.setVisibility(View.VISIBLE);

                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                //stringArrayList.add(fieldCaption);
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }

                CommonMethod.setMaterialSpinnerAdapter(TAG , context, ProductInformationFragment.getSingletonInstance().mFlatExtraSpinner, ListSpinner);
                ProductInformationFragment.getSingletonInstance().updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION, true, ListSpinner);

                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                ProductInformationFragment.getSingletonInstance().mFlatExtraLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                ProductInformationFragment.getSingletonInstance().mTextLabelFlatExtra.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION);
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");

                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mFlatExtraLayout.setVisibility(View.GONE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraInputLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                ProductInformationFragment.getSingletonInstance().mFlatExtraLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraInputLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraText.setInputType(TYPE_CLASS_TEXT);
                ProductInformationFragment.getSingletonInstance().mTextLabelFlatExtra.setText(fieldCaption);
                ProductInformationFragment.getSingletonInstance().updateDynamicEditTextVisibility(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION);
                CommonMethod.log(TAG , "Handling string ");
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mFlatExtraLayout.setVisibility(View.GONE);
                ProductInformationFragment.getSingletonInstance().mFlatExtraInputLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void handlingStaticViewForPrOption(String fieldType, String OrigkeywordName){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling SA list");
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().setOptionsUIDetails();
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling dob");
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().setOptionsUIDetails();
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Number");
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().setOptionsUIDetails();
                break;
            case Output:
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.GONE);
                break;
            case CheckBox:

                break;
            case String:
                CommonMethod.log(TAG , "Handling string ");
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.VISIBLE);
                ProductInformationFragment.getSingletonInstance().setOptionsUIDetails();
                break;
            case Disabled:
                ProductInformationFragment.getSingletonInstance().mOptionLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }


    public void createViewIfNotPresent(String fieldType, String fieldCaption){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling Dummy list");
                /*for (Map.Entry<String, ArrayList<StringKeyValuePair>>  entry : keyvaluePairHashMap.entrySet()){
                    CommonMethod.log(TAG , "Entry key " + entry.getKey());
                    for (StringKeyValuePair stringKeyValuePair: entry.getValue()){
                        CommonMethod.log(TAG , "Entry value size " + entry.getValue().size());
                        CommonMethod.log(TAG , "String Key size " + stringKeyValuePair.getKey());
                        CommonMethod.log(TAG , "String value  " + stringKeyValuePair.getValue());
                    }

                }*/

                ArrayList<StringKeyValuePair> itemList =  keyvaluePairHashMap.get("@"+fieldCaption);
                CommonMethod.log(TAG , "Item list size " + itemList.size());
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                for (StringKeyValuePair item : itemList){
                    //stringList.add(entry.getValue());
                    stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                }
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling Create view of not present dob");
                EditText dobeditText = CommonMethod.createDobTypeEditText(context, fieldCaption);

                ProductInformationFragment.getSingletonInstance().mDynamicLayout.addView(dobeditText);
                DynamicEditTextModel dobdynamicEditTextModel = new DynamicEditTextModel();
                dobdynamicEditTextModel.setDynamicFieldIdentifier(fieldCaption);
                dobdynamicEditTextModel.setEditText(dobeditText);
                dobdynamicEditTextModel.setVisible(true);
                //addStaticAndDynamicEditTextToMap(fieldCaption, dobdynamicEditTextModel);
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Dummy Number");
                break;
            case Output:
                CommonMethod.log(TAG , "Handling Dummy for output");
                break;
            case CheckBox:
                CommonMethod.log(TAG , "Handling Dummy for check box ");
                break;
            case String:
                CommonMethod.log(TAG , "Handling Dummy string ");
                break;
            case Disabled:
                CommonMethod.log(TAG , "Handling Dummy for disabled");
                break;

            default:
                break;
        }

    }


    public void dummyforcopypaste(String fieldType){

        switch (NvestLibraryConfig.FieldType.valueOf(fieldType)){
            case List:
                CommonMethod.log(TAG , "Handling Dummy list");
                break;
            case DOB:
                CommonMethod.log(TAG , "Handling Dummy dob");
                break;
            case Number:
                CommonMethod.log(TAG , "Handling Dummy Number");
                break;
            case Output:
                CommonMethod.log(TAG , "Handling Dummy for output");
                break;
            case CheckBox:
                CommonMethod.log(TAG , "Handling Dummy for check box ");
                break;
            case String:
                CommonMethod.log(TAG , "Handling Dummy string ");
                break;
            case Disabled:
                CommonMethod.log(TAG , "Handling Dummy for disabled");
                break;

            default:
                break;
        }
    }

    TextWatcher watchMI = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };
    TextWatcher watchSA = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String word = CommonMethod.convertIntoWords(s.toString());
            CommonMethod.log(TAG , "Char sequence " + word);
            if(word != null && !word.isEmpty()){
                ProductInformationFragment.getSingletonInstance().mTextSASumToWords.setText(word);
            }
            else {
                ProductInformationFragment.getSingletonInstance().mTextSASumToWords.setText("");
            }

        }
    };
}
