package com.nvest.user.LogUtils;

import android.content.Context;
import android.util.Log;

import com.nvest.user.appConfig.Config;
import com.nvest.user.databaseFiles.RoomDatabaseSingleton;
import com.nvest.user.databaseFiles.dao.keyvaluestoretable.KeyValueStoreRoom;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class LogUtils {

    private static String LOGTAG = LogUtils.class.getSimpleName();

    public static void log(String TAG , String message){
        Log.e(LOGTAG , TAG  + " - " + message );
    }

    public static void evaluateExpression(String evalPassed){
        try {
            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(Config.SCRIPT_ENGINE_NAME);
            LogUtils.log(LOGTAG , "Evaluating " + evalPassed);
            LogUtils.log(LOGTAG , "Evaluating " + scriptEngine.eval(evalPassed));
        }
        catch (ScriptException s){
            LogUtils.log(LOGTAG , "Script exception " + s.toString());
        }
        catch (Exception e){
            LogUtils.log(LOGTAG, "Exception occurred " + e.toString());
        }


    }

    public static String generateUUID(){
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    public static void logQuery(String TAG , String queryString){
        int start = 0;
        int end = 100;
        int iteratefor = (int)queryString.length() /100;
        LogUtils.log(TAG , "Iterate for " + iteratefor);
        for (int k = 0 ; k < iteratefor; k++){
            LogUtils.log(LOGTAG ,TAG + " - Subs " + queryString.substring(start, end+1));
            start = end +1;
            end = end + 100;
        }
        LogUtils.log(LOGTAG , TAG + " - Subs final " + queryString.substring(start, queryString.length()) + " Length " + queryString.length());
        LogUtils.log(LOGTAG,TAG+"--------------------------------------------------------------");
    }

    public static boolean updateCurrentDateTime(){
        final boolean[] complete = {false};
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                LogUtils.log(TAG , "Inside update date time");
                Date currentTime = Calendar.getInstance().getTime();
                KeyValueStoreRoom keyValueStoreRoom = new KeyValueStoreRoom();
                //keyValueStoreRoom.setId(2L);
                keyValueStoreRoom.setKeyValue(currentTime.toString());
                keyValueStoreRoom.setKeyName("sync-time");
                RoomDatabaseSingleton.getRoomDatabaseSession().keyValueStoreRoomDao().insertKeyValueVariable(keyValueStoreRoom);
            }
        }).observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.log(TAG , "Inside on subscribe");
            }

            @Override
            public void onComplete() {
                LogUtils.log(TAG , "Inside on update complete");
                complete[0] = true;
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.log(TAG , "Inside on update failed");
                complete[0] = false;
            }
        });

        return complete[0];
    }


}
