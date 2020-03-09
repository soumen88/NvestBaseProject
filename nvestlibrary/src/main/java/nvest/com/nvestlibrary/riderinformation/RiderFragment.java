package nvest.com.nvestlibrary.riderinformation;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import java.util.ArrayList;
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
import nvest.com.nvestlibrary.nvestWebModel.DynamicEditTextModel;
import nvest.com.nvestlibrary.nvestWebModel.RiderInformationModel;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;


/**
 * Created by viren on 20/06/17.
 */

public class RiderFragment extends Fragment implements RiderInformationDataViewModel.RiderInformationDataListener, RiderAdapter.RiderListener, ValidateInformationDataViewModel.ValidateInformationDataListener, View.OnTouchListener {
    private static String TAG = RiderFragment.class.getSimpleName();

    // lists, collections etc...
    private ArrayList<RiderInformationModel> riderInformationModelArrayList;
    private LinkedHashMap<RiderInformationModel, DynamicEditTextModel> editTexts;
    private List<ValidationIP> validationIpList;
    private HashMap<String, String> errors;

    // view models
    private RiderInformationDataViewModel riderInformationDataViewModel;
    private ValidateInformationDataViewModel validateInformationDataViewModel;

    // context and activity
    private Context context;
    private Activity activity;
    private LandingActivity landingActivity;

    //views
    private RecyclerView mListRider;
    private TextView tvProductName;
    private Button mRiderValidateBtn;
    private ProgressBar progressBar;
    private FrameLayout touchLayout;

