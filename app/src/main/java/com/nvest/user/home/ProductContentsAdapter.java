package com.nvest.user.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nvest.user.LogUtils.LogUtils;
import com.nvest.user.R;

import java.util.ArrayList;
import java.util.List;

public class ProductContentsAdapter extends RecyclerView.Adapter<ProductContentsAdapter.MyViewHolder>{

    private static String TAG = ProductContentsAdapter.class.getSimpleName();
    private List<ProductContents> productContentsList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nvest_product_content_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setProductContents(List<ProductContents> productContentsList){
        this.productContentsList = new ArrayList<>();
        this.productContentsList.addAll(productContentsList);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            ProductContents productContents = productContentsList.get(position);
            if(productContents != null){
                holder.status.setText("Status " + productContents.getStatusFlag());
                holder.description.setText("Description: " + productContents.getDescription());
                holder.productuin.setText("UNI "+ productContents.getProductUIN());
                holder.productName.setText("Name: "+productContents.getProductName());
                holder.txtPosition.setText("Position " + position);
            }
        }
        catch (Exception e){
            LogUtils.log(TAG , "Exception in product contents adapter " + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return productContentsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView status, description, productName, productuin, txtPosition;


        public MyViewHolder(View view) {
            super(view);
            status = (TextView)view.findViewById(R.id.status);
            description = (TextView)view.findViewById(R.id.description);
            productName = (TextView)view.findViewById(R.id.productName);
            productuin = (TextView)view.findViewById(R.id.productuin);
            txtPosition = (TextView)view.findViewById(R.id.position);
        }
    }
}
