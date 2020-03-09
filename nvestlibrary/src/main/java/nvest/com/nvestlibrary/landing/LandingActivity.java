package nvest.com.nvestlibrary.landing;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.base.BaseActivity;
import nvest.com.nvestlibrary.basicInformation.BasicInformationFragment;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.landingfragment.LandingFragment;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.AllInputList;
import nvest.com.nvestlibrary.nvestWebModel.SelectedProductDetails;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.nvestcheck.NvestCheckActivity;
import nvest.com.nvestlibrary.nvestsync.SyncActivity;
import nvest.com.nvestlibrary.productinformation.ProductInformationFragment;
import nvest.com.nvestlibrary.riderinformation.RiderFragment;
import nvest.com.nvestlibrary.summary.SummaryFragment;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationFragment;

import static androidx.navigation.Navigation.findNavController;

public class LandingActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ValidateInformationDataViewModel.ValidateInformationDataListener{
    private FragmentManager mFragmentManager;
    private LandingFragment landingFragment;
    private static String TAG = LandingActivity.class.getSimpleName();
    private BasicInformationFragment basicInformationFragment;
    private ProductInformationFragment productInformationFragment;
    private RiderFragment riderFragment;
    private ValidateInformationFragment validateInformationFragment;
    private SummaryFragment summaryFragment;
    private String fragmentName;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavController navController;
    public BottomNavigationView bottomNavigationView;
    public NavigationView navigationView;
    private ValidateInformationDataViewModel validateInformationDataViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        bottomNavigationView=findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_right);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView=findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(navigationView, navController);

        //navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setElevation(4.0f);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //startSyncActivity();
        /*bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()== R.id.navigation_shop){
                    CommonMethod.log(TAG,"inside shop");
                }
                else if (menuItem.getItemId()== R.id.navigation_gifts) {
                    CommonMethod.log(TAG,"inside gift");
                }
                else if (menuItem.getItemId()== R.id.navigation_cart) {
                    CommonMethod.log(TAG,"inside cart");
                }
                else if (menuItem.getItemId()== R.id.navigation_profile) {
                    CommonMethod.log(TAG,"inside profile");
                }
                else if (menuItem.getItemId()== R.id.navigation_sync) {
                    startSyncActivity();
                    CommonMethod.log(TAG,"inside sync");
                }
                else {
                    CommonMethod.log(TAG,"No Option selected");
                }

                return false;
            }
        });*/

        accessNvestAssetDatabase();
        accessRoomDatabase();
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        CommonMethod.log(TAG , "On navigation item selected");
        if(menuItem.getItemId()== R.id.navigation_shop){
            CommonMethod.log(TAG,"inside shop");
            bottomNavigationView.setVisibility(View.GONE);
            navController.navigate(R.id.needFragment);
        }
        else if (menuItem.getItemId()== R.id.navigation_gifts) {
            CommonMethod.log(TAG,"inside gift");
            Intent intent = new Intent(getApplicationContext(), NvestCheckActivity.class);
            startActivity(intent);

        }
        else if (menuItem.getItemId()== R.id.navigation_cart) {
            CommonMethod.log(TAG,"inside cart");
        }
        else if (menuItem.getItemId()== R.id.navigation_profile) {
            CommonMethod.log(TAG,"inside profile");
            validateInformationDataViewModel.NeedAdvisory(31, 35, 15,1,1000000);
        }
        else if (menuItem.getItemId()== R.id.navigation_sync) {
            startSyncActivity();
            CommonMethod.log(TAG,"inside sync");

        }
        else {
            CommonMethod.log(TAG,"No Option selected");
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*CommonMethod.log(TAG, "Back Button clicked " + fragmentName);
                Fragment visibleFragment = getCurrentFragment();

                if (fragmentName.equals(RiderFragment.class.getSimpleName())) {
                    actionBar.setTitle(NvestLibraryConfig.TITLE_BASIC_INFORMATION);
                } else if (fragmentName.equals(BasicInformationFragment.class.getSimpleName())) {
                    actionBar.setTitle(NvestLibraryConfig.TITLE_SELECT_PRODUCT);
                }
                CommonMethod.log(TAG, "Visible fragment " + visibleFragment);*/
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void startSyncActivity() {
        Intent intent = new Intent(getApplicationContext(), SyncActivity.class);
        startActivity(intent);
    }

    @Override
    public void init() {
        // startLandingFragment();
        //startSyncActivity();

        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);
        validateInformationDataViewModel.setValidateInformationDataListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeNvestAssetDatabase();
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData) {

    }

    @Override
    public void onCompleteRidersValidation(MutableLiveData<List<ValidationIP>> validIpListLiveData) {

    }

    @Override
    public void onBiGenerated(LinkedHashMap<Integer, HashMap<String, String>> biData) {

    }

    @Override
    public void onBiUlipGenerated(LinkedHashMap<Integer, HashMap<String, String>>[] biData) {

    }
}
