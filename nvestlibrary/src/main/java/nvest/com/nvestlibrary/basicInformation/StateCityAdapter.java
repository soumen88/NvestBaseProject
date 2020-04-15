package nvest.com.nvestlibrary.basicInformation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestWebModel.StateCitiesModel;

public class StateCityAdapter extends RecyclerView.Adapter<StateCityAdapter.MyViewHolder> {
    private ArrayList<StateCitiesModel> stateCitiesModelArrayList;
    private static String TAG = StateCityAdapter.class.getSimpleName();
    private StateCityListener stateCityListener;

    public StateCityListener getStateCityListener() {
        return stateCityListener;
    }

    public void setStateCityListener(StateCityListener stateCityListener) {
        this.stateCityListener = stateCityListener;
    }

    public interface StateCityListener{
        void selectedStateCityItem(StateCitiesModel stateCitiesModel);
    }

    public StateCityAdapter(ArrayList<StateCitiesModel> stateCitiesModelArrayList, StateCityListener stateCityListener) {
        this.stateCitiesModelArrayList = stateCitiesModelArrayList;
        CommonMethod.log(TAG,"count " +stateCitiesModelArrayList.size());
        this.stateCityListener = stateCityListener;
    }

    public void setProductContents(ArrayList<StateCitiesModel> stateCitiesModelArrayList, StateCityListener stateCityListener){
        this.stateCitiesModelArrayList = new ArrayList<>();
        this.stateCitiesModelArrayList.addAll(stateCitiesModelArrayList);
        this.stateCityListener = stateCityListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        StateCitiesModel stateCitiesModel=stateCitiesModelArrayList.get(position);
        if(stateCitiesModel!=null){
            CommonMethod.log(TAG , "Position " + position + " City name " + stateCitiesModel.getCityName());
            myViewHolder.title.setText(stateCitiesModel.getCityName());
            myViewHolder.title1.setText(stateCitiesModel.getStateName());
            myViewHolder.relStateCity.setOnClickListener(view->{
                if(stateCityListener != null){
                    stateCityListener.selectedStateCityItem(stateCitiesModel);
                }
            });

        }
        else {
            CommonMethod.log(TAG , "If state city is null");
        }
    }

    @Override
    public int getItemCount() {
        return stateCitiesModelArrayList.size();
        //return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView title1;
        public TextView title2;
        public RelativeLayout relStateCity;
        //public CardView card;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.state);
            title1 = (TextView)view.findViewById(R.id.city);
            title2 = (TextView)view.findViewById(R.id.title2);
            relStateCity = (RelativeLayout) view.findViewById(R.id.relStateCity);
            //card = (CardView) view.findViewById(R.id.item_card);

        }
    }
}
