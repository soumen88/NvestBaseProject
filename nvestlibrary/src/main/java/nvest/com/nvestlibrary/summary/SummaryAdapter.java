package nvest.com.nvestlibrary.summary;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = SummaryAdapter.class.getSimpleName();
    private final int ENTRY = 0, TITLE = 1;

    private Context context;
    private List<Object> list;

    public SummaryAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        switch (viewType) {
            case TITLE:
                view = inflater.inflate(R.layout.item_summary_title, viewGroup, false);
                viewHolder = new TitleViewHolder(view);
                break;
            case ENTRY:
                view = inflater.inflate(R.layout.item_summary_entry, viewGroup, false);
                viewHolder = new EntryViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Object data = list.get(position);
        switch (viewHolder.getItemViewType()) {
            case TITLE:
                String title = data.toString();

                TitleViewHolder titleViewHolder = (TitleViewHolder) viewHolder;
                titleViewHolder.tvTitle.setText(title);
                break;
            case ENTRY:
                EntryViewHolder entryViewHolder = (EntryViewHolder) viewHolder;
                SummaryEntry entry = (SummaryEntry) data;

                String label = entry.getLabel();
                String rupeeSymbol = context.getString(R.string.Rs);
                String amount = CommonMethod.getCommaSeparatedAmount(entry.getAmount(),2);

                entryViewHolder.tvLabel.setText(label);
                entryViewHolder.tvAmount.setText(String.format("%s %s", rupeeSymbol, amount));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof SummaryEntry) {
            return ENTRY;
        } else if (list.get(position) instanceof String) {
            return TITLE;
        }
        return -1;
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel, tvAmount;

        EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
