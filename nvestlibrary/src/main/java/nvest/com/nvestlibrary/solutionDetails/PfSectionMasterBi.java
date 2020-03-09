package nvest.com.nvestlibrary.solutionDetails;

public class PfSectionMasterBi {
    private int id;
    private String sectionName;
    private String sectionHeader;
    private int defaultSequence;

    public PfSectionMasterBi(int id, String sectionName, String sectionHeader, int defaultSequence) {
        this.id = id;
        this.sectionName = sectionName;
        this.sectionHeader = sectionHeader;
        this.defaultSequence = defaultSequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(String sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public int getDefaultSequence() {
        return defaultSequence;
    }

    public void setDefaultSequence(int defaultSequence) {
        this.defaultSequence = defaultSequence;
    }
}
