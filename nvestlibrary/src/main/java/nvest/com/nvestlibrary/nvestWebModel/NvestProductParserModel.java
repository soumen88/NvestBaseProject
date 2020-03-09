package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NvestProductParserModel {
    @SerializedName("Query")
    @Expose
    String Query;

    @SerializedName("Response")
    @Expose
    String Response;

    @SerializedName("Status")
    @Expose
    boolean Status = false;

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
