package com.nvest.user.home;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.nvest.user.R;

import nvest.com.nvestlibrary.NvestHelper;

public class HomeScreenActivity extends AppCompatActivity{

    private static String TAG = HomeScreenActivity.class.getSimpleName();
    private Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = (Button) findViewById(R.id.check);
        startLibActivity();
    }

    public void startLibActivity(){
        try {
            NvestHelper nvestHelper = new NvestHelper(this);
            nvestHelper.start();
        } catch (Exception e) {

        }
    }


}
