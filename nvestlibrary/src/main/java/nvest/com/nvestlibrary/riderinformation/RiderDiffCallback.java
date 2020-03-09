package nvest.com.nvestlibrary.riderinformation;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import nvest.com.nvestlibrary.nvestWebModel.RiderInformationModel;

public class RiderDiffCallback extends DiffUtil.Callback {
    private final List<RiderInformationModel> oldList;
    private final List<RiderInformationModel> newList;

    public RiderDiffCallback(List<RiderInformationModel> oldList, List<RiderInformationModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getRiderId() == newList.get(newItemPosition).getRiderId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
