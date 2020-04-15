package nvest.com.nvestlibrary.basicInformation;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.navigation.Navigation;

import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.landing.LandingActivity;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.Cities;
import nvest.com.nvestlibrary.nvestWebModel.Keyword;
import nvest.com.nvestlibrary.nvestWebModel.SelectedProductDetails;
import nvest.com.nvestlibrary.nvestWebModel.StateCitiesModel;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;


import static nvest.com.nvestlibrary.commonMethod.CommonMethod.getSpinnerKey;
import static nvest.com.nvestlibrary.commonMethod.CommonMethod.isValidEmail;

public class BasicInformationFragment extends Fragment implements BasicInformationDataViewModel.BasicInformationDataListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, MaterialSpinner.OnItemSelectedListener, FrameLayout.OnTouchListener, StateCityAdapter.StateCityListener {
    private static String TAG = BasicInformationFragment.class.getSimpleName();
    private BasicInformationDataViewModel basicInformationDataViewModel;
    // private MaterialSpinner mSelectStateSpinner;
    private EditText mSelectState;
    //private MaterialSpinner mSelectCitySpinner;
    //Not used
    //private Spinner mSelectBranchSpinner;
    private Button mButtonNext;
    private EditText mEditDateText;
    public TextView mProductName;
    private MaterialSpinner mSelectGender;
    private EditText mEditDateExtraText;
    private MaterialSpinner mSelectExtraGender;
    private RelativeLayout genderLayout;
    private EditText mEmailText;
    private TextInputLayout mEmailTextLayout;
    private EditText mEditNumber;
    private TextInputLayout mNumberLayout;
    private RelativeLayout mSameAsAboveExtraLayout;
    private Switch mSameAsAboveSwitch;
    private EditText mFirstName;
    private EditText mExtraFirstName;
    private EditText mLastName;
    private EditText mExtraLastName;
    private TextView mStateLbl;



    private ImageButton closePopupBtn;
    private ArrayList<StateCitiesModel> stateCitiesModelArrayList;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private StateCityAdapter mAdapter;
    private EditText editSearch;


    private TextInputLayout mFirstNameLayout;
    private TextInputLayout mLastNameLayout;
    private TextInputLayout mDatePickerLayout;
    private TextInputLayout mFirstNameExtraLayout;
    private TextInputLayout mLastNameExtraLayout;
    private TextInputLayout mDatePickerExtraLayout;
    private FrameLayout frameLayout;
    private LandingActivity landingActivity;
    DatabaseHelper myDb;


    private RelativeLayout mBasicLayout;
    private ProgressBar mProgressBar;
    private ScrollView mScrollView;
    private EditText mTextAge;
    private TextView genderLbl;
    private TextView extragenderLbl;
    private EditText mTextExtraAge;
    private TextInputLayout mTextAgeLayout;
    private TextInputLayout mTextExtraAgeLayout;
    private RelativeLayout mProgressDimLayout;
    private SelectedProductDetails mSelectedProductDetails;
    private boolean isGenderVisible = false;
    private LinearLayout productnamelayout;
    private boolean isSmokingVisible = false;
    private String mSelectedStateText = null;
    private String mSelectedCityText = null;
    private String mSelectedBranchText = null;
    private String mSelectedSmokingValue = null;
    private String mSelectedGenderValue = null;
    private String mSelectedChannelValue = null;
    private String mSelectedSmokingId = "N";
    private String mSelectedGenderId = null;
    private String mSelectedChannelId = null;
    private String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private Products products;
    List<String> stringList;
    private Map<String, ArrayList<StringKeyValuePair>> keyvaluePairHashMap;
    private LinkedHashMap<String, Keyword> keywordLinkedHashMap;
    private int mYear, mMonth, mDay;
    private View view;


