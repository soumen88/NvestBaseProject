package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InputByteStreamReader {
    @SerializedName("DataBytes")
    @Expose
    //private ArrayList<Byte> dataBytes = null;
    private ArrayList<Byte> dataBytes = null;
    @SerializedName("Status")
    @Expose
    private String status;

    public ArrayList<Byte> getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(ArrayList<Byte> dataBytes) {
        this.dataBytes = dataBytes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
