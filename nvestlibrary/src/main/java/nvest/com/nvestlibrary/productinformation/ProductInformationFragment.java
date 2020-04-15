package nvest.com.nvestlibrary.productinformation;

import android.app.DatePickerDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.basicInformation.BasicInformationDataViewModel;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.landing.LandingActivity;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestWebModel.DynamicCheckBoxModel;
import nvest.com.nvestlibrary.nvestWebModel.DynamicEditTextModel;
import nvest.com.nvestlibrary.nvestWebModel.DynamicSpinnerModel;
import nvest.com.nvestlibrary.nvestWebModel.FundStrategyModel;
import nvest.com.nvestlibrary.nvestWebModel.FundsModel;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;
import nvest.com.nvestlibrary.nvestWebModel.LoadNextOption;
import nvest.com.nvestlibrary.nvestWebModel.MasterOption;
import nvest.com.nvestlibrary.nvestWebModel.RiderInformationModel;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.riderinformation.RiderInformationDataViewModel;
import nvest.com.nvestlibrary.solutionDetails.SolutionProducts;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;

public class ProductInformationFragment extends Fragment implements BasicInformationDataViewModel.BasicInformationDataListener, View.OnClickListener, ProductInformationDataViewModel.ProductInformationDataListener, MaterialSpinner.OnItemSelectedListener, View.OnTouchListener, ValidateInformationDataViewModel.ValidateInformationDataListener, RiderInformationDataViewModel.RiderInformationDataListener, FundsAdapter.FundsValueListener {
    public static String TAG = ProductInformationFragment.class.getSimpleName();
    public ProductInformationDataViewModel productInformationDataViewModel;
    public BasicInformationDataViewModel basicInformationDataViewModel;
    private ValidateInformationDataViewModel validateInformationDataViewModel;
    public Products products;
    private SolutionProducts solutionProduct;
    private int productIndex = -1;

    public Map<String, ArrayList<StringKeyValuePair>> keyvaluePairHashMap;
    public LinkedHashMap<String, Keyword> keywordLinkedHashMap;

    public TextView mProductName;
    public LinearLayout mPTLayout;
    public LinearLayout mPPTLayout;
    public LinearLayout mModeLayout;
    public EditText mPTText;
    public EditText mPPTText;
    public EditText mModeText;
    public EditText mEMRText;
    public EditText mFlatExtraText;
    public TextInputLayout mPTInputLayout;
    public TextInputLayout mPPTInputLayout;
    public TextInputLayout mModeInputLayout;
    public TextInputLayout mEMRInputLayout;
    public TextInputLayout mFlatExtraInputLayout;
    public MaterialSpinner mPTSpinner;
    public MaterialSpinner mPPTSpinner;
    public MaterialSpinner mModeSpinner;
    public MaterialSpinner mEMRSpinner;
    public MaterialSpinner mFlatExtraSpinner;
    public FrameLayout frameLayout;
    private LandingActivity landingActivity;


    public LinearLayout mSAHeaderLayout;
    public LinearLayout mMPHeaderLayout;
    public LinearLayout mAPHeaderLayout;
    public LinearLayout mMIHeaderLayout;
    public LinearLayout mSAMFHeaderLayout;
    public TextInputLayout mSAInputLayout;
    public TextInputLayout mMPInputLayout;
    public TextInputLayout mAPInputLayout;
    public TextInputLayout mMIInputLayout;
    public TextInputLayout mSAMFInputLayout;
    public EditText mTextSA;
    public EditText mTextMP;
    public EditText mTextAP;
    public EditText mTextMI;
    public EditText mTextSAMF;
    public MaterialSpinner mSelectSpinnerSA;
    public MaterialSpinner mSelectSpinnerMP;
    public MaterialSpinner mSelectSpinnerAP;
    public MaterialSpinner mSelectSpinnerMI;
    public MaterialSpinner mSelectSpinnerSAMF;
    public MaterialSpinner mSelectFundStrategy;
    public LinearLayout mBackgroundLayout;
    public RecyclerView mFundsRecyclerView;
    public Button mBtnValidate;
    public LinearLayout mOptionLayout;
    public TextView mTextFund;
    public View mSeperator;
    public LinearLayout mTotalLayout;
    public RelativeLayout mProductMainLayout;
    public LinearLayout mDynamicLayout;
    public LinearLayout mLayoutFunds;
    public TextView mTextErrorFunds;
    public TextView mTextFundsSum;
    public TextView mTextSASumToWords;
    public TextView mTextMPSumToWords;
    public TextView mTextAPSumToWords;
    public TextView mTextMISumToWords;
    public TextView mTextSAMFSumToWords;
    public LinearLayout mRecyclerFundLayout;
    public RelativeLayout mProgressDimLayout;
    public LinearLayout mEMRLayout;
    public LinearLayout mFlatExtraLayout;
    public TextView mTextLabelSA;
    public TextView mTextLabelMP;
    public TextView mTextLabelAP;
    public TextView mTextLabelMI;
    public TextView mTextLabelSAMF;
    public TextView mTextLabelPT;
    public TextView mTextLabelEMR;
    public TextView mTextLabelFlatExtra;
    public TextView fundstrategyLabel;

    public LinkedHashMap<Integer, MaterialSpinner> spinnerLinkedHashMap;
    public ArrayList<EditText> numberEditTextArrayList;

    public TreeMap<String, DynamicSpinnerModel> stringDynamicSpinnerModelTreeMap;
    public TreeMap<String, DynamicEditTextModel> stringDynamicEditTextModelTreeMap;
    public TreeMap<String, DynamicCheckBoxModel> stringDynamicCheckBoxModelTreeMap;

    public TreeMap<String, TextView> textViewTreeMap;
    public LinkedHashMap<Integer, LoadNextOption> loadNextOptionLinkedHashMap;
    public TreeMap<String, String> treeMapOptionItemSelected;
    public int mYear, mMonth, mDay;
    public static ProductInformationFragment productInformationFragmentInstance;
    public ArrayList<MasterOption> mMasterOptionWithValuesList;
    public String dummyString = "http://221.135.132.235:4552/NSureServices.svc/LoadOptions?";
    public TreeMap<String, String> newOptionsList;
    public TreeMap<Integer, StringKeyValuePair> stringKeyValuePairTreeMap;
    public TreeMap<Integer, StringKeyValuePair> optionListTreeMap;

    public TextView ptLabel;
    public TextView pptLabel;
    public TextView modeLabel;
    public TextView emrLabel;
    public TextView flatextraLabel;
    private RiderInformationDataViewModel riderInformationDataViewModel;
    private ArrayList<RiderInformationModel> riderInformationModelArrayList;
    private List<FundStrategyModel> fundStrategyList = new ArrayList<>();
    private int totalFundPercent;
    private FundStrategyModel fundStrategyModel;
    private View view;
    private HashMap<String, String> defaultValues;
    private FundsAdapter fundsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonMethod.log(TAG, "On create view started");
        view = inflater.inflate(R.layout.product_information_fragment, container, false);
        productInformationFragmentInstance = this;
        /*Products productsreceived = getArguments().getParcelable("products");
        if (productsreceived != null) {
            products = productsreceived;
            CommonMethod.log(TAG, "Product name passed " + products.getProductName());
            CommonMethod.log(TAG, "Product ID passed " + products.getProductId());
        }*/
        defaultValues = new HashMap<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            Products productsreceived = bundle.getParcelable("products");
            if (productsreceived != null) {
                products = productsreceived;
            }

