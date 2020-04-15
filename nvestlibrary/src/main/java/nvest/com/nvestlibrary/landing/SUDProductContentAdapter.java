package nvest.com.nvestlibrary.landing;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.nvestCursorModel.Products;

public class SUDProductContentAdapter extends RecyclerView.Adapter<SUDProductContentAdapter.MyViewHolder>{

    private static String TAG = SUDProductContentAdapter.class.getSimpleName();
    private List<Products> productsList;
    private SelectProductListener selectProductListener;
    private Context context;

    public SUDProductContentAdapter(List<Products> productsList, Context context, SelectProductListener selectProductListener) {
        this.productsList = productsList;
        this.context = context;
        this.selectProductListener = selectProductListener;
    }

    public interface SelectProductListener{
        void selectedItem(Products products);
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nvest_product_content_item, parent, false);
        return new MyViewHolder(itemView);


    }

    public void setProductContents(List<Products> productsList){
        this.productsList = new ArrayList<>();
        this.productsList.addAll(productsList);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Products productContents = productsList.get(position);
            if(productContents != null){
                holder.productName.setText("Name: "+productContents.getProductName());
                holder.thumbnail.setOnClickListener(view->selectProduct(productContents));
                holder.planName.setText(productContents.getCompCatName());
                //CommonMethod.loadimage(context, holder.thumbnail, null, TAG);
            }
        }
        catch (Exception e){
            CommonMethod.log(TAG , "Exception in product contents adapter " + e.toString());
        }
    }

    public void selectProduct(Products products){
        CommonMethod.log(TAG  , "Sud product content adapter " + selectProductListener);
        if(selectProductListener != null){
            selectProductListener.selectedItem(products);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, planName;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            productName = (TextView)view.findViewById(R.id.productName);
            planName = (TextView)view.findViewById(R.id.planName);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}

