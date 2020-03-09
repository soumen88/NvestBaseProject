package nvest.com.nvestlibrary.needbasedanalyser.education;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestCustomScrollView;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestWebModel.DynamicSpinnerModel;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;

import static nvest.com.nvestlibrary.commonMethod.CommonMethod.getSpinnerKey;


public class EducationFragment extends Fragment implements MaterialSpinner.OnItemSelectedListener {
    private static String TAG = EducationFragment.class.getSimpleName();

    private View view;
    private LinearLayout mDynamicLayout;
    private RelativeLayout frequencyLayout;
    private TextView frequencyLabel;
    private MaterialSpinner selectFrequency;
    private TextInputLayout enterAmtLayout;
    private EditText textAmt;
    private TextView amtLabel;
    private TextView amtToWrds;
    private TextInputLayout alreadyAmtLayout;
    private EditText textAlreadyAmt;
    private TextView alreadyAmtLabel;
    private TextView alreadyAmtToWrds;
    private Button proceed;
    private TextInputLayout nameLayout;
    private EditText textName;
    private TextView nameLabel;
    public TreeMap<String, DynamicSpinnerModel> stringDynamicSpinnerModelTreeMap;
    private double amntReqInYrs = 0;
    private double amntReqd = 0;
    private double inflation = 0;
    private double totalRequirement = 0;
    private double expectedROI = 0;
    private double amntSetAside = 0;
    private double currShortBy = 0;
    private double A_Step_1 = 0;
    private double A_Step_1_1 = 0;
    private double investmentGap = 0;
    private double a = 0;
    private double b = 0;
    private double yearlyContri = 0;
    private NvestCustomScrollView mScrollView;
    private  int currentChildAge = 0;
    private int expectedChildAge  = 18;
    private int inflationper = 0;
    private int frequency = 0;
    private TextView textAmtTotalRequirement, totalAmtDeficit;
    private int expectedRoiPer = 0;
    private TextView needName;
    private List<StringKeyValuePair> frequencyList;
    private ValidateInformationDataViewModel validateInformationDataViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.education_fragment, container, false);
        mDynamicLayout = (LinearLayout) view.findViewById(R.id.education_dynamic_layout);
        frequencyLabel= (TextView) view.findViewById(R.id.frequencyLabel);
        selectFrequency =(MaterialSpinner) view.findViewById(R.id.selectFrequency);
        enterAmtLayout=(TextInputLayout)view.findViewById(R.id.enterAmtLayout);
        textAmt=(EditText)view.findViewById(R.id.text_amt);
        amtLabel=(TextView)view.findViewById(R.id.amtLabel);
        amtToWrds=(TextView)view.findViewById(R.id.amt_to_words);
        alreadyAmtLayout=(TextInputLayout)view.findViewById(R.id.already_amount_layout);
        textAlreadyAmt=(EditText) view.findViewById(R.id.text_already);
        alreadyAmtLabel=(TextView)view.findViewById(R.id.alreadyAmtlabel);
        alreadyAmtToWrds=(TextView)view.findViewById(R.id.text_already_num_to_words);
        proceed=(Button)view.findViewById(R.id.button_next);
        mScrollView=(NvestCustomScrollView)view.findViewById(R.id.scrollMain);
        nameLayout=(TextInputLayout)view.findViewById(R.id.nameLayout);
        textName=(EditText)view.findViewById(R.id.text_name);
        nameLabel=(TextView)view.findViewById(R.id.nameLabel);
        totalAmtDeficit = (TextView) view.findViewById(R.id.text_amt_deficit);
        textAmtTotalRequirement = (TextView) view.findViewById(R.id.text_amt_total_requirement);
        needName = (TextView) view.findViewById(R.id.text_product_name);
        KeyValuePair keyValuePair = getArguments().getParcelable("selected_need");
        if(keyValuePair != null){
            CommonMethod.log(TAG , "Received Key " + keyValuePair.getKey());
            CommonMethod.log(TAG , "Received Value " + keyValuePair.getValue());
            needName.setText(keyValuePair.getValue());
        }
        init();
        return view;
    }


    private void init(){
        stringDynamicSpinnerModelTreeMap = new TreeMap<>();
        setDynamicView();
        frequencyList = CommonMethod.frequencySpinnerValues();
        CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), selectFrequency,frequencyList);
        textName.addTextChangedListener(new EducationFragment.GenericTextWatcher(textName));
        textAmt.addTextChangedListener(new EducationFragment.GenericTextWatcher(textAmt));
        textAlreadyAmt.addTextChangedListener(new EducationFragment.GenericTextWatcher(textAlreadyAmt));
        selectFrequency.setTag(NvestLibraryConfig.educationFragmentTag.SELECT_FREQUENCY_TAG);
        selectFrequency.setOnItemSelectedListener(this);
        selectFrequency.setOnClickListener(view -> {
            frequencyLabel.setTextColor(getResources().getColor(R.color.colorSkyBlue));
        });
        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);
        proceed.setOnClickListener(view->{
            /*boolean isAllSpinnersValidated = getDetailsFromStaticAndDynamicSpinners();
            if( isAllSpinnersValidated){
                CommonMethod.log(TAG , "All validations are over hence can start new screen");

            } else {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.all_fields_with_red_are_mandatory), getActivity(), 0);

                CommonMethod.log(TAG, "Complete all validations");
            }
             validate();*/
            CommonMethod.log(TAG , "Proceed btn click");
            validateInformationDataViewModel.NeedAdvisory(31, 35, 15,1,1000000);
        });


    }

    private void setDynamicView(){
        createTextView(NvestLibraryConfig.CURRENT_AGE);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_CURRENT_AGE,NvestLibraryConfig.MAX_CURRENT_AGE,String.valueOf(NvestLibraryConfig.educationFragmentTag.CURRENT_AGE_TAG));

        createTextView(NvestLibraryConfig.CHILD_AGE_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_CHILD_AGE, NvestLibraryConfig.MAX_CHILD_AGE, String.valueOf(NvestLibraryConfig.educationFragmentTag.CHILD_AGE_TAG));

        createTextView(NvestLibraryConfig.EXPECTED_USAGE_OF_FUNDS_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_EXPECTED_USAGE_OF_FUNDS,NvestLibraryConfig.MAX_EXPECTED_USAGE_OF_FUNDS,String.valueOf(NvestLibraryConfig.educationFragmentTag.EXPECTED_USAGE_OF_FUNDS_TAG));

        createTextView(NvestLibraryConfig.RETURN_ON_INVESTMNET_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_RETURN_ON_INVESTMENT, NvestLibraryConfig.MAX_RETURN_ON_INVESTMENT, String.valueOf(NvestLibraryConfig.educationFragmentTag.RETURN_ON_INVESTMENT_TAG));

        createTextView(NvestLibraryConfig.INFALTION_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_INFLATION,NvestLibraryConfig.MAX_INFLATION,String.valueOf(NvestLibraryConfig.educationFragmentTag.INFLATION_TAG));


    }

    public void createTextView(String fieldCaption){
        TextView textView = CommonMethod.createTextView(getActivity(), fieldCaption);
        mDynamicLayout.addView(textView);
    }

    public void addViewToDynamicLayout(int minValue, int maxValue, String fieldCaption){
        List<StringKeyValuePair> returnOnInvestmentList = new ArrayList<>();
        for (int i = minValue; i < maxValue ;i++){
            StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
            stringKeyValuePair.setKey(String.valueOf(i));
            stringKeyValuePair.setValue(String.valueOf(i));
            returnOnInvestmentList.add(stringKeyValuePair);
        }
        MaterialSpinner returnOnInvestmentspinner = CommonMethod.createSpinner(getActivity(), fieldCaption);
        CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), returnOnInvestmentspinner, returnOnInvestmentList);
        returnOnInvestmentspinner.setOnItemSelectedListener(this);
        mDynamicLayout.addView(returnOnInvestmentspinner);

        DynamicSpinnerModel returnOnInvestmentSpinnerModel = new DynamicSpinnerModel();
        returnOnInvestmentSpinnerModel.setDynamicFieldIdentifier(fieldCaption);
        returnOnInvestmentSpinnerModel.setMaterialSpinner(returnOnInvestmentspinner);
        returnOnInvestmentSpinnerModel.setVisible(true);
        returnOnInvestmentSpinnerModel.setSpinnerItemList(returnOnInvestmentList);

        stringDynamicSpinnerModelTreeMap.put(fieldCaption, returnOnInvestmentSpinnerModel);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

        switch (NvestLibraryConfig.educationFragmentTag.valueOf(String.valueOf(view.getTag()))){
            case CHILD_AGE_TAG:
                CommonMethod.log(TAG , "Child age selected " + view.getText().toString());
                currentChildAge = Integer.parseInt(view.getText().toString());
                break;
            case RETURN_ON_INVESTMENT_TAG:
                CommonMethod.log(TAG , "ROI switch executed");
                expectedRoiPer = Integer.parseInt(view.getText().toString());
                break;
            case CURRENT_AGE_TAG:
                CommonMethod.log(TAG,"Current age switch executed");

                break;
            case EXPECTED_USAGE_OF_FUNDS_TAG:
                CommonMethod.log(TAG,"Expected usage of funds switch executed");
                expectedChildAge = Integer.parseInt(view.getText().toString());
                break;
            case INFLATION_TAG:
                CommonMethod.log(TAG,"Inflation switch executed");
                inflationper = Integer.parseInt(view.getText().toString());
            break;
            case TEXT_AMT_TAG:
                break;
            case ALREADY_TEXT_AMT_TAG:
                break;
            case SELECT_FREQUENCY_TAG:
                CommonMethod.log(TAG , "Frequency val selected " + frequencyList.get(position).getKey());
                frequency = Integer.parseInt(frequencyList.get(position).getKey());
                if(frequency == -1){
                    frequency = 0;
                }
                CommonMethod.createSpinnerAttributes(getActivity(), selectFrequency);
                selectFrequency.setError(null);
                frequencyLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                selectFrequency.setTextColor(Color.BLACK);

            break;
            default:
                break;
        }

        calculate();
    }

    public boolean getDetailsFromStaticAndDynamicSpinners() {
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        boolean isCorrect = true;
        for (Map.Entry<String, DynamicSpinnerModel> entry : stringDynamicSpinnerModelTreeMap.entrySet()) {
            if(entry.getValue().isVisible()){
                MaterialSpinner spinner = entry.getValue().getMaterialSpinner();
                if(spinner.getText().toString().equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)){
                    CommonMethod.spinnerErrorAttributes(getActivity(), spinner);
                    isCorrect = false;
                }
                CommonMethod.addDynamicKeyWordToGenericDTO(entry.getValue().getDynamicFieldIdentifier(), getSpinnerKey(spinner), spinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.List), nameofCurrMethod);
                CommonMethod.log(TAG, "Annotation " + entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.log(TAG, "Spinner selection " + spinner.getText().toString());
                CommonMethod.log(TAG, "Visible " + entry.getValue().isVisible());
                CommonMethod.log(TAG, "Level " + entry.getKey());
            }
        }
        return isCorrect;
    }

    public void validate(){
        if(selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION)|| textAmt.length()==0 || textAlreadyAmt.length()==0 ||textName.length()==0){
            if (selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                selectFrequency.setError("");
                frequencyLabel.setTextColor(Color.RED);
                //mSelectExtraGender.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, selectFrequency.getTop());
            } else {
                selectFrequency.setError(null);
            }

            if (textAmt.length() == 0) {
                textAmt.setError("Required");
                amtLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, textAmt.getTop());
            } else {
                amtLabel.setError(null);

            }

            if (textAlreadyAmt.length() == 0) {
                textAlreadyAmt.setError("Required");
                alreadyAmtLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, textAlreadyAmt.getTop());
            } else {
                alreadyAmtLabel.setError(null);

            }
            if (textName.length() == 0) {
                textName.setError("Required");
                nameLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, textName.getTop());
            } else {
                nameLabel.setError(null);

            }



        }
    }




    public void calculate(){
        //int frequency = 12;
        amntReqInYrs = expectedChildAge - currentChildAge;
        inflation = (double) inflationper/100;
        CommonMethod.log(TAG,"Inflation " + inflation);
        totalRequirement = (amntReqd)
                *
                (
                        Math.pow(
                                (1 + ((inflation) / (frequency)))
                                ,
                                ((amntReqInYrs) * (frequency))
                        )
                );

        CommonMethod.log(TAG,"Total Requirement " + Math.round(totalRequirement));

        expectedROI = (double) expectedRoiPer /100;
        CommonMethod.log(TAG,"Expected ROI " + expectedROI);


        double valWithExistingFund = amntSetAside;
        double currShortBy = totalRequirement - amntSetAside;

        CommonMethod.log(TAG,"Current short by " + currShortBy);
        A_Step_1 = Math.pow((1 + ((expectedROI) / (frequency))), ((amntReqInYrs) * (frequency)));
        //CommonMethod.log(TAG,"A step 1 " + A_Step_1 );

        A_Step_1_1 = A_Step_1 - 1;


        investmentGap = (currShortBy) * (expectedROI) / ((frequency) * (A_Step_1_1) * (1 + ((expectedROI) / (frequency))));;

        CommonMethod.log(TAG,"Investment gap " + investmentGap);


        a = Math.pow((1 + expectedROI), amntReqInYrs);
        CommonMethod.log(TAG,"Value for a " + a);

        b = 1 + expectedROI;

        CommonMethod.log(TAG,"Value for b " + b);
        yearlyContri = currShortBy * ((expectedROI)/(((a - 1))*(b)));

        CommonMethod.log(TAG,"Yearly contri " + yearlyContri);
        textAmtTotalRequirement.setText(""+Math.round(totalRequirement));
        totalAmtDeficit.setText(""+Math.round(currShortBy));
    }


    public class GenericTextWatcher implements TextWatcher {
        public View view;
        public GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            if (view.getId() == R.id.text_amt) {
                CommonMethod.log(TAG , "Inside Text Amt ");
                amtLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    amntReqd=Integer.parseInt(text);
                    calculate();
                }

            }

            else if(view.getId() == R.id.text_already){
                CommonMethod.log(TAG , "Inside Text Already Amt ");
                alreadyAmtLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    amntSetAside =  Integer.parseInt(text);
                    calculate();
                }

            }

            else if(view.getId() == R.id.text_name){
                CommonMethod.log(TAG , "Inside Name ");
                nameLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
