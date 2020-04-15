package nvest.com.nvestlibrary.landingfragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestCustomScrollView;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.landing.LandingActivity;
import nvest.com.nvestlibrary.landing.ProductDataViewModel;
import nvest.com.nvestlibrary.landing.SUDProductContentAdapter;
import nvest.com.nvestlibrary.landing.SolutionAdapter;
import nvest.com.nvestlibrary.landing.SolutionVieModel;
import nvest.com.nvestlibrary.landing.User;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;
import nvest.com.nvestlibrary.nvestcheck.NvestCheckActivity;
import nvest.com.nvestlibrary.nvestsync.SyncActivity;
import nvest.com.nvestlibrary.solutionDetails.SolutionMaster;

public class LandingFragment extends Fragment implements SUDProductContentAdapter.SelectProductListener, ProductDataViewModel.ProductDataListener, FrameLayout.OnTouchListener, SolutionVieModel.SolutionListener, SolutionAdapter.SolutionAdapterListener , NvestCustomScrollView.OnScrollChangedListener {
    private static String TAG = LandingFragment.class.getSimpleName();
    //private RecyclerView productRecyclerView, solutionRecyclerView;
    private SUDProductContentAdapter sudProductContentAdapter;
    private List<Products> productsList;
    private ProductDataViewModel productDataViewModel;
    private SolutionVieModel solutionVieModel;
    private AutoCompleteTextView searchedit;
    private FrameLayout frameLayout;
    private LandingActivity landingActivity;
    private LinearLayout dynamicLayout;
    private String[] productarray;
    private int nextIndex = 0;
    private SolutionAdapter solutionAdapter;
    private NvestCustomScrollView productScrollView;
    public LandingFragment() {
        CommonMethod.log(TAG, "Landing fragment started");
    }
    private View view;
    private LinkedHashMap<String, Integer> productAddedInDynamicLayout;
    private Disposable disposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonMethod.log(TAG, "On create view started");
        view = inflater.inflate(R.layout.fragment_landing, container, false);
        dynamicLayout = view.findViewById(R.id.add_dynamic);
        //productRecyclerView = (RecyclerView) view.findViewById(R.id.productRecyclerView);
        //solutionRecyclerView = view.findViewById(R.id.solutionRecyclerView);
        frameLayout = (FrameLayout) view.findViewById(R.id.touchLayout);
        //productRecyclerView.setNestedScrollingEnabled(true);
        //solutionRecyclerView.setNestedScrollingEnabled(true);
        //solutionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        searchedit = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        productScrollView = (NvestCustomScrollView) view.findViewById(R.id.product_scroll_view);

