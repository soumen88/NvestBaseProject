package nvest.com.nvestlibrary.nvestWebModel;

public class FundsModel {
    private int fundId, companyId;
    private String fundName, fundType, fundUin;
    private double fmcBaseCharge, guaranteeCharge;

    public int getFundId() {
        return fundId;
    }

    public void setFundId(int fundId) {
        this.fundId = fundId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundUin() {
        return fundUin;
    }

    public void setFundUin(String fundUin) {
        this.fundUin = fundUin;
    }

    public double getFmcBaseCharge() {
        return fmcBaseCharge;
    }

    public void setFmcBaseCharge(double fmcBaseCharge) {
        this.fmcBaseCharge = fmcBaseCharge;
    }

    public double getGuaranteeCharge() {
        return guaranteeCharge;
    }

    public void setGuaranteeCharge(double guaranteeCharge) {
        this.guaranteeCharge = guaranteeCharge;
    }
}