    private void setStaticView() {
        for (Map.Entry<String, Keyword> entry : keywordLinkedHashMap.entrySet()) {
            String keywordName = entry.getValue().getKeywordName();
            String fieldType = entry.getValue().getFieldType();
            if (fieldType.equalsIgnoreCase("" + NvestLibraryConfig.FieldType.List) && keywordName.equalsIgnoreCase(NvestLibraryConfig.GENDER_ANNOTATION)) {
                isGenderVisible = true;
                CommonMethod.log(TAG, "Gender found");
                mSelectGender.setVisibility(View.VISIBLE);
                mSelectExtraGender.setVisibility(View.VISIBLE);
                //stringList = new ArrayList<>();
                List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                ArrayList<StringKeyValuePair> itemList = keyvaluePairHashMap.get(entry.getKey());
                for (StringKeyValuePair item : itemList) {
                    StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                    stringKeyValuePair.setKey(String.valueOf(item.getKey()));
                    stringKeyValuePair.setValue(item.getValue());
                    ListSpinner.add(stringKeyValuePair);
                    // stringList.add(item.getValue());
                }
                CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mSelectGender, ListSpinner);
                CommonMethod.setMaterialSpinnerAdapter(TAG, getActivity(), mSelectExtraGender, ListSpinner);

                String gender = GenericDTO.getAttributeValue(NvestLibraryConfig.GENDER_ANNOTATION);
                String prGender = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_GENDER_ANNOTATION);
                CommonMethod.setDefaultValue(gender, mSelectGender);
                CommonMethod.setDefaultValue(prGender, mSelectExtraGender);

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CommonMethod.log(TAG, "On create view started");
        view = inflater.inflate(R.layout.fragment_basic_information, container, false);
        Products productsreceived = getArguments().getParcelable("products");
        if (productsreceived != null) {
            products = productsreceived;
            CommonMethod.log(TAG, "Product name passed " + products.getProductName());

        }
//        BasicInformationFragment fragment=new BasicInformationFragment();
//        getFragmentManager().findFragmentById(R.);
        // mSelectStateSpinner=(MaterialSpinner) view.findViewById(R.id.selectState1);
        mSelectState = (EditText) view.findViewById(R.id.selectState);
        // mSelectCitySpinner=(MaterialSpinner)view.findViewById(R.id.selectCity);
        frameLayout = (FrameLayout) view.findViewById(R.id.touchLayout);
        productnamelayout = (LinearLayout) view.findViewById(R.id.product_name_layout);
        genderLayout=(RelativeLayout)view.findViewById(R.id.genderLayout);
        genderLbl=(TextView)view.findViewById(R.id.genderLabel);
        extragenderLbl=(TextView)view.findViewById(R.id.extraGenderLabel);
        mStateLbl=(TextView)view.findViewById(R.id.StateLabel);

        myDb = new DatabaseHelper(getActivity());


        mButtonNext = (Button) view.findViewById(R.id.button_next);
        mEditDateText = (EditText) view.findViewById(R.id.editDatePicker);
        mSelectGender = (MaterialSpinner) view.findViewById(R.id.selectGender);
        mEditDateExtraText = (EditText) view.findViewById(R.id.editDateExtraPicker);
        mProductName = (TextView) view.findViewById(R.id.text_product_name);
        genderLbl=(TextView)view.findViewById(R.id.genderLabel);
        extragenderLbl=(TextView)view.findViewById(R.id.extraGenderLabel);
        genderLayout=(RelativeLayout)view.findViewById(R.id.genderLayout);
        mSelectExtraGender = (MaterialSpinner) view.findViewById(R.id.selectExtraGender);
        mEmailText = (EditText) view.findViewById(R.id.editEmail);
        mEmailTextLayout = (TextInputLayout) view.findViewById(R.id.mailLayout);
        mEditNumber = (EditText) view.findViewById(R.id.editMobileNumber);
        mNumberLayout = (TextInputLayout) view.findViewById(R.id.mobileLayout);
        mSameAsAboveExtraLayout = (RelativeLayout) view.findViewById(R.id.sameAsAboveExtraLayout);
        mSameAsAboveSwitch = (Switch) view.findViewById(R.id.switch1);
        mFirstName = (EditText) view.findViewById(R.id.editFirstName);
        mExtraFirstName = (EditText) view.findViewById(R.id.editExtraFirstName);
        mLastName = (EditText) view.findViewById(R.id.editLastName);
        mExtraLastName = (EditText) view.findViewById(R.id.editExtraLastName);
        mFirstNameLayout = (TextInputLayout) view.findViewById(R.id.editFirstNameLayout);
        mLastNameLayout = (TextInputLayout) view.findViewById(R.id.editLastNameLayout);
        mDatePickerLayout = (TextInputLayout) view.findViewById(R.id.editDatePickerLayout);
        mFirstNameExtraLayout = (TextInputLayout) view.findViewById(R.id.editExtraFirstNameLayout);
        mLastNameExtraLayout = (TextInputLayout) view.findViewById(R.id.editExtraLastNameLayout);
        mDatePickerExtraLayout = (TextInputLayout) view.findViewById(R.id.editDateExtraPickerLayout);
        mBasicLayout = (RelativeLayout) view.findViewById(R.id.layout_basic);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_basic_information);
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        mTextAge = (EditText) view.findViewById(R.id.editAgeText);
        mTextExtraAge = (EditText) view.findViewById(R.id.editAgeExtraText);
        mTextAgeLayout = (TextInputLayout) view.findViewById(R.id.editAgeLayout);
        mTextExtraAgeLayout = (TextInputLayout) view.findViewById(R.id.editAgeExtraLayout);
        mProgressDimLayout = (RelativeLayout) view.findViewById(R.id.progress_basic_layout);
        init();
        //setStaticView();
        //createDummyList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

/*

    public void createDummyList(){
        LinkedHashMap<String, String> dummyList = new LinkedHashMap<>();
        for (int i = 0 ; i < 100 ; i++){
            dummyList.put(String.valueOf(i),"This is the item + " + i);
        }

        for (Map.Entry<String, String> entry: dummyList.entrySet()){
            CommonMethod.log(TAG , "Entry " + entry.getKey());
            CommonMethod.log(TAG , "Entry Value " + entry.getValue());
        }

        */
/*adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        pojoSpinner.setAdapter(adapter);
        pojoSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });*//*

    }
*/


    public void init() {
        keyvaluePairHashMap = new HashMap<>();
        keywordLinkedHashMap = new LinkedHashMap<>();
        mProductName.setText(products.getProductName());

        // TODO: remove this in production, setting dummy values only for testing
        mProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName.setText("John");
                mLastName.setText("Doe");
                mSelectGender.setText("Male");
                mExtraFirstName.setText("John");
                mExtraLastName.setText("Doe");
                mSelectExtraGender.setText("Male");
                mEmailText.setText("abc@gmail.com");
                mEditNumber.setText("9876543210");
                mSelectState.setText("Chandigarh, Massachusetts");
                mTextAgeLayout.setVisibility(View.VISIBLE);
                String text = "21/" + "Jun" + "/1980" ;
                mSelectedProductDetails.setDob(text);
                mEditDateText.setText(text);
                mTextAge.setText(mSelectedProductDetails.getDob());
            }
        });

        mEditDateText.setTag("mEditDateText");
        mEditDateText.setOnClickListener(this);
        mEditDateExtraText.setTag("mEditDateExtraText");
        mEditDateExtraText.setOnClickListener(this);
