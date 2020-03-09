package nvest.com.nvestlibrary.needbasedanalyser.wealth;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
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

import static nvest.com.nvestlibrary.commonMethod.CommonMethod.getSpinnerKey;

public class WealthAccumulationFragment extends Fragment implements MaterialSpinner.OnItemSelectedListener {
    private static String TAG = WealthAccumulationFragment.class.getSimpleName();
    private LinearLayout mDynamicLayout;
    private View view;
    private NvestCustomScrollView mScrollView;
    private TextInputLayout nameLayout;
    private EditText textName;
    private TextView nameLabel;
    private TextInputLayout wealthAmtLayout;
    private EditText wealthAmt;
    private TextView wealthLabel;
    private TextView wealthAmtToWrds;
    private TextInputLayout currentAmtLayout;
    private EditText currentAmt;
    private TextView currentLabel;
    private TextView currentAmtToWrds;
    private RelativeLayout frequencyLayout;
    private TextView frequencyLabel;
    private MaterialSpinner selectFrequency;
    private Button proceed;
    private LinearLayout buttonLayout;
    private TextView textAmtTotalRequirement, totalAmtDeficit;
    public TreeMap<String, DynamicSpinnerModel> stringDynamicSpinnerModelTreeMap;
    private double amntReqInYrs = 0;
    private double futureReqdAmt = 0;
    private double expectedROI = 0;
    private double currSavings = 0 ;
    private int expectedRoiPer = 0;
    private int frequency = 0;
    private TextView needName;
    private List<StringKeyValuePair> frequencyList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wealth_accumulation_fragment, container, false);
        mDynamicLayout = (LinearLayout) view.findViewById(R.id.wealth_accumulation_dynamic_layout);
        nameLayout=(TextInputLayout)view.findViewById(R.id.nameLayout);
        textName=(EditText)view.findViewById(R.id.text_name);
        nameLabel=(TextView)view.findViewById(R.id.nameLabel);
        wealthAmtLayout=(TextInputLayout)view.findViewById(R.id.wealthAmtLayout);
        wealthAmt=(EditText)view.findViewById(R.id.wealth_amt);
        wealthLabel=(TextView)view.findViewById(R.id.wealthLabel);
        wealthAmtToWrds=(TextView)view.findViewById(R.id.wealth_amt_to_words);
        currentAmtLayout=(TextInputLayout)view.findViewById(R.id.currentAmtLayout);
        currentAmt=(EditText)view.findViewById(R.id.current_amt);
        currentLabel=(TextView)view.findViewById(R.id.currentLabel);
        currentAmtToWrds=(TextView)view.findViewById(R.id.current_amt_to_words);
        frequencyLayout=(RelativeLayout)view.findViewById(R.id.frequencyLayout);
        frequencyLabel=(TextView)view.findViewById(R.id.frequencyLabel);
        selectFrequency=(MaterialSpinner)view.findViewById(R.id.selectFrequency);
        proceed=(Button)view.findViewById(R.id.button_next);
        buttonLayout =  (LinearLayout) view.findViewById(R.id.button_layout);
        mScrollView=(NvestCustomScrollView)view.findViewById(R.id.scrollMain);
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
        proceed.setOnClickListener(view->{
            boolean isAllSpinnersValidated = getDetailsFromStaticAndDynamicSpinners();
            if( isAllSpinnersValidated){
                CommonMethod.log(TAG , "All validations are over hence can start new screen");

            } else {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.all_fields_with_red_are_mandatory), getActivity(), 0);

                CommonMethod.log(TAG, "Complete all validations");
            }
            validate();

        });


        textName.addTextChangedListener(new GenericTextWatcher(textName));
        wealthAmt.addTextChangedListener(new GenericTextWatcher(wealthAmt));
        currentAmt.addTextChangedListener(new GenericTextWatcher(currentAmt));

        selectFrequency.setTag(NvestLibraryConfig.WealthFragmentTag.WEALTH_FREQUENCY_TAG);
        selectFrequency.setOnItemSelectedListener(this);
        selectFrequency.setOnClickListener(view->{
            frequencyLabel.setTextColor(getResources().getColor(R.color.colorSkyBlue));
        });
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

        if(selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION)|| textName.length()==0 || wealthAmt.length()==0 ||currentAmt.length()==0){


            if (selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                selectFrequency.setError("");
                frequencyLabel.setTextColor(Color.RED);
                //mSelectExtraGender.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, selectFrequency.getTop());
            } else {
                selectFrequency.setError(null);
            }


            if (textName.length() == 0) {
                textName.setError("Required");
                nameLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, textName.getTop());
            } else {
                nameLabel.setError(null);

            }


            if (wealthAmt.length() == 0) {
                wealthAmt.setError("Required");
                wealthLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, wealthAmt.getTop());
            } else {
                wealthLabel.setError(null);

            }

            if (currentAmt.length() == 0) {
                currentAmt.setError("Required");
                currentLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, currentAmt.getTop());
            } else {
                currentLabel.setError(null);

            }
        }


    }
    private void setDynamicView(){
        createTextView(NvestLibraryConfig.WEALTH_CURRENT_AGE_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.WEALTH_MIN_AGE_VALUE, NvestLibraryConfig.WEALTH_MAX_AGE_VALUE, String.valueOf(NvestLibraryConfig.WealthFragmentTag.CURRENT_AGE_TAG));

        createTextView(NvestLibraryConfig.WEALTH_YEARS_WANT_THIS_SAVING_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.WEALTH_MIN_YEARS_SAVING_VALUE, NvestLibraryConfig.WEALTH_MAX_YEARS_SAVING_VALUE, String.valueOf(NvestLibraryConfig.WealthFragmentTag.YEARS_WANT_THIS_SAVING_TAG));

        createTextView(NvestLibraryConfig.WEALTH_EXPECTED_RETURN_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.WEALTH_MIN_ROI_VALUE, NvestLibraryConfig.WEALTH_MAX_ROI_VALUE, String.valueOf(NvestLibraryConfig.WealthFragmentTag.EXPECTED_RETURN_ON_INVESTMENT_TAG));
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

        switch (NvestLibraryConfig.WealthFragmentTag.valueOf(String.valueOf(view.getTag()))){
            case CURRENT_AGE_TAG:
                CommonMethod.log(TAG , "Child age switch executed");
                break;
            case YEARS_WANT_THIS_SAVING_TAG:
                CommonMethod.log(TAG , "ROI switch executed");
                amntReqInYrs = Integer.parseInt(view.getText().toString());
                break;
            case EXPECTED_RETURN_ON_INVESTMENT_TAG:
                expectedRoiPer = Integer.parseInt(view.getText().toString());
                break;
            case WEALTH_FREQUENCY_TAG:
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

    public void calculate(){
        //int frequency = 12;


        double currShortBy = futureReqdAmt - currSavings;
        CommonMethod.log(TAG,"Short fall " + currShortBy);
        expectedROI = (double) expectedRoiPer /100;
        CommonMethod.log(TAG,"Expected roi  " + expectedROI);
        double A_Step_1 = Math.pow((1 + ((expectedROI) / (frequency))), ((amntReqInYrs) * (frequency)));
        CommonMethod.log(TAG," A step 1 " + A_Step_1);
        double A_Step_1_1 = (A_Step_1) - 1;
        CommonMethod.log(TAG," A step 1_1 " + A_Step_1_1);
        double investmentGap = (currShortBy)
                * (expectedROI)
                / ((frequency) * (A_Step_1_1)
                * (
                1 + ((expectedROI) / (frequency))
        )
        );

        CommonMethod.log(TAG,"Investment gap " +  investmentGap);
        double yearlyContri = (currShortBy) * (((expectedROI))) / ((Math.pow(((1) + ((expectedROI))),(amntReqInYrs)) - (1)) * ((1) + ((expectedROI))));
        CommonMethod.log(TAG,"Yearly contri " + yearlyContri);

        textAmtTotalRequirement.setText(""+Math.round(futureReqdAmt));
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
            if (view.getId() == R.id.text_name) {
                CommonMethod.log(TAG , "Inside Text name ");
                nameLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
            }
            else if(view.getId() == R.id.current_amt){
                CommonMethod.log(TAG , "Inside current amount ");
                currentLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    currSavings = Double.parseDouble(text);
                    calculate();
                }
            }
            else if(view.getId() == R.id.wealth_amt){
                CommonMethod.log(TAG , "Inside wealth amount ");
                wealthLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    futureReqdAmt = Double.parseDouble(text);
                    calculate();
                }
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