        //addAnnotations();
        ///startEducationFragment();
        //startCheckActivity();
        //startSyncActivity();
        init();
        return view;
    }

    private void startSyncActivity() {
        Intent intent = new Intent(getActivity(), SyncActivity.class);
        startActivity(intent);
    }


    public void push(String e) {
        try {
            //CommonMethod.log(TAG, "Inserting " + e + " at " + nextIndex);
            productarray[nextIndex] = e;
            ++nextIndex;

        } catch (Exception e1) {
            CommonMethod.log(TAG, "Exception " + e1.toString());
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void init() {
        productsList = new ArrayList<>();
        productAddedInDynamicLayout = new LinkedHashMap<>();
        //solutionAdapter = new SolutionAdapter(getContext());
        //solutionAdapter.setSolutionAdapterListener(this);
        productDataViewModel = ViewModelProviders.of(this).get(ProductDataViewModel.class);
        solutionVieModel = ViewModelProviders.of(this).get(SolutionVieModel.class);
        productDataViewModel.setProductDataListener(this);
        solutionVieModel.setSolutionListener(this);
        frameLayout.setOnTouchListener(this);
        //ExecutorService executorService = Executors.newFixedThreadPool(2);
        /*ExecutorService executorService = Executors.newCachedThreadPool();

        Runnable loadProductstask = ()-> {
            if (productDataViewModel != null) {
                productDataViewModel.loadProducts();
            } else {
                CommonMethod.log(TAG, "Product data view model is null");
            }
        };

        Runnable solutionProductstask = ()-> {
            if (solutionVieModel != null) {
                solutionVieModel.getSolutions();
            }
        };
        executorService.submit(loadProductstask);
        executorService.submit(solutionProductstask);
        executorService.shutdown();
*/
        if (productDataViewModel != null) {
            productDataViewModel.loadProducts();
        } else {
            CommonMethod.log(TAG, "Product data view model is null");
        }
        if (solutionVieModel != null) {
            solutionVieModel.getSolutions();
        }
        productScrollView.setOnScrollChangedListener(this);
        //temp();
        tempnew();
        //startCheckActivity();
    }

    /**
     * Assume this as a network call
     * returns Users with address filed added
     */
    private Observable<User> getAddressObservable(final User user) {

        final String[] addresses = new String[]{
                "1600 Amphitheatre Parkway, Mountain View, CA 94043",
                "2300 Traverwood Dr. Ann Arbor, MI 48105",
                "500 W 2nd St Suite 2900 Austin, TX 78701",
                "355 Main Street Cambridge, MA 02142"
        };

        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {

                        if (!emitter.isDisposed()) {
                            // Generate network latency of random duration
                            int sleepTime = new Random().nextInt(1000) + 500;

                            Thread.sleep(sleepTime);
                            emitter.onNext(user);
                            emitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * Assume this is a network call to fetch users
     * returns Users with name and gender but missing address
     */
    private Observable<User> getUsersObservable() {
        String[] maleUsers = new String[]{"Mark", "John", "Trump", "Obama"};

        final List<User> users = new ArrayList<>();

        for (String name : maleUsers) {
            User user = new User();
            user.setName(name);
            user.setGender("male");

            users.add(user);
        }

        return Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        for (User user : users) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(user);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }



    @Override
    public void selectedItem(Products products) {
        landingActivity.navigationView.setVisibility(View.GONE);
        String currentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        if (products != null) {
            GenericDTO.clearResultSetMap();
            GenericDTO.clearDynamicParams();
            GenericDTO.addAttribute(NvestLibraryConfig.IS_ULIP, products.isUlip());
            String productId = String.valueOf(products.getProductId());
            CommonMethod.addDynamicKeyWordToGenericDTO(NvestLibraryConfig.PRODUCT_ID_ANNOTATION, productId, productId, TAG, String.valueOf(NvestLibraryConfig.FieldType.Number), currentMethod);
            // mListener.startBasicInfromationFragment(products);
            Bundle args = new Bundle();
            args.putParcelable("products", products);
            Navigation.findNavController(view).navigate(R.id.action_landingFragment_to_basicInformationFragment, args);
            //Navigation.findNavController(view).navigate(R.id.action_landingFragment_to_needFragment);

        } else {
            CommonMethod.log(TAG, "Product is null");
        }
        //addAnnotations();
        //startCheckActivity();

        //NvestAssetDatabaseAccess.getSingletonInstance().insertIntoChangedProductDetailsTry(2, "Dummy new Action 2", "1", "3213", "Input", "4994","311", "311" , "1483257600000-0800", "31-JUN-2019");
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonMethod.log(TAG , "Inside on resume");
        landingActivity = ((LandingActivity) getActivity());
        landingActivity.navigationView.setVisibility(View.VISIBLE);
        nextIndex =  0;
    }

    @Override
    public void completestatus(int productsize) {
        CommonMethod.log(TAG, "Status complete");
        productDataViewModel.setSegregatedCategoryLiveData().observe(this, segregatedCategoryList -> {
            productarray = new String[productsize];
            //productarray = new String[100];
            /*for (int i = 0 ; i < 8 ; i++){
                Map.Entry<String, ArrayList<Products>> entry123 =  segregatedCategoryList.entrySet().iterator().next();
                addCardView(entry123.getKey(), entry123.getValue());
            }*/

            for (Map.Entry<String, ArrayList<Products>> entry : segregatedCategoryList.entrySet()) {
                if(!productAddedInDynamicLayout.containsValue(entry.getKey())){
                    productAddedInDynamicLayout.put(entry.getKey(), entry.getValue().size());
                    addCardView(entry.getKey(), entry.getValue());
                    this.productsList.addAll(entry.getValue());

                    ArrayList<Products> productsListList = entry.getValue();

                    for (int i = 0; i < productsListList.size(); i++) {
                        //CommonMethod.log(TAG, "i " + i);
                        //productarray[i] = productsList.get(i).getProductName();

                        // CommonMethod.log(TAG,"test" +productarray[0]);
                        // CommonMethod.log(TAG,"test" +productsList.size());
                        push(productsListList.get(i).getProductName());
                    }
                }

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productarray);
            searchedit.setAdapter(adapter);
            searchedit.setThreshold(1);

            searchedit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = searchedit.getAdapter().getItem(position).toString();
                    CommonMethod.log(TAG, "Selected item " + selectedItem);
                    CommonMethod.log(TAG, "Products size " + productsList.size());
                    int productposition =  productsList.indexOf(new Products(selectedItem));
                    Products foundProduct = productsList.get(productposition);
                    selectedItem(foundProduct);
                    /*for (Products products : productsList) {
                        if (products.getProductName().equalsIgnoreCase(selectedItem)) {
                            selectedItem(products);
                            break;
                        }
                    }*/
                }
            });

        });
    }

    @Override
    public void onProductLiveDataReceived(boolean complete) {

    }


    public void setDataToProductContentAdapter() {
        CommonMethod.log(TAG, "Setting data to product contents");
        sudProductContentAdapter = new SUDProductContentAdapter(productsList, getActivity(), this);
        //productRecyclerView.setAdapter(sudProductContentAdapter);
        //sudProductContentAdapter.setProductContents(productsList);
    }

    public void temp(){
        //CommonMethod.log(TAG , "Output " + CommonMethod.getCurrentDate());
        final String[] states = {"Lagos", "Abuja", "Imo", "Enugu"};
        Observable<String> statesObservable = Observable.fromArray(states);
/*
        statesObservable
                .flatMap(s -> Observable.create(getPopulation(s)))
                .subscribeOn(Schedulers.io())
                .subscribe(pair -> {

            CommonMethod.log(TAG, pair.first + " population is " + pair.second);

        });*/
        statesObservable.flatMap(
                s -> Observable.create(getPopulation(s))
                        .subscribeOn(Schedulers.io())
        ).subscribe(pair -> {
            CommonMethod.log(TAG, pair.first + " population is " + pair.second);
        });
        CommonMethod.log(TAG ,  "----------------------------------------------------------");
        statesObservable.concatMap(
                s -> Observable.create(getPopulation(s))
                        .subscribeOn(Schedulers.io())
        ).subscribe(pair -> {
            CommonMethod.log(TAG, "Output from concat map "+pair.first + " population is " + pair.second);
        });
    }

    private ObservableOnSubscribe<Pair> getPopulation(String state) {
        return(emitter -> {
            Random r = new Random();
            CommonMethod.log(TAG, "getPopulation() for " + state + " called on " + Thread.currentThread().getName());
            emitter.onNext(new Pair(state, r.nextInt(300000 - 10000) + 10000));
            emitter.onComplete();
        });
    }

    private void tempnew(){
        CommonMethod.log(TAG , "Inside temp new");
        //Cursor c = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery("Select POWER((1.000000000000+0.08),(1.000000000000/12.000000000000))-1 AS [INV_RATE]");
        Cursor c = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery("Select POWER(2,3) AS [INV_RATE]");
        //Cursor c = NvestAssetDatabaseAccess.getSingletonInstance().ExecuteBiQuery("Select productname  from productmaster");
        if(c != null){
            c.moveToFirst();
            CommonMethod.log(TAG , "Count " + c.getCount());
            do {
                CommonMethod.log(TAG , "Output " + c.getString(0));
            }
            while (c.moveToNext());
        }


    }

    public void startCheckActivity() {
        //mListener.updateFragment(NvestLibraryConfig.PRODUCT_INFORMATION_FRAGMENT_NAME);
        Intent intent = new Intent(getActivity(), NvestCheckActivity.class);
        startActivity(intent);
    }


    public void addCardView(String title, ArrayList<Products> productsList) {

        /*LinearLayout parent_linear_layout = new LinearLayout(getActivity());
        parent_linear_layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams parent_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        parent_linear_layout.setLayoutParams(parent_params);
        parent_linear_layout.setBackgroundColor(Color.WHITE);
        */

        LinearLayout.LayoutParams text_view_heading_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(text_view_heading_params);
        tv.setPadding(30, 15, 5, 15);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.LTGRAY);


        CardView parent_cardview = new CardView(getActivity());

        // Set the CardView layoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //params.setMargins(8,8,8,8);
        parent_cardview.setLayoutParams(params);

        // Set CardView corner radius
        parent_cardview.setRadius(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parent_cardview.setElevation(0f);
        }


        // Set a background color for CardView
        parent_cardview.setCardBackgroundColor(Color.WHITE);

        // Set the CardView maximum elevation
        //parent_cardview.setMaxCardElevation(15);

        // Set CardView elevation
        //parent_cardview.setCardElevation(9);
        parent_cardview.addView(tv);

        LinearLayout.LayoutParams recyclerView_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                //260
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 10, 5, 8);


        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutParams(recyclerView_params);
        recyclerView.setPadding(25, 100, 10, 0);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setNestedScrollingEnabled(false);
        sudProductContentAdapter = new SUDProductContentAdapter(productsList, getActivity(), this);
        recyclerView.setAdapter(sudProductContentAdapter);
        parent_cardview.addView(recyclerView);

        dynamicLayout.setPadding(10, 10, 10, 10);
        dynamicLayout.addView(parent_cardview);
    }

    public void addSolutionProductsToCardView(String title, ArrayList<SolutionMaster> solutionMasterArrayList) {

        /*LinearLayout parent_linear_layout = new LinearLayout(getActivity());
        parent_linear_layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams parent_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        parent_linear_layout.setLayoutParams(parent_params);
        parent_linear_layout.setBackgroundColor(Color.WHITE);*/


        LinearLayout.LayoutParams text_view_heading_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(text_view_heading_params);
        tv.setPadding(30, 15, 5, 15);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.LTGRAY);


        CardView parent_cardview = new CardView(getActivity());

        // Set the CardView layoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //params.setMargins(8,8,8,8);
        parent_cardview.setLayoutParams(params);

        // Set CardView corner radius
        parent_cardview.setRadius(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parent_cardview.setElevation(0f);
        }


        // Set a background color for CardView
        parent_cardview.setCardBackgroundColor(Color.WHITE);

        // Set the CardView maximum elevation
        //parent_cardview.setMaxCardElevation(15);

        // Set CardView elevation
        //parent_cardview.setCardElevation(9);
        parent_cardview.addView(tv);

        LinearLayout.LayoutParams recyclerView_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                //260
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 10, 5, 8);


        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutParams(recyclerView_params);
        recyclerView.setPadding(25, 100, 10, 0);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        solutionAdapter = new SolutionAdapter(getActivity(), solutionMasterArrayList);
        //sudProductContentAdapter = new SUDProductContentAdapter(productsList, getActivity(), this);
        solutionAdapter.setSolutionAdapterListener(this);
        recyclerView.setAdapter(solutionAdapter);
        parent_cardview.addView(recyclerView);

        dynamicLayout.setPadding(10, 10, 10, 10);
        dynamicLayout.addView(parent_cardview);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        CommonMethod.log(TAG, "Frame layout touch event found");
        landingActivity.hideKeyboard(view);
        return false;
    }


    @Override
    public void onSolutionChanged(boolean complete) {
        try {
            if(complete){
                solutionVieModel.getMutableSolutionProductList().observe(this, solutionProductLinkedHashMap ->{
                    CommonMethod.log(TAG , "Solutions Size " + solutionProductLinkedHashMap.size());
                    for (Map.Entry<String, ArrayList<SolutionMaster>> entry : solutionProductLinkedHashMap.entrySet()) {
                        if(!productAddedInDynamicLayout.containsKey(entry.getKey())){
                            productAddedInDynamicLayout.put(entry.getKey(), entry.getValue().size());
                            addSolutionProductsToCardView(entry.getKey(), entry.getValue());
                        }

                    }
                });
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exceptipon " + e.toString());
        }

    }

    @Override
    public void onProductsChanged(boolean complete) {

    }

    @Override
    public void onModeRecieved(boolean modeRecieved) {

    }

    @Override
    public void onSolutionProductsChanged(boolean solutionProductsChanged) {

    }

    @Override
    public void onInputKeywordsChanged(boolean inputKeywordsChanged) {

    }


    @Override
    public void solutionSelected(SolutionMaster solutionMaster) {
        landingActivity.navigationView.setVisibility(View.GONE);
        GenericDTO.clearResultSetMap();
        GenericDTO.clearDynamicParams();
        Bundle args = new Bundle();
        args.putParcelable("solution", solutionMaster);
        Navigation.findNavController(view).navigate(R.id.action_landingFragment_to_solutionDetailsFragment,args);
    }
/*

    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
            ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    if (productScrollView.getScrollY() == 0) {
                        //swipeRefreshLayout.setEnabled(true);
                        CommonMethod.log(TAG , "User has scrolled vertically upwards...");
                    } else{
                        //swipeRefreshLayout.setEnabled(false);
                        CommonMethod.log(TAG , "User has scrolled vertically downwards...");
                    }

                }


            };
*/

    @Override
    public void onScrollStart() {
        CommonMethod.log(TAG , "Scrolling started..");

    }

    @Override
    public void onScrollEnd() {
        CommonMethod.log(TAG , "Scrolling stopped..");

    }
}