//        mSelectStateSpinner.setTag("mSelectStateSpinner");
//        mSelectStateSpinner.setOnItemSelectedListener(this);
//        mSelectStateSpinner.setTag("mSelectStateSpinner");
//        mSelectStateSpinner.setOnClickListener(view-> {
//            if (view.getTag().equals("mSelectStateSpinner")) {
//                createPopUp(view);
//            }
//        });
        mSelectState.setTag("mSelectState");

        CommonMethod.checkEdittext(mFirstName,mFirstNameLayout);
        CommonMethod.checkEdittext(mLastName,mLastNameLayout);
        CommonMethod.checkEdittext(mEditDateText,mDatePickerLayout);
        CommonMethod.checkEdittext(mEditNumber,mNumberLayout);
        CommonMethod.checkEdittext(mExtraFirstName,mFirstNameExtraLayout);
        CommonMethod.checkEdittext(mExtraLastName,mLastNameExtraLayout);
        CommonMethod.checkEdittext(mEditDateExtraText,mDatePickerExtraLayout);
        CommonMethod.checkEdittext(mTextAge,mTextAgeLayout);
        CommonMethod.checkEdittext(mTextExtraAge,mTextExtraAgeLayout);
        //CommonMethod.checkEdittext(genderLbl,genderLayout);

        mSelectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("mSelectState")) {
                    mSelectState.setError(null);
                    mStateLbl.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
                    createPopUp(v);
                }
            }

        });
        //mSelectCitySpinner.setTag("mSelectCitySpinner");
        //  mSelectCitySpinner.setOnItemSelectedListener(this);
        mSelectGender.setTag("mSelectGender");
        mSelectGender.setOnItemSelectedListener(this);
        mSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderLbl.setTextColor(getResources().getColor(R.color.colorSkyBlue));
            }
        });
        mSelectExtraGender.setTag("mSelectExtraGender");
        mSelectExtraGender.setOnItemSelectedListener(this);
        mSelectExtraGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extragenderLbl.setTextColor(getResources().getColor(R.color.colorSkyBlue));
            }
        });
        mSelectExtraGender.setOnItemSelectedListener(this);
        /*button.setTag("SelectStateCity");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("SelectStateCity")) {
                    createPopUp(v);
                }
            }
        });*/
        basicInformationDataViewModel = ViewModelProviders.of(this).get(BasicInformationDataViewModel.class);
        stateCitiesModelArrayList = new ArrayList<>();
        basicInformationDataViewModel.setBasicInformationDataListener(this);
        basicInformationDataViewModel.getStateCityList();
        productnamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicInformationDataViewModel.getbasicinformationdetail();
            }
        });
        frameLayout.setOnTouchListener(this);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                //startNextScreen();

            }
        });
        mSelectedProductDetails = new SelectedProductDetails();
        mSameAsAboveSwitch.setTag("mSameAsAboveSwitch");
        mSameAsAboveSwitch.setOnCheckedChangeListener(this);
        basicInformationDataViewModel = ViewModelProviders.of(this).get(BasicInformationDataViewModel.class);
        basicInformationDataViewModel.setBasicInformationDataListener(this);
        basicInformationDataViewModel.getRiders(products.getProductId());
        basicInformationDataViewModel.getStateList();
        basicInformationDataViewModel.getKeywords(products.getProductId());
        //CommonMethod.insertIntoKeyValueStore(TAG ,getActivity(),"Dummy", "Dummy", TAG, "Dummy" );
    }

    @Override
    public void onResume() {
        super.onResume();
        landingActivity = ((LandingActivity) getActivity());

        // set values in textviews and spinners if user is coming back from product information fragment

        // get dynamic params
        String firstName = GenericDTO.getAttributeValue(NvestLibraryConfig.LI_FIRST_NAME_ANNOTATION);
        String lastName = GenericDTO.getAttributeValue(NvestLibraryConfig.LI_LAST_NAME_ANNOTATION);
        String dob = GenericDTO.getAttributeValue(NvestLibraryConfig.LI_DOB_ANNOTATION);
        String age = GenericDTO.getAttributeValue(NvestLibraryConfig.LA_AGE_ANNOTATION);
        String gender = GenericDTO.getAttributeValue(NvestLibraryConfig.GENDER_ANNOTATION);

        String prFirstName = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_FIRST_NAME_ANNOTATION);
        String prLastName = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_LAST_NAME_ANNOTATION);
        String prDob = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_DOB_ANNOTATION);
        String prAge = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_AGE_ANNOTATION);
        String prGender = GenericDTO.getAttributeValue(NvestLibraryConfig.PR_GENDER_ANNOTATION);

        String state = GenericDTO.getAttributeValue(NvestLibraryConfig.STATE_DETAIL_ANNOTATION);
        String city = GenericDTO.getAttributeValue(NvestLibraryConfig.CITY_DETAIL_ANNOTATION);

        // set li default values
        CommonMethod.setDefaultValue(firstName, mFirstName);
        CommonMethod.setDefaultValue(lastName, mLastName);
        CommonMethod.setDefaultValue(dob, mEditDateText);
        CommonMethod.setDefaultValue(age, mTextAge);
        CommonMethod.setDefaultValue(gender, mSelectGender);

        // set pr default values
        CommonMethod.setDefaultValue(prFirstName, mExtraFirstName);
        CommonMethod.setDefaultValue(prLastName, mExtraLastName);
        CommonMethod.setDefaultValue(prDob, mEditDateExtraText);
        CommonMethod.setDefaultValue(prAge, mTextExtraAge);
        CommonMethod.setDefaultValue(prGender, mSelectExtraGender);

        // set state and city default values
        /*CommonMethod.setDefaultValue(state, mSelectStateSpinner);
        CommonMethod.setDefaultValue(city, mSelectCitySpinner);*/

    }

    public void startNextScreen() {
        Bundle args = new Bundle();
        args.putParcelable("products", products);
        Navigation.findNavController(view).navigate(R.id.action_basicInformationFragment_to_productInformationFragment, args);

        // mListener.startProductInformationFragment(products);
        //mListener.startRiderFragment(products);
    }

    public void validate() {

        //myDb.insertData(mSelectedProductDetails);

//        if (mSameAsAboveSwitch.isChecked() == false) {
        if (mSameAsAboveExtraLayout.getVisibility() == View.VISIBLE) {
            if (mFirstName.length() == 0 || mLastName.length() == 0 || mEditDateText.length() == 0 || mTextAge.length() ==0
                    || mExtraFirstName.length() == 0 || mExtraLastName.length() == 0 || mEditDateExtraText.length() == 0 ||mTextExtraAge.length() ==0|| mSelectState.length() == 0
                    || mEditNumber.length() == 0 || !CommonMethod.isValidPhoneNumber(mEditNumber.getText().toString().trim())
                    || !CommonMethod.isValidEmail(mEmailText.getText().toString().trim())
                    || (isGenderVisible && mSelectGender.getText().equals(NvestLibraryConfig.SELECT_OPTION))
                    || (isGenderVisible && mSelectExtraGender.getText().equals(NvestLibraryConfig.SELECT_OPTION))
//                    || mSelectCitySpinner.length() == 0 || mSelectCitySpinner.length() == 0
            ) {


                if (mFirstName.length() == 0) {
                    mFirstNameLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mFirstName.getTop());
                } else {
                    mFirstNameLayout.setError(null);

                }
                if (mLastName.length() == 0) {
                    mLastNameLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mLastName.getTop());
                } else {
                    mLastNameLayout.setError(null);
                }
                if (mEditDateText.length() == 0) {
                    mDatePickerLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mEditDateText.getTop());
                } else {
                    mDatePickerLayout.setError(null);
                }

                if(mTextAge.length() == 0){
                    mTextAgeLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mTextAge.getTop());
                }
                else{
                    mTextAgeLayout.setError(null);
                }
                if (isGenderVisible) {
                    if (mSelectGender.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                        mSelectGender.setError("");
                        genderLbl.setTextColor(Color.RED);
                        //mSelectExtraGender.setTextColor(Color.RED);
                        mScrollView.smoothScrollTo(0, mSelectGender.getTop());
                    } else {
                        mSelectGender.setError(null);
                    }
                }

                if (!CommonMethod.isValidEmail(mEmailText.getText().toString())) {
                    mEmailTextLayout.setError("Invalid Email!");
                    return;
                } else {
                    mEmailTextLayout.setError(null);
                }
                if (!CommonMethod.isValidPhoneNumber(mEditNumber.getText().toString()) || mEditNumber.length() == 0) {
                    if (mEditNumber.length() != 0) {
                        mNumberLayout.setError(" Invalid Mobile Number");
                        return;
                    } else {
                        mNumberLayout.setError(" ");
                    }
                } else {
                    mNumberLayout.setError(null);
                }
                if (mExtraFirstName.length() == 0) {
                                /*ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor((R.color.colorRed), null));
                                mExtraFirstName.setBackgroundTintList(colorStateList);*/
                    mFirstNameExtraLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mExtraFirstName.getTop());
                } else {
                    mFirstNameExtraLayout.setError(null);
                }
                if (mExtraLastName.length() == 0) {
                    mLastNameExtraLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mExtraLastName.getTop());
                } else {
                    mLastNameExtraLayout.setError(null);
                }
                if (mEditDateExtraText.length() == 0) {
                    mDatePickerExtraLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mEditDateExtraText.getTop());
                } else {
                    mDatePickerExtraLayout.setError(null);
                }

                if(mTextExtraAge.length() == 0){
                    mTextExtraAgeLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mTextExtraAge.getTop());
                }
                else{
                    mTextExtraAgeLayout.setError(null);
                }

                if (isGenderVisible) {
                    if (mSelectExtraGender.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                        mSelectExtraGender.setError("");
                        extragenderLbl.setTextColor(Color.RED);
                        //mSelectExtraGender.setTextColor(Color.RED);
                        mScrollView.smoothScrollTo(0, mSelectExtraGender.getTop());
                    } else {
                        mSelectExtraGender.setError(null);
                    }
                }
                if (mSelectState.length() == 0) {
                    mSelectState.setError("");
                    mStateLbl.setTextColor(Color.RED);
                } else {
                    mSelectState.setError(null);
                }

