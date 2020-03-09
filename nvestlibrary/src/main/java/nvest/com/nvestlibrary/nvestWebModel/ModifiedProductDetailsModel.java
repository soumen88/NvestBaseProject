package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModifiedProductDetailsModel {
    @SerializedName("IsRateMaster")
    @Expose
    private Boolean isRateMaster;
    @SerializedName("LastModify")
    @Expose
    private String lastModify;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    public int getIsRateMaster() {
        int val = (boolean)isRateMaster ? 1 : 0;
        return val;
    }

    public boolean getIsRateMasterBoolean() {
        return isRateMaster;
    }

    public void setIsRateMaster(Boolean isRateMaster) {
        this.isRateMaster = isRateMaster;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
