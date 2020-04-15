package nvest.com.nvestlibrary.solutionDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class SolutionProductsAdapter extends RecyclerView.Adapter<SolutionProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<SolutionProducts> solutionProducts;
    private SolutionProductsListener solutionProductsListener;


    public SolutionProductsAdapter(Context context, List<SolutionProducts> solutionProducts, SolutionProductsListener solutionProductsListener) {
        this.context = context;
        this.solutionProducts = solutionProducts;
        this.solutionProductsListener = solutionProductsListener;
    }

    public void setSolutionProducts(List<SolutionProducts> solutionProducts) {
        this.solutionProducts = solutionProducts;
    }

    public void setSolutionProductsListener(SolutionProductsListener solutionProductsListener) {
        this.solutionProductsListener = solutionProductsListener;
    }

    public interface SolutionProductsListener {
        void onProductEdit(SolutionProducts solutionProducts);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_combo_product, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position) {
        SolutionProducts solutionProducts = this.solutionProducts.get(position);

        HashMap<String, String> combineProduct = GenericDTO.getAllCombinedProductsMap(solutionProducts.getProductId());

        // set on edit listener
        productViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solutionProductsListener.onProductEdit(solutionProducts);
            }
        });

        // set card view elevation
        productViewHolder.cardView.setCardElevation(2.0f);


        productViewHolder.cardView.setOnClickListener(view->{
            solutionProductsListener.onProductEdit(solutionProducts);
        });
        // set product name
        productViewHolder.tvProductName.setText(solutionProducts.getProductName());

        // set pt value
        String ptValue = combineProduct.get(NvestLibraryConfig.PR_PT_ANNOTATION);
        if (ptValue != null) {
            productViewHolder.tvPt.setText(ptValue);
        }
        /*List prPtList = GenericDTO.getAttributeList(NvestLibraryConfig.PR_PT_ANNOTATION);
        String ptValue = (String) prPtList.get(position);
        if (ptValue != null) {
            productViewHolder.tvPt.setText(ptValue);
        }*/

        // set ppt value
        String pptValue = combineProduct.get(NvestLibraryConfig.PR_PPT_ANNOTATION);
        if (pptValue != null) {
            productViewHolder.tvPpt.setText(pptValue);
        }
        /*List prPptList = GenericDTO.getAttributeList(NvestLibraryConfig.PR_PPT_ANNOTATION);
        String pptValue = (String) prPptList.get(position);
        if (pptValue != null) {
            productViewHolder.tvPpt.setText(pptValue);
        }*/

        // set sum assured value
        String sumAssuredValue = combineProduct.get(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
        if (sumAssuredValue != null) {
            productViewHolder.tvSumAssured.setText(sumAssuredValue);
        }
        /*List prSumAssuredList = GenericDTO.getAttributeList(NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION);
        String sumAssuredValue = (String) prSumAssuredList.get(position);
        if (sumAssuredValue != null) {
            productViewHolder.tvSumAssured.setText(sumAssuredValue);
        }*/


        /*String inputString = solutionProducts.getInputString();
        try {
            JSONArray jsonInputString = new JSONArray(inputString);
            for (int i = 0; i < jsonInputString.length(); i++) {
                JSONObject jsonObject = jsonInputString.getJSONObject(i);

                String key = "";
                String value = "";

                if (jsonObject.has("key")) {
                    key = jsonObject.getString("key");
                }

                if (jsonObject.has("value")) {
                    value = jsonObject.getString("value");
                }


                switch (key) {
                    case NvestLibraryConfig.PR_PT_ANNOTATION:
                        productViewHolder.tvPt.setText(value);
                        break;
                    case NvestLibraryConfig.PR_PPT_ANNOTATION:
                        productViewHolder.tvPpt.setText(value);
                        break;
                    case NvestLibraryConfig.PRODUCT_SUM_ASSURED_ANNOTATION:
                        productViewHolder.tvSumAssured.setText(String.format("%s %s", context.getString(R.string.Rs), CommonMethod.getCommaSeparatedAmount(Double.parseDouble(value), 0)));
                        break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public int getItemCount() {
        return solutionProducts.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvPt, tvPpt, tvSumAssured;
        MaterialCardView cardView;
        ImageView ivEdit;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPt = itemView.findViewById(R.id.tvPt);
            tvPpt = itemView.findViewById(R.id.tvPpt);
            tvSumAssured = itemView.findViewById(R.id.tvSumAssured);
            cardView = itemView.findViewById(R.id.cardView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