//                mSelectedStateText = mSelectStateSpinner.getText().toString();
//                if (mSelectedStateText.equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
//                    mSelectStateSpinner.setError("");
//                   // mSelectStateSpinner.setTextColor(Color.RED);
//                    mScrollView.smoothScrollTo(0, mSelectStateSpinner.getTop());
//
//                } else {
//                    mSelectStateSpinner.setError(null);
//                }

//                mSelectedStateText=mSelectState.getText().toString();
//                if (mSelectedStateText.equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
//                    mSelectState.setError("");
//                    // mSelectStateSpinner.setTextColor(Color.RED);
//                    mScrollView.smoothScrollTo(0, mSelectState.getTop());
//
//                } else {
//                    mSelectState.setError(null);
//                }

//
//                mSelectedCityText = mSelectCitySpinner.getText().toString();
//                if (mSelectedCityText.equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
//                    mSelectCitySpinner.setError("");
//                   // mSelectCitySpinner.setTextColor(Color.RED);
//                    mScrollView.smoothScrollTo(0, mSelectCitySpinner.getTop());
//
//                } else {
//                    mSelectExtraGender.setError(null);
//                }


                CommonMethod.showErrorAlert(getActivity().getString(R.string.error_dialog_header),
                        getActivity().getString(R.string.empty_validation_error), getActivity(), 0);
            } else {

                String nameofCurrMethod = new Object() {
                }.getClass().getEnclosingMethod().getName();
                String liGender = "";
                String prGender = "";
                if (mSelectGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_MALE)) {
                    liGender = "M";
                } else if (mSelectGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_FEMALE)) {
                    liGender = "F";
                }
                if (mSelectExtraGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_MALE)) {
                    prGender = "M";
                } else if (mSelectExtraGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_FEMALE)) {
                    prGender = "F";
                }
                mSelectedProductDetails.setFirstName(mFirstName.getText().toString().trim());
                mSelectedProductDetails.setLastName(mLastName.getText().toString().trim());
                mSelectedProductDetails.setDob(mEditDateText.getText().toString().trim());
                mSelectedGenderId = mSelectGender.getText().toString();
                mSelectedProductDetails.setGender(mSelectedGenderId);
                //mSelectedProductDetails.setSmoking(mSelectedSmokingId);
                mSelectedProductDetails.setFirstExtraName(mExtraFirstName.getText().toString().trim());
                mSelectedProductDetails.setLastExtraName(mExtraLastName.getText().toString().trim());
                mSelectedProductDetails.setDobExtra(mEditDateExtraText.getText().toString().trim());

                mSelectedProductDetails.setGenderExtra(mSelectExtraGender.getText().toString().trim());
                // mSelectedProductDetails.setSmokingExtra(mSelectExtraSmoking.getText().toString().trim());
                mSelectedProductDetails.setDobextradate(mEditDateExtraText.getText().toString());
                mSelectedProductDetails.setDobdate(mEditDateText.getText().toString().trim());
                mSelectedProductDetails.setEmail(mEmailText.getText().toString().trim());
                mSelectedProductDetails.setMobileNumber(mEditNumber.getText().toString().trim());
                //mSelectedProductDetails.setState(mSelectStateSpinner.getText().toString().trim());
                //mSelectedProductDetails.setState(mSelectState.getText().toString().trim());
                //mSelectedProductDetails.setCity(mSelectCitySpinner.getText().toString().trim());
                // mSelectedProductDetails.setBranch(mSelectBranchSpinner.getText().toString().trim());

                // add data to dynamic params generic dto
                String userName = mFirstName.getText().toString().trim() + " " + mLastName.getText().toString().trim();
                String proposerName = mExtraFirstName.getText().toString().trim() + " " + mExtraLastName.getText().toString().trim();

                // add li details
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.GENDER_ANNOTATION, getSpinnerKey(mSelectGender), mSelectGender.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_FIRST_NAME_ANNOTATION, mFirstName.getText().toString().trim(), mFirstName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_LAST_NAME_ANNOTATION, mLastName.getText().toString().trim(), mLastName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_DOB_ANNOTATION, mEditDateText.getText().toString().trim(), mEditDateText.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LA_AGE_ANNOTATION, mTextAge.getText().toString(), mTextAge.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_NAME_ANNOTATION, userName, userName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), nameofCurrMethod);

                // add proposer details
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_GENDER_ANNOTATION, getSpinnerKey(mSelectExtraGender), mSelectExtraGender.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_FIRST_NAME_ANNOTATION, mExtraFirstName.getText().toString().trim(), mExtraFirstName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_LAST_NAME_ANNOTATION, mExtraLastName.getText().toString().trim(), mExtraLastName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_DOB_ANNOTATION, mEditDateExtraText.getText().toString().trim(), mEditDateExtraText.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_AGE_ANNOTATION, mTextExtraAge.getText().toString(), mTextExtraAge.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_NAME_ANNOTATION, proposerName, proposerName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), nameofCurrMethod);


                // add city and state details
