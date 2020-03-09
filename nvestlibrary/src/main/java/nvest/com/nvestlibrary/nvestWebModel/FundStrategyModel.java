package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundStrategyModel implements Comparable<FundStrategyModel>{
    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    @SerializedName("StrategyId")
    @Expose
    private Integer strategyId;

    @SerializedName("StrategyName")
    @Expose
    private String strategyName;

    @SerializedName("ParentId")
    @Expose
    private String parentId;

    @SerializedName("isInput")
    @Expose
    private boolean isInput;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    @Override
    public int compareTo(FundStrategyModel fundStrategyModel) {
        return (strategyId - fundStrategyModel.getStrategyId());
    }
}
