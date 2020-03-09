package com.nvest.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nvest.user.LogUtils.GenericDTO;
import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.appConfig.Config;


public class CheckActivity extends AppCompatActivity {
    private static String TAG = CheckActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nvest_check);
        LogUtils.log(TAG , "Shared obj " + GenericDTO.getAttributeValue(Config.PT_ANNOTATION));
    }
}
