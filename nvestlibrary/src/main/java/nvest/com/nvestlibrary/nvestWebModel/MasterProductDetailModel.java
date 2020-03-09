package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterProductDetailModel {
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("Query")
    @Expose
    private String query;
    @SerializedName("Status")
    @Expose
    private boolean status;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
