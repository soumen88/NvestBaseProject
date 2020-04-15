package nvest.com.nvestlibrary.solutionDetails;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PdfGenerator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.commonMethod.ViewTag;
import nvest.com.nvestlibrary.generatePdf.PdfUtils;
import nvest.com.nvestlibrary.generatePdf.models.ModeMaster;
import nvest.com.nvestlibrary.landing.LandingActivity;
import nvest.com.nvestlibrary.landing.ProductDataViewModel;
import nvest.com.nvestlibrary.landing.SolutionVieModel;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolutionDetailsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, SolutionVieModel.SolutionListener, SolutionProductsAdapter.SolutionProductsListener, ProductDataViewModel.ProductDataListener, ValidateInformationDataViewModel.ValidateInformationDataListener, PdfGenerator.OnPdfGeneratedListener {
    private static final String TAG = SolutionDetailsFragment.class.getSimpleName();
    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    // pojos..
    private SolutionMaster solutionMaster;
    private Products productReceived;

    // adapters
    private SolutionProductsAdapter solutionProductsAdapter;

    // view models
    private SolutionVieModel solutionVieModel;
    private ProductDataViewModel productDataViewModel;
    private ValidateInformationDataViewModel validateInformationDataViewModel;

    // collections
    private List<SolutionProducts> solutionProductsList;
    private List<Products> productsList;
    private List<View> fieldsList;
    private List<ValidationIP> validationIpList;

    // context and activity
    private Context context;
    private LandingActivity landingActivity;
    private Activity activity;

    // views
    private View view;
    private FrameLayout frameLayout;
    private TextView tvSolutionName, tvProgress;
    private MaterialSpinner spGender, spMode;
    private MaterialEditText etFirstName, etLastName, etDob, etAge, etTotalPremium;
    private RecyclerView rvProducts;
    private Button btnGenerateCombo;
    private LinearLayout layoutDob, layoutProgress, layoutTotalPremium;
    private ProgressBar pbGeneratingPdf;

    // alert dialogs
    private AlertDialog storagePermissionExplainerDialog, permissionDeniedDialog;

    // async tasks
    private GenerateHtmlTask generateHtmlTask;
    private GeneratePdfTask generatePdfTask;

    // other
    private boolean flag;
    private int productIndex = -1;
    private String comboProductHtml;
    private PdfUtils pdfUtils;
    private boolean askTotalPremium;
    private boolean hasSumAssured, hasAnnualPremium, hasModalPremium;


    public SolutionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_solution_details, container, false);

        // set solutionMaster name as title
        Bundle bundle = getArguments();
        if (bundle != null) {
            solutionMaster = bundle.getParcelable("solution");
            if (solutionMaster != null) {
                tvSolutionName = view.findViewById(R.id.text_solution_name);
                tvSolutionName.setText(solutionMaster.getSolutionName());
                askTotalPremium = solutionMaster.getPremiumInput().contains("Single");
            }
        }

        init();
        setViewTags();
        setListeners();
        setGenderSpinnerData();
        setModeSpinnerData();

        solutionVieModel.getSolutionProducts(solutionMaster.getId());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (generateHtmlTask != null) {
            generateHtmlTask.cancel(true);
        }

        if (generatePdfTask != null) {
            generatePdfTask.cancel(true);
        }
    }


    private void init() {


        // initialize views
        frameLayout = view.findViewById(R.id.touchLayout);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etDob = view.findViewById(R.id.etDob);
        etAge = view.findViewById(R.id.etAge);
        etTotalPremium = view.findViewById(R.id.etTotalPremium);
        spGender = view.findViewById(R.id.spGender);
        spMode = view.findViewById(R.id.spMode);
        rvProducts = view.findViewById(R.id.rvProducts);
        btnGenerateCombo = view.findViewById(R.id.btnGenerateCombo);
        layoutDob = view.findViewById(R.id.layoutDob);
        layoutTotalPremium = view.findViewById(R.id.layoutTotalPremium);
        layoutProgress = view.findViewById(R.id.layoutProgress);
        pbGeneratingPdf = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tvProgress);

        if (askTotalPremium) {
            layoutTotalPremium.setVisibility(View.VISIBLE);
            etTotalPremium.setVisibility(View.VISIBLE);
        } else {
            layoutTotalPremium.setVisibility(View.GONE);
            etTotalPremium.setVisibility(View.GONE);
        }

        fieldsList = new ArrayList<>();
        fieldsList.add(spGender);
        fieldsList.add(spMode);
        fieldsList.add(etFirstName);
        fieldsList.add(etLastName);
        fieldsList.add(etDob);
        fieldsList.add(etAge);
        fieldsList.add(etTotalPremium);


        // initialize context and activity
        context = getContext();
        activity = getActivity();
        landingActivity = (LandingActivity) activity;

        // initialize view models
        solutionVieModel = ViewModelProviders.of(this).get(SolutionVieModel.class);
        productDataViewModel = ViewModelProviders.of(this).get(ProductDataViewModel.class);
        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);

        // create dialogs
        storagePermissionExplainerDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(R.string.permission_storage_explanation)
                .setPositiveButton(R.string.permission_storage_request_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestStoragePermission();
                    }
                }).setNegativeButton(R.string.permission_storage_not_now_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hideProgressBar();
                    }
                }).create();

        permissionDeniedDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_storage_permission_denied_dialog_title)
                .setMessage(R.string.permission_storage_permission_denied_message)
                .setPositiveButton(R.string.permission_storage_retry_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestStoragePermission();
                    }
                }).setNegativeButton(R.string.permission_storage_i_am_sure_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hideProgressBar();
                    }
                }).create();

    }

    private void setViewTags() {
        tvSolutionName.setTag(ViewTag.PRODUCT_TITLE);
        spGender.setTag(ViewTag.SP_GENDER);
        spMode.setTag(ViewTag.SP_MODE);

        etFirstName.setTag(ViewTag.ET_FIRST_NAME);
        etLastName.setTag(ViewTag.ET_LAST_NAME);
        etDob.setTag(ViewTag.ET_DOB);
        etAge.setTag(ViewTag.ET_AGE);
        etTotalPremium.setTag(ViewTag.ET_TOTAL_PREMIUM);

        btnGenerateCombo.setTag(ViewTag.BTN_GENERATE_COMBO);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        // set view listeners
        frameLayout.setOnTouchListener(this);
        etDob.setOnClickListener(this);
        btnGenerateCombo.setOnClickListener(this);
        tvSolutionName.setOnClickListener(this);


        //set view model listeners
        solutionVieModel.setSolutionListener(this);
        productDataViewModel.setProductDataListener(this);
        validateInformationDataViewModel.setValidateInformationDataListener(this);

        /*//set adapter listener
        solutionProductsAdapter.setSolutionProductsListener(this);*/
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        landingActivity.hideKeyboard(v);
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals(ViewTag.ET_DOB)) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.AppDatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    int age = CommonMethod.convertToAge(date);
                    // layoutDob.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f));
                    LinearLayout.LayoutParams layoutDobParams = (LinearLayout.LayoutParams) layoutDob.getLayoutParams();
                    layoutDobParams.weight = 0.7f;
                    layoutDob.setLayoutParams(layoutDobParams);
                    etDob.setText(date);
                    etAge.setText(String.valueOf(age));
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            if (!datePickerDialog.isShowing()) {
                datePickerDialog.show();
            }

        } else if (view.getTag().equals(ViewTag.BTN_GENERATE_COMBO)) {
            boolean isValid = doFieldValidation();
            if (isValid) {
                addValuesInGenericDto();
                if (validate()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                storagePermissionExplainerDialog.show();
                            } else {
                                requestStoragePermission();
                            }
                        } else {
                            savePdf();
                        }
                    } else {
                        savePdf();
                    }
                }
            } else {
                CommonMethod.showErrorAlert(activity.getString(R.string.error_dialog_header),
                        activity.getString(R.string.empty_validation_error), activity, 0);
            }
        } else if (view.getTag().equals(ViewTag.PRODUCT_TITLE)) {
            etFirstName.setText("John");
            etLastName.setText("Doe");
            etTotalPremium.setText("850000");
        }
    }

    private boolean validate() {
        String currentMethod = "validate()";

        boolean valid = true;
        validationIpList = new ArrayList<>();
        disableBtn();
        showProgressBar();
        tvProgress.setText("Validating values...");
        HashMap<String, String> errorMessages = new HashMap<>();
        for (Products product : productsList) {
            HashMap<String, String> params = GenericDTO.getAllCombinedProductsMap(product.getProductId());

            /*if (askTotalPremium) {
                HashMap<String, Double> totalPremiumReverseCalculation = validateInformationDataViewModel.ReverseCalculate(params);

                if (params.containsKey(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(product.getProductId(), NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("SumAssured")), String.valueOf(totalPremiumReverseCalculation.get("SumAssured")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                if (params.containsKey(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(product.getProductId(), NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("AnnualPremium")), String.valueOf(totalPremiumReverseCalculation.get("AnnualPremium")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                if (params.containsKey(NvestLibraryConfig.MODAL_PREM_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(product.getProductId(), NvestLibraryConfig.MODAL_PREM_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("ModalPremium")), String.valueOf(totalPremiumReverseCalculation.get("ModalPremium")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                params = GenericDTO.getAllCombinedProductsMap(product.getProductId());
            }*/

            ValidationIP validIp = validateInformationDataViewModel.ValidateInput(params);
            validationIpList.add(validIp);
            HashMap<String, String> validationErrors = validIp.getErrorMessage();
            if (validationErrors != null) {
                errorMessages.putAll(validationErrors);
            }
        }

        if (errorMessages.size() > 0) {
            valid = false;
            CommonMethod.showErrorsList(context, activity, errorMessages);
            enableBtn();
            hideProgressBar();
        }

        return valid;
    }

    private String generateHtml() {
        HashMap<Integer, Map<String, DynamicParams>> allComboProducts = GenericDTO.getAllCombinedProducts();
        HashMap<String, Object> biOutput = validateInformationDataViewModel.ClearBIOP(allComboProducts);
        // HashMap<String, Object> biOutput = validateInformationDataViewModel.ClearBIOP();

        Cursor biMasterCursor = (Cursor) biOutput.get("BIMaster");
        Cursor biDetailsCursor = (Cursor) biOutput.get("AllDetails");
        LinkedHashMap<Integer, HashMap<String, String>> comboData4 = (LinkedHashMap<Integer, HashMap<String, String>>) biOutput.get("SumDt");
        LinkedHashMap<Integer, HashMap<String, String>> comboData8 = (LinkedHashMap<Integer, HashMap<String, String>>) biOutput.get("SumDt8");

        pdfUtils = new PdfUtils(context, comboData4, comboData8, biMasterCursor, biDetailsCursor, productsList);
        pdfUtils.setOnPdfGeneratedListener(this);
        pdfUtils.setValidationIpList(validationIpList);

        String comboHtml = pdfUtils.generateComboProductsHtml();
        return comboHtml;
    }

    private void generatePdf() {
        if (comboProductHtml != null) {
            // save pdf in external documents directory
            File path;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Nvest/");
            } else {
                path = new File(Environment.getExternalStorageDirectory() + "/Documents/Nvest/");
            }
            String pdfFileName = pdfUtils.getPdfFileName(solutionMaster);
            pdfUtils.generatePdf(path, comboProductHtml, pdfFileName);
        }
    }

    private void savePdf() {
        // generate combo html
        generateHtmlTask = new GenerateHtmlTask();
        try {
            comboProductHtml = generateHtmlTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // generate pdf
        generatePdfTask = new GeneratePdfTask();
        generatePdfTask.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NvestLibraryConfig.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePdf();
            } else {
                permissionDeniedDialog.show();
            }
        }
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, NvestLibraryConfig.STORAGE_PERMISSION_REQUEST_CODE);
    }

    private boolean doFieldValidation() {
        boolean error = false;
        for (View view : fieldsList) {
            if (view instanceof MaterialEditText && view.getVisibility() == View.VISIBLE) {
                MaterialEditText materialEditText = (MaterialEditText) view;
                Editable editable = materialEditText.getText();
                if (editable != null) {
                    String etText = editable.toString();
                    if (etText.isEmpty()) {
                        materialEditText.setError("Required");
                        error = true;
                    }
                }
            } else if (view instanceof MaterialSpinner) {
                MaterialSpinner materialSpinner = (MaterialSpinner) view;
                if (materialSpinner.getText().toString().isEmpty()) {
                    materialSpinner.setError("");
                    error = true;
                }
            }
        }

        return !error;
    }

    private void addValuesInGenericDto() {
        String currentMethod = "addValuesInGenericDto()";


        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String fullName = firstName + lastName;
        String gender = spGender.getText().toString();
        String dob = etDob.getText().toString();
        String age = etAge.getText().toString();
        String totalPremium = etTotalPremium.getText().toString();
        String mode = spMode.getText().toString();

        for (Products product : productsList) {
            int productId = product.getProductId();

            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.LI_NAME_ANNOTATION, fullName, fullName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.LI_FIRST_NAME_ANNOTATION, firstName, firstName, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.LI_LAST_NAME_ANNOTATION, lastName, lastName, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.GENDER_ANNOTATION, CommonMethod.getSpinnerKey(spGender), gender, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.LI_DOB_ANNOTATION, dob, dob, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.LA_AGE_ANNOTATION, age, age, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            CommonMethod.addCombinedProductsToGenericDTO(productId, "@TOTAL_PREMIUM", totalPremium, totalPremium, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
            // CommonMethod.addCombinedProductsToGenericDTO(productId, NvestLibraryConfig.INPUT_MODE_ANNOTATION, CommonMethod.getSpinnerKey(spMode), mode, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        }

        /*CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_NAME_ANNOTATION, fullName, fullName, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_FIRST_NAME_ANNOTATION, firstName, firstName, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_LAST_NAME_ANNOTATION, lastName, lastName, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.GENDER_ANNOTATION, CommonMethod.getSpinnerKey(spGender), gender, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LI_DOB_ANNOTATION, dob, dob, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.LA_AGE_ANNOTATION, age, age, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO("@TOTAL_PREMIUM", totalPremium, totalPremium, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
        CommonMethod.addDynamicKeyWordToGenericDTO("@PR_CHANNEL", CommonMethod.getSpinnerKey(spMode), mode, TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);*/

    }

    private void setGenderSpinnerData() {
        List<StringKeyValuePair> genders = new ArrayList<>();

        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("M");
        stringKeyValuePair.setValue("Male");
        genders.add(stringKeyValuePair);

        stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("F");
        stringKeyValuePair.setValue("Female");
        genders.add(stringKeyValuePair);

        CommonMethod.setMaterialSpinnerAdapter(TAG, context, spGender, genders);
    }


    private void setModeSpinnerData() {
        solutionVieModel.getModes();
    }


    @Override
    public void onSolutionChanged(boolean complete) {

    }

    @Override
    public void onProductsChanged(boolean complete) {
        if (complete) {
            solutionVieModel.getProductLiveDataList().observe(this, products -> productsList = products);
        }
    }

    @Override
    public void onModeRecieved(boolean modeRecieved) {
        if (modeRecieved) {
            solutionVieModel.getModeLiveDataList().observe(this, modeMasters -> {
                List<StringKeyValuePair> modes = new ArrayList<>();
                if (modeMasters != null) {
                    for (ModeMaster modeMaster : modeMasters) {
                        StringKeyValuePair stringKeyValuePair = new StringKeyValuePair();
                        stringKeyValuePair.setKey(String.valueOf(modeMaster.getFrequency()));
                        stringKeyValuePair.setValue(modeMaster.getModeName());
                        modes.add(stringKeyValuePair);
                    }
                }
                CommonMethod.setMaterialSpinnerAdapter(TAG, context, spMode, modes);
            });
        }
    }

    @Override
    public void onSolutionProductsChanged(boolean solutionProductsChanged) {
        if (solutionProductsChanged) {
            solutionVieModel.getSolutionProductsLiveDataList().observe(this, solutionProducts -> {
                String currentMethod = "onSolutionProductsChanged()";

                // clear generic dto
                /*GenericDTO.clearDynamicParams();
                GenericDTO.clearResultSetMap();*/
                if (!flag) {
                    flag = true;
                    if (solutionProducts != null) {
                        // set recycler view adapter
                        solutionProductsList = solutionProducts;
//                        solutionProductsAdapter.setSolutionProducts(solutionProductsList);
//                        rvProducts.setAdapter(solutionProductsAdapter);

                        // get productsList
                        int[] productIds = new int[solutionProductsList.size()];
                        for (int i = 0; i < solutionProductsList.size(); i++) {
                            productIds[i] = solutionProductsList.get(i).getProductId();
                        }
                        solutionVieModel.getProducts(productIds);

                        // add data in generic dto
                        for (SolutionProducts solutionProduct : solutionProductsList) {
                            String inputString = solutionProduct.getInputString();
                            try {
                                JSONArray jsonInputString = new JSONArray(inputString);
                                for (int i = 0; i < jsonInputString.length(); i++) {
                                    JSONObject jsonObject = jsonInputString.getJSONObject(i);

                                    String key = "";
                                    String value = "";

                                    if (jsonObject.has("key")) {
                                        key = jsonObject.getString("key");
                                    }

                                    if (jsonObject.has("value")) {
                                        value = jsonObject.getString("value");
                                    }

                                    if (!key.isEmpty()) {
                                        // CommonMethod.addDynamicParamListToGenericDTO(key, value, value, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                                        CommonMethod.addCombinedProductsToGenericDTO(solutionProduct.getProductId(), key, value, value, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                setupRecycelerView();
            });
        }
    }

    @Override
    public void onInputKeywordsChanged(boolean inputKeywordsChanged) {
        if (inputKeywordsChanged) {
            solutionVieModel.getInputKeywordsLiveDataList().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> inputKeywordList) {
                    if (inputKeywordList != null) {

                        hasSumAssured = inputKeywordList.contains(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
                        hasAnnualPremium = inputKeywordList.contains(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION);
                        hasModalPremium = inputKeywordList.contains(NvestLibraryConfig.MODAL_PREM_ANNOTATION);
                        int prId = productReceived.getProductId();
                        System.out.println(prId + "" + hasSumAssured + "" + hasAnnualPremium + "" + hasModalPremium);
                    }
                }
            });
        }
    }

    private void setupRecycelerView() {
        // initialize adapter
        solutionProductsAdapter = new SolutionProductsAdapter(context, solutionProductsList, this);

        // set recycler view properties
        rvProducts.setNestedScrollingEnabled(false);
        rvProducts.setLayoutManager(new LinearLayoutManager(context));
        rvProducts.setAdapter(solutionProductsAdapter);
    }

    @Override
    public void onProductEdit(SolutionProducts solutionProducts) {
        String currentMethod = "onProductEdit()";
        boolean isValid = doFieldValidation();
        if (isValid) {
            addValuesInGenericDto();

            productDataViewModel.getProductById(solutionProducts.getProductId());

            HashMap<String, String> params = GenericDTO.getAllCombinedProductsMap(solutionProducts.getProductId());

            /*if (askTotalPremium) {
                HashMap<String, Double> totalPremiumReverseCalculation = validateInformationDataViewModel.ReverseCalculate(params);

                if (params.containsKey(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(solutionProducts.getProductId(), NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("SumAssured")), String.valueOf(totalPremiumReverseCalculation.get("SumAssured")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                if (params.containsKey(NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(solutionProducts.getProductId(), NvestLibraryConfig.PR_ANN_PREMIUM_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("AnnualPremium")), String.valueOf(totalPremiumReverseCalculation.get("AnnualPremium")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                if (params.containsKey(NvestLibraryConfig.MODAL_PREM_ANNOTATION)) {
                    CommonMethod.addCombinedProductsToGenericDTO(solutionProducts.getProductId(), NvestLibraryConfig.MODAL_PREM_ANNOTATION, String.valueOf(totalPremiumReverseCalculation.get("ModalPremium")), String.valueOf(totalPremiumReverseCalculation.get("ModalPremium")), TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                }

                params = GenericDTO.getAllCombinedProductsMap(solutionProducts.getProductId());
            }*/

            // add data in hashmap for product prefill values

            /*String inputString = solutionProducts.getInputString();
            try {
                JSONArray jsonInputString = new JSONArray(inputString);
                for (int i = 0; i < jsonInputString.length(); i++) {
                    JSONObject jsonObject = jsonInputString.getJSONObject(i);

                    String key = "";
                    String value = "";

                    if (jsonObject.has("key")) {
                        key = jsonObject.getString("key");
                    }

                    if (jsonObject.has("value")) {
                        value = jsonObject.getString("value");
                    }

                    if (!key.isEmpty()) {
                        defaultValues.put(key, value);
                        // CommonMethod.addDynamicParamListToGenericDTO(key, value, value, TAG, String.valueOf(NvestLibraryConfig.FieldType.String), currentMethod);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


            Bundle args = new Bundle();
            args.putParcelable("solutionProduct", solutionProducts);
            args.putParcelable("solution", solutionMaster);
            args.putParcelable("products", productReceived);
//        args.putInt("productIndex", productIndex);
            args.putSerializable("defaultValues", params);

            Navigation.findNavController(view).navigate(R.id.action_solutionDetailsFragment_to_productInformationFragment, args);
        } else {
            CommonMethod.showErrorAlert(activity.getString(R.string.error_dialog_header),
                    activity.getString(R.string.empty_validation_error), activity, 0);
        }
    }

    @Override
    public void completestatus(int productsize) {
        // ignore
    }

    @Override
    public void onProductLiveDataReceived(boolean complete) {
        /*if (productLiveData != null) {
            product = productLiveData.getValue();
        }*/
        if (complete) {
            productDataViewModel.setSingleProductLiveData().observe(this, productLiveData -> {
                productReceived = productLiveData;
                if (productReceived != null) {
                    solutionVieModel.getInputKeywords(productReceived.getProductId());
                }
            });
        }

    }

    @Override
    public void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData) {
        // ignore
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
    public void pdfGenerated(String pdfFilePath, String pdfFileName) {
        try {
            Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(pdfFilePath));
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.setDataAndType(path, "application/pdf");
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open pdf file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open " + pdfFileName + ". Please try again later.", Toast.LENGTH_SHORT).show();
        } finally {
            enableBtn();
            hideProgressBar();
            tvProgress.setText("");
        }
    }

    class GenerateHtmlTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disableBtn();
            showProgressBar();
            tvProgress.setText("Preparing PDF generation...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            return generateHtml();
        }
    }

    class GeneratePdfTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvProgress.setText("Generating PDF...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            generatePdf();
            return null;
        }
    }

    private void disableBtn() {
        if (btnGenerateCombo.isEnabled()) {
            btnGenerateCombo.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnGenerateCombo.setBackgroundColor(getResources().getColor(R.color.colorRedDisabled, null));
                btnGenerateCombo.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            } else {
                btnGenerateCombo.setBackgroundColor(getResources().getColor(R.color.colorRedDisabled));
                btnGenerateCombo.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    private void enableBtn() {
        if (!btnGenerateCombo.isEnabled()) {
            btnGenerateCombo.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnGenerateCombo.setBackgroundColor(getResources().getColor(R.color.colorRed, null));
                btnGenerateCombo.setTextColor(getResources().getColor(android.R.color.white, null));
            } else {
                btnGenerateCombo.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btnGenerateCombo.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void hideProgressBar() {
        if (layoutProgress.getVisibility() != View.GONE) {
            layoutProgress.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (layoutProgress.getVisibility() != View.VISIBLE) {
            layoutProgress.setVisibility(View.VISIBLE);
        }
    }
}