            solutionProduct = bundle.getParcelable("solutionProduct");
            productIndex = bundle.getInt("productIndex");
            if (bundle.getSerializable("defaultValues") != null) {
                defaultValues = (HashMap<String, String>) bundle.getSerializable("defaultValues");
            } else {
                defaultValues = CommonMethod.getAllParamsFromGenericDTO();
            }
            CommonMethod.log(TAG, "Default values " + defaultValues);
            CommonMethod.log(TAG, "Product id received " + products.getProductId());
        }

        mProductName = (TextView) view.findViewById(R.id.text_product_name);
        mPTLayout = (LinearLayout) view.findViewById(R.id.layout_PT);
        mPPTLayout = (LinearLayout) view.findViewById(R.id.layout_PPT);
        mModeLayout = (LinearLayout) view.findViewById(R.id.layout_mode);

        mPTText = (EditText) view.findViewById(R.id.text_PT);

        mPPTText = (EditText) view.findViewById(R.id.text_PPT);

        mModeText = (EditText) view.findViewById(R.id.text_mode);

        mEMRText = (EditText) view.findViewById(R.id.text_emr);

        frameLayout = (FrameLayout) view.findViewById(R.id.touchLayout);

        mFlatExtraText = (EditText) view.findViewById(R.id.text_flat_extra);
        mPTInputLayout = (TextInputLayout) view.findViewById(R.id.text_PT_input_layout);
        mPPTInputLayout = (TextInputLayout) view.findViewById(R.id.text_PPT_input_layout);
        mModeInputLayout = (TextInputLayout) view.findViewById(R.id.text_mode_input_layout);
        mEMRInputLayout = (TextInputLayout) view.findViewById(R.id.text_emr_input_layout);
        mFlatExtraInputLayout = (TextInputLayout) view.findViewById(R.id.text_flat_extra_input_layout);
        mPTSpinner = (MaterialSpinner) view.findViewById(R.id.select_PT);
        mPPTSpinner = (MaterialSpinner) view.findViewById(R.id.select_PPT);
        mModeSpinner = (MaterialSpinner) view.findViewById(R.id.select_mode);
        mSelectFundStrategy = (MaterialSpinner) view.findViewById(R.id.select_fund_strategy);
                /*@InjectView(R.id.select_samf)
                Spinner mSAMFSpinner;*/
        mEMRSpinner = (MaterialSpinner) view.findViewById(R.id.select_emr);
        mFlatExtraSpinner = (MaterialSpinner) view.findViewById(R.id.select_flat_extra);

        mSAHeaderLayout = (LinearLayout) view.findViewById(R.id.sum_assured_header_layout);
        mMPHeaderLayout = (LinearLayout) view.findViewById(R.id.modal_premium_header_layout);
        mAPHeaderLayout = (LinearLayout) view.findViewById(R.id.annual_premium_header_layout);
        mMIHeaderLayout = (LinearLayout) view.findViewById(R.id.monthly_income_header_layout);
        mSAMFHeaderLayout = (LinearLayout) view.findViewById(R.id.samf_header_layout);
        mSAInputLayout = (TextInputLayout) view.findViewById(R.id.sum_assured_input_layout);
        mMPInputLayout = (TextInputLayout) view.findViewById(R.id.modal_premium_input_layout);
        mAPInputLayout = (TextInputLayout) view.findViewById(R.id.annual_premium_input_layout);
        mMIInputLayout = (TextInputLayout) view.findViewById(R.id.monthly_income_input_layout);
        mSAMFInputLayout = (TextInputLayout) view.findViewById(R.id.samf_input_layout);
        mTextSA = (EditText) view.findViewById(R.id.text_sum_assured);
        mTextMP = (EditText) view.findViewById(R.id.text_modal_premium);
        mTextAP = (EditText) view.findViewById(R.id.text_annual_premium);
        mTextMI = (EditText) view.findViewById(R.id.text_monthly_income);
        mTextSAMF = (EditText) view.findViewById(R.id.text_samf);
        mSelectSpinnerSA = (MaterialSpinner) view.findViewById(R.id.select_sa_input_layout);
        mSelectSpinnerMP = (MaterialSpinner) view.findViewById(R.id.select_mp_input_layout);
        mSelectSpinnerAP = (MaterialSpinner) view.findViewById(R.id.select_ap_input_layout);
        mSelectSpinnerMI = (MaterialSpinner) view.findViewById(R.id.select_mi_input_layout);
        mSelectSpinnerSAMF = (MaterialSpinner) view.findViewById(R.id.select_samf_input_layout);
        mBackgroundLayout = (LinearLayout) view.findViewById(R.id.background_layout);
        mFundsRecyclerView = (RecyclerView) view.findViewById(R.id.funds_recycler_view);
        mBtnValidate = (Button) view.findViewById(R.id.btn_validate);
        mOptionLayout = (LinearLayout) view.findViewById(R.id.options_layout);
        mTextFund = (TextView) view.findViewById(R.id.text_fund);
        mSeperator = (View) view.findViewById(R.id.seperator);
        mTotalLayout = (LinearLayout) view.findViewById(R.id.total_layout);
        mProductMainLayout = (RelativeLayout) view.findViewById(R.id.product_main_layout);
                /*@InjectView(R.id.progress_product_information)
                ProgressBar mProgressBar;*/
        mDynamicLayout = (LinearLayout) view.findViewById(R.id.dynamic_layout);
        mLayoutFunds = (LinearLayout) view.findViewById(R.id.layout_funds);
        mTextErrorFunds = (TextView) view.findViewById(R.id.text_error_fund);
        mTextFundsSum = (TextView) view.findViewById(R.id.text_funds_sum);
        mTextSASumToWords = (TextView) view.findViewById(R.id.text_SA_num_to_words);
        mTextMPSumToWords = (TextView) view.findViewById(R.id.text_MP_num_to_words);
        mTextAPSumToWords = (TextView) view.findViewById(R.id.text_AP_num_to_words);
        mTextMISumToWords = (TextView) view.findViewById(R.id.text_MI_num_to_words);
        mTextSAMFSumToWords = (TextView) view.findViewById(R.id.text_samf_num_to_words);
        mRecyclerFundLayout = (LinearLayout) view.findViewById(R.id.recycler_layout);

                /*@InjectView(R.id.layout_samf)
                LinearLayout mSAMFLayout;*/
        mEMRLayout = (LinearLayout) view.findViewById(R.id.layout_emr);
        mFlatExtraLayout = (LinearLayout) view.findViewById(R.id.layout_flat_extra);
        mTextLabelSA = (TextView) view.findViewById(R.id.text_label_SA);
        mTextLabelMP = (TextView) view.findViewById(R.id.text_label_MP);
        mTextLabelAP = (TextView) view.findViewById(R.id.text_label_AP);
        mTextLabelMI = (TextView) view.findViewById(R.id.text_label_MI);
        fundstrategyLabel = (TextView) view.findViewById(R.id.text_label_fund_strategy);
        mTextLabelSAMF = (TextView) view.findViewById(R.id.text_label_samf);

        mTextLabelPT = (TextView) view.findViewById(R.id.header_PT);
                /*@InjectView(v
               R.id.header_samf)
                TextView mTextLabelSAMF;*/
        mTextLabelEMR = (TextView) view.findViewById(R.id.header_emr);
        mTextLabelFlatExtra = (TextView) view.findViewById(R.id.header_flat_extra);
        ptLabel = (TextView) view.findViewById(R.id.ptLabel);
        pptLabel = (TextView) view.findViewById(R.id.pptLabel);
        modeLabel = (TextView) view.findViewById(R.id.modeLabel);
        emrLabel = (TextView) view.findViewById(R.id.emrLabel);
        flatextraLabel = (TextView) view.findViewById(R.id.flatExtraLabel);

        if (solutionProduct != null) {
            mBtnValidate.setText(R.string.save);
        }


        init();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static ProductInformationFragment getSingletonInstance() {
        if (productInformationFragmentInstance == null) {
            throw new RuntimeException("Product information fragment instantiated");
        }
        return productInformationFragmentInstance;
    }

    public void addStaticAndDynamicEditTextToMap(String fieldCaption, DynamicEditTextModel dynamicEditTextModel) {
        stringDynamicEditTextModelTreeMap.put(fieldCaption, dynamicEditTextModel);
    }

    public void addStaticAndDynamicCheckBoxToMap(String fieldCaption, DynamicCheckBoxModel dynamicCheckBoxModel) {
        stringDynamicCheckBoxModelTreeMap.put(fieldCaption, dynamicCheckBoxModel);
    }

    public void addStaticAndDynamicSpinnerToMap(String fieldCaption, DynamicSpinnerModel dynamicSpinnerModel) {
        stringDynamicSpinnerModelTreeMap.put(fieldCaption, dynamicSpinnerModel);
    }

    public void updateDynamicSpinnerVisibility(String key, boolean visibility, List<StringKeyValuePair> itemsAddedInthisSpinner) {
        if (stringDynamicSpinnerModelTreeMap.containsKey(key)) {
            DynamicSpinnerModel dynamicSpinnerModel = stringDynamicSpinnerModelTreeMap.get(key);
            dynamicSpinnerModel.setVisible(visibility);
            dynamicSpinnerModel.setDynamicFieldIdentifier(key);
            if (itemsAddedInthisSpinner != null && itemsAddedInthisSpinner.size() > 0) {
                dynamicSpinnerModel.setSpinnerItemList(itemsAddedInthisSpinner);
            }
            stringDynamicSpinnerModelTreeMap.put(key, dynamicSpinnerModel);
        }
    }


    public void updateDynamicEditTextVisibility(String key) {
        if (stringDynamicEditTextModelTreeMap.containsKey(key)) {
            DynamicEditTextModel dynamicEditTextModel = stringDynamicEditTextModelTreeMap.get(key);
            dynamicEditTextModel.setVisible(true);
            /*LinkedHashMap<String, DynamicEditTextModel> newmap=(LinkedHashMap<String, DynamicEditTextModel>) stringDynamicEditTextModelLinkedHashMap.clone();
            stringDynamicEditTextModelLinkedHashMap.clear();*/
            stringDynamicEditTextModelTreeMap.put(key, dynamicEditTextModel);
            //stringDynamicEditTextModelLinkedHashMap.putAll(newmap);
        }
    }

    public void printOptionItemSelectedDummy() {
        for (Map.Entry<String, String> entry : treeMapOptionItemSelected.entrySet()) {
            CommonMethod.log(TAG, "level Key " + entry.getKey());
            CommonMethod.log(TAG, "level value " + entry.getValue());
        }
    }

    public void setStaticView() {
        ProductInformationHandlers productInformationHandlers = new ProductInformationHandlers(products, keyvaluePairHashMap, keywordLinkedHashMap, getActivity());
        for (Map.Entry<String, Keyword> entry : keywordLinkedHashMap.entrySet()) {
            boolean isMapped = entry.getValue().getIsMapped();
            if (isMapped) {
                String fieldCaption = entry.getValue().getFieldCaption();
                String keywordName = entry.getValue().getKeywordName();
                String OrigkeywordName = entry.getValue().getKeywordName();
                String fieldType = entry.getValue().getFieldType();
                String tempKeyword;
                if (keywordName.contains("@")) {
                    CommonMethod.log(TAG, "Static view keyword " + keywordName.substring(1));
                    tempKeyword = keywordName.substring(1);
                } else {
                    tempKeyword = keywordName;
                }

                /*switch (NvestLibraryConfig.FieldType.valueOf(fieldType)) {
                    case List:
                        CommonMethod.log(TAG, "StaticNew Found List " + fieldCaption);
                        break;
                    case DOB:
                        CommonMethod.log(TAG, "StaticNew Found dob " + fieldCaption);
                        break;
                    case Number:
                        CommonMethod.log(TAG, "StaticNew Found Number " + fieldCaption);
                        break;
                    case Output:
                        CommonMethod.log(TAG, "StaticNew Found output " + fieldCaption);
                        break;
                    case CheckBox:
                        CommonMethod.log(TAG, "Creating check box for " + fieldCaption);
                        break;
                    case String:
                        CommonMethod.log(TAG, "Creating edit text for " + fieldCaption);
                        break;
                    default:
                        break;
                }*/

                switch (NvestLibraryConfig.KeywordName.valueOf(tempKeyword)) {
                    case PR_PT:
                        productInformationHandlers.handlingPremiumTermInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_SA:
                        productInformationHandlers.handlingSumAssuredACasesInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_MODALPREM:
                        productInformationHandlers.handlingModalPremiumInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_ANNPREM:
                        productInformationHandlers.handlingAnnualPremiumInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_MI:
                        productInformationHandlers.handlingMonthlyIncomeInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_SAMF:
                        productInformationHandlers.handlingSumAssuredMultiplyingFactorInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_PPT:
                        productInformationHandlers.handlingPremiumPaymentTermInStaticView(fieldType, OrigkeywordName);
                        break;
                    case INPUT_MODE:
                        productInformationHandlers.handlingInputModeTermInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_EMRID:
                        productInformationHandlers.handlingEMRIDInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_FLATEXTRAID:
                        productInformationHandlers.handlingFlatExtraIdInStaticView(fieldType, fieldCaption, OrigkeywordName);
                        break;
                    case PR_OPTION:
                        productInformationHandlers.handlingStaticViewForPrOption(fieldType, OrigkeywordName);
                        break;
                    case LI_SMOKE:
                        productInformationHandlers.dummyforcopypaste(fieldType);
                        break;
                    /*case PROPOSER_AGE:
                        productInformationHandlers.dummyforcopypaste(fieldType);
                        break;*/
                    default:
                        break;
                }

            }
        }
    }


    public void setOptionsUIDetails() {
        mBtnValidate.setVisibility(View.VISIBLE);
        mMasterOptionWithValuesList = productInformationDataViewModel.getOptions(products.getProductId());
        CommonMethod.log(TAG, "Master options size " + mMasterOptionWithValuesList.size());
        boolean foundValueAsNull = false;
        for (MasterOption masterOption : mMasterOptionWithValuesList) {
            Map<Integer, KeyValuePair> options = masterOption.getOptions();
            List<StringKeyValuePair> ListSpinner = new ArrayList<>();
            CommonMethod.log(TAG, "Adding name to spinner " + masterOption.getLevelName() + " Level " + masterOption.getLevel());
            StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
           /* stringKeyValuePair.setKey(String.valueOf(masterOption.getLevel()));
            stringKeyValuePair.setValue(masterOption.getLevelName());*/
            stringKeyValuePair.setKey("-1");
            stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
            ListSpinner.add(stringKeyValuePair);
            if (options != null && options.size() > 0) {
                CommonMethod.log(TAG, "Entry name outside for loop " + options.get(0).getValue());

                for (int i = 0; i < options.size(); i++) {
                    KeyValuePair keyValuePair = options.get(i);
                    CommonMethod.log(TAG, "New options key " + options.get(i).getKey());
                    CommonMethod.log(TAG, "New Options value " + options.get(i).getValue());
                    CommonMethod.log(TAG, "Master option input type " + masterOption.getInputType());
                    //newOptionsList.put(options.get(i).getValue(), String.valueOf(options.get(i).getKey()));
                    if (keyValuePair.getValue() == null || keyValuePair.getValue().isEmpty()) {
                        CommonMethod.log(TAG, "Null or empty");
                        foundValueAsNull = true;
                        String identifier = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + masterOption.getLevel();
                        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_OPTION_ANNOTATION + masterOption.getLevelName(), String.valueOf(options.get(i).getKey()), String.valueOf(options.get(i).getKey()), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), "SetOptionUi");
                        if (masterOption.getInputType() != null) {
                            TextView textView = CommonMethod.createTextView(getActivity(), masterOption.getLevelName());
                            textViewTreeMap.put(masterOption.getLevelName(), textView);
                            mDynamicLayout.addView(textView);
                            switch (NvestLibraryConfig.FieldType.valueOf(masterOption.getInputType())) {
                                case String:
                                    EditText stringeditText = CommonMethod.createStringTypeEditText(getActivity(), masterOption.getLevelName());
                                    mDynamicLayout.addView(stringeditText);
                                    DynamicEditTextModel stringdynamicEditTextModel = new DynamicEditTextModel();
                                    stringdynamicEditTextModel.setDynamicFieldIdentifier(identifier);
                                    stringdynamicEditTextModel.setEditText(stringeditText);
                                    stringdynamicEditTextModel.setVisible(true);
                                    addStaticAndDynamicEditTextToMap(identifier, stringdynamicEditTextModel);
                                    break;
                                case DOB:
                                    EditText dobEditText = CommonMethod.createDobTypeEditText(getActivity(), masterOption.getLevelName());
                                    mDynamicLayout.addView(dobEditText);
                                    DynamicEditTextModel dobdynamicEditTextModel = new DynamicEditTextModel();
                                    dobdynamicEditTextModel.setDynamicFieldIdentifier(identifier);
                                    dobdynamicEditTextModel.setEditText(dobEditText);
                                    dobdynamicEditTextModel.setVisible(true);
                                    addStaticAndDynamicEditTextToMap(identifier, dobdynamicEditTextModel);
                                    break;
                                case Number:
                                    EditText numberEditText = CommonMethod.createNumberTypeEditText(getActivity(), masterOption.getLevelName());
                                    mDynamicLayout.addView(numberEditText);
                                    DynamicEditTextModel numberdynamicEditTextModel = new DynamicEditTextModel();
                                    numberdynamicEditTextModel.setDynamicFieldIdentifier(identifier);
                                    numberdynamicEditTextModel.setEditText(numberEditText);
                                    numberdynamicEditTextModel.setVisible(true);
                                    addStaticAndDynamicEditTextToMap(identifier, numberdynamicEditTextModel);
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(options.get(i).getKey()));
                        stringKeyValuePair.setValue(options.get(i).getValue());
                        ListSpinner.add(stringKeyValuePair);
                    }
                }
                if (foundValueAsNull) {
                    CommonMethod.log(TAG, "Found value as null");
                    foundValueAsNull = false;
                    continue;
                }
            } else {
                CommonMethod.log(TAG, "Options is null");
            }
            MaterialSpinner spinner = CommonMethod.createRedSpinner(getActivity(), String.valueOf(masterOption.getLevel()));
            TextView textView = CommonMethod.createTextView(getActivity(), masterOption.getLevelName());
            mDynamicLayout.addView(textView);
            CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), spinner, ListSpinner);
            spinner.setOnItemSelectedListener(this);
            spinnerLinkedHashMap.put(masterOption.getLevel(), spinner);
            mDynamicLayout.addView(spinner);
            String identifier = NvestLibraryConfig.PR_OPTION_ANNOTATION + masterOption.getLevel();
            DynamicSpinnerModel dynamicSpinnerModel = new DynamicSpinnerModel();
            dynamicSpinnerModel.setDynamicFieldIdentifier(identifier);
            dynamicSpinnerModel.setMaterialSpinner(spinner);
            //addStaticAndDynamicSpinnerToMap(String.valueOf(masterOption.getLevel()), dynamicSpinnerModel);
            addStaticAndDynamicSpinnerToMap(identifier, dynamicSpinnerModel);
            if (ListSpinner.size() > 1) {
                //dynamicSpinnerModel.setVisible(true);
                updateDynamicSpinnerVisibility(identifier, true, ListSpinner);
            }
        }

    }

    public void setDynamicView() {
        for (Map.Entry<String, Keyword> entry : keywordLinkedHashMap.entrySet()) {
            boolean isMapped = entry.getValue().getIsMapped();
            if (isMapped == false) { // check dynamic field
                String fieldCaption = entry.getValue().getFieldCaption();
                String keywordName = entry.getValue().getKeywordName();
                String fieldType = entry.getValue().getFieldType();
                CommonMethod.log(TAG, "Dynamic view keyword " + keywordName);

                switch (NvestLibraryConfig.FieldType.valueOf(fieldType)) {
                    case List:
                        createTextView(fieldCaption);
                        List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                        ArrayList<StringKeyValuePair> itemList = keyvaluePairHashMap.get(entry.getKey());
                        CommonMethod.log(TAG, "Item list size " + itemList.size());
                        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey("-1");
                        stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                        ListSpinner.add(stringKeyValuePair);
                        for (StringKeyValuePair item : itemList) {
                            stringKeyValuePair = new StringKeyValuePair();
                            stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                            stringKeyValuePair.setValue(item.getValue());
                            ListSpinner.add(stringKeyValuePair);
                        }
                        MaterialSpinner spinner = CommonMethod.createSpinner(getActivity(), entry.getKey());
                        CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), spinner, ListSpinner);
                        spinner.setOnItemSelectedListener(this);
                        mDynamicLayout.addView(spinner);
                        DynamicSpinnerModel dynamicSpinnerModel = new DynamicSpinnerModel();
                        dynamicSpinnerModel.setDynamicFieldIdentifier(keywordName);
                        dynamicSpinnerModel.setMaterialSpinner(spinner);
                        //dynamicSpinnerModel.setVisible(true);
                        addStaticAndDynamicSpinnerToMap(keywordName, dynamicSpinnerModel);
                        if (ListSpinner.size() > 1) {
                            updateDynamicSpinnerVisibility(keywordName, true, ListSpinner);
                        }
                        break;
                    case DOB:
                        createTextView(fieldCaption);
                        CommonMethod.log(TAG, "Found dob " + entry.getKey());
                        EditText dobeditText = CommonMethod.createDobTypeEditText(getActivity(), fieldCaption);
                        dobeditText.setOnClickListener(this);
                        mDynamicLayout.addView(dobeditText);
                        DynamicEditTextModel dobdynamicEditTextModel = new DynamicEditTextModel();
                        dobdynamicEditTextModel.setDynamicFieldIdentifier(keywordName);
                        dobdynamicEditTextModel.setEditText(dobeditText);
                        dobdynamicEditTextModel.setVisible(true);
                        addStaticAndDynamicEditTextToMap(fieldCaption, dobdynamicEditTextModel);
                        break;
                    case Number:
                        createTextView(fieldCaption);
                        EditText editText = CommonMethod.createNumberTypeEditText(getActivity(), fieldCaption);
                        mDynamicLayout.addView(editText);
                        DynamicEditTextModel numberdynamicEditTextModel = new DynamicEditTextModel();
                        numberdynamicEditTextModel.setDynamicFieldIdentifier(keywordName);
                        numberdynamicEditTextModel.setEditText(editText);
                        numberdynamicEditTextModel.setVisible(true);
                        addStaticAndDynamicEditTextToMap(fieldCaption, numberdynamicEditTextModel);
                        break;
                    case Output:

                        break;
                    case CheckBox:
                        createTextView(fieldCaption);
                        CheckBox checkBox = CommonMethod.createCheckBox(getActivity(), entry.getKey());
                        mDynamicLayout.addView(checkBox);
                        DynamicCheckBoxModel dynamicCheckBoxModel = new DynamicCheckBoxModel();
                        dynamicCheckBoxModel.setCheckBox(checkBox);
                        dynamicCheckBoxModel.setDynamicFieldIdentifier(keywordName);
                        dynamicCheckBoxModel.setVisible(true);
                        addStaticAndDynamicCheckBoxToMap(fieldCaption, dynamicCheckBoxModel);
                        break;
                    case String:
                        createTextView(fieldCaption);
                        CommonMethod.log(TAG, "Creating edit text for " + fieldCaption);
                        EditText stringeditText = CommonMethod.createStringTypeEditText(getActivity(), fieldCaption);
                        mDynamicLayout.addView(stringeditText);
                        DynamicEditTextModel stringEditTextModel = new DynamicEditTextModel();
                        stringEditTextModel.setDynamicFieldIdentifier(keywordName);
                        stringEditTextModel.setEditText(stringeditText);
                        stringEditTextModel.setVisible(true);
                        addStaticAndDynamicEditTextToMap(fieldCaption, stringEditTextModel);
                        break;
                    default:
                        break;
                }
            }

        }
    }

    public void createTextView(String fieldCaption) {
        TextView textView = CommonMethod.createTextView(getActivity(), fieldCaption);
        mDynamicLayout.addView(textView);
    }

    public void init() {
        keyvaluePairHashMap = new HashMap<>();
        keywordLinkedHashMap = new LinkedHashMap<>();
        spinnerLinkedHashMap = new LinkedHashMap<>();
        textViewTreeMap = new TreeMap<>();
        treeMapOptionItemSelected = new TreeMap<>();
        stringDynamicSpinnerModelTreeMap = new TreeMap<>();
        stringDynamicEditTextModelTreeMap = new TreeMap<>();
        stringDynamicCheckBoxModelTreeMap = new TreeMap<>();
        optionListTreeMap = new TreeMap<>();
        mMasterOptionWithValuesList = new ArrayList<>();
        riderInformationModelArrayList = new ArrayList<>();
        loadNextOptionLinkedHashMap = new LinkedHashMap<>();
        stringKeyValuePairTreeMap = new TreeMap<>();
        mProductName.setText(products.getProductName());
        productInformationDataViewModel = ViewModelProviders.of(this).get(ProductInformationDataViewModel.class);
        productInformationDataViewModel.setProductInformationDataListener(this);
        basicInformationDataViewModel = ViewModelProviders.of(this).get(BasicInformationDataViewModel.class);
        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);
        riderInformationDataViewModel = ViewModelProviders.of(this).get(RiderInformationDataViewModel.class);
        riderInformationDataViewModel.setRiderInformationDataListener(this);
        validateInformationDataViewModel.setValidateInformationDataListener(this);
        riderInformationDataViewModel.getRidersFromProductId(products.getProductId());
        basicInformationDataViewModel.setBasicInformationDataListener(this);
        newOptionsList = new TreeMap<>();
        basicInformationDataViewModel.getKeywords(products.getProductId());
        productInformationDataViewModel.getFundStrategyByProductId(products.getProductId());
        productInformationDataViewModel.getPtList(products.getProductId());
        //productInformationDataViewModel.getPtPPTMaster(products.getProductId(), "0");
        mPTSpinner.setTag(NvestLibraryConfig.PT_SPINNER);
        mPTSpinner.setOnItemSelectedListener(this);
        mPPTSpinner.setTag(NvestLibraryConfig.PPT_SPINNER);
        mPPTSpinner.setOnItemSelectedListener(this);
        mModeSpinner.setTag(NvestLibraryConfig.MODE_SPINNER);
        mModeSpinner.setOnItemSelectedListener(this);
        mEMRSpinner.setTag(NvestLibraryConfig.EMR_SPINNER);
        mEMRSpinner.setOnItemSelectedListener(this);
        mFlatExtraSpinner.setTag(NvestLibraryConfig.FLAT_EXTRA_SPINNER);
        mFlatExtraSpinner.setOnItemSelectedListener(this);
        mSelectFundStrategy.setTag(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION);
        mSelectFundStrategy.setOnItemSelectedListener(this);
        //productInformationDataViewModel.getOptions(productid);
        //productInformationDataViewModel.loadOptions(productid,7,"null,,,,,,","10","10","Option","4");
        /*loadOptionsList = productInformationDataViewModel.loadOptions(1016,3,"2,1,","15","1","Option","2");
        Gson gson = new GsonBuilder().serializeNulls().create();
        CommonMethod.log(TAG , "Final master option list size " + gson.toJson(loadOptionsList));*/
        addSpinnerToDynamicModel();
        addEditTextToDynamicModel();
        mBtnValidate.setOnClickListener(v -> {
            //validate()
            addDetailsForUnSelectedLevels();
            boolean isAllEditTextValidated = getDetailsFromStaticAndDynamicEditText();
            boolean isAllSpinnersValidated = getDetailsFromStaticAndDynamicSpinners();
            boolean isAllCheckBoxesValidated = getDetailsFromStaticAndDynamicCheckBoxes();
            if (isAllEditTextValidated && isAllSpinnersValidated && isAllCheckBoxesValidated) {
                CommonMethod.log(TAG, "All validations are over hence can start new screen");
                validate();
            } else {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.all_fields_with_red_are_mandatory), getActivity(), 0);

                CommonMethod.log(TAG, "Complete all validations");
            }

            //setDefaultValues();
        });
    }

    public void addSpinnerToDynamicModel() {
        DynamicSpinnerModel prptSpinnerModel = new DynamicSpinnerModel();
        prptSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_PT_ANNOTATION);
        prptSpinnerModel.setMaterialSpinner(mPTSpinner);
        //prptSpinnerModel.setVisible(false);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_PT_ANNOTATION, prptSpinnerModel);

        DynamicSpinnerModel pptSpinnerModel = new DynamicSpinnerModel();
        pptSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_PPT_ANNOTATION);
        pptSpinnerModel.setMaterialSpinner(mPPTSpinner);
        //pptSpinnerModel.setVisible(false);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_PPT_ANNOTATION, pptSpinnerModel);

        DynamicSpinnerModel mModeSpinnerModel = new DynamicSpinnerModel();
        mModeSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.INPUT_MODE_ANNOTATION);
        mModeSpinnerModel.setMaterialSpinner(mModeSpinner);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.INPUT_MODE_ANNOTATION, mModeSpinnerModel);

        DynamicSpinnerModel emrSpinnerModel = new DynamicSpinnerModel();
        emrSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
        emrSpinnerModel.setMaterialSpinner(mEMRSpinner);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION, emrSpinnerModel);

        DynamicSpinnerModel flatExtraSpinnerModel = new DynamicSpinnerModel();
        flatExtraSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION);
        flatExtraSpinnerModel.setMaterialSpinner(mFlatExtraSpinner);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION, flatExtraSpinnerModel);

        DynamicSpinnerModel productSumAssuredModel = new DynamicSpinnerModel();
        productSumAssuredModel.setDynamicFieldIdentifier(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
        productSumAssuredModel.setMaterialSpinner(mSelectSpinnerSA);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, productSumAssuredModel);

        DynamicSpinnerModel productmodalPremiumModel = new DynamicSpinnerModel();
        productmodalPremiumModel.setDynamicFieldIdentifier(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
        productmodalPremiumModel.setMaterialSpinner(mSelectSpinnerMP);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.MODAL_PREM_ANNOTATION, productmodalPremiumModel);

        DynamicSpinnerModel annualPremiumSpinnerModel = new DynamicSpinnerModel();
        annualPremiumSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
        annualPremiumSpinnerModel.setMaterialSpinner(mSelectSpinnerAP);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION, annualPremiumSpinnerModel);

        DynamicSpinnerModel monthlyIncomeSpinnerModel = new DynamicSpinnerModel();
        monthlyIncomeSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
        monthlyIncomeSpinnerModel.setMaterialSpinner(mSelectSpinnerMI);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION, monthlyIncomeSpinnerModel);

        DynamicSpinnerModel samfSpinnerModel = new DynamicSpinnerModel();
        samfSpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_SAMF_ANNOTATION);
        samfSpinnerModel.setMaterialSpinner(mSelectSpinnerSAMF);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.PR_SAMF_ANNOTATION, samfSpinnerModel);

        DynamicSpinnerModel fundStrategySpinnerModel = new DynamicSpinnerModel();
        fundStrategySpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION);
        fundStrategySpinnerModel.setMaterialSpinner(mSelectFundStrategy);
        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION, fundStrategySpinnerModel);


    }

    public void addEditTextToDynamicModel() {
        DynamicEditTextModel ptEditTextModel = new DynamicEditTextModel();
        ptEditTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_PT_ANNOTATION);
        ptEditTextModel.setEditText(mPTText);
        //ptEditTextModel.setVisible(true);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_PT_ANNOTATION, ptEditTextModel);

        DynamicEditTextModel pptTextModel = new DynamicEditTextModel();
        pptTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_PPT_ANNOTATION);
        pptTextModel.setEditText(mPPTText);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_PPT_ANNOTATION, pptTextModel);

        DynamicEditTextModel modeTextModel = new DynamicEditTextModel();
        modeTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.INPUT_MODE_ANNOTATION);
        modeTextModel.setEditText(mModeText);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.INPUT_MODE_ANNOTATION, modeTextModel);

        DynamicEditTextModel emrTextModel = new DynamicEditTextModel();
        emrTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION);
        emrTextModel.setEditText(mEMRText);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.EXTRA_MORTALITY_RATE_ANNOTATION, emrTextModel);

        DynamicEditTextModel flatextraTextModel = new DynamicEditTextModel();
        flatextraTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION);
        flatextraTextModel.setEditText(mFlatExtraText);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_FLAT_EXTRA_ANNOTATION, flatextraTextModel);

        DynamicEditTextModel saTextModel = new DynamicEditTextModel();
        saTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
        saTextModel.setEditText(mTextSA);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, saTextModel);

        DynamicEditTextModel mpTextModel = new DynamicEditTextModel();
        mpTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
        mpTextModel.setEditText(mTextMP);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.MODAL_PREM_ANNOTATION, mpTextModel);

        DynamicEditTextModel apTextModel = new DynamicEditTextModel();
        apTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
        apTextModel.setEditText(mTextAP);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION, apTextModel);

        DynamicEditTextModel miTextModel = new DynamicEditTextModel();
        miTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION);
        miTextModel.setEditText(mTextMI);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_MONTHLY_INCOME_ANNOTATION, miTextModel);

        DynamicEditTextModel samfTextModel = new DynamicEditTextModel();
        samfTextModel.setDynamicFieldIdentifier(NvestLibraryConfig.PR_SAMF_ANNOTATION);
        samfTextModel.setEditText(mTextSAMF);
        addStaticAndDynamicEditTextToMap(NvestLibraryConfig.PR_SAMF_ANNOTATION, samfTextModel);

    }

    private void validate() {
        HashMap<String, String> params = CommonMethod.getAllParamsFromGenericDTO();
        validateInformationDataViewModel.ValidateInput(params);
    }

    public void addDetailsForUnSelectedLevels() {
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        CommonMethod.log(TAG, "Size of dynamic spinner " + stringDynamicSpinnerModelTreeMap.size());
        for (int i = 0; i < mMasterOptionWithValuesList.size(); i++) {
            int level = mMasterOptionWithValuesList.get(i).getLevel();
            if (!stringDynamicEditTextModelTreeMap.containsKey(NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level)) {
                CommonMethod.log(TAG, "Option value not found against value " + level);
                CommonMethod.addDynamicKeyWordToGenericDTO(String.valueOf(NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level), NvestLibraryConfig.UNSELECTED_LEVEL_MINUS_999, NvestLibraryConfig.UNSELECTED_LEVEL_MINUS_999, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
            }
        }
        /*for (Map.Entry<String, DynamicSpinnerModel> entry : stringDynamicSpinnerModelTreeMap.entrySet()) {
            if(!entry.getValue().isVisible()){
                MaterialSpinner spinner = entry.getValue().getMaterialSpinner();
                CommonMethod.log(TAG , "Annotation " + entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.log(TAG , "Visible " + entry.getValue().isVisible());
                CommonMethod.log(TAG , "Level " + entry.getKey());
            }
        }*/
    }

    public boolean getDetailsFromStaticAndDynamicEditText() {
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean isCorrect = true;
        for (Map.Entry<String, DynamicEditTextModel> entry : stringDynamicEditTextModelTreeMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                CommonMethod.log(TAG, "Entry key " + entry.getKey());
                EditText editText = entry.getValue().getEditText();
                if (editText.getText().toString().isEmpty()) {
                    //editText.setBackgroundColor(Color.RED);
                    CommonMethod.isEmpty(getActivity(), editText, "");
                    isCorrect = false;
                }
                CommonMethod.log(TAG, "Info " + editText.getText().toString());
                CommonMethod.log(TAG, "Identifier " + entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), editText.getText().toString(), editText.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addCombinedProductsToGenericDTO(products.getProductId(), entry.getValue().getDynamicFieldIdentifier(), editText.getText().toString(), editText.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);

            }
        }
        return isCorrect;
    }

    public boolean getDetailsFromStaticAndDynamicSpinners() {
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean isCorrect = true;
        for (Map.Entry<String, DynamicSpinnerModel> entry : stringDynamicSpinnerModelTreeMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                MaterialSpinner spinner = entry.getValue().getMaterialSpinner();
                if (spinner.getText().toString().equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
                    CommonMethod.spinnerErrorAttributes(getActivity(), spinner);
                    isCorrect = false;
                }
                CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), getSpinnerKey(spinner), spinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.List), nameofCurrMethod);
                CommonMethod.addCombinedProductsToGenericDTO(products.getProductId(), entry.getValue().getDynamicFieldIdentifier(), getSpinnerKey(spinner), spinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.List), nameofCurrMethod);
                CommonMethod.log(TAG, "Annotation " + entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.log(TAG, "Spinner selection " + spinner.getText().toString());
                CommonMethod.log(TAG, "Visible " + entry.getValue().isVisible());
                CommonMethod.log(TAG, "Level " + entry.getKey());
            }
            else if(entry.getKey().contains(NvestLibraryConfig.PR_OPTION_ANNOTATION)) {
                CommonMethod.log(TAG , "Not visible key "+ entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), "0", "0", TAG, String.valueOf(NvestLibraryConfig.FieldType.List), nameofCurrMethod);
                CommonMethod.addCombinedProductsToGenericDTO(products.getProductId(), entry.getValue().getDynamicFieldIdentifier(), "0", "0", TAG, String.valueOf(NvestLibraryConfig.FieldType.List), nameofCurrMethod);
            }
        }
        return isCorrect;
    }

    public boolean getDetailsFromStaticAndDynamicCheckBoxes() {
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean isCorrect = true;
        for (Map.Entry<String, DynamicCheckBoxModel> entry : stringDynamicCheckBoxModelTreeMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                CommonMethod.log(TAG, "Yes check box is visible " + entry.getValue().getDynamicFieldIdentifier());
                CheckBox checkBox = entry.getValue().getCheckBox();
                if(checkBox.isChecked()){
                    CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), "1", "1", TAG, String.valueOf(NvestLibraryConfig.FieldType.CheckBox), nameofCurrMethod);
                    CommonMethod.addCombinedProductsToGenericDTO(products.getProductId(), entry.getValue().getDynamicFieldIdentifier(), "1", "1", TAG, String.valueOf(NvestLibraryConfig.FieldType.CheckBox), nameofCurrMethod);
                }
                else {
                    CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), "0", "0", TAG, String.valueOf(NvestLibraryConfig.FieldType.CheckBox), nameofCurrMethod);
                    CommonMethod.addCombinedProductsToGenericDTO(products.getProductId(), entry.getValue().getDynamicFieldIdentifier(), "0", "0", TAG, String.valueOf(NvestLibraryConfig.FieldType.CheckBox), nameofCurrMethod);
                }
                CommonMethod.log(TAG, "Checked " + checkBox.isChecked());
            }
        }
        return true;
    }


    public void startNextScreen() {
        // if the product has any rider start rider screen else start summary screen
        Bundle args = new Bundle();
        args.putParcelable("products", products);
        if (solutionProduct == null) {
            if (riderInformationModelArrayList.size() < 1) {
                // mListener.startSummaryFragment(products);
                Navigation.findNavController(view).navigate(R.id.action_productInformationFragment_to_summaryFragment, args);
            } else {
                // mListener.startRiderFragment(products);
                Navigation.findNavController(view).navigate(R.id.action_productInformationFragment_to_riderFragment, args);
            }
        } else {
            Navigation.findNavController(view).popBackStack();

            /*EventMessage eventMessage = new EventMessage();
            eventMessage.setProductIndexSolutionDetailsFragment(productIndex);
            eventMessage.setDefaultValuesSolutionDetailsFragment(defaultValues);
            eventMessage.setValid(true);
            EventBus.getDefault().post(eventMessage);*/
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        landingActivity = ((LandingActivity) getActivity());
    }

    @Override
    public void onStateCityListCompleted(boolean complete) {

    }

    @Override
    public void onStateListCompleted(boolean complete) {

    }

    @Override
    public void onCityListCompleted(boolean complete) {

    }

    @Override
    public void onKeywordsListObtained(boolean complete) {
        basicInformationDataViewModel.setKeyWordMutableLiveData().observe(this, new Observer<LinkedHashMap<String, Keyword>>() {
            @Override
            public void onChanged(@Nullable LinkedHashMap<String, Keyword> stringKeywordLinkedHashMap) {
                CommonMethod.log(TAG, "Putting values in key word map");
                keywordLinkedHashMap.putAll(stringKeywordLinkedHashMap);
                setDynamicView();
                setStaticView();
                setDefaultValues();
            }
        });
    }

    @Override
    public void onKeyValuePairListObtained(boolean complete) {
        if (complete) {
            basicInformationDataViewModel.setKeyValuePairMutableLiveData().observe(this, new Observer<Map<String, ArrayList<StringKeyValuePair>>>() {
                @Override
                public void onChanged(@Nullable Map<String, ArrayList<StringKeyValuePair>> stringArrayListMap) {
                    CommonMethod.log(TAG, "Putting values in key hash map");
                    keyvaluePairHashMap.putAll(stringArrayListMap);
                }
            });
        }
    }

    @Override
    public void onBasicInformationDetailCompleted(boolean complete) {

    }


    public void createOptionValuesByLevel(String pt, String ppt) {
        for (MasterOption masterOption : mMasterOptionWithValuesList) {
            if (masterOption.getLevel() == 1) {
                LoadNextOption loadNextOption = CommonMethod.createLoadNextOption(masterOption.getLevel(), products.getProductId(), "", pt, ppt, "PT");
                loadNextOptionLinkedHashMap.put(masterOption.getLevel(), loadNextOption);
            } else {
                String parentId = "";
               /* for (int i = 0; i < masterOption.getLevel() - 1; ++i) {
                    parentId = "," + parentId;
                }*/
                //CommonMethod.log(TAG , "Parent id " + parentId);
                LoadNextOption loadNextOption = CommonMethod.createLoadNextOption(masterOption.getLevel(), products.getProductId(), parentId, pt, ppt, "PT");
                loadNextOptionLinkedHashMap.put(masterOption.getLevel(), loadNextOption);
            }
        }
        loadOptionsFromStart();
    }


    public void refreshLoadOptionsByLevel(int level, LoadNextOption loadNextOption) {
        if (loadNextOptionLinkedHashMap.containsKey(level)) {
            CommonMethod.log(TAG, "Removing key since it is found at " + level);
            loadNextOptionLinkedHashMap.remove(level);
        }
        CommonMethod.log(TAG, "Load next option before put " + loadNextOption.getParentIdList());
        loadNextOptionLinkedHashMap.put(level, loadNextOption);
    }

    public void printLoadOptions() {
        for (Map.Entry<Integer, LoadNextOption> entry : loadNextOptionLinkedHashMap.entrySet()) {
            CommonMethod.log(TAG, "Starting to load options " + entry.getKey());
            LoadNextOption newLoadNextOption = entry.getValue();
            CommonMethod.log(TAG, "List of options " + newLoadNextOption.getParentIdList());
            CommonMethod.log(TAG, "Sender " + newLoadNextOption.getSender());
        }
    }

    public void loadOptionsFromStart() {
        for (Map.Entry<Integer, LoadNextOption> entry : loadNextOptionLinkedHashMap.entrySet()) {
            /*LoadNextOption current = entry.getValue();
            CommonMethod.log(TAG , "Parent id " + current.getParentIdList());
            CommonMethod.log(TAG , "Change level " + current.getChangelevel());
            CommonMethod.log(TAG , "level " + current.getOptionlevel());*/
            loadNewOptions(entry.getKey());
        }
    }

    public void getOptionsValuesAgainstALevel(int level, String optionId) {
        CommonMethod.log(TAG, "String value pair started..." + optionId + " and level " + level);
        if (optionId != null) {
            StringKeyValuePair stringKeyValuePair = productInformationDataViewModel.optionInputField(products.getProductId(), Integer.parseInt(optionId));
            removeViewFromDynamicLayout(level);
            CommonMethod.log(TAG, "Master options size " + mMasterOptionWithValuesList.size());
            if (stringKeyValuePair.getKey() != null && stringKeyValuePair.getValue() != null) {
                stringKeyValuePairTreeMap.put(level, stringKeyValuePair);
                switch (NvestLibraryConfig.FieldType.valueOf(stringKeyValuePair.getValue())) {
                    case DOB:
                        addViewForOptionSelected(level, stringKeyValuePair.getKey(), String.valueOf(NvestLibraryConfig.FieldType.DOB));
                        break;
                    case Number:
                        CommonMethod.log(TAG, "Handling Number");
                        addViewForOptionSelected(level, stringKeyValuePair.getKey(), String.valueOf(NvestLibraryConfig.FieldType.Number));
                        break;
                    case Output:

                        break;
                    case CheckBox:

                        break;
                    case String:
                        addViewForOptionSelected(level, stringKeyValuePair.getKey(), String.valueOf(NvestLibraryConfig.FieldType.String));
                        break;
                    case Disabled:
                        break;

                    default:
                        break;
                }
            } else {
                CommonMethod.log(TAG, "String value contents pair is null");
            }
        }
    }

    public void loadOptionsFromLevel(int level) {
        for (int i = level; i <= loadNextOptionLinkedHashMap.size(); i++) {
            LoadNextOption loadNextOption = loadNextOptionLinkedHashMap.get(i);
            CommonMethod.log(TAG, "Level " + loadNextOption.getOptionlevel());
            CommonMethod.log(TAG, "Parent id list  " + loadNextOption.getParentIdList());
            loadNewOptions(loadNextOption.getOptionlevel());
        }
    }

    public void addEditTextLayoutAtPosition(int position, String key, String type, int level) {
        /*EditText stringeditText1 = CommonMethod.createStringTypeEditText(getActivity(),key);
        mDynamicLayout.addView(stringeditText1,position);*/
        TextView textView = CommonMethod.createTextView(getActivity(), key);
        String fieldCaption = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level;
        textViewTreeMap.put(fieldCaption, textView);
        mDynamicLayout.addView(textView, position);
        switch (NvestLibraryConfig.FieldType.valueOf(type)) {
            case DOB:
                EditText dobeditText = CommonMethod.createDobTypeEditText(getActivity(), key);
                dobeditText.setOnClickListener(this);
                mDynamicLayout.addView(dobeditText, position + 1);
                DynamicEditTextModel dynamicEditTextModel = new DynamicEditTextModel();
                dynamicEditTextModel.setEditText(dobeditText);
                dynamicEditTextModel.setDynamicFieldIdentifier(fieldCaption);
                dynamicEditTextModel.setVisible(true);
                addStaticAndDynamicEditTextToMap(fieldCaption, dynamicEditTextModel);
                break;
            case String:
                EditText stringeditText = CommonMethod.createStringTypeEditText(getActivity(), key);
                mDynamicLayout.addView(stringeditText, position + 1);
                CommonMethod.log(TAG, "Handling string ");
                DynamicEditTextModel stringEditTextModel = new DynamicEditTextModel();
                stringEditTextModel.setEditText(stringeditText);
                stringEditTextModel.setDynamicFieldIdentifier(fieldCaption);
                stringEditTextModel.setVisible(true);
                addStaticAndDynamicEditTextToMap(fieldCaption, stringEditTextModel);
                break;
            case Number:
                EditText numbereditText = CommonMethod.createNumberTypeEditText(getActivity(), key);
                mDynamicLayout.addView(numbereditText, position + 1);
                DynamicEditTextModel numberEditTextModel = new DynamicEditTextModel();
                numberEditTextModel.setEditText(numbereditText);
                numberEditTextModel.setDynamicFieldIdentifier(fieldCaption);
                numberEditTextModel.setVisible(true);
                addStaticAndDynamicEditTextToMap(fieldCaption, numberEditTextModel);
                break;
            default:
                break;
        }
    }

    public void addViewForOptionSelected(int level, String key, String type) {
        final int childCount = mDynamicLayout.getChildCount();
        CommonMethod.log(TAG, "Child count " + childCount + " level " + level);
        //String elementKey = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level;
        String editTextKey = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level;
        String spinnerKey = NvestLibraryConfig.PR_OPTION_ANNOTATION + level;
        //if(stringDynamicSpinnerModelTreeMap.containsKey(String.valueOf(level)) && !stringDynamicEditTextModelTreeMap.containsKey(editTextKey)){
        if (stringDynamicSpinnerModelTreeMap.containsKey(spinnerKey) && !stringDynamicEditTextModelTreeMap.containsKey(editTextKey)) {
            for (int i = 0; i < childCount; i++) {
                View element = mDynamicLayout.getChildAt(i);
                if (element instanceof MaterialSpinner) {
                    MaterialSpinner materialSpinner = (MaterialSpinner) element;
                    //MaterialSpinner obtained = stringDynamicSpinnerModelTreeMap.get(String.valueOf(level)).getMaterialSpinner();
                    MaterialSpinner obtained = stringDynamicSpinnerModelTreeMap.get(spinnerKey).getMaterialSpinner();
                    CommonMethod.log(TAG, "Obtained Id " + obtained.getId());
                    CommonMethod.log(TAG, "Obtained TAG " + obtained.getTag());
                    CommonMethod.log(TAG, "Element id " + materialSpinner.getId());
                    CommonMethod.log(TAG, "Element tag " + materialSpinner.getTag());
                    //if(obtained.getId() == materialSpinner.getId() ){
                    if (obtained.getTag() == materialSpinner.getTag()) {
                        CommonMethod.log(TAG, "Inside if");
                        addEditTextLayoutAtPosition(i + 1, key, type, level);
                        break;
                    }
                }
            }
        }
    }

    public void replaceEditTextInDynamicLayout(String key) {
        CommonMethod.log(TAG, "Replace edit text key " + key);
        final int childCount = mDynamicLayout.getChildCount();
        CommonMethod.log(TAG, "Size " + stringDynamicEditTextModelTreeMap.size());
        for (int i = 0; i < childCount; i++) {
            View element = mDynamicLayout.getChildAt(i);
            if (element instanceof EditText) {
                EditText editText = (EditText) element;
                if (editText.getTag().equals(key)) {
                    editText.setEnabled(false);
                    editText.setFocusableInTouchMode(false);
                    editText.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorDisabled));
                    //DynamicEditTextModel dynamicEditTextModel = getEditTextFromTreeMapById(String.valueOf(editText.getTag()));
                    DynamicEditTextModel dynamicEditTextModel = stringDynamicEditTextModelTreeMap.get(String.valueOf(editText.getTag()));
                    if (dynamicEditTextModel != null) {
                        dynamicEditTextModel.setVisible(false);
                        String fieldCaption = dynamicEditTextModel.getDynamicFieldIdentifier();
                        stringDynamicEditTextModelTreeMap.remove(fieldCaption);
                        stringDynamicEditTextModelTreeMap.put(fieldCaption, dynamicEditTextModel);
                        break;
                    }
                }
            }
        }
    }

    public void removeViewFromDynamicLayout(int level) {
        if (stringKeyValuePairTreeMap.size() > 0) {
            if (stringKeyValuePairTreeMap.containsKey(level)) {
                int maxLevel = level;
                do {
                    CommonMethod.log(TAG, "Current value of max level " + maxLevel);
                    String elementkey = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + maxLevel;
                    CommonMethod.log(TAG, "Element key " + elementkey);
                    if (stringDynamicEditTextModelTreeMap.containsKey(elementkey)) {
                        EditText editText = stringDynamicEditTextModelTreeMap.get(elementkey).getEditText();
                        if (editText != null) {
                            mDynamicLayout.removeView(editText);
                            stringDynamicEditTextModelTreeMap.remove(elementkey);
                        }
                        TextView textView = textViewTreeMap.get(elementkey);
                        if (textView != null) {
                            mDynamicLayout.removeView(textView);
                            textViewTreeMap.remove(elementkey);
                        }
                        stringKeyValuePairTreeMap.remove(maxLevel);
                    }
                    maxLevel++;
                }
                while (stringKeyValuePairTreeMap.containsKey(maxLevel));
            }
        }
    }

    public void replaceView(int level, String key, String type) {
        final int childCount = mDynamicLayout.getChildCount();
        CommonMethod.log(TAG, "Child count " + childCount + " level " + level);
        CommonMethod.log(TAG, "Replace view key " + key);
        CommonMethod.log(TAG, "Spinner list " + stringDynamicSpinnerModelTreeMap.size());
        CommonMethod.log(TAG, "Edittext list " + stringDynamicEditTextModelTreeMap.size());

        for (int i = 0; i < childCount; i++) {
            View element = mDynamicLayout.getChildAt(i);
            if (element instanceof EditText) {
                EditText editText = (EditText) element;
                if (editText.getTag().equals(key)) {
                    //DynamicEditTextModel dynamicEditTextModel = getEditTextFromTreeMapById(String.valueOf(editText.getTag()));
                    DynamicEditTextModel dynamicEditTextModel = stringDynamicEditTextModelTreeMap.get(String.valueOf(editText.getTag()));
                    /*editText.setEnabled(true);
                    editText.setFocusableInTouchMode(true);
                    editText.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));*/
                    if (dynamicEditTextModel != null) {
                        dynamicEditTextModel.getEditText().setEnabled(true);
                        dynamicEditTextModel.getEditText().setFocusableInTouchMode(true);
                        dynamicEditTextModel.getEditText().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
                        dynamicEditTextModel.setVisible(true);
                        String fieldCaption = dynamicEditTextModel.getDynamicFieldIdentifier();
                        CommonMethod.log(TAG, "Field " + fieldCaption);
                        stringDynamicEditTextModelTreeMap.remove(fieldCaption);
                        stringDynamicEditTextModelTreeMap.put(fieldCaption, dynamicEditTextModel);
                        break;
                    }
                }
            }
            if (element instanceof MaterialSpinner) {
                MaterialSpinner materialSpinner = (MaterialSpinner) element;
                CommonMethod.log(TAG, "Material spinner tag " + materialSpinner.getTag());
                //DynamicSpinnerModel obtained = stringDynamicSpinnerModelTreeMap.get(String.valueOf(level));
                DynamicSpinnerModel obtained = stringDynamicSpinnerModelTreeMap.get(String.valueOf(level));
                if (obtained != null) {
                    CommonMethod.log(TAG, "Obtained spinner tag " + obtained.getMaterialSpinner().getTag());
                    if (materialSpinner.getTag().equals(obtained.getMaterialSpinner().getTag())) {
                        stringDynamicSpinnerModelTreeMap.remove(String.valueOf(level));
                        /*View namebar = mDynamicLayout.findViewById(materialSpinner.getId());
                        ((ViewGroup) namebar.getParent()).removeView(namebar);*/
                        mDynamicLayout.removeView(materialSpinner);
                        EditText stringeditText = CommonMethod.createStringTypeEditText(getActivity(), key);
                        mDynamicLayout.addView(stringeditText, i);
                        CommonMethod.log(TAG, "Handling string ");
                        DynamicEditTextModel stringEditTextModel = new DynamicEditTextModel();
                        String stringidentifier = NvestLibraryConfig.OPTION_VALUE_ANNOTATION + level;
                        stringEditTextModel.setEditText(stringeditText);
                        stringEditTextModel.setDynamicFieldIdentifier(stringidentifier);
                        stringEditTextModel.setVisible(true);
                        addStaticAndDynamicEditTextToMap(key, stringEditTextModel);
                        CommonMethod.log(TAG, "Inside if");
                        break;
                    }
                }

            }

        }
    }

    public DynamicEditTextModel getEditTextFromTreeMapById(String elementTag) {
        if (elementTag != null) {
            for (Map.Entry<String, DynamicEditTextModel> entry : stringDynamicEditTextModelTreeMap.entrySet()) {
                EditText editText = entry.getValue().getEditText();
                if (editText != null && editText.getTag().equals(elementTag)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public DynamicSpinnerModel getMaterialSpinnerFromTreeMapById(int elementid) {
        for (Map.Entry<String, DynamicSpinnerModel> entry : stringDynamicSpinnerModelTreeMap.entrySet()) {
            if (entry.getValue().getMaterialSpinner().getId() == elementid) {
                return entry.getValue();
            }
        }

        return null;
    }

    public void loadNewOptions(int level) {
        LoadNextOption loadNextOption = loadNextOptionLinkedHashMap.get(level);
        //newOptionsList = new LinkedHashMap<>();
        CommonMethod.log(TAG, "load new options at " + level + " option id list " + loadNextOption.getOptionlevel());
        Map<Integer, KeyValuePair> newOptions = productInformationDataViewModel.loadOptions(products.getProductId(), loadNextOption.getOptionlevel(), loadNextOption.getParentIdList(), loadNextOption.getPt(), loadNextOption.getPpt(), loadNextOption.getSender(), loadNextOption.getChangelevel());
        //if (newOptions != null && newOptions.size() > 0) {
        if (newOptions != null) {
            CommonMethod.log(TAG, "New option size " + newOptions.size());
            if (spinnerLinkedHashMap.containsKey(level)) {
                MaterialSpinner spinner = spinnerLinkedHashMap.get(level);
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                stringKeyValuePair.setKey("-1");
                stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                ListSpinner.add(stringKeyValuePair);
                MasterOption masterOption = mMasterOptionWithValuesList.get(level - 1);
                replaceEditTextInDynamicLayout(masterOption.getLevelName());
                for (Map.Entry<Integer, KeyValuePair> entry : newOptions.entrySet()) {
                    //CommonMethod.log(TAG , "New option " + entry.getValue().getValue());
                    if (entry.getValue().getKey() != null && entry.getValue().getValue() != null && !entry.getValue().getValue().isEmpty()) {
                        stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(entry.getValue().getKey()));
                        stringKeyValuePair.setValue(entry.getValue().getValue());
                        CommonMethod.log(TAG, "New option Key " + entry.getValue().getKey());
                        CommonMethod.log(TAG, "New option value " + entry.getValue().getValue());
                        //newOptionsList.put(entry.getValue().getValue(), String.valueOf(entry.getValue().getKey()));
                        optionListTreeMap.put(level, stringKeyValuePair);
                        ListSpinner.add(stringKeyValuePair);
                    } else if (entry.getValue().getKey() != null && (entry.getValue().getValue() == null || entry.getValue().getValue().isEmpty())) {
                        replaceView(level, masterOption.getLevelName(), String.valueOf(NvestLibraryConfig.FieldType.String));
                    }
                }
                if (ListSpinner.size() > 1) {
                    //updateDynamicSpinnerVisibility(String.valueOf(level), true, ListSpinner);
                    updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_OPTION_ANNOTATION+level, true, ListSpinner);
                } else {
                    updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_OPTION_ANNOTATION+level, false, null);
                }
                spinner.setOnItemSelectedListener(this);
                CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), spinner, ListSpinner);
            } else {
                CommonMethod.log(TAG, "Do not have spinner key at level " + level);
            }
        } else {
            CommonMethod.log(TAG, "New options is null");
        }
    }

    @Override
    public void onClick(View view) {
        CommonMethod.log(TAG, "View tag " + view.getTag());
        EditText dobEditText = (EditText) view;
        CommonMethod.log(TAG, "Input type " + dobEditText.getInputType());
        if (InputType.TYPE_DATETIME_VARIATION_DATE == dobEditText.getInputType()) {
            setDateField(dobEditText);
        }
        /*DynamicEditTextModel dynamicEditTextModel = stringDynamicEditTextModelTreeMap.get(view.getTag());
        setDateField(dynamicEditTextModel.getEditText());*/
    }


    private void setDateField(final EditText datePickerText) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        CommonMethod.setEditTextSelectedAttributes(getActivity(), datePickerText);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.AppDatePickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            try {
                                String text = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                datePickerText.setText("" + CommonMethod.convertToAge(text));
                                CommonMethod.log(TAG, "print here " + text);
                            } catch (Exception e) {
                                CommonMethod.log(TAG, "Exception " + e.toString());
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @Override
    public void getPtlistcompleted(boolean complete) {
        productInformationDataViewModel.setKeyValuePairMutableLiveData().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(@Nullable Map<String, String> stringStringMap) {
                CommonMethod.log(TAG, "Setting values in pt list " + stringStringMap.size());
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                TreeMap<String, String> keyValuePairLinkHashMap = new TreeMap<>(Collections.reverseOrder());
                if (stringStringMap.size() > 0) {
                    ptLabel.setVisibility(View.VISIBLE);
                    mPTSpinner.setVisibility(View.VISIBLE);
                    mPTSpinner.setError(null);
                    StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey("-1");
                    keyValuePairLinkHashMap.put(NvestLibraryConfig.SELECT_OPTION, "-1");
                    stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                    ListSpinner.add(stringKeyValuePair);
                    for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                        if (entry.getValue() != null) {
                            stringKeyValuePair = new StringKeyValuePair();
                            stringKeyValuePair.setKey(entry.getKey());
                            stringKeyValuePair.setValue(String.valueOf(entry.getValue()));
                            //stringKeyValuePair.add(entry.getKey(),entry.getValue());
                            keyValuePairLinkHashMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                            ListSpinner.add(stringKeyValuePair);
                        }
                    }

                    /*for (Map.Entry<String, String> entry: keyValuePairLinkHashMap.entrySet()){
                        CommonMethod.log(TAG , "Reverse order " + entry.getKey());
                        CommonMethod.log(TAG , "Reverse order value " + entry.getValue());
                    }*/
                    if (ListSpinner.size() > 1) {
                        updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_PT_ANNOTATION, true, ListSpinner);
                    }
                    //CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mPTSpinner, ListSpinner);
                    int positionFetched = CommonMethod.setMaterialSpinnerAdapterUpdated(TAG, getActivity(), mPTSpinner, ListSpinner, defaultValues, NvestLibraryConfig.PR_PT_ANNOTATION);
                    if (positionFetched != -1) {
                        String keyFromSpinner = getSpinnerKeyNew(mPTSpinner, positionFetched);
                        boolean flag = CommonMethod.checkIfStringIsNumber(keyFromSpinner);
                        if (flag) {
                            productInformationDataViewModel.getPtPPTMaster(products.getProductId(), keyFromSpinner);
                        } else {
                            productInformationDataViewModel.getPtPPTMaster(products.getProductId(), "0");
                        }
                    }
                }

            }
        });

    }


    public JSONObject jsonString(String jsonKey, String jsonValue) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("demo", "Value Against a demo");
            return jsonObject;
        } catch (Exception e) {
            CommonMethod.log(TAG, "Json exception " + e.toString());
        }
        return null;
    }

    @Override
    public void getOutputPtlistcompleted(boolean complete) {
        productInformationDataViewModel.setPtOutputLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String value) {
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                //Creating hint for user
                if (value != null && !value.isEmpty()) {
                    ptLabel.setVisibility(View.VISIBLE);
                    mPTSpinner.setVisibility(View.VISIBLE);
                    StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey("-1");
                    stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                    ListSpinner.add(stringKeyValuePair);
                    stringKeyValuePair = new StringKeyValuePair();

                    stringKeyValuePair.setKey("key");
                    stringKeyValuePair.setValue(value);
                    ListSpinner.add(stringKeyValuePair);
                    CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mPTSpinner, ListSpinner);
                }
            }
        });
    }

    @Override
    public void getRiderlist(boolean complete) {
        productInformationDataViewModel.setFundsMutableList().observe(this, new Observer<List<FundsModel>>() {
            @Override
            public void onChanged(@Nullable List<FundsModel> fundsList) {
                if (fundsList != null && fundStrategyModel != null && fundStrategyModel.isInput()) {
                    CommonMethod.log(TAG, "Fund list " + fundsList.size());
                    if (fundsList.size() > 0) {
                        mRecyclerFundLayout.setVisibility(View.VISIBLE);
                        mLayoutFunds.setVisibility(View.VISIBLE);
                        mTextFund.setVisibility(View.VISIBLE);
                        mSeperator.setVisibility(View.VISIBLE);
                        mTotalLayout.setVisibility(View.VISIBLE);
                        fundsAdapter = new FundsAdapter(getActivity(), fundsList);
                        fundsAdapter.setFundsValueListener(ProductInformationFragment.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        mFundsRecyclerView.setLayoutManager(mLayoutManager);
                        mFundsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mFundsRecyclerView.setAdapter(fundsAdapter);
                    }
                } else {
                    mRecyclerFundLayout.setVisibility(View.GONE);
                    mLayoutFunds.setVisibility(View.GONE);
                    mTextFund.setVisibility(View.GONE);
                    mSeperator.setVisibility(View.GONE);
                    mTotalLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void getFundStrategyList(boolean complete) {
        productInformationDataViewModel.setFundStrategyMutableLiveData().observe(this, new Observer<List<FundStrategyModel>>() {
            @Override
            public void onChanged(@Nullable List<FundStrategyModel> fundStrategyModelsList) {
                if (fundStrategyModelsList != null) {
                    fundStrategyList.clear();
                    fundStrategyList.addAll(fundStrategyModelsList);
                    int numberofFundStrategies = fundStrategyModelsList.size();
                    if (numberofFundStrategies > 1) {
                        DynamicSpinnerModel fundStrategySpinnerModel = new DynamicSpinnerModel();
                        fundStrategySpinnerModel.setDynamicFieldIdentifier(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION);
                        fundStrategySpinnerModel.setMaterialSpinner(mSelectFundStrategy);
                        addStaticAndDynamicSpinnerToMap(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION, fundStrategySpinnerModel);
                        CommonMethod.log(TAG, "Fund strategy model size received  " + fundStrategyModelsList.size());
                        Collections.sort(fundStrategyModelsList);
                        fundstrategyLabel.setVisibility(View.VISIBLE);
                        mSelectFundStrategy.setVisibility(View.VISIBLE);

                        // add items in spinner
                        List<StringKeyValuePair> fundStrategyList = new ArrayList<>();
                        StringKeyValuePair defaultSelect = new StringKeyValuePair();
                        defaultSelect.setKey("-1");
                        defaultSelect.setValue(NvestLibraryConfig.SELECT_OPTION);
                        fundStrategyList.add(defaultSelect);

                        for (FundStrategyModel fundStrategyModel : fundStrategyModelsList) {
                            StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                            stringKeyValuePair.setKey(String.valueOf(fundStrategyModel.getProductId()));
                            stringKeyValuePair.setValue(fundStrategyModel.getStrategyName());
                            fundStrategyList.add(stringKeyValuePair);
                        }

                        CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mSelectFundStrategy, fundStrategyList);
                        if (fundStrategyList.size() > 1) {
                            updateDynamicSpinnerVisibility(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION, true, fundStrategyList);
                        }
                    } else if (numberofFundStrategies == 1) {
                        fundStrategyModel = fundStrategyModelsList.get(0);
                        productInformationDataViewModel.getFunds(products.getProductId());
                    }
                }
            }
        });
    }

    @Override
    public void getPtPptmasterlist(boolean complete) {
        productInformationDataViewModel.setPtPptMutableLiveData().observe(this, new Observer<Map<String, String>>() {
            @Override
            public void onChanged(@Nullable Map<String, String> stringStringMap) {
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                CommonMethod.log(TAG, "PPT list fetching size " + stringStringMap.size());
                if (!stringStringMap.isEmpty()) {
                    pptLabel.setVisibility(View.VISIBLE);
                    mPPTSpinner.setVisibility(View.VISIBLE);
                    //stringList.add(NvestLibraryConfig.SELECT_OPTION);
                    StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey("-1");
                    stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
                    ListSpinner.add(stringKeyValuePair);
                    for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                        if (entry.getValue() != null) {
                            stringKeyValuePair = new StringKeyValuePair();
                            stringKeyValuePair.setKey(String.valueOf(entry.getKey()));
                            stringKeyValuePair.setValue(entry.getValue());
                            ListSpinner.add(stringKeyValuePair);
                        }
                    }
                    if (ListSpinner.size() > 1) {
                        updateDynamicSpinnerVisibility(NvestLibraryConfig.PR_PPT_ANNOTATION, true, ListSpinner);
                    }
                    //CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mPPTSpinner, ListSpinner);
                    CommonMethod.setMaterialSpinnerAdapterUpdated(TAG, getActivity(), mPPTSpinner, ListSpinner, defaultValues, NvestLibraryConfig.PR_PPT_ANNOTATION);

                }
            }
        });
    }


    public void loadPtValue(String parentid, String optionvalue) {
        //productInformationDataViewModel.loadPtValue(products.getProductId(),Integer.parseInt(mPPTSpinner.getText().toString()), parentid,optionvalue);
        //productInformationDataViewModel.loadPtValue(1001,10, "2,7,10","," );
    }

    public Map<String, ArrayList<StringKeyValuePair>> getInputModeMasters() {
        Map<String, ArrayList<StringKeyValuePair>> stringArrayListMap;
        ArrayList<StringKeyValuePair> keyValuePairArrayList = new ArrayList<>();
        //Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().getModeMasters(products.getProductId());
        Cursor cursor = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteQuery("Select modeid, modename from ModeMaster where modeid in (Select modeid from ProductMode where productId = "+products.getProductId()+")");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    StringKeyValuePair keyValuePair = new StringKeyValuePair();
                    keyValuePair.setKey(cursor.getString(0));
                    keyValuePair.setValue(cursor.getString(1));
                    keyValuePairArrayList.add(keyValuePair);
                }
                while (cursor.moveToNext());
            }
        }
        keyvaluePairHashMap.put(NvestLibraryConfig.INPUT_MODE_ANNOTATION, keyValuePairArrayList);
        return keyvaluePairHashMap;
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        CommonMethod.log(TAG, "Inside on item selcted material spinner");
        view.setError(null);
        /*String selectValue = view.getText().toString();
        if(selectValue.equals(NvestLibraryConfig.SELECT_OPTION)){
           return;
        }*/
        if (view.getTag().equals(mPTSpinner.getTag())) {
            CommonMethod.log(TAG, "pt spinner is clicked ");
            if (CommonMethod.checkIfStringIsNumber(getSpinnerKey(mPTSpinner))) {
                productInformationDataViewModel.getPtPPTMaster(products.getProductId(), getSpinnerKey(mPTSpinner));
            } else {
                productInformationDataViewModel.getPtPPTMaster(products.getProductId(), "0");
            }
        } else if (view.getTag().equals(mPPTSpinner.getTag())) {
            CommonMethod.log(TAG, "PPT spinner is clicked");
            if (CommonMethod.checkIfStringIsNumber(mPTSpinner.getText().toString())) {
                createOptionValuesByLevel(mPTSpinner.getText().toString(), mPPTSpinner.getText().toString());
            } else {
                createOptionValuesByLevel("0", mPPTSpinner.getText().toString());
            }

        } else if (view.getTag().equals(mSelectFundStrategy.getTag())) {
            int selectedIndex = mSelectFundStrategy.getSelectedIndex();
            if (selectedIndex != 0) {
                FundStrategyModel selectedFundStrategy = fundStrategyList.get(selectedIndex - 1);
                fundStrategyModel = selectedFundStrategy;
                productInformationDataViewModel.getFunds(products.getProductId());
            } else {
                fundStrategyModel = null;
                productInformationDataViewModel.getFunds(-1);
            }
        } else {
            CommonMethod.log(TAG, "Clicked on ");
            for (Map.Entry<Integer, MaterialSpinner> entry : spinnerLinkedHashMap.entrySet()) {
                CommonMethod.log(TAG, "Tag added " + entry.getValue().getTag());
                if (view.getTag().toString().equals(entry.getValue().getTag())) {
                    CommonMethod.log(TAG, "Inside the parent tag position selected " + (position + 1));
                    int level = Integer.parseInt(view.getTag().toString());
                    MaterialSpinner spinner = entry.getValue();
                    CommonMethod.setSpinnerSelectedAttributes(getActivity(), spinner);
                    CommonMethod.log(TAG, "Spinner value " + spinner.getText() + " position " + position);
                    //CommonMethod.log(TAG, "Option value " +  getSpinnerKey(spinner));
                    String optionIdSelected = getSpinnerKey(spinner);
                    //CommonMethod.log(TAG, "New option list id " +  newOptionsList.get(spinner.getText()));
                    treeMapOptionItemSelected.put(String.valueOf(level), optionIdSelected);
                    String parentId = getParentIdStringList(level);
                    for (int i = level + 1; i <= loadNextOptionLinkedHashMap.size(); i++) {
                        LoadNextOption loadNextOption = loadNextOptionLinkedHashMap.get(i);
                        loadNextOption.setSender("option");
                        //parentId = parentId + ",";
                        CommonMethod.log(TAG, "Parent id new " + parentId + " Value of i " + i);
                        loadNextOption.setParentIdList(parentId);
                        refreshLoadOptionsByLevel(i, loadNextOption);
                    }
                    //printLoadOptions();
                    getOptionsValuesAgainstALevel(level, optionIdSelected);
                    loadOptionsFromLevel((level + 1));
                    break;
                }
            }
        }

    }

    public String getSpinnerKey(MaterialSpinner mSpinner) {
        String Key = "0";
        CommonMethod.log(TAG, "Selected index " + mSpinner.getSelectedIndex());
        try {
            Key = ((StringKeyValuePair) mSpinner.getItems().get(mSpinner.getSelectedIndex())).getKey();
        } catch (Exception ex) {
            Key = "0";
        }
        return Key;
    }

    public String findSpinnerKey(MaterialSpinner mSpinner) {
        String Key = "0";
        CommonMethod.log(TAG, "Selected index " + mSpinner.getSelectedIndex());
        try {
            Key = ((StringKeyValuePair) mSpinner.getItems().get(mSpinner.getSelectedIndex())).getKey();
        } catch (Exception ex) {
            Key = "0";
        }
        return Key;
    }

    public String getSpinnerKeyNew(MaterialSpinner mSpinner, int selectedIndexPosition) {
        String Key = "0";
        CommonMethod.log(TAG, "Selected index at new " + mSpinner.getSelectedIndex());
        try {
            Key = ((StringKeyValuePair) mSpinner.getItems().get(selectedIndexPosition)).getKey();
        } catch (Exception ex) {
            Key = "0";
        }
        return Key;
    }

    public String getParentIdStringList(int level) {
        String listString = "";
        for (Map.Entry<String, String> entry : treeMapOptionItemSelected.entrySet()) {
            if (Integer.parseInt((entry.getKey())) <= level) {
                listString = listString + entry.getValue() + ",";
            }
        }
        return listString;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CommonMethod.log(TAG, "Frame layout touch event found");
        landingActivity.hideKeyboard(v);
        return false;
    }

    @Override
    public void getRider(boolean complete) {
        riderInformationDataViewModel.setRiderInformationMutableLiveData().observe(this, new Observer<ArrayList<RiderInformationModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<RiderInformationModel> riderInformationModels) {
                if (riderInformationModels != null) {
                    CommonMethod.log(TAG, "Size obtained " + riderInformationModels.size());
                    riderInformationModelArrayList.clear();
                    riderInformationModelArrayList.addAll(riderInformationModels);
                }
            }
        });
    }

    @Override
    public void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData) {
        String currentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        if (validationIpLiveData != null) {
            ValidationIP validateInput = validationIpLiveData.getValue();
            GenericDTO.addAttribute(NvestLibraryConfig.VALIDATION_IP, validateInput);
            HashMap<String, String> errorMessages = validateInput.getErrorMessage();
            if (errorMessages != null && errorMessages.size() > 0) {
                CommonMethod.showErrorsList(getContext(), getActivity(), errorMessages);
            } else if (products.isUlip() && fundStrategyList.size() > 1 && fundStrategyModel == null) {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.select_at_least_one_fund_strategy), getActivity(), 0);
            } else if (products.isUlip() && fundStrategyModel != null && fundStrategyModel.isInput() && totalFundPercent != 100) {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.total_fund_percent_error), getActivity(), 0);
            } else {
                if (fundStrategyModel != null) {
                    if (!fundStrategyModel.isInput()) {
                        // remove unnecessary fund ids from
                        GenericDTO.removeAllDynamicParamsStartsWith(NvestLibraryConfig.FUND_ID_ANNOTATION);
                    }
                    // add fund strategy id in generic dto
                    CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.FUND_STRATEGY_ID_ANNOTATION, String.valueOf(fundStrategyModel.getStrategyId()), String.valueOf(fundStrategyModel.getStrategyName()), TAG, String.valueOf(NvestLibraryConfig.FieldType.List), currentMethod);
                }
                startNextScreen();
            }
        }
    }

    @Override
    public void onCompleteRidersValidation(MutableLiveData<List<ValidationIP>> validIpListLiveData) {
        // ignore
    }

    @Override
    public void onBiGenerated(LinkedHashMap<Integer, HashMap<String, String>> biData) {
        // ignore
    }

    @Override
    public void onBiUlipGenerated(LinkedHashMap<Integer, HashMap<String, String>>[] biData) {
        // ignore
    }


    @Override
    public void totalValueChanged(int totalFundPercentage) {
        totalFundPercent = totalFundPercentage;
        mTextFundsSum.setText(totalFundPercentage + "%");
        CommonMethod.log(TAG, "TOTAL FUND PERCENTAGE => " + totalFundPercentage + " %");
    }


    private void setDefaultValues() {
        // prefill values in spinners
        /*for (Map.Entry<String, DynamicSpinnerModel> entry : stringDynamicSpinnerModelTreeMap.entrySet()) {
            DynamicSpinnerModel dynamicSpinnerModel = entry.getValue();
            if (dynamicSpinnerModel != null && dynamicSpinnerModel.isVisible()) {
                if(dynamicSpinnerModel.getDynamicFieldIdentifier().equals(NvestLibraryConfig.PR_PPT_ANNOTATION) || dynamicSpinnerModel.getDynamicFieldIdentifier().equals(NvestLibraryConfig.PR_PPT_ANNOTATION) ){
                    return;
                }
                String  keywordName = dynamicSpinnerModel.getDynamicFieldIdentifier();
                String tempKeyword1 = null;
                String tempKeyword2 = null;
                if (!keywordName.contains("DISPLAY")) {
                    tempKeyword1 = "@DISPLAY_"+keywordName.substring(1);
                } else {
                    tempKeyword2 = keywordName + "_DISPLAY";
                }
                CommonMethod.log(TAG , " 1 " + tempKeyword1 + " 2 " + tempKeyword2);
                if(defaultValues.containsKey(tempKeyword1) || defaultValues.containsKey(tempKeyword2) ){
                    CommonMethod.log(TAG , "Doing 1 " + defaultValues.get(tempKeyword1) + " Keyword spinner " + keywordName );
                    CommonMethod.log(TAG , "Doing 2 " + defaultValues.get(tempKeyword2)  + " Keyword spinner " + keywordName);
                }
                MaterialSpinner spinner = dynamicSpinnerModel.getMaterialSpinner();
                spinner.setItems(dynamicSpinnerModel.getSpinnerItemList());
                if (spinner != null) {
                    String value = GenericDTO.getAttributeValue(dynamicSpinnerModel.getDynamicFieldIdentifier());
                    if (value != null) {
                        CommonMethod.log(TAG , "Found Value " + value + " Spinner value " + getSpinnerKey(spinner));
                        CommonMethod.setDefaultValue(value, spinner);
                    }
                }
            }
        }*/

        // prefill values in edittexts
        /*for (Map.Entry<String, DynamicEditTextModel> entry : stringDynamicEditTextModelTreeMap.entrySet()) {
            DynamicEditTextModel dynamicEditTextModel = entry.getValue();
            if (dynamicEditTextModel != null && dynamicEditTextModel.isVisible()) {
                EditText editText = dynamicEditTextModel.getEditText();
                if (editText != null) {
                    String value = GenericDTO.getAttributeValue(dynamicEditTextModel.getDynamicFieldIdentifier());
                    if (value != null) {
                        try {
                            value = BigDecimal.valueOf(Double.parseDouble(value)).toPlainString();
                            CommonMethod.setDefaultValue(value, editText);
                        } catch (NumberFormatException e) {
                            CommonMethod.setDefaultValue(value, editText);
                        }

                    }
                }
            }
        }*/

        // pre-check checkboxes
        /*for (Map.Entry<String, DynamicCheckBoxModel> entry : stringDynamicCheckBoxModelTreeMap.entrySet()) {
            DynamicCheckBoxModel dynamicCheckBoxModel = entry.getValue();
            if (dynamicCheckBoxModel != null && dynamicCheckBoxModel.isVisible()) {
                CheckBox checkBox = dynamicCheckBoxModel.getCheckBox();
                if (checkBox != null) {
                    String value = GenericDTO.getAttributeValue(dynamicCheckBoxModel.getDynamicFieldIdentifier());
                    if (value != null) {
                        checkBox.setChecked(value.equalsIgnoreCase("true"));
                    }
                }
            }
        }*/

        /*for (Map.Entry<String, String> currentValue : defaultValues.entrySet()){
            String  keywordName = currentValue.getKey();
            String tempKeyword1 = null;
            String tempKeyword2 = null;
            if (!keywordName.contains("DISPLAY")) {
                tempKeyword1 = "@DISPLAY_"+keywordName.substring(1);
            } else {
                tempKeyword2 = keywordName + "_DISPLAY";
            }
            CommonMethod.log(TAG , " 1 " + tempKeyword1 + " 2 " + tempKeyword2);
        }*/
        if (defaultValues != null) {
            for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
                String keyword = entry.getKey();
                CommonMethod.log(TAG, "Checking for key " + keyword);
                if (keyword.contains(NvestLibraryConfig.PR_PPT_ANNOTATION)) {
                    CommonMethod.log(TAG, "Found PPT annotation in default values");
                    String tempPt = defaultValues.get(NvestLibraryConfig.PR_PT_ANNOTATION);
                    String tempPPt = defaultValues.get(NvestLibraryConfig.PR_PPT_ANNOTATION);
                    CommonMethod.log(TAG, "Temp pt " + tempPt);
                    CommonMethod.log(TAG, "Temp ppt " + tempPPt);
                    createOptionValuesByLevel(tempPt, tempPPt);
                }
                if (keyword.contains(NvestLibraryConfig.OPTIONS_ANNOTATION)) {
                    String[] option = entry.getValue().split(",");
                    for (int i = 0; i < option.length; i++) {
                        String key = NvestLibraryConfig.PR_OPTION_ANNOTATION + (i + 1);
                        if (setValueToSpinner(key, option[i])) {
                            int level = i + 1;
                            String optionIdSelected = option[i];
                            //CommonMethod.log(TAG, "New option list id " +  newOptionsList.get(spinner.getText()));
                            treeMapOptionItemSelected.put(String.valueOf(level), option[i]);
                            String parentId = getParentIdStringList(level);
                            for (int j = level + 1; j <= loadNextOptionLinkedHashMap.size(); j++) {
                                LoadNextOption loadNextOption = loadNextOptionLinkedHashMap.get(j);
                                loadNextOption.setSender("option");
                                //parentId = parentId + ",";
                                CommonMethod.log(TAG, "Parent id new " + parentId + " Value of j " + j);
                                loadNextOption.setParentIdList(parentId);
                                refreshLoadOptionsByLevel(j, loadNextOption);
                            }
                            //printLoadOptions();
                            getOptionsValuesAgainstALevel(level, optionIdSelected);
                            loadOptionsFromLevel((level + 1));
                        }
                    }
                } else {
                    setValueToSpinner(keyword, entry.getValue());
                }
            }
            //Edit text
            for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
                String keyword = entry.getKey();
                if (stringDynamicEditTextModelTreeMap.containsKey(keyword)) {
                    CommonMethod.log(TAG, "Edit text found");
                    DynamicEditTextModel dynamicEditTextModel = stringDynamicEditTextModelTreeMap.get(keyword);
                    if (dynamicEditTextModel != null && dynamicEditTextModel.isVisible()) {
                        EditText editText = dynamicEditTextModel.getEditText();
                        if (editText != null) {
                            //String value = GenericDTO.getAttributeValue(dynamicEditTextModel.getDynamicFieldIdentifier());
                            String value = entry.getValue();
                            if (value != null) {
                                try {
                                    value = BigDecimal.valueOf(Double.parseDouble(value)).toPlainString();
                                    CommonMethod.setDefaultValue(value, editText);
                                } catch (NumberFormatException e) {
                                    CommonMethod.setDefaultValue(value, editText);
                                }

                            }
                        }
                    }
                }
            }
            if (fundsAdapter != null) {
                fundsAdapter.setDefaultFundsValue(defaultValues);
            } else {
                CommonMethod.log(TAG, "Adapter  is null");
            }
        }


    }


    private boolean setValueToSpinner(String found, String value) {
        if (stringDynamicSpinnerModelTreeMap.containsKey(found)) {
            DynamicSpinnerModel dynamicSpinnerModel = stringDynamicSpinnerModelTreeMap.get(found);
            TreeMap<String, String> spinneritems = dynamicSpinnerModel.getSpinnerItemList();
            CommonMethod.log(TAG, "Spinner item size " + spinneritems.size());
                /*for (Map.Entry<String, String> stringEntry : spinneritems.entrySet()){
                    CommonMethod.log(TAG , "Found key " + stringEntry.getKey());
                    CommonMethod.log(TAG , "Entry Value " + entry.getValue()
                    );*/


            if (spinneritems.containsKey(value)) {
                if (CommonMethod.checkIfStringIsNumber(value)) {
                    int position = spinneritems.headMap(value).size();
                    CommonMethod.log(TAG, "I have found the position " + position);
                    dynamicSpinnerModel.getMaterialSpinner().setSelectedIndex(position);
                }
                CommonMethod.log(TAG, "Found key " + spinneritems.get(value));
                CommonMethod.setDefaultValue(spinneritems.get(value), dynamicSpinnerModel.getMaterialSpinner());
                CommonMethod.setSpinnerSelectedAttributes(getActivity(), dynamicSpinnerModel.getMaterialSpinner());
                return true;
            }
            /*for (Map.Entry<String, String> spinnerValue: spinneritems.entrySet() ){
                if (spinnerValue.getValue().equalsIgnoreCase(entry.getValue())){
                    CommonMethod.log(TAG , "Found key " + spinnerValue.getValue());
                    CommonMethod.setDefaultValue(spinnerValue.getValue(), dynamicSpinnerModel.getMaterialSpinner());
                    break;
                }
            }*/
        } else {
            return false;
        }
        return false;
    }

}
