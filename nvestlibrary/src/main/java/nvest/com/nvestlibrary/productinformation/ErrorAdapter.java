package nvest.com.nvestlibrary.productinformation;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;

public class ErrorAdapter extends RecyclerView.Adapter<ErrorAdapter.ErrorViewHolder> {
    private static final String TAG = ErrorAdapter.class.getSimpleName();
    private Context context;
    private HashMap<String, String> errors;
    private List<String> errorList;

    public ErrorAdapter(Context context, HashMap<String, String> errors) {
        this.context = context;
        this.errors = errors;
        errorList = new ArrayList<>(errors.values());
    }

    @NonNull
    @Override
    public ErrorAdapter.ErrorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CommonMethod.log(TAG, "POSITION=> " + i);
        View view = LayoutInflater.from(context).inflate(R.layout.item_validation_error, viewGroup, false);
        return new ErrorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorAdapter.ErrorViewHolder errorViewHolder, int position) {
        String error = errorList.get(position);

        errorViewHolder.tvBullet.setText(String.valueOf(position + 1) + ".");
        errorViewHolder.tvError.setText(error);
    }

    @Override
    public int getItemCount() {
        return errors.size();
    }

    class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView tvBullet, tvError;

        ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBullet = itemView.findViewById(R.id.tvBullet);
            tvError = itemView.findViewById(R.id.tvError);
        }
    }
}