//                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.STATE_DETAIL_ANNOTATION, getSpinnerKey(mSelectStateSpinner), mSelectStateSpinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
//                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.CITY_DETAIL_ANNOTATION, getSpinnerKey(mSelectCitySpinner), mSelectCitySpinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                myDb.insertData(mFirstName.getText().toString(), mLastName.getText().toString(), mEditDateText.getText().toString(), mTextAge.getText().toString(), mSelectGender.getText().toString(), mExtraFirstName.getText().toString(), mExtraLastName.getText().toString(), mEditDateExtraText.getText().toString(), mTextExtraAge.getText().toString(), mSelectExtraGender.getText().toString(), mEmailText.getText().toString(), mSelectedProductDetails.getCity(), mSelectedProductDetails.getState(), mEditNumber.getText().toString());
                startNextScreen();


            }
        } else {
            if (mFirstName.length() == 0 || mLastName.length() == 0 || mEditDateText.length() == 0 || mTextAge.length() ==0
                    // || mExtraFirstName.length() == 0 || mExtraLastName.length() == 0 || mEditDateExtraText.length() == 0
                    || mEditNumber.length() == 0 || !CommonMethod.isValidPhoneNumber(mEditNumber.getText().toString().trim())
                    || !isValidEmail(mEmailText.getText().toString().trim())
                    || (isGenderVisible && TextUtils.isEmpty(mSelectGender.getText().toString()))
                    //|| mSelectCitySpinner.length() == 0 || mSelectCitySpinner.length() == 0
//                    || !CommonMethod.isEmpty(getActivity(), mSelectState, "")
                    || mSelectState.length() == 0
            ) {


                if (mFirstName.length() == 0) {
                    mFirstNameLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mFirstName.getTop());
                } else {
                    mFirstNameLayout.setError(null);
                }
                if (mLastName.length() == 0) {
                    mLastNameLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mLastName.getTop());
                } else {
                    mLastNameLayout.setError(null);
                }
                if (mEditDateText.length() == 0) {
                    mDatePickerLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mEditDateText.getTop());
                } else {
                    mDatePickerLayout.setError(null);
                }

                if(mTextAge.length() == 0){
                    mTextAgeLayout.setError(" ");
                    mScrollView.smoothScrollTo(0, mTextAge.getTop());
                }
                else{
                    mTextAgeLayout.setError(null);
                }

                if (!CommonMethod.isValidEmail(mEmailText.getText().toString())) {
                    mEmailTextLayout.setError("Invalid Email!");
                    return;
                } else {
                    mEmailTextLayout.setError(null);
                }
                if (!CommonMethod.isValidPhoneNumber(mEditNumber.getText().toString()) || mEditNumber.length() == 0) {
                    if (mEditNumber.length() != 0) {
                        mNumberLayout.setError(" Invalid Mobile Number");
                        return;
                    } else {
                        mNumberLayout.setError(" ");
                    }
                } else {
                    mNumberLayout.setError(null);
                }
                if (mSelectState.length() == 0) {
                    mSelectState.setError("");
                    mStateLbl.setTextColor(Color.RED);
                } else {
                    mSelectState.setError(null);
                }
                if (isGenderVisible) {
                    if (mSelectGender.getText().equals(NvestLibraryConfig.SELECT_OPTION)) {
                        mSelectGender.setError("");
                        genderLbl.setTextColor(Color.RED);
                        //mSelectGender.setTextColor(Color.RED);
                        mScrollView.smoothScrollTo(0, mSelectGender.getTop());
                    } else {
                        mSelectGender.setError(null);
                    }
                }
