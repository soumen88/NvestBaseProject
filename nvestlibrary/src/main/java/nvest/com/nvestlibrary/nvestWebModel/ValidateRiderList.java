package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by viren on 28/07/17.
 */

public class ValidateRiderList {
    @SerializedName("RiderMessage")
    @Expose
    private List<ValidateInputList> riderMessage = null;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SumofRiderValidation")
    @Expose
    private String sumofRiderValidation;

    public List<ValidateInputList> getRiderMessage() {
        return riderMessage;
    }

    public void setRiderMessage(List<ValidateInputList> riderMessage) {
        this.riderMessage = riderMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSumofRiderValidation() {
        return sumofRiderValidation;
    }

    public void setSumofRiderValidation(String sumofRiderValidation) {
        this.sumofRiderValidation = sumofRiderValidation;
    }
}
