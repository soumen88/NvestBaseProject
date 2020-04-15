package nvest.com.nvestlibrary.riderinformation;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.nvestWebModel.DynamicEditTextModel;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.RiderInformationModel;

public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderViewHolder> {
    private static final String TAG = RiderAdapter.class.getSimpleName();

    private Context context;
    private List<RiderInformationModel> riderList;
    private int selectedCheckBoxCounter;
    private LinkedHashMap<RiderInformationModel, DynamicEditTextModel> stringDynamicEditTextModelLinkedHashMap;
    private RiderListener riderListener;

    public RiderAdapter(Context context, List<RiderInformationModel> riderList, RiderListener riderListener) {
        this.context = context;
        this.riderList = riderList;
        this.stringDynamicEditTextModelLinkedHashMap = new LinkedHashMap<>();
        this.riderListener = riderListener;
    }

    public interface RiderListener {
        void riderChecked(LinkedHashMap<RiderInformationModel, DynamicEditTextModel> stringDynamicEditTextModelLinkedHashMap);
    }

    @NonNull
    @Override
    public RiderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riders, viewGroup, false);
        return new RiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiderViewHolder riderViewHolder, int position) {
        RiderInformationModel rider = riderList.get(position);

        String key = String.valueOf(rider.getRiderId());


        // set rider name
        riderViewHolder.tvRiderName.setText(rider.getRiderName());

        // by default remove if any rider id is present in generic dto
        // GenericDTO.removeDynamicParams(NvestLibraryConfig.RIDER_ID_ANNOTATION + rider.getRiderId());




        // toggle checkbox on layout click
        riderViewHolder.relativeLayoutRiderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riderViewHolder.cbSelect.toggle();
            }
        });


        // toggle view visibility
        riderViewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String currentMethod = new Object() {
                }
                        .getClass()
                        .getEnclosingMethod()
                        .getName();

                if (isChecked) {

                    // increase the counter irrespective if it is wop or not
                    selectedCheckBoxCounter += 1;

                    // GenericDTO.addAttribute(NvestLibraryConfig.RIDER_ID_ANNOTATION + rider.getRiderId(), "1");
                    CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.RIDER_ID_ANNOTATION + rider.getRiderId(), "1", "1", TAG, String.valueOf(NvestLibraryConfig.FieldType.CheckBox), currentMethod);

                    if (!rider.isWop()) {

                        if (!riderViewHolder.tvAmountInWords.getText().equals("")) {
                            riderViewHolder.tvAmountInWords.setVisibility(View.VISIBLE);
                        }


                        // add edit text
                        EditText editText = CommonMethod.createNumberTypeEditText(context, "Enter amount");
                        riderViewHolder.dynamicLayout.addView(editText);

                        DynamicParams riderSumAssuredDynamicParams = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.RIDER_SUM_ASSURED_ANNOTATION + rider.getRiderId());
                        if (riderSumAssuredDynamicParams != null) {
                            String riderSumAssured = riderSumAssuredDynamicParams.getFieldValue();
                            editText.setText(riderSumAssured);
                        }

                        // put dynamic edit text into linked hashmap
                        DynamicEditTextModel dynamicEditTextModel = new DynamicEditTextModel();
                        dynamicEditTextModel.setDynamicFieldIdentifier(key);
                        dynamicEditTextModel.setEditText(editText);
                        stringDynamicEditTextModelLinkedHashMap.put(rider, dynamicEditTextModel);

                        // fire listener
                        riderListener.riderChecked(stringDynamicEditTextModelLinkedHashMap);

                        // convert amount into words
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String editTextValue = s.toString();

                                String word = CommonMethod.convertIntoWords(editTextValue.replace(",", ""));
                                if (word != null && !word.isEmpty()) {
                                    riderViewHolder.tvAmountInWords.setText(word);
                                    riderViewHolder.tvAmountInWords.setVisibility(View.VISIBLE);
                                } else {
                                    riderViewHolder.tvAmountInWords.setText("");
                                    riderViewHolder.tvAmountInWords.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } else {
                    // decreasse the counter
                    selectedCheckBoxCounter -= 1;

                    GenericDTO.removeDynamicParams(NvestLibraryConfig.RIDER_ID_ANNOTATION + rider.getRiderId());
                    GenericDTO.removeDynamicParams(NvestLibraryConfig.RIDER_SUM_ASSURED_ANNOTATION + rider.getRiderId());

                    if (riderViewHolder.tvAmountInWords.getVisibility() == View.VISIBLE) {
                        riderViewHolder.tvAmountInWords.setText("");
                        riderViewHolder.tvAmountInWords.setVisibility(View.GONE);
                    }
                    // remove all dynamic views
                    riderViewHolder.dynamicLayout.removeAllViews();

                    //  remove dynamic edit text from linked hashmap
                    stringDynamicEditTextModelLinkedHashMap.remove(rider);

                    // fire listener
                    riderListener.riderChecked(stringDynamicEditTextModelLinkedHashMap);
                }
            }
        });

        DynamicParams riderIdDynamicParams = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.RIDER_ID_ANNOTATION + rider.getRiderId());
        if (riderIdDynamicParams != null) {
            riderViewHolder.cbSelect.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return riderList.size();
    }

    public void updateRiderList(List<RiderInformationModel> newRiderList) {
        RiderDiffCallback riderDiffCallback = new RiderDiffCallback(riderList, newRiderList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(riderDiffCallback);

        diffResult.dispatchUpdatesTo(this);
        riderList.clear();
        riderList.addAll(newRiderList);
    }

    int getSelectedCheckBoxCounter() {
        return selectedCheckBoxCounter;
    }

    class RiderViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutRiderName;
        TextView tvRiderName, tvAmountInWords;
        CheckBox cbSelect;
        LinearLayout dynamicLayout;

        RiderViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayoutRiderName = itemView.findViewById(R.id.relativeLayoutRiderName);
            tvRiderName = itemView.findViewById(R.id.tvRiderName);
            tvAmountInWords = itemView.findViewById(R.id.tvAmountInWords);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            dynamicLayout = (LinearLayout) itemView.findViewById(R.id.rider_dynamic_layout);
        }
    }
}
