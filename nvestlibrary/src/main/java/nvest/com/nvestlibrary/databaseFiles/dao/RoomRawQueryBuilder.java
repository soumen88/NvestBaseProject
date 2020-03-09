package nvest.com.nvestlibrary.databaseFiles.dao;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;

public final class RoomRawQueryBuilder implements SupportSQLiteQuery {
    private static String TAG = RoomRawQueryBuilder.class.getSimpleName();
    public String modifiedquery;
    public Set<String> argumentSet =new HashSet<>();
    public boolean conversionCompleted = false;
    public RoomQueryListener roomQueryListener;

    public RoomRawQueryBuilder(String query, RoomQueryListener roomQueryListener) {
        //CommonMethod.log(TAG ,"Room query builder started...");
        this.argumentSet = new HashSet<>();
        this.modifiedquery = query.toUpperCase();
        this.roomQueryListener = roomQueryListener;
        getSql();
    }

    public interface RoomQueryListener{
        void complete(boolean flag, String query);
    }

    public RoomQueryListener getRoomQueryListener() {
        return roomQueryListener;
    }

    public void setRoomQueryListener(RoomQueryListener roomQueryListener) {
        this.roomQueryListener = roomQueryListener;
    }

    @Override
    public String getSql() {
        try {
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(modifiedquery);
            HashMap<String, String> params = CommonMethod.getAllParamsFromGenericDTO();
            conversionCompleted = false;
            //testingarg(modifiedquery);
            while (matcher.find()) {
                //CommonMethod.log(TAG , "Matching words " + matcher.group() + " Key value " + valueHashMap.get(matcher.group()) + " Count " + matcher.groupCount());
                argumentSet.add(matcher.group());
            }
            for (String arg : argumentSet){
                //CommonMethod.log(TAG , "Set value " + arg);
                String valueReceived = params.get(arg);
                if(valueReceived != null){
                    modifiedquery = modifiedquery.replaceAll("(?<!\\\\\\\\S)"+arg+"(?<!\\\\\\\\S)",valueReceived );
                    //CommonMethod.log(TAG , "Modified query " + modifiedquery);
                }
                else {
                    CommonMethod.log(TAG , "Did not find DTO value for " + arg);
                }
            }
            if(roomQueryListener != null){
                roomQueryListener.complete(true, modifiedquery);
            }
            else {
                roomQueryListener.complete(false, null);
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in raw query " + e.toString());
        }
        return null;
    }

    @Override
    public void bindTo(SupportSQLiteProgram statement) {

    }

    public void testingarg(String query){
        CommonMethod.log(TAG , "Starting test arg...");
        CommonMethod.logQuery(TAG , query);
        Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(query);
        Set<String> tempargset =new HashSet<>();
        while (matcher.find()) {
            CommonMethod.log(TAG , "Temp Matching words " + matcher.group() );
            tempargset.add(matcher.group());
        }
        for (String arg : tempargset){
            CommonMethod.log(TAG , "Set value " + arg);
        }

    }

    public boolean isAllArgumentsConverted(){
        return conversionCompleted;
    }

    @Override
    public int getArgCount() {
        return argumentSet.size();
    }
}