    //others
    private Products product;
    private RiderAdapter mAdapter;
    private Menu toolbarMenu;
    private MenuItem skipMenuItem;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rider, container, false);
        context = getContext();
        activity = getActivity();
        landingActivity = (LandingActivity) activity;

        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            Toast.makeText(context, "Bundle size: " + savedInstanceState.size(), Toast.LENGTH_SHORT).show();
        }
        Products productsreceived = getArguments().getParcelable("products");
        if (productsreceived != null) {
            product = productsreceived;
            CommonMethod.log(TAG, "Product name passed " + product.getProductName());
        }

        mListRider = view.findViewById(R.id.list_rider);
        tvProductName = view.findViewById(R.id.text_product_name);
        mRiderValidateBtn = view.findViewById(R.id.button_rider_validate);
        progressBar = view.findViewById(R.id.progressBar);
        touchLayout = view.findViewById(R.id.touchLayout);

        // set product name in title
        tvProductName.setText(product.getProductName());

        view.setBackgroundColor(Color.WHITE);
        init();
        return view;
    }

    public void init() {
        riderInformationModelArrayList = new ArrayList<>();
        editTexts = new LinkedHashMap<>();
        riderInformationDataViewModel = ViewModelProviders.of(this).get(RiderInformationDataViewModel.class);
        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);
        validateInformationDataViewModel.setValidateInformationDataListener(this);

        riderInformationDataViewModel.setRiderInformationDataListener(this);
        riderInformationDataViewModel.getRidersFromProductId(product.getProductId());
        mRiderValidateBtn.setTag("mRiderValidateBtn");
        mRiderValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate();
                } catch (Exception e) {
                    CommonMethod.log(TAG, "Exception " + e.toString());
                }
            }
        });

        touchLayout.setOnTouchListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbarMenu = menu;
        inflater.inflate(R.menu.menu_rider, menu);
        skipMenuItem = toolbarMenu.findItem(R.id.skip);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == skipMenuItem) {
            removeRidersFromGenericDto();
            // mListener.startSummaryFragment(product);
            Bundle args = new Bundle();
            args.putParcelable("products", product);
            Navigation.findNavController(view).navigate(R.id.action_riderFragment_to_summaryFragment, args);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeRidersFromGenericDto() {
        GenericDTO.removeAttribute(NvestLibraryConfig.LIST_VALIDATION_IP);
        GenericDTO.removeAllDynamicParamsContains(NvestLibraryConfig.RIDER_SUM_ASSURED_ANNOTATION);
        GenericDTO.removeAllDynamicParamsContains(NvestLibraryConfig.RIDER_ID_ANNOTATION);
    }

    private void validate() {
        String currentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        try {
            CommonMethod.log(TAG, "validate called");

            int numberOfCheckedRiders = mAdapter.getSelectedCheckBoxCounter();
            if (numberOfCheckedRiders < 1) {
                CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                        getString(R.string.minimum_one_rider), getActivity(), 0);
            } else {
                boolean allAmountsNotEmpty = true;

                for (RiderInformationModel riderInformation : riderInformationModelArrayList) {
                    boolean isNotWop = !riderInformation.isWop();
                    if (isNotWop) {
                        int numberOfEditTexts = editTexts.size();
                        if (numberOfEditTexts > 0) {
                            boolean editTextPresent = editTexts.containsKey(riderInformation);
                            if (editTextPresent) {
                                EditText editText = editTexts.get(riderInformation).getEditText();
                                if (editText != null) {
                                    String editTextValue = editText.getText().toString();
                                    if (editTextValue.isEmpty()) {
                                        allAmountsNotEmpty = false;
                                        editText.setError("Amount can't be empty");
                                    } else {
                                        allAmountsNotEmpty = true;
                                        editText.setError(null);
                                    }
                                }
                            }
                        }
                    }
                }
                if (allAmountsNotEmpty) {
                    // GenericDTO.removeAllDynamicParamsStartsWith(NvestLibraryConfig.RIDER_SUM_ASSURED_ANNOTATION);
                    for (Map.Entry<RiderInformationModel, DynamicEditTextModel> entry : editTexts.entrySet()) {
                        String sumAssuredValue = entry.getValue().getEditText().getText().toString();
                        int riderId = entry.getKey().getRiderId();
                        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.RIDER_SUM_ASSURED_ANNOTATION + riderId, sumAssuredValue, sumAssuredValue, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
                    }
                    validateInformationDataViewModel.ValidateAllRiders();
                } else {
                    CommonMethod.showErrorAlert(getString(R.string.error_dialog_header),
                            getString(R.string.all_fields_with_red_are_mandatory), getActivity(), 0);
                }
            }

        } catch (Exception e) {
            CommonMethod.log(TAG, "Exception " + e.toString());
        }

    }

    private void validateRiderValues(List<ValidationIP> validationIpList) {

        progressBar.setVisibility(View.VISIBLE);
        errors = new HashMap<>();

        for (ValidationIP validationIp : validationIpList) {
            HashMap<String, String> errorMessages = validationIp.getErrorMessage();
            if (errorMessages != null) {
                errors.putAll(errorMessages);
            }
        }

        for (ValidationIP validationIp : validationIpList) {
            List<ValidationIP> wopProducts = validationIp.getWOProducts();
            if (wopProducts != null) {
                for (ValidationIP wopValidationIp : wopProducts) {
                    HashMap<String, String> errorMessages = wopValidationIp.getErrorMessage();
                    if (errorMessages != null) {
                        errors.putAll(errorMessages);
                    }
                }
            }
        }


        if (errors.size() > 0) {
            CommonMethod.showErrorsList(context, activity, errors);
        } else {
            GenericDTO.addAttribute(NvestLibraryConfig.LIST_VALIDATION_IP, validationIpList);
            // mListener.startSummaryFragment(product);
            Bundle args = new Bundle();
            args.putParcelable("products", product);
            Navigation.findNavController(view).navigate(R.id.action_riderFragment_to_summaryFragment, args);
        }
        progressBar.setVisibility(View.GONE);
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
                    setDataToAdapter();
                }
            }
        });
    }

    private void setDataToAdapter() {
        mAdapter = new RiderAdapter(context, riderInformationModelArrayList, this);
        mListRider.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListRider.setItemAnimator(new DefaultItemAnimator());
        mListRider.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mListRider.setAdapter(mAdapter);
    }

    @Override
    public void riderChecked(LinkedHashMap<RiderInformationModel, DynamicEditTextModel> stringDynamicEditTextModelLinkedHashMap) {
        editTexts.clear();
        editTexts.putAll(stringDynamicEditTextModelLinkedHashMap);
    }

    @Override
    public void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData) {

    }

    @Override
    public void onCompleteRidersValidation(MutableLiveData<List<ValidationIP>> validIpListLiveData) {
        if (validIpListLiveData != null) {
            validationIpList = validIpListLiveData.getValue();
            if (validationIpList != null) {
                validateRiderValues(validationIpList);
            }
        }
    }

    @Override
    public void onBiGenerated(LinkedHashMap<Integer, HashMap<String, String>> biData) {

    }

    @Override
    public void onBiUlipGenerated(LinkedHashMap<Integer, HashMap<String, String>>[] biData) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        landingActivity.hideKeyboard(v);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonMethod.log(TAG, "FRAGMENT CHECK:: " + TAG + " destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CommonMethod.log(TAG, "FRAGMENT CHECK:: " + TAG + " detached");
    }
}
