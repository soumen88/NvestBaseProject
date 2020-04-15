package nvest.com.nvestlibrary.productinformation;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.FundsModel;

/**
 * Created by Soumen on 6/21/2017.
 */

public class FundsAdapter extends RecyclerView.Adapter<FundsAdapter.MyViewHolder> {
    private static final String TAG = FundsAdapter.class.getSimpleName();
    private Context mContext;
    private List<FundsModel> mFundsArrayList;
    private int totalFundPercentage;
    private FundsValueListener fundsValueListener;
    private HashMap<String, EditText> etFundValues;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFundName;
        EditText etFundPercentValue;

        MyViewHolder(View view) {
            super(view);
            tvFundName = view.findViewById(R.id.funds_name);
            etFundPercentValue = view.findViewById(R.id.edit_funds_percent);
        }
    }

    public interface FundsValueListener {
        void totalValueChanged(int totalFundPercentage);
    }

    FundsAdapter(Context context, List<FundsModel> arrayList) {
        this.mContext = context;
        this.mFundsArrayList = arrayList;
        // fundsValueListener.fundsValueChanged(totalFundPercentage);
        etFundValues = new HashMap<>();
    }

    public void setFundsValueListener(FundsValueListener fundsValueListener) {
        this.fundsValueListener = fundsValueListener;
    }

    @Override
    public FundsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.funds_list_row, parent, false);
        return new FundsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FundsAdapter.MyViewHolder holder, final int position) {
        FundsModel fundsModel = mFundsArrayList.get(position);
        holder.tvFundName.setText(fundsModel.getFundName());
        etFundValues.put(String.valueOf(fundsModel.getFundId()), holder.etFundPercentValue);


        holder.etFundPercentValue.setTag(position);
        holder.etFundPercentValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    int currentPosition = (int) v.getTag();
                    if (currentPosition == position) {
                        String fundsEnteredPercent = holder.etFundPercentValue.getText().toString();
                        int fundsEnteredInteger = 0;
                        try {
                            fundsEnteredInteger = Integer.parseInt(fundsEnteredPercent);
                            //mFragment.selectedFundValue.put(mFundsArrayList.get(position),fundsEnteredPercent);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (fundsEnteredInteger < 10 && fundsEnteredInteger != 0) {
                            v.setBackgroundResource(R.drawable.rectangular_red_edit_text);
                            //mFragment.setFundsError(mContext.getString(R.string.entered_funds_error));
                        } else {
                            // mFragment.setFundsError("");
                            v.setBackgroundResource(R.drawable.rectangular_edit_text);
                        }
                        //mFragment.setSumText(mFundsPercentSum);

                    }
                } else if (holder.etFundPercentValue.getText().length() > 0) {
                    int removePercentInt = 0;
                    String removePercent = holder.etFundPercentValue.getText().toString();
                    try {
                        removePercentInt = Integer.parseInt(removePercent);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        holder.etFundPercentValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int currentPosition = (int) v.getTag();
                if (currentPosition == position) {
                    String fundsEnteredPercent = holder.etFundPercentValue.getText().toString();
                    int fundsEnteredInteger = 0;
                    try {
                        fundsEnteredInteger = Integer.parseInt(fundsEnteredPercent);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (fundsEnteredInteger < 10) {
                        v.setBackgroundResource(R.drawable.rectangular_red_edit_text);
                        //mFragment.setFundsError(mContext.getString(R.string.entered_funds_error));
                    } else {
                        //mFragment.setFundsError("");
                        v.setBackgroundResource(R.drawable.rectangular_edit_text);
                    }
                }
                return false;
            }
        });

        holder.etFundPercentValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String fundPercentText = s.toString();
                if (!fundPercentText.isEmpty()) {
                    int fundPercent = Integer.parseInt(fundPercentText);
                    totalFundPercentage -= fundPercent;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String currentMethod = new Object() {
                    }
                            .getClass()
                            .getEnclosingMethod()
                            .getName();
                    String fundPercentText = s.toString();
                    if (!fundPercentText.isEmpty()) {
                        int fundPercent = Integer.parseInt(fundPercentText);
                        totalFundPercentage += fundPercent;
                        CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.FUND_ID_ANNOTATION + fundsModel.getFundId(), fundPercentText, fundsModel.getFundName(), TAG, String.valueOf(NvestLibraryConfig.FieldType.Input), currentMethod);
                        // GenericDTO.addAttribute(NvestLibraryConfig.FUND_ID_ANNOTATION + fundsModel.getFundId(), String.valueOf(fundPercent));
                    }
                }
                catch (NumberFormatException e){
                    CommonMethod.log(TAG , "Number format exception occurred " + e.toString());
                }
                catch (Exception e){
                    CommonMethod.log(TAG , "Exception occurred " + e.toString());
                }


                fundsValueListener.totalValueChanged(totalFundPercentage);
            }
        });

        /*DynamicParams fundDp = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.FUND_ID_ANNOTATION + fundsModel.getFundId());
        if (fundDp != null) {
            //holder.etFundPercentValue.setText(fundDp.getFieldKey());
            holder.etFundPercentValue.setText(fundDp.getFieldValue());
        } else {
            holder.etFundPercentValue.setText("0");
        }*/
    }

    public void setDefaultFundsValue(HashMap<String, String> defaultValuesPassed) {
        /*for (FundsModel fundsModel : mFundsArrayList){
            CommonMethod.log(TAG , "Display Fund id "+ fundsModel.getFundId());
            String fundIdFound = String.valueOf(fundsModel.getFundId()) ;
            for (Map.Entry<String, String>  defaultValueEntry : defaultFundsValue.entrySet()){
                String fundId = defaultValueEntry.getValue();
                if(fundIdFound.equals(fundId)){
                    CommonMethod.log(TAG , "Found a match");
                    EditText editText = entry.getValue();
                    CommonMethod.setDefaultValue();
                }
            }
        }*/
        if (etFundValues != null) {
            int i = 0;
            for (Map.Entry<String, EditText> entry : etFundValues.entrySet()) {
                CommonMethod.log(TAG, "Display Fund id " + entry.getKey());
                String editTextFundId = entry.getKey();
                for (Map.Entry<String, String> defaultValueEntry : defaultValuesPassed.entrySet()) {
                    String fundId = defaultValueEntry.getValue();
                    String keyword = null;
                    if (editTextFundId.equalsIgnoreCase(fundId)) {
                        CommonMethod.log(TAG, "Inside ...");
                        if (defaultValueEntry.getKey().contains("Id")) {
                            keyword = defaultValueEntry.getKey().replace("Id", "Per");
                            CommonMethod.log(TAG, "Found a match " + keyword);
                            if (defaultValuesPassed.containsKey(keyword)) {
                                CommonMethod.setDefaultValue(defaultValuesPassed.get(keyword), entry.getValue());
                            } else {
                                DynamicParams fundDp = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.FUND_ID_ANNOTATION + mFundsArrayList.get(i).getFundId());
                                if (fundDp != null) {
                                    //holder.etFundPercentValue.setText(fundDp.getFieldKey());
                                    entry.getValue().setText(fundDp.getFieldValue());
                                } else {
                                    entry.getValue().setText("0");
                                }
                                CommonMethod.log(TAG, "No fund percentage found against fund id " + editTextFundId);
                            }
                        }
                    }
                }
                i++;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mFundsArrayList.size();
    }

}