//                mSelectedStateText = mSelectStateSpinner.getText().toString();
//                if (mSelectedStateText.equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
//                    mSelectStateSpinner.setError("");
//                    //mSelectStateSpinner.setTextColor(Color.RED);
//                    mScrollView.smoothScrollTo(0, mSelectStateSpinner.getTop());
//
//                } else {
//                    mSelectStateSpinner.setError(null);
//                }
//                mSelectedCityText = mSelectCitySpinner.getText().toString();
//                if (mSelectedCityText.equalsIgnoreCase(NvestLibraryConfig.SELECT_OPTION)) {
//                    mSelectCitySpinner.setError("");
//                    //mSelectCitySpinner.setTextColor(Color.RED);
//                    mScrollView.smoothScrollTo(0, mSelectCitySpinner.getTop());
//
//                } else {
//                    mSelectCitySpinner.setError(null);
//                }
//                mSelectedStateText = mSelectStateSpinner.getText().toString();
//                if (mSelectedStateText == null || mSelectedStateText.isEmpty()) {
//                    //CommonMethod.setErrorToSpinner(mSelectStateSpinner, NvestLibraryConfig.MANDATORY_FIELD);
//                }


                CommonMethod.showErrorAlert(getActivity().getString(R.string.error_dialog_header),
                        getActivity().getString(R.string.empty_validation_error), getActivity(), 0);
            } else {
                String nameofCurrMethod = new Object() {
                }.getClass().getEnclosingMethod().getName();
                String liGender = "";
                if (mSelectGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_MALE)) {
                    liGender = "M";
                } else if (mSelectGender.getText().toString().equalsIgnoreCase(NvestLibraryConfig.GENDER_FEMALE)) {
                    liGender = "F";
                }


                mSelectedProductDetails.setFirstName(mFirstName.getText().toString().trim());
                mSelectedProductDetails.setLastName(mLastName.getText().toString().trim());
                mSelectedProductDetails.setDob(mEditDateText.getText().toString().trim());
                mSelectedGenderId = mSelectGender.getText().toString();
                mSelectedProductDetails.setGender(mSelectedGenderId);
                //mSelectedProductDetails.setSmoking(mSelectedSmokingId);
                // mSelectedProductDetails.setFirstExtraName(mExtraFirstName.getText().toString().trim());
                // mSelectedProductDetails.setLastExtraName(mExtraLastName.getText().toString().trim());
                // mSelectedProductDetails.setDobExtra(mEditDateExtraText.getText().toString().trim());

                //mSelectedProductDetails.setGenderExtra(mSelectExtraGender.getSelectedItem().toString().trim());
                // mSelectedProductDetails.setSmokingExtra(mSelectExtraSmoking.getText().toString().trim());
                //mSelectedProductDetails.setDobextradate(mEditDateExtraText.getText().toString());
                mSelectedProductDetails.setDobdate(mEditDateText.getText().toString().trim());
                mSelectedProductDetails.setEmail(mEmailText.getText().toString().trim());
                mSelectedProductDetails.setMobileNumber(mEditNumber.getText().toString().trim());
                //mSelectedProductDetails.setState(mSelectStateSpinner.getText().toString().trim());
                //mSelectedProductDetails.setCity(mSelectCitySpinner.getText().toString().trim());
                // mSelectedProductDetails.setBranch(mSelectBranchSpinner.getText().toString().trim());

                // add data to dynamic params generic dto
                String userName = mFirstName.getText().toString().trim() + " " + mLastName.getText().toString().trim();
                String proposerName = mExtraFirstName.getText().toString().trim() + " " + mExtraLastName.getText().toString().trim();

                // add li details
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.GENDER_ANNOTATION, getSpinnerKey(mSelectGender), mSelectGender.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_FIRST_NAME_ANNOTATION, mFirstName.getText().toString().trim(), mFirstName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_LAST_NAME_ANNOTATION, mLastName.getText().toString().trim(), mLastName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_DOB_ANNOTATION, mEditDateText.getText().toString().trim(), mEditDateText.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LA_AGE_ANNOTATION, mTextAge.getText().toString(), mTextAge.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_NAME_ANNOTATION, userName, userName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), nameofCurrMethod);

                //add pr details
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_GENDER_ANNOTATION, getSpinnerKey(mSelectGender), mSelectGender.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_FIRST_NAME_ANNOTATION, mFirstName.getText().toString().trim(), mFirstName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_LAST_NAME_ANNOTATION, mLastName.getText().toString().trim(), mLastName.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_DOB_ANNOTATION, mEditDateText.getText().toString().trim(), mEditDateText.getText().toString().trim(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_AGE_ANNOTATION, mTextAge.getText().toString(), mTextAge.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PR_NAME_ANNOTATION, proposerName, proposerName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), nameofCurrMethod);


                // add city and state details
