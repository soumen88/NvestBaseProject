package nvest.com.nvestlibrary.generatePdf.models;

public class PtPptMaster {
    private int id, productId, ptPptId, optionId;
    private String ptDisplay, ptFormula, pptDisplay, pptFormula, pptName;

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

    public int getPtPptId() {
        return ptPptId;
    }

    public void setPtPptId(int ptPptId) {
        this.ptPptId = ptPptId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getPtDisplay() {
        return ptDisplay;
    }

    public void setPtDisplay(String ptDisplay) {
        this.ptDisplay = ptDisplay;
    }

    public String getPtFormula() {
        return ptFormula;
    }

    public void setPtFormula(String ptFormula) {
        this.ptFormula = ptFormula;
    }

    public String getPptDisplay() {
        return pptDisplay;
    }

    public void setPptDisplay(String pptDisplay) {
        this.pptDisplay = pptDisplay;
    }

    public String getPptFormula() {
        return pptFormula;
    }

    public void setPptFormula(String pptFormula) {
        this.pptFormula = pptFormula;
    }

    public String getPptName() {
        return pptName;
    }

    public void setPptName(String pptName) {
        this.pptName = pptName;
    }
}
