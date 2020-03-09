package nvest.com.nvestlibrary.summary;

public class SummaryEntry {
    private String label;
    private float amount;

    public static final String ANNUAL_PREMIUM = "Annual Premium";
    public static final String INSTALLMENT_PREMIUM = "Installment Premium";
    public static final String GST = "Goods and Services Tax";
    public static final String INSTALLMENT_PREMIUM_WITH_GST = "Inst. Premium with GST";


    public SummaryEntry(String label, float amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
