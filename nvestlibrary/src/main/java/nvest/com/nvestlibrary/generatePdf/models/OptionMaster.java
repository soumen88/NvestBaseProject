package nvest.com.nvestlibrary.generatePdf.models;

public class OptionMaster {
    private int productId, optionId, optionLevelId, isDefault;
    private String optionName, optionParent, inputFieldName, inputFieldType;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getOptionLevelId() {
        return optionLevelId;
    }

    public void setOptionLevelId(int optionLevelId) {
        this.optionLevelId = optionLevelId;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionParent() {
        return optionParent;
    }

    public void setOptionParent(String optionParent) {
        this.optionParent = optionParent;
    }

    public String getInputFieldName() {
        return inputFieldName;
    }

    public void setInputFieldName(String inputFieldName) {
        this.inputFieldName = inputFieldName;
    }

    public String getInputFieldType() {
        return inputFieldType;
    }

    public void setInputFieldType(String inputFieldType) {
        this.inputFieldType = inputFieldType;
    }

    @Override
    public String toString() {
        return "\n\nOptionMaster{" +
                "productId=" + productId +
                ", optionId=" + optionId +
                ", optionLevelId=" + optionLevelId +
                ", isDefault=" + isDefault +
                ", optionName='" + optionName + '\'' +
                ", optionParent='" + optionParent + '\'' +
                ", inputFieldName='" + inputFieldName + '\'' +
                ", inputFieldType='" + inputFieldType + '\'' +
                "}\n";
    }
}
