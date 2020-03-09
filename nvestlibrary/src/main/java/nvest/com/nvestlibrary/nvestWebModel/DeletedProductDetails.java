package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeletedProductDetails {
    @SerializedName("DeletedDate")
    @Expose
    private String deletedDate;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
