package com.nvest.user.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.R;
import com.nvest.user.databaseFiles.dao.tempbitable.TestPojoSUD;

import java.util.List;

public class TempBIAdapter extends RecyclerView.Adapter<TempBIAdapter.MyViewHolder> {

    private static String TAG = TempBIAdapter.class.getSimpleName();
    private List<TestPojoSUD> testPojoSUDList;

    public TempBIAdapter(List<TestPojoSUD> testPojoSUDList) {
        this.testPojoSUDList = testPojoSUDList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_table, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            TestPojoSUD testPojoSUD = testPojoSUDList.get(position);
            if(testPojoSUD != null){
                holder.age.setText("Age: " + testPojoSUD.getLI_ATTAINED_AGE());
                holder.policyYear.setText("Policy Year: " + testPojoSUD.getPOLICYYEAR());
                holder.annPremium.setText("Premium: " + testPojoSUD.getPREMIUMRATE());
                holder.dbG.setText("DBG: " + testPojoSUD.getDB_G());
                holder.txtPosition.setText("Position: " + position);
            }
            else {
                LogUtils.log(TAG , "Test sud is null at " + position);
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception " + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return testPojoSUDList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView age, policyYear, annPremium, dbG, txtPosition;


        public MyViewHolder(View view) {
            super(view);
            age = (TextView)view.findViewById(R.id.age);
            policyYear = (TextView)view.findViewById(R.id.policyYear);
            annPremium = (TextView)view.findViewById(R.id.annPremium);
            dbG = (TextView)view.findViewById(R.id.dbG);
            txtPosition = (TextView)view.findViewById(R.id.txtPosition);


        }
    }
}
