package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Value {

    @SerializedName("Keyword")
    @Expose
    private String keyword;
    @SerializedName("valuePair")
    @Expose
    private List<KeyValuePair> valuePair = null;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<KeyValuePair> getValuePair() {
        return valuePair;
    }

    public void setValuePair(List<KeyValuePair> valuePair) {
        this.valuePair = valuePair;
    }

}