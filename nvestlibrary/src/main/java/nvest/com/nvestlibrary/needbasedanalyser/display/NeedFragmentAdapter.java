package nvest.com.nvestlibrary.needbasedanalyser.display;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.databaseFiles.dao.productMasterTable.ProductMasterRoom;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.KeyValuePair;

public class NeedFragmentAdapter extends RecyclerView.Adapter<NeedFragmentAdapter.MyViewHolder>{

    private static String TAG = NeedFragmentAdapter.class.getSimpleName();
    private List<KeyValuePair> productsList;
    private NeedProductListener needProductListener;
    private Context context;

    public NeedFragmentAdapter(List<KeyValuePair> productsList, Context context, NeedProductListener needProductListener) {
        this.productsList = productsList;
        this.context = context;
        this.needProductListener = needProductListener;
    }

    public interface NeedProductListener{
        void selectedItem(KeyValuePair keyValuePair);
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nvest_product_content_item, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            KeyValuePair keyValuePair = productsList.get(position);
            if(keyValuePair != null){

                holder.thumbnail.setOnClickListener(view->selectProduct(keyValuePair));
                holder.planName.setText(keyValuePair.getValue());
                //CommonMethod.loadimage(context, holder.thumbnail, null, TAG);
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in product contents adapter " + e.toString());
        }
    }

    public void selectProduct(KeyValuePair keyValuePair){
        CommonMethod.log(TAG  , "Sud product content adapter " );
        if(needProductListener != null){
            needProductListener.selectedItem(keyValuePair);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  planName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            planName = (TextView)view.findViewById(R.id.planName);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}

