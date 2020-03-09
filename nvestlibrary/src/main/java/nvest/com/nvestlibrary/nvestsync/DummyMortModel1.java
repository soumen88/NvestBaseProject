package nvest.com.nvestlibrary.nvestsync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable.MortalityChargeRoom;

public class DummyMortModel1 {
    @SerializedName("Query")
    @Expose
    private ArrayList<MortalityChargeRoom> mortalityChargeRoomArrayList = new ArrayList<>();

    public ArrayList<MortalityChargeRoom> getMortalityChargeRoomArrayList() {
        return mortalityChargeRoomArrayList;
    }

    public void setMortalityChargeRoomArrayList(ArrayList<MortalityChargeRoom> mortalityChargeRoomArrayList) {
        this.mortalityChargeRoomArrayList = mortalityChargeRoomArrayList;
    }
}
