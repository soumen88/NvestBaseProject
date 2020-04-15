package nvest.com.nvestlibrary.commonMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nvest.com.nvestlibrary.BuildConfig;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.LoadNextOption;
import nvest.com.nvestlibrary.nvestWebModel.NumToWords;
import nvest.com.nvestlibrary.nvestWebModel.StringKeyValuePair;
import nvest.com.nvestlibrary.productinformation.ErrorAdapter;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class CommonMethod {
    private static String LOGTAG = CommonMethod.class.getSimpleName();
    public static KeyValueStoreDataViewModel keyValueStoreDataViewModel;

    public static void log(String TAG, String message) {
        Log.e(LOGTAG, TAG + " - " + message);
    }

    public static void logToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkIfStringIsNumber(String inputString) {
        if (inputString.matches("\\d+(?:\\.\\d+)?")) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<String> getDeleteProductList(){
        ArrayList<String> deleteProductList = new ArrayList<>();
        deleteProductList.add("AgeMaster");
        deleteProductList.add("AllocCharge");
        deleteProductList.add("BonusGuarantee");
        deleteProductList.add("BonusNonGuarantee");
        deleteProductList.add("BonusTerminal");
        deleteProductList.add("ChannelDiscount");
        deleteProductList.add("EMRRates");
        deleteProductList.add("Formulas");
        deleteProductList.add("FundAllocationRule");
        deleteProductList.add("FundRuleMap");
        deleteProductList.add("FundStrategyMaster");
        deleteProductList.add("GSV");
        deleteProductList.add("GuaranteeAdd");
        deleteProductList.add("InputKeywords");
        deleteProductList.add("InputOutputMaster");
        deleteProductList.add("LSADMaster");
        deleteProductList.add("MIMaster");
        deleteProductList.add("MortalityCharge");
        deleteProductList.add("OptionRateMaster");
        deleteProductList.add("OptionValidation");
        deleteProductList.add("OutputSummary");
        deleteProductList.add("OutputValidationFormula");
        deleteProductList.add("PW");
        deleteProductList.add("PWAttainedAge");
        deleteProductList.add("PWValidation");
        deleteProductList.add("PensionAnnuity");
        deleteProductList.add("PolAdminCharge");
        //deleteProductList.add("PremiumRates");
        deleteProductList.add("ProductCategoryMap");
        deleteProductList.add("ProductCompanyCategories");
        deleteProductList.add("ProductMaster");
        deleteProductList.add("ProductRiderMap");
        deleteProductList.add("BonusScr");
        deleteProductList.add("EMRMaster");
        deleteProductList.add("FlatExtraMaster");
        deleteProductList.add("FundMapping");
        deleteProductList.add("InputKeywordValues");
        deleteProductList.add("InputValidationFormula");
        deleteProductList.add("InputValidation");
        deleteProductList.add("OptionLevelMaster");
        deleteProductList.add("OptionMaster");
        deleteProductList.add("PremiumMaster");
        deleteProductList.add("ProductChannelMapping");
        deleteProductList.add("ProductCommission");
        deleteProductList.add("ProductMode");
        deleteProductList.add("ProductOtherInfo");
        deleteProductList.add("PtPptMaster");
        deleteProductList.add("RateTable1");
        deleteProductList.add("RiderRuleMap");
        deleteProductList.add("SAMaster");
        deleteProductList.add("SV");
        deleteProductList.add("UlipSAMaster");
        deleteProductList.add("UlipSV");
        deleteProductList.add("FormulasSqlite");
        deleteProductList.add("InputValidationFormulaSQLite");
        deleteProductList.add("OutputFormat");
        deleteProductList.add("ProdNeedDefaultInputs");
        deleteProductList.add("ProdNeedMap");
        return deleteProductList;
    }

    public static boolean createTempFolder(){
        try {
            File file = new File(NvestLibraryConfig.STORAGE_DIRECTORY_PATH);
            if(!file.exists()){
                file.mkdir();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMonth(){
        Calendar now = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(now.getTime());
        return month_name;
    }

    public static String getCurrentDate(){
        Calendar now = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(now.getTime());
        now = Calendar.getInstance();
        now.add(Calendar.YEAR, -3);
        String requiredDate =  now.get(Calendar.DATE) + "-" + month_name.toUpperCase() + "-" +now.get(Calendar.YEAR);
        return requiredDate;
    }

    public static boolean deleteTempfilesIfPresent() {
        try {
            File dir = new File(NvestLibraryConfig.STORAGE_DIRECTORY_PATH);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                if (children.length > 0) {
                    for (int i = 0; i < children.length; i++) {
                        File childFile = new File(dir, children[i]);
                        childFile.delete();
                        //refreshGallery(childFile);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean loadimage(Context context, ImageView imageView, String imageurl, String TAG) {
        final boolean[] complete = {false};
        Glide.with(context)
                .load(imageurl)
                .placeholder(R.drawable.sudlife)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .skipMemoryCache(false)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        CommonMethod.log(TAG, "Loading failed " + e.toString());
                        complete[0] = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        CommonMethod.log(TAG, "Resource is ready ");
                        complete[0] = true;
                        return false;
                    }
                })
                .into(imageView);
        return complete[0];
    }

    public static boolean isLibraryDebuggable(){
        boolean appDebuggable = BuildConfig.LIBRARY_DEBUGABLE;
        return appDebuggable;
    }

    public static void checkEdittext(EditText edittext, TextInputLayout textInputLayout){

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=s.toString();

                if (text != null && !text.isEmpty()) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });

    }
    public static void logQuery(String TAG, String queryString) {
        int start = 0;
        int end = 100;
        int iteratefor = (int) queryString.length() / 100;
        CommonMethod.log(TAG, "Iterate for " + iteratefor);
        for (int k = 0; k < iteratefor; k++) {
            CommonMethod.log(LOGTAG, TAG + queryString.substring(start, end + 1));
            start = end + 1;
            end = end + 100;
        }
        CommonMethod.log(LOGTAG, TAG + queryString.substring(start, queryString.length()));
        //CommonMethod.log(LOGTAG, TAG + " - Subs final " + queryString.substring(start, queryString.length()) + " Length " + queryString.length());
        CommonMethod.log(LOGTAG, TAG + "--------------------------------------------------------------");
    }

    public static int contvertToAge(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date today = new Date(); //Get system date
        //Convert a String to Date
        Date dob = new Date();
        try {
            dob = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonMethod.getDiffYears(dob, today);

    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);


        return cal;
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public static boolean isEmpty(Context context, EditText editText, String stringToCompare) {
        if (editText.getText().toString().trim().equals(stringToCompare)) {
            editText.requestFocus();

            int ecolor = Color.RED; // whatever color you want
            String estring = "Required";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
            editText.setError(ssbuilder);
            editText.requestFocus();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //editText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorRedUnderline));
                editText.setError("Required");
            }
//            editText.setError("Required", context.getResources().getDrawable(R.drawable.missed));
            return true;
        } else {
            editText.setError(null);
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return true;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showErrorAlert(String title, String message, final Activity ctx, final int flag) {

        final Dialog dialog = new Dialog(ctx);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_error_popup);
        // set values for custom dialog components - text, image and button
        TextView errorTitleText = (TextView) dialog.findViewById(R.id.error_popup_title);
        errorTitleText.setText(title);
        TextView textViewOrder = (TextView) dialog.findViewById(R.id.textViewErrorMessage);
        textViewOrder.setText(message);
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.button_ok);
        declineButton.setVisibility(View.VISIBLE);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (flag == 1) {
                    ctx.finish();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    public static void setErrorToSpinner(Spinner spinner, String errormessage) {
        TextView errorText = (TextView) spinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText(errormessage);//changes the selected item text to this
    }

    public static void setAdapter(String TAG, Context context, Spinner spinner, List<String> stringList) {
        try {
            if (stringList.size() > 0) {
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, stringList.toArray(new String[0]));
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                /*ArrayAdapter adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_dropdown_item_1line, stringList.toArray(new String[0]));*/
                spinner.setAdapter(adapter);
                spinner.setSelected(false);
                spinner.setSelection(0, false);
                if (stringList.size() == 1) {
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                }
                else {
                    spinner.setClickable(true);
                    spinner.setEnabled(true);
                }
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception occurred " + e.toString());
        }
    }

    public static int setMaterialSpinnerAdapterUpdated(String TAG, Context context, MaterialSpinner spinner, List<StringKeyValuePair> stringList ,  HashMap<String, String> defaultValues , String key ) {
        int position = -1;
        try {

            if (stringList.size() > 0) {

                Collections.sort(stringList);
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
                spinner.setSelected(false);
                //spinner.setSelection(0,false);
                if (stringList.size() == 1) {
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                } else {
                    spinner.setClickable(true);
                    spinner.setEnabled(true);
                }
                String tempKeyword;
                if (key.contains("@")) {
                    //CommonMethod.log(TAG, "Substring keyword " + key.substring(1));
                    tempKeyword = "@DISPLAY_"+key.substring(1);
                    if(defaultValues != null && defaultValues.containsKey(tempKeyword)){
                        //CommonMethod.log(TAG, "Value in default hash map "  + defaultValues.get(tempKeyword));
                        //if(stringList.contains(defaultValues.get(tempKeyword))){
                        if(stringList.contains(new StringKeyValuePair(defaultValues.get(tempKeyword)))){
                            /*CommonMethod.log(TAG , "Yes contains");
                            CommonMethod.log(TAG , "Position of contains " + stringList.indexOf(new StringKeyValuePair(defaultValues.get(tempKeyword))));*/
                            position =  stringList.indexOf(new StringKeyValuePair(defaultValues.get(tempKeyword)));
                            spinner.setText(defaultValues.get(tempKeyword));
                        }
                        else{
                            CommonMethod.log(TAG , "no it doest not contains");
                        }
                        /*for (int i = 0 ; i < stringList.size() ; i++){
                            StringKeyValuePair stringKeyValuePair = stringList.get(i);
                            if(stringKeyValuePair.getValue().equals(defaultValues.get(tempKeyword))){
                                CommonMethod.log(TAG , "Setting value "+  stringKeyValuePair.getValue());
                                spinner.setText(stringKeyValuePair.getValue());
                                position = i;
                                break;
                            }
                        }*/
                        CommonMethod.log(TAG , "Before contains " + position);
                        return position;
                        //}
                    }
                    else {
                        CommonMethod.log(TAG , "Default value is either null or does not have keyword");
                        return position;
                    }
                }


            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception occurred " + e.toString());
        }
        return position;
    }

    public static void setMaterialSpinnerAdapter(String TAG, Context context, MaterialSpinner spinner, List<StringKeyValuePair> stringList) {
        try {
            if (stringList.size() > 0) {
                Collections.sort(stringList);
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapter);
                spinner.setSelected(false);
                //spinner.setSelection(0,false);
                if (stringList.size() == 1) {
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                } else {
                    spinner.setClickable(true);
                    spinner.setEnabled(true);
                }
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception occurred " + e.toString());
        }
    }

    public static void setDefaultValue(DynamicParams dynamicParams, TextView view) {
        if (dynamicParams != null) {
            view.setText(dynamicParams.getFieldValue());
        }
    }

    public static void setDefaultValue(String value, TextView view) {
        if (value != null) {
            view.setText(value);
        }
    }

    public static void setDefaultValue(String value, MaterialSpinner spinner) {
        if (value != null) {
            spinner.setText(value);
        }
    }


    public static MaterialSpinner createSpinner(Context context, String tagPassed) {
        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsSpinner.setMargins(10, 10, 10, 10);
        final MaterialSpinner spinner = new MaterialSpinner(context);
        spinner.setTag(tagPassed);
        spinner.setPadding(10,25,25,25);
        spinner.setId(GenericDTO.getResourceId());
        spinner.setTextColor(Color.BLACK);
        spinner.setTextSize(13);
        spinner.setLayoutParams(paramsSpinner);
        if(CommonMethod.isLibraryDebuggable()){
            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSkyBlue));
        }

        return spinner;
    }

    public static List<StringKeyValuePair> frequencySpinnerValues(){
        List<StringKeyValuePair> list = new ArrayList<>();
        StringKeyValuePair stringKeyValuePair;

        stringKeyValuePair=new StringKeyValuePair();
        stringKeyValuePair.setKey("-1");
        stringKeyValuePair.setValue(NvestLibraryConfig.SELECT_OPTION);
        list.add(stringKeyValuePair);

        stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("12");
        stringKeyValuePair.setValue("Monthly");
        list.add(stringKeyValuePair);

        stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("4");
        stringKeyValuePair.setValue("Quaterly");
        list.add(stringKeyValuePair);

        stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("2");
        stringKeyValuePair.setValue("Semi-Annual");
        list.add(stringKeyValuePair);

        stringKeyValuePair = new StringKeyValuePair();
        stringKeyValuePair.setKey("1");
        stringKeyValuePair.setValue("Annual");
        list.add(stringKeyValuePair);

        //calculate.setOnClickListener(view->calculate());

        return list;
    }

    public static void createSpinnerAttributes(Context context, MaterialSpinner spinner) {
        if(CommonMethod.isLibraryDebuggable()) {

            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSkyBlue));
        }
        spinner.setError(null);
        spinner.setTextColor(Color.BLACK);
    }

    public static void spinnerErrorAttributes(Context context,MaterialSpinner spinner){
        if(CommonMethod.isLibraryDebuggable()) {
            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));

        }
        spinner.setError("");
    }
    public static void setSpinnerSelectedAttributes(Context context,MaterialSpinner spinner) {
        if (CommonMethod.isLibraryDebuggable()) {
            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSkyBlue));
            spinner.setError(null);
        }

    }

    public static void setEditTextSelectedAttributes(Context context,EditText editText) {
        if (CommonMethod.isLibraryDebuggable()) {
            editText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            editText.setError(null);
        }

    }

    public static void setSpinnerUnSelectedAttributes(Context context,MaterialSpinner spinner){
        if(CommonMethod.isLibraryDebuggable()){
            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        }
    }

    public static MaterialSpinner createRedSpinner(Context context, String tagPassed) {
        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsSpinner.setMargins(10, 10, 10, 10);
        final MaterialSpinner spinner = new MaterialSpinner(context);
        spinner.setTag(tagPassed);
        spinner.setPadding(10,25,25,25);
        spinner.setId(GenericDTO.getResourceId());
        spinner.setTextSize(13);
        spinner.setTextColor(Color.BLACK);
        if(CommonMethod.isLibraryDebuggable()) {
            spinner.setTextColor(ContextCompat.getColor(context, R.color.blackBG));
        }
        spinner.setLayoutParams(paramsSpinner);
        if(CommonMethod.isLibraryDebuggable()){
            spinner.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue));
        }

        return spinner;
    }

    public static EditText createNumberTypeEditText(Context context, String tagPassed) {
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 10);
        final EditText editText = new EditText(context);
        editText.setTag(tagPassed);
        editText.setId(GenericDTO.getResourceId());
        editText.setHint(tagPassed);
        editText.setPadding(10,25,25,25);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setLayoutParams(editTextParams);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        return editText;
    }

    public static EditText createStringTypeEditText(Context context, String tagPassed) {
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 10);
        final EditText editText = new EditText(context);
        editText.setTag(tagPassed);
        editText.setPadding(10,25,25,25);
        editText.setSingleLine(true);
        editText.setTextSize(13);
        editText.setId(GenericDTO.getResourceId());
        //editText.setHint(tagPassed);
        editText.setLayoutParams(editTextParams);
        editText.setSingleLine(true);
        editText.setFocusableInTouchMode(true);
        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.colorCheckboxGray));
        //editText.setBackgroundTintList(colorStateList);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && editText instanceof AppCompatEditText) {
            ((AppCompatEditText) editText).setSupportBackgroundTintList(colorStateList);
        } else {
            ViewCompat.setBackgroundTintList(editText, colorStateList);
        }

        //editText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        return editText;
    }

    public static EditText createDobTypeEditText(Context context, String tagPassed) {
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 10);
        final EditText editText = new EditText(context);
        editText.setTag(tagPassed);
        editText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        editText.setHint(tagPassed);
        editText.setTextSize(13);
        editText.setTextColor(ContextCompat.getColor(context,R.color.blackBG));
        editText.setPadding(10,25,25,25);
        editText.setId(GenericDTO.getResourceId());
        editText.setLayoutParams(editTextParams);
        editText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        editText.setFocusable(false);
        return editText;
    }

    @SuppressLint("NewApi")
    public static TextView createTextView(Context context, String param) {
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 10);
        final TextView textView = new TextView(context);
        textView.setId(GenericDTO.getResourceId());
        textView.setText(param);
        textView.setLayoutParams(editTextParams);
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        //textView.setTextAppearance(R.style.InputLabel);
        return textView;
    }

    public static CheckBox createCheckBox(Context context, String tagPassed) {
        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxParams.setMargins(10, 10, 10, 10);
        final CheckBox checkBox = new CheckBox(context);
        checkBox.setId(GenericDTO.getResourceId());
        checkBox.setTag(tagPassed);
        checkBox.setText(tagPassed);
        checkBox.setLayoutParams(checkBoxParams);
        return checkBox;
    }

    public static String convertIntoWords(String number) {
        if (number.length() > 0) {
            NumToWords words = new NumToWords();
            double d = Double.parseDouble(number);
            long num = (long) d;
            String wordsFromNum = "Rupees " + words.convertNumberToWords(num) + " Only";
            return wordsFromNum.replaceAll(" {2}", " ");
        } else {
            return null;
        }
    }

    public static String convertListToCommaSeparatedValues(List<String> listPassed) {
        String idList = listPassed.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        return csv;
    }

    public static LoadNextOption createLoadNextOption(int optionLevel, int productId, String parentIdSelected, String pt, String ppt, String sender) {
        LoadNextOption loadNextOption = new LoadNextOption();
        loadNextOption.setProductId(productId);
        loadNextOption.setOptionlevel(optionLevel);
        int changeLevel = optionLevel - 1;
        loadNextOption.setChangelevel(String.valueOf(changeLevel));
        loadNextOption.setPt(pt);
        loadNextOption.setPpt(ppt);
        loadNextOption.setSender(sender);
        loadNextOption.setParentIdList(parentIdSelected);
        return loadNextOption;
    }

    public static void insertIntoKeyValueStore(String TAG, Context context, String key, String value, String tagPassed, String fieldName) {
        CommonMethod.log(TAG, "Insert common method");
        keyValueStoreDataViewModel = ViewModelProviders.of((FragmentActivity) context).get(KeyValueStoreDataViewModel.class);
        keyValueStoreDataViewModel.insertIntoKeyValueStore(TAG, key, value, tagPassed, fieldName);
    }

    public static String ReplaceParams(String Query, HashMap<String, String> Param, boolean isValid) {
        Set<String> argumentSet = new HashSet<>();
        try {
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(Query);
            while (matcher.find()) {
                argumentSet.add(matcher.group());
            }
            for (String arg : argumentSet) {
                String valueReceived = (String) Param.get(arg.toUpperCase());
                if (valueReceived != null) {
                    try {
                        Float isnumeric = Float.parseFloat(Param.get(arg.toUpperCase()));
                        Query = Query.replaceAll("(?<!\\\\\\\\S)"+arg+"(?<!\\\\\\\\S)",valueReceived );
                    }
                    catch(Exception e) {
                        Query = Query.replaceAll("(?<!\\\\\\\\S)"+arg+"(?<!\\\\\\\\S)","'" + valueReceived +"'" );
                    }
                    CommonMethod.log(LOGTAG , "Modified query " + Query);
                }
                else {
                    CommonMethod.log(LOGTAG , "Did not find parameter value for " + arg);
                }
            }

            if (isValid == true) {
                Query = Query.toUpperCase().replace("POLICYYEAR", "1").replace("POLICYDURATION", "1").replace("POLICYMONTH", "1");
            }
            Query = Query.toUpperCase().replace("ISNULL", "IFNULL");
            Query = Query.toUpperCase().replace("GETDATE()", "Date('now')");
            Query = Query.toUpperCase().replace("2,1", "2");
            Query = Query.toUpperCase().replace("CONVERT(DECIMAL(16,14)", "ROUND(");
            Query = Query.toUpperCase().replace("CONVERT(DECIMAL(10,4),", "(");
            Query = Query.toUpperCase().replace("FLOOR", "ROUND");
            return Query;
        }
        catch (Exception e){
            log(LOGTAG , "Exception in raw query " + e.toString());
        }
        return null;
    }

    //Used for replacemnet when using ScriptEngine
    public static String ReplaceParamsUlip(String Query) {

        Query = Query.toUpperCase().replace("]", "\"))").replace("[", "(paramValues.get(\"");
        // Query=Query.toUpperCase().replace("]","\"))").replace("[","parseFloat(paramValues.get(\"");
        try {
            //  Query = Query.replace("MAX(", "Math.max(").replace("MIN(", "Math.min(");;
            Query = Query.replace("MAX(", "Collections.max(Arrays.asList(").replace("MIN(", "Collections.min(Arrays.asList(");
            ;
            if (Query.contains("Collections")) {
                Query = Query + ")";
            }
            return Query;
        } catch (Exception e) {
            log(LOGTAG, "Exception in raw query " + e.toString());
        }
        return null;
    }

    public static void amal(String TAG) {
        CommonMethod.log(TAG, "this is a test 2");
    }

    public static int convertToAge(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date(); //Get system date
        //Convert a String to Date
        Date dob = new Date();
        try {
            dob = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonMethod.getDiffYears(dob, today);

    }

    public static DynamicParams addDynamicKeyWordToGenericDTO(String key, String fieldKey, String fieldValue, String fileName, String fieldType, String method) {
        DynamicParams dynamicParams = new DynamicParams(fieldKey, fieldValue, fileName, fieldType, method);
        GenericDTO.addDynamicParam(key, dynamicParams);
        GenericDTO.addAttribute(key, fieldValue);
        return dynamicParams;
    }

    public static DynamicParams addDynamicParamListToGenericDTO(String key, String fieldKey, String fieldValue, String fileName, String fieldType, String method) {
        DynamicParams dynamicParams = new DynamicParams(fieldKey, fieldValue, fileName, fieldType, method);
        GenericDTO.addListDynamicParams(key, dynamicParams);
        GenericDTO.addListAttribute(key, fieldValue);
        return dynamicParams;
    }

    public static void addCombinedProductsToGenericDTO(int id, String key, String fieldKey, String fieldValue, String fileName, String fieldType, String method) {
        DynamicParams dynamicParams = new DynamicParams(fieldKey, fieldValue, fileName, fieldType, method);
        GenericDTO.addCombinedProduct(id,key,dynamicParams);
    }


    public static String getfieldKeyFromGenericDTO(String fieldKey) {
        DynamicParams dynamicParams = GenericDTO.getDynamicParamByKey(fieldKey);
        return dynamicParams != null ? dynamicParams.getFieldKey() : "";
    }

    public static String getfieldValueFromGenericDTO(String fieldKey) {
        DynamicParams dynamicParams = GenericDTO.getDynamicParamByKey(fieldKey);
        return dynamicParams != null ? dynamicParams.getFieldValue() : "";
    }

    public static String getCommaSeparatedAmount(double amount, int decimalPrecision) {
        // default pattern
        StringBuilder patternBuilder = new StringBuilder("#,##,##,##,#00");

        // add decimals in pattern
        if (decimalPrecision > 0) {
            patternBuilder.append(".");
            for (int i = 0; i < decimalPrecision; i++) {
                patternBuilder.append("0");
            }
        }

        // generate pattern
        String pattern = patternBuilder.toString();

        // generate decimal formatter
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern(pattern);

        return formatter.format(amount);

    }

    public static String getCommaSeparatedAmount(float amount, int decimalPrecision) {
        return getCommaSeparatedAmount((double) amount, decimalPrecision);
    }

    public static HashMap<String, String> getAllParamsFromGenericDTO() {
        HashMap<String, String> genericDTOParams = new LinkedHashMap<>();
        Map<String, List<DynamicParams>> all = GenericDTO.getAllDynamicResultSetMap();
        for (Map.Entry<String, List<DynamicParams>> entry : all.entrySet()) {
            String key = entry.getKey();
            // CommonMethod.log("Generic", "Key " + key);
            String value = "";
            for (DynamicParams dynamicParams : entry.getValue()) {
                value = dynamicParams.getFieldKey();
                //   CommonMethod.log("Generic", "Value " + value);
            }
            genericDTOParams.put(key, value);
        }
        return genericDTOParams;
    }
    //Added for soumen
    public static HashMap<String, String> getAllParamsFromDynamicParms(Map<String, DynamicParams> dynParams) {
        HashMap<String,String> Params = new HashMap<>();
        for (Map.Entry<String, DynamicParams> entry : dynParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getFieldKey();
            Params.put(key, value);
        }
        return Params;
    }

    public static String getSpinnerKey(MaterialSpinner mSpinner) {
        String Key = "0";
        try {
            Key = ((StringKeyValuePair) mSpinner.getItems().get(mSpinner.getSelectedIndex())).getKey();
        } catch (Exception ex) {
            Key = "0";
        }
        return Key;
    }

    public static void showErrorsList(Context context, Activity activity, HashMap<String, String> errorMessages) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            View errorView = layoutInflater.inflate(R.layout.item_validation_error_popup, null);

            RecyclerView errorRecyclerView = errorView.findViewById(R.id.recyclerviewError);
            Button btnOk = errorView.findViewById(R.id.buttonOk);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            int width = displayMetrics.widthPixels - 80;

            // set popup properties
            PopupWindow errorPopup = new PopupWindow(errorView, width, height, true);
            errorPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            errorPopup.setOutsideTouchable(true);
            errorPopup.setFocusable(true);

            // set recyclerview properties
            ErrorAdapter errorAdapter = new ErrorAdapter(context, errorMessages);
            errorRecyclerView.setAdapter(errorAdapter);
            errorRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            errorRecyclerView.setItemAnimator(new DefaultItemAnimator());
            errorAdapter.notifyDataSetChanged();

            // errorPopup.showAtLocation(errorView, Gravity.CENTER, 0, 0);

            AlertDialog errorDialog = new AlertDialog.Builder(context)
                    .setView(errorView)
                    .create();
            errorDialog.show();

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (errorPopup.isShowing()) {
                        errorPopup.dismiss();
                    }
                    if (errorDialog.isShowing()) {
                        errorDialog.dismiss();
                    }
                }
            });
        }
    }

    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