//                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.STATE_DETAIL_ANNOTATION, getSpinnerKey(mSelectStateSpinner), mSelectStateSpinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);
//                CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.CITY_DETAIL_ANNOTATION, getSpinnerKey(mSelectCitySpinner), mSelectCitySpinner.getText().toString(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), nameofCurrMethod);

                myDb.insertData(mFirstName.getText().toString(), mLastName.getText().toString(), mEditDateText.getText().toString(), mTextAge.getText().toString(), mSelectGender.getText().toString(), mExtraFirstName.getText().toString(), mExtraLastName.getText().toString(), mEditDateExtraText.getText().toString(), mTextExtraAge.getText().toString(), mSelectExtraGender.getText().toString(), mEmailText.getText().toString(), mSelectedProductDetails.getCity(), mSelectedProductDetails.getState(), mEditNumber.getText().toString());

                startNextScreen();

            }

        }


    }


    @Override
    public void onStateCityListCompleted(boolean complete) {

        if (complete) {
            basicInformationDataViewModel.setStateCityLiveData().observe(this, stateCitiesModelArrayListReceived -> {
                stateCitiesModelArrayList.addAll(stateCitiesModelArrayListReceived);
                //setDataToAdapter();
            });
        }

    }

    private void filter(String searchtext) {
        ArrayList<StateCitiesModel> filterdProductList = new ArrayList<>();
        CommonMethod.log(TAG, "Search text  " + searchtext);
        for (StateCitiesModel products : stateCitiesModelArrayList) {
            if (products.getStateName().toLowerCase().contains(searchtext.toLowerCase()) || products.getCityName().toLowerCase().contains(searchtext.toLowerCase())) {
                CommonMethod.log(TAG, "Adding Product name " + products.getStateName());
                filterdProductList.add(products);
            }
            /*if (products.getCityName().toLowerCase().contains(searchtext.toLowerCase())) {
                CommonMethod.log(TAG, "Adding Product name " + products.getCityName());
                filterdProductList.add(products);
            }*/
        }
        if (filterdProductList.size() > 0) {
            mAdapter.setProductContents(filterdProductList, this);
        } else {
            mAdapter.setProductContents(stateCitiesModelArrayList, this);
        }
    }

    public void createPopUp(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup, null);
        closePopupBtn = (ImageButton) customView.findViewById(R.id.closePopupBtn);
        recyclerView = (RecyclerView) customView.findViewById(R.id.recyclerView);
        editSearch = (EditText) customView.findViewById(R.id.editSearch);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        width = displayMetrics.widthPixels - 100;
        popupWindow = new PopupWindow(
                customView,
                width,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mAdapter = new StateCityAdapter(stateCitiesModelArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    @Override
    public void onStateListCompleted(boolean complete) {
        if (complete) {
            basicInformationDataViewModel.setStateMutableLiveData().observe(this, new Observer<LinkedHashMap<Integer, String>>() {
                @Override
                public void onChanged(@Nullable LinkedHashMap<Integer, String> stateLinkedHashMap) {
                    CommonMethod.log(TAG, "State list size " + stateLinkedHashMap.size());
                    stringList = new ArrayList<>();
                    List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                    for (Map.Entry<Integer, String> entry : stateLinkedHashMap.entrySet()) {
                        //stringList.add(entry.getValue());
                        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(entry.getKey()));
                        stringKeyValuePair.setValue(entry.getValue());
                        ListSpinner.add(stringKeyValuePair);
                    }
                    // CommonMethod.setMaterialSpinnerAdapter(TAG , getActivity(), mSelectStateSpinner, ListSpinner);
                }
            });
        }
    }

    @Override
    public void onCityListCompleted(boolean complete) {
        if (complete) {
            basicInformationDataViewModel.setCityMutableLiveData().observe(this, new Observer<LinkedHashMap<Integer, Cities>>() {
                @Override
                public void onChanged(@Nullable LinkedHashMap<Integer, Cities> citiesLinkedHashMap) {
                    //stringList = new ArrayList<>();
                    List<StringKeyValuePair> ListSpinner = new ArrayList<>();
                    for (Map.Entry<Integer, Cities> entry : citiesLinkedHashMap.entrySet()) {
                        //stringList.add(entry.getValue());
                        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(entry.getValue().getCityId()));
                        stringKeyValuePair.setValue(entry.getValue().getCityName());
                        ListSpinner.add(stringKeyValuePair);
                    }
                    // CommonMethod.setMaterialSpinnerAdapter(TAG , getActivity(), mSelectCitySpinner, ListSpinner);
                }
            });
        }
    }

    @Override
    public void onKeywordsListObtained(boolean complete) {
        basicInformationDataViewModel.setKeyWordMutableLiveData().observe(this, new Observer<LinkedHashMap<String, Keyword>>() {
            @Override
            public void onChanged(@Nullable LinkedHashMap<String, Keyword> stringKeywordLinkedHashMap) {
                CommonMethod.log(TAG, "Putting values in key word map");
                keywordLinkedHashMap.putAll(stringKeywordLinkedHashMap);
                setStaticView();

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
        basicInformationDataViewModel.setBasicInformationLiveData().observe(this, selectedProductDetails -> {
            if (selectedProductDetails != null) {
                CommonMethod.log(TAG, "First name received " + selectedProductDetails.getFirstName());
                mFirstName.setText(selectedProductDetails.getFirstName());
                mLastName.setText(selectedProductDetails.getLastName());
                mEditDateText.setText(selectedProductDetails.getDobdate());
                mTextAge.setText(selectedProductDetails.getDob());
                mSelectGender.setText(selectedProductDetails.getGender());
                mExtraFirstName.setText(selectedProductDetails.getFirstExtraName());
                mExtraLastName.setText(selectedProductDetails.getLastExtraName());
                mEditDateExtraText.setText(selectedProductDetails.getDobextradate());
                mTextExtraAge.setText(selectedProductDetails.getDobExtra());
                mSelectExtraGender.setText(selectedProductDetails.getGenderExtra());
                mEmailText.setText(selectedProductDetails.getEmail());
                // mSelectStateSpinner.setText(selectedProductDetails.getState());
                //  mSelectCitySpinner.setText(selectedProductDetails.getCity());
                mEditNumber.setText(selectedProductDetails.getMobileNumber());
                mSelectState.setText(selectedProductDetails.getCity() + "," + selectedProductDetails.getState());
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            mSameAsAboveExtraLayout.setVisibility(View.VISIBLE);
        } else {
            mSameAsAboveExtraLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals(mEditDateText.getTag())) {
            setDateField(mEditDateText, 0);
        }
        if (view.getTag().equals(mEditDateExtraText.getTag())) {
            setDateField(mEditDateExtraText, 1);
        }
    }

    private void setDateField(final EditText datePickerText, final int flag) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String nameofCurrMethod = new Object() {
        }.getClass().getEnclosingMethod().getName();
        CommonMethod.log(TAG, "Current method name " + nameofCurrMethod);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.AppDatePickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            try {
                                String text = dayOfMonth + "/" + months[monthOfYear] + "/" + year;
                                datePickerText.setText(text);
                                CommonMethod.log(TAG, "print" + text);
//                        datePickerText.setText(dayOfMonth + " " + new DateFormatSymbols().getMonths()[monthOfYear] + " " + year);
                                if (flag == 0) {
                                    mTextAgeLayout.setVisibility(View.VISIBLE);
                                    mDatePickerLayout.setErrorEnabled(false);
                                    mSelectedProductDetails.setDob(text);
                                    mTextAge.setText(mSelectedProductDetails.getDob());


                                } else {
                                    mTextExtraAgeLayout.setVisibility(View.VISIBLE);
                                    mDatePickerExtraLayout.setErrorEnabled(false);
                                    mSelectedProductDetails.setDob(text);
                                    mTextExtraAge.setText(mSelectedProductDetails.getDob());
                                }
                                datePickerText.setError(null);
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
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

       /* if(view.getTag().equals(mSelectStateSpinner.getTag())){
            basicInformationDataViewModel.getStateCityList();
            CommonMethod.createSpinnerAttributes(getActivity(),mSelectStateSpinner);
        }*/
//        else if(view.getTag().equals(mSelectCitySpinner.getTag())){
//            CommonMethod.createSpinnerAttributes(getActivity(),mSelectCitySpinner);
//        }
        if (view.getTag().equals(mSelectGender.getTag())) {
            CommonMethod.createSpinnerAttributes(getActivity(), mSelectGender);
            mSelectGender.setError(null);
            genderLbl.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
            mSelectGender.setTextColor(Color.BLACK);
        } else if (view.getTag().equals(mSelectExtraGender.getTag())) {
            CommonMethod.createSpinnerAttributes(getActivity(), mSelectExtraGender);
            mSelectExtraGender.setError(null);
            extragenderLbl.setTextColor(getResources().getColor(R.color.colorCheckboxGray));
            mSelectExtraGender.setTextColor(Color.BLACK);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CommonMethod.log(TAG, "Frame layout touch event found");
        landingActivity.hideKeyboard(v);
        return false;
    }

    @Override
    public void selectedStateCityItem(StateCitiesModel stateCitiesModel) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (stateCitiesModel != null) {
            mSelectState.setText(stateCitiesModel.getCityName() + "," + stateCitiesModel.getStateName());
            mSelectedProductDetails.setState(stateCitiesModel.getStateName());
            mSelectedProductDetails.setCity(stateCitiesModel.getCityName());
        }
    }
}
