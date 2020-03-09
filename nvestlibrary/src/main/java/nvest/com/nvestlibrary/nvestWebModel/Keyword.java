package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keyword {

    @SerializedName("FieldCaption")
    @Expose
    private String fieldCaption;
    @SerializedName("FieldType")
    @Expose
    private String fieldType;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("InputRefScreen")
    @Expose
    private Object inputRefScreen;
    @SerializedName("IsMapped")
    @Expose
    private Boolean isMapped;
    @SerializedName("KeywordName")
    @Expose
    private String keywordName;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Object getInputRefScreen() {
        return inputRefScreen;
    }

    public void setInputRefScreen(Object inputRefScreen) {
        this.inputRefScreen = inputRefScreen;
    }

    public Boolean getIsMapped() {
        return isMapped;
    }

    public void setIsMapped(Boolean isMapped) {
        this.isMapped = isMapped;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}