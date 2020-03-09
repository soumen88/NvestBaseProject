package nvest.com.nvestlibrary.needbasedanalyser.retirement;

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
import nvest.com.nvestlibrary.needbasedanalyser.wealth.WealthAccumulationFragment;
import nvest.com.nvestlibrary.nvestWebModel.DynamicSpinnerModel;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;

import static nvest.com.nvestlibrary.commonMethod.CommonMethod.getSpinnerKey;

public class RetirementFragment extends Fragment implements MaterialSpinner.OnItemSelectedListener {
    private static String TAG = RetirementFragment.class.getSimpleName();
    private LinearLayout mDynamicLayout;
    private View view;
    private Button calculate;
    private TextInputLayout nameLayout;
    private EditText nameText;
    private TextView nameLabel;
    private RelativeLayout frequencyLayout;
    private TextView frequencyLabel;
    private MaterialSpinner selectFrequency;
    private TextInputLayout retirementAmtLayout;
    private EditText retirementAmt;
    private TextView retirementLabel;
    private TextInputLayout alreadyRetirementAmtLayout;
    private EditText alreadyRetirementAmt;
    private TextView alreadyRetirementLabel;
    private Button proceed;
    private NvestCustomScrollView mScrollView;
    public TreeMap<String, DynamicSpinnerModel> stringDynamicSpinnerModelTreeMap;
    double txtAmntAlreadytSet;
    double txtDesiredAmnt;
    int retirementAge = 0;
    int currentAge = 0;
    int lifeExpectancy = 0;
    private double inflation = 0;
    private double postRetirementInvestmentReturn = 0;
    private double pretRetirementInvestmentReturn = 0.0;
    private int inflationper = 0;
    private int frequency = 0;
    private int postReturnInvestmentReturnPer = 0;
    private int pretReturnInvestmentReturnPer = 0;
    private TextView textAmtTotalRequirement, totalAmtDeficit;
    private TextView needName;
    private List<StringKeyValuePair> frequencyList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.retirement_fragment, container, false);
        mDynamicLayout = (LinearLayout) view.findViewById(R.id.retirement_dynamic_layout);
        calculate = (Button) view.findViewById(R.id.calculate);
        nameLayout = (TextInputLayout) view.findViewById(R.id.nameLayout);
        nameText = (EditText) view.findViewById(R.id.text_name);
        nameLabel = (TextView) view.findViewById(R.id.nameLabel);
        frequencyLabel = (TextView) view.findViewById(R.id.frequencyLabel);
        frequencyLayout = (RelativeLayout) view.findViewById(R.id.frequencyLayout);
        selectFrequency = (MaterialSpinner) view.findViewById(R.id.selectFrequency);
        retirementAmtLayout = (TextInputLayout) view.findViewById(R.id.retirementAmtLayout);
        retirementAmt = (EditText) view.findViewById(R.id.retirement_amt);
        retirementLabel = (TextView) view.findViewById(R.id.retirementLabel);
        alreadyRetirementAmtLayout = (TextInputLayout) view.findViewById(R.id.alreadyRetirementAmtLayout);
        alreadyRetirementAmt = (EditText) view.findViewById(R.id.already_retirement_amt);
        alreadyRetirementLabel = (TextView) view.findViewById(R.id.alreadyRetirementLabel);
        mScrollView = (NvestCustomScrollView) view.findViewById(R.id.scrollMain);
        proceed = (Button) view.findViewById(R.id.button_next);
        totalAmtDeficit = (TextView) view.findViewById(R.id.text_amt_deficit);
        textAmtTotalRequirement = (TextView) view.findViewById(R.id.text_amt_total_requirement);
        needName = (TextView) view.findViewById(R.id.text_product_name);
        proceed.setOnClickListener(view -> {
            boolean isAllSpinnersValidated = getDetailsFromStaticAndDynamicSpinners();
            if (isAllSpinnersValidated) {
                CommonMethod.log(TAG, "All validations are over hence can start new screen");

            } else {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.all_fields_with_red_are_mandatory), getActivity(), 0);

                CommonMethod.log(TAG, "Complete all validations");
            }
            validate();

        });
        KeyValuePair keyValuePair = getArguments().getParcelable("selected_need");
        if(keyValuePair != null){
            CommonMethod.log(TAG , "Received Key " + keyValuePair.getKey());
            CommonMethod.log(TAG , "Received Value " + keyValuePair.getValue());
            needName.setText(keyValuePair.getValue());
        }
        init();
        return view;
    }


    public void validate() {
        if (selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION) || retirementAmt.length() == 0 || alreadyRetirementAmt.length() == 0 || nameText.length() == 0) {

            if (selectFrequency.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                selectFrequency.setError("");
                frequencyLabel.setTextColor(Color.RED);
                //mSelectExtraGender.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, selectFrequency.getTop());
            } else {
                selectFrequency.setError(null);
            }

            if (retirementAmt.length() == 0) {
                retirementAmt.setError("Required");
                retirementLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, retirementAmt.getTop());
            } else {
                retirementLabel.setError(null);

            }

            if (alreadyRetirementAmt.length() == 0) {
                alreadyRetirementAmt.setError("Required");
                alreadyRetirementLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, alreadyRetirementAmt.getTop());
            } else {
                alreadyRetirementLabel.setError(null);

            }

            if (nameText.length() == 0) {
                nameText.setError("Required");
                nameLabel.setTextColor(Color.RED);
                mScrollView.smoothScrollTo(0, nameText.getTop());
            } else {
                nameLabel.setError(null);

            }

        }

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
                CommonMethod.log(TAG, "Annotation " + entry.getValue().getDynamicFieldIdentifier());
                CommonMethod.log(TAG, "Spinner selection " + spinner.getText().toString());
                CommonMethod.log(TAG, "Visible " + entry.getValue().isVisible());
                CommonMethod.log(TAG, "Level " + entry.getKey());
            }
        }
        return isCorrect;
    }

    private void init() {
        stringDynamicSpinnerModelTreeMap = new TreeMap<>();
        setDynamicView();
        frequencyList = CommonMethod.frequencySpinnerValues();
        CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), selectFrequency,frequencyList);
        nameText.addTextChangedListener(new RetirementFragment.GenericTextWatcher(nameText));
        retirementAmt.addTextChangedListener(new RetirementFragment.GenericTextWatcher(retirementAmt));
        alreadyRetirementAmt.addTextChangedListener(new RetirementFragment.GenericTextWatcher(alreadyRetirementAmt));
        selectFrequency.setTag(NvestLibraryConfig.retirementFragmentTag.RETIREMENT_FREQUENCY_TAG);
        selectFrequency.setOnItemSelectedListener(this);
        selectFrequency.setOnClickListener(view -> {
            frequencyLabel.setTextColor(getResources().getColor(R.color.colorSkyBlue));
        });
    }


    private void setDynamicView() {
        createTextView(NvestLibraryConfig.RETIREMENT_CURRENT_AGE_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_RETIREMENT_AGE, NvestLibraryConfig.MAX_RETIREMENT_AGE, String.valueOf(NvestLibraryConfig.retirementFragmentTag.CURRENT_AGE_TAG));

        createTextView(NvestLibraryConfig.RETIREMENT_WHEN_TO_RETIRE_AGE_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_RETIREMENT_AGE_WHEN, NvestLibraryConfig.MAX_RETIREMENT_AGE_WHEN, String.valueOf(NvestLibraryConfig.retirementFragmentTag.RETIREMENT_AGE_TAG));

        createTextView(NvestLibraryConfig.LIFE_EXPECTANCY_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_LIFE_EXPECTANCY, NvestLibraryConfig.MAX_LIFE_EXPECTANCY, String.valueOf(NvestLibraryConfig.retirementFragmentTag.LIFE_EXPECTANCY_TAG));

        createTextView(NvestLibraryConfig.PRE_RETIREMENT_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_PRE_RETIREMENT_RETURN, NvestLibraryConfig.MAX_PRE_RETIREMENT_RETURN, String.valueOf(NvestLibraryConfig.retirementFragmentTag.PRE_RETIREMENT_INVESTMENT_RETURN_TAG));

        createTextView(NvestLibraryConfig.POST_RETIREMENT_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_POST_RETIREMENT_RETURN, NvestLibraryConfig.MAX_POST_RETIREMENT_RETURN, String.valueOf(NvestLibraryConfig.retirementFragmentTag.POST_RETIREMENT_INVESTMENT_RETURN_TAG));

        createTextView(NvestLibraryConfig.RETIREMENT_INFLATION_CAPTION);
        addViewToDynamicLayout(NvestLibraryConfig.MIN_RETIREMENT_INFLATION, NvestLibraryConfig.MAX_RETIREMENT_INFLATION, String.valueOf(NvestLibraryConfig.retirementFragmentTag.RETIREMENT_INFLATION_TAG));
    }

    public void createTextView(String fieldCaption) {
        TextView textView = CommonMethod.createTextView(getActivity(), fieldCaption);
        mDynamicLayout.addView(textView);
    }

    public void addViewToDynamicLayout(int minValue, int maxValue, String fieldCaption) {
        List<StringKeyValuePair> returnOnInvestmentList = new ArrayList<>();
        for (int i = minValue; i < maxValue; i++) {
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
        if(view.getText().toString().equals(NvestLibraryConfig.SELECT_OPTION)){
            CommonMethod.logToast(getActivity(), "Invalid input");

            return;
        }
        switch (NvestLibraryConfig.retirementFragmentTag.valueOf(String.valueOf(view.getTag()))) {
            case CURRENT_AGE_TAG:
                CommonMethod.log(TAG, "Child age switch executed");
                currentAge= Integer.parseInt(view.getText().toString());
                break;
            case RETIREMENT_AGE_TAG:
                CommonMethod.log(TAG, "ROI switch executed");
                retirementAge = Integer.parseInt(view.getText().toString());
                break;
            case LIFE_EXPECTANCY_TAG:
                lifeExpectancy = Integer.parseInt(view.getText().toString());
                break;
            case RETIREMENT_INFLATION_TAG:
                inflationper = Integer.parseInt(view.getText().toString());
                break;
            case PRE_RETIREMENT_INVESTMENT_RETURN_TAG:
                pretReturnInvestmentReturnPer  = Integer.parseInt(view.getText().toString());
                break;
            case POST_RETIREMENT_INVESTMENT_RETURN_TAG:
                postReturnInvestmentReturnPer = Integer.parseInt(view.getText().toString());
                break;
            case RETIREMENT_FREQUENCY_TAG:
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

    public void calculate() {
        //int frequency = 12;

        inflation = (double) inflationper / 100;
        postRetirementInvestmentReturn = (double) postReturnInvestmentReturnPer / 100;
        pretRetirementInvestmentReturn = (double) pretReturnInvestmentReturnPer / 100;
        double amntRqdNoOfYrs = retirementAge - currentAge;
        double futureValOfMonthlyIncome = (txtDesiredAmnt) * (Math.pow((1 + ((inflation) / (frequency))), (amntRqdNoOfYrs) * (frequency)));


        double noOfRetirmntAge = lifeExpectancy - retirementAge;
        double futureValAtEndOfLife = (futureValOfMonthlyIncome) * (Math.pow((1 + ((inflation) / (frequency))), (noOfRetirmntAge) * (frequency)));

        double totalReq = (((futureValOfMonthlyIncome) / ((postRetirementInvestmentReturn) - (inflation))) - (((futureValAtEndOfLife) / ((postRetirementInvestmentReturn) - (inflation))) / (Math.pow((1 + ((postRetirementInvestmentReturn) / (frequency))), ((noOfRetirmntAge) * (frequency)))))) * 12;


        CommonMethod.log(TAG, "Total requirement " + totalReq);
        double currSavGrow = txtAmntAlreadytSet;
        double currShortBy = totalReq - currSavGrow;
        CommonMethod.log(TAG, "Current short by " + currShortBy);
        double step_1 = Math.pow((1 + ((pretRetirementInvestmentReturn) / (frequency))), ((amntRqdNoOfYrs) * (frequency)));

        CommonMethod.log(TAG, "Step 1 " + step_1);

        double step_2 = (step_1) - 1;

        double gapAmnt = (currShortBy) * ((pretRetirementInvestmentReturn) / ((frequency) * (step_2) * (1 + ((pretRetirementInvestmentReturn) / (frequency)))));

        CommonMethod.log(TAG, "Gap amnt " + gapAmnt);

        double yearlyContri = (currShortBy) * (((pretRetirementInvestmentReturn))) / ((Math.pow((1 + ((pretRetirementInvestmentReturn))), (amntRqdNoOfYrs)) - (1)) * ((1) + ((pretRetirementInvestmentReturn))));
        CommonMethod.log(TAG, "Yearly contri " + yearlyContri);
        textAmtTotalRequirement.setText(""+Math.round(totalReq));
        totalAmtDeficit.setText(""+Math.round(currShortBy));
    }

    public class GenericTextWatcher implements TextWatcher {
        public View view;

        public GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (view.getId() == R.id.text_name) {
                CommonMethod.log(TAG , "Inside Text name ");
                nameLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
            }
            else if(view.getId() == R.id.retirement_amt){
                CommonMethod.log(TAG , "Inside current amount ");
                retirementLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    txtDesiredAmnt = Double.parseDouble(text);
                    calculate();
                }

            }
            else if(view.getId() == R.id.already_retirement_amt){
                CommonMethod.log(TAG , "Inside wealth amount ");
                alreadyRetirementLabel.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                if(text != null && !text.isEmpty()){
                    txtAmntAlreadytSet = Double.parseDouble(text);
                    calculate();
                }
            }
        }
    }
}
