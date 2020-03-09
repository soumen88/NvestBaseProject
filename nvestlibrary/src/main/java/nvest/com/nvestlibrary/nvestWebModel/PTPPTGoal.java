package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PTPPTGoal {

    @SerializedName("ProductId")
    @Expose
    public Integer ProductId;
    @SerializedName("PTDisplay")
    @Expose
    public String PTDisplay;
    @SerializedName("PTValue")
    @Expose
    public Integer PTValue;
    @SerializedName("PPTDisplay")
    @Expose
    public String PPTDisplay;
    @SerializedName("PPTValue")
    @Expose
    public Integer PPTValue;

    public Integer getProductId() {    return ProductId;   }

    public void setProductId(Integer ProductId) {
        this.ProductId = ProductId;
    }

    public String getPTDisplay() {
        return PTDisplay;
    }

    public void setPTDisplay(String PTDisplay) {
        this.PTDisplay = PTDisplay;
    }

    public Integer getPTValue() {
        return PTValue;
    }

    public void setPTValue(Integer PTValue) {
        this.PTValue = PTValue;
    }

    public String getPPTDisplay() {
        return PPTDisplay;
    }

    public void setPPTDisplay(String PTFormula) {
        this.PPTDisplay = PPTDisplay;
    }

    public Integer getPPTValue() {
        return PPTValue;
    }

    public void setPPTValue(Integer PPTValue) {
        this.PPTValue = PPTValue;
    }

}
