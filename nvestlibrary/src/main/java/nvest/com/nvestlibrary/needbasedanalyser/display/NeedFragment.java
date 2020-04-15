package nvest.com.nvestlibrary.needbasedanalyser.display;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.commonMethod.SpacesItemDecoration;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;

public class NeedFragment extends Fragment implements  NeedFragmentViewModel.NeedViewModelListener, NeedFragmentAdapter.NeedProductListener{
    private static String TAG = NeedFragment.class.getSimpleName();
    private RecyclerView needRecyclerView;
    private View view;
    private NeedFragmentViewModel needFragmentViewModel;
    private NeedFragmentAdapter needFragmentAdapter;
    private List<KeyValuePair> keyValuePairList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.need_analyser_display_fragment, container, false);
        needRecyclerView = (RecyclerView) view.findViewById(R.id.needRecyclerView);
        init();
        return view;
    }

    public void init(){

        keyValuePairList = new ArrayList<>();

        needRecyclerView.addItemDecoration(new SpacesItemDecoration(CommonMethod.dpToPx(5, getActivity()), 2));
        needRecyclerView.setItemAnimator(new DefaultItemAnimator());
        needRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        needFragmentViewModel = ViewModelProviders.of(this).get(NeedFragmentViewModel.class);
        needFragmentViewModel.setNeedViewModelListener(this);
        needFragmentViewModel.getAllNeeds();
    }


    @Override
    public void complete(boolean flag) {
        needFragmentViewModel.setMutableNeedList().observe(this, keyValuePairs -> {
            CommonMethod.log(TAG , "Size of list " + keyValuePairs.size());
            keyValuePairList.addAll(keyValuePairs);
            needFragmentAdapter = new NeedFragmentAdapter(keyValuePairList, getActivity(),this );
            needRecyclerView.setAdapter(needFragmentAdapter);
        });
    }

    @Override
    public void selectedItem(KeyValuePair keyValuePair) {
        CommonMethod.log(TAG, "Key value pair " + keyValuePair.getValue());
        //Navigation.findNavController(view).navigate(R.id.action_needFragment_to_educationFragment);
        Bundle args = new Bundle();
        args.putParcelable("selected_need", keyValuePair);
        if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.ACCUMULATE_WEALTH_CAPTION)){
            Navigation.findNavController(view).navigate(R.id.action_needFragment_to_wealthFragment, args);
        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.INCOME_REPLACEMENT_CAPTION)){

        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.HOUSE_CAPTION)){

        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.CHILD_AGE_CAPTION)){

        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.RETIREMENT_CAPTION)){
            Navigation.findNavController(view).navigate(R.id.action_needFragment_to_RetirementFragment, args);
        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.MARRIAGE_CAPTION)){

        }
        else if(keyValuePair.getValue().equalsIgnoreCase(NvestLibraryConfig.EDUCATION_CAPTION)){
            Navigation.findNavController(view).navigate(R.id.action_needFragment_to_educationFragment, args);
        }
    }
}
