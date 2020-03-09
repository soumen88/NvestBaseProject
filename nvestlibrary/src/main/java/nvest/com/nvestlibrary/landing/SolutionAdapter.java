package nvest.com.nvestlibrary.landing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.solutionDetails.SolutionMaster;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.SolutionViewHolder> {

    private Context context;
    private List<SolutionMaster> solutionMasters;
    private SolutionAdapterListener solutionAdapterListener;

    public SolutionAdapter(Context context, List<SolutionMaster> solutionMasters) {
        this.context = context;
        this.solutionMasters = solutionMasters;
    }

    public void setSolutionMasters() {

    }

    public void setSolutionAdapterListener(SolutionAdapterListener solutionAdapterListener) {
        this.solutionAdapterListener = solutionAdapterListener;
    }

    @NonNull
    @Override
    public SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_solution, viewGroup, false);
        return new SolutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionViewHolder solutionViewHolder, int position) {
        SolutionMaster solutionMaster = solutionMasters.get(position);
        solutionViewHolder.tvSolutionName.setText(solutionMaster.getSolutionName());
        solutionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solutionAdapterListener.solutionSelected(solutionMaster);
            }
        });
    }

    @Override
    public int getItemCount() {
        return solutionMasters.size();
    }

    class SolutionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSolutionName;

        SolutionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSolutionName = itemView.findViewById(R.id.solutionName);
        }
    }

    public interface SolutionAdapterListener {
        void solutionSelected(SolutionMaster solutionMaster);
    }
}

