package com.nvest.user.databaseFiles;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.util.Log;

import com.nvest.user.LogUtils.GenericDTO;
import com.nvest.user.LogUtils.LogUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class  RoomRawQueryBuilder implements SupportSQLiteQuery {
    private static String TAG = RoomRawQueryBuilder.class.getSimpleName();
    public String modifiedquery;
    Set<String> argumentSet =new HashSet<>();
    public RoomRawQueryBuilder(String query) {
        //LogUtils.log(TAG ,"Room query builder started...");
        this.modifiedquery = query;
    }



    @Override
    public String getSql() {
        try {
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(modifiedquery);
            testingarg(modifiedquery);
            while (matcher.find()) {
                //LogUtils.log(TAG , "Matching words " + matcher.group() + " Key value " + valueHashMap.get(matcher.group()) + " Count " + matcher.groupCount());
                argumentSet.add(matcher.group());
            }
            for (String arg : argumentSet){
                //LogUtils.log(TAG , "Set value " + arg);
                String valueReceived = (String) GenericDTO.getAttributeValue(arg);
                if(valueReceived != null){
                    modifiedquery = modifiedquery.replaceAll("(?<!\\\\\\\\S)"+arg+"(?<!\\\\\\\\S)",valueReceived );
                    LogUtils.log(TAG , "Modified query " + modifiedquery);
                }
                else {
                    LogUtils.log(TAG , "Did not find DTO value for " + arg);
                }
            }
            //LogUtils.log(TAG , "-------------------------------------------------" );
            return modifiedquery;
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in raw query " + e.toString());
        }
        return null;
    }

    @Override
    public void bindTo(SupportSQLiteProgram statement) {

    }

    public void testingarg(String query){
        LogUtils.log(TAG , "Starting test arg...");
        LogUtils.logQuery(TAG , query);
        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(query);
        Set<String> tempargset =new HashSet<>();
        while (matcher.find()) {
            LogUtils.log(TAG , "Temp Matching words " + matcher.group() );
            tempargset.add(matcher.group());
        }
        for (String arg : tempargset){
            LogUtils.log(TAG , "Set value " + arg);

        }

    }

    @Override
    public int getArgCount() {
        return argumentSet.size();
    }
}
