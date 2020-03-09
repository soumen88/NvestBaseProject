package nvest.com.nvestlibrary.generatePdf.models;

public class OutputSummary {

    private int id, productId, display;
    private String outputKeywordName, outputKeyword, biRow, displayType, section, optionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getOutputKeywordName() {
        return outputKeywordName;
    }

    public void setOutputKeywordName(String outputKeywordName) {
        this.outputKeywordName = outputKeywordName;
    }

    public String getOutputKeyword() {
        return outputKeyword.toUpperCase();
    }

    public void setOutputKeyword(String outputKeyword) {
        this.outputKeyword = outputKeyword;
    }

    public String getBiRow() {
        return biRow;
    }

    public void setBiRow(String biRow) {
        this.biRow = biRow;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    @Override
    public String toString() {
        return "\n\nOutputSummary{" +
                "id=" + id +
                ", productId=" + productId +
                ", display=" + display +
                ", outputKeywordName='" + outputKeywordName + '\'' +
                ", outputKeyword='" + outputKeyword + '\'' +
                ", biRow='" + biRow + '\'' +
                ", displayType='" + displayType + '\'' +
                ", section='" + section + '\'' +
                ", optionId='" + optionId + '\'' +
                "}\n";
    }
}
