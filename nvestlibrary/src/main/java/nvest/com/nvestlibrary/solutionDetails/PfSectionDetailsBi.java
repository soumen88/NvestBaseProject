package nvest.com.nvestlibrary.solutionDetails;

public class PfSectionDetailsBi {
    private int id;
    private int productId;
    private String outputKeyword;
    private String keywordHeader;
    private int isSum;
    private int sectionId;
    private int sequence;

    public PfSectionDetailsBi(int id, int productId, String outputKeyword, String keywordHeader, int isSum, int sectionId, int sequence) {
        this.id = id;
        this.productId = productId;
        this.outputKeyword = outputKeyword;
        this.keywordHeader = keywordHeader;
        this.isSum = isSum;
        this.sectionId = sectionId;
        this.sequence = sequence;
    }

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

    public String getOutputKeyword() {
        return outputKeyword;
    }

    public void setOutputKeyword(String outputKeyword) {
        this.outputKeyword = outputKeyword;
    }

    public String getKeywordHeader() {
        return keywordHeader;
    }

    public void setKeywordHeader(String keywordHeader) {
        this.keywordHeader = keywordHeader;
    }

    public int getIsSum() {
        return isSum;
    }

    public void setIsSum(int isSum) {
        this.isSum = isSum;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
