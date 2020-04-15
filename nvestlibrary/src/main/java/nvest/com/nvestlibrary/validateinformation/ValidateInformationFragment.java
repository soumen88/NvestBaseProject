package nvest.com.nvestlibrary.validateinformation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;

public class ValidateInformationFragment extends Fragment {
    private static String TAG = ValidateInformationFragment.class.getSimpleName();
    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonMethod.log(TAG, "On create view started");
        View view = inflater.inflate(R.layout.fragment_validate_information, container, false);

        //getCountFromPtPPtMaster();
        context = getContext();
        activity = getActivity();

        init();
        return view;
    }

    public void init() {
        //validateInformationDataViewModel.ValidateInput(Param);
        //validateInformationDataViewModel.ValidateAllRiders();
        /*generatePdfDataViewModel = ViewModelProviders.of(this).get(GeneratePdfDataViewModel.class);
        generatePdfDataViewModel.setGeneratePdfDataViewModelListener(this);
        DynamicParams dynamicParams = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.LA_AGE_ANNOTATION);
        if (dynamicParams != null) {
            CommonMethod.log(TAG, "Dynamic param key" + dynamicParams.getFieldKey());
            CommonMethod.log(TAG, "Dynamic param value" + dynamicParams.getFieldValue());
            CommonMethod.log(TAG, "Dynamic param field type " + dynamicParams.getFieldType());
            CommonMethod.log(TAG, "Dynamic param fileName " + dynamicParams.getFileName());
        }

        generatePdfDataViewModel.temp();*/
    }

}
