package nvest.com.nvestlibrary.nvestWebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by viren on 02/07/17.
 */

public class ValidateInputList {
    @SerializedName("AgeMasterId")
    @Expose
    private Double ageMasterId;
    @SerializedName("AnnualPremium")
    @Expose
    private Double annualPremium;
    @SerializedName("EntryAge")
    @Expose
    private String entryAge;
    @SerializedName("ErrorMessage")
    @Expose
    private List<StringKeyValuePairSmallCase> errorMessage = null;
    @SerializedName("FailedCount")
    @Expose
    private Integer failedCount;
    @SerializedName("GeneralError")
    @Expose
    private String generalError;
    @SerializedName("IpKeyword")
    @Expose
    private List<Object> ipKeyword = null;
    @SerializedName("IpKwMessage")
    @Expose
    private List<Object> ipKwMessage = null;
    @SerializedName("LoadAnnPrems")
    @Expose
    private Double loadAnnPrems;
    @SerializedName("MIError")
    @Expose
    private String mIError;
    @SerializedName("MaturityAge")
    @Expose
    private String maturityAge;
    @SerializedName("ModalPremium")
    @Expose
    private Double modalPremium;
    @SerializedName("ModeDisc")
    @Expose
    private Double modeDisc;
    @SerializedName("ModeFreq")
    @Expose
    private Integer modeFreq;
    @SerializedName("OptionError")
    @Expose
    private List<Object> optionError = null;
    @SerializedName("PPT")
    @Expose
    private Integer pPT;
    @SerializedName("PT")
    @Expose
    private Integer pT;
    @SerializedName("Premium")
    @Expose
    private String premium;
    @SerializedName("RiderMessage")
    @Expose
    private String riderMessage;
    @SerializedName("SA")
    @Expose
    private Double sA;
    @SerializedName("SumAssured")
    @Expose
    private String sumAssured;
    @SerializedName("Tax")
    @Expose
    private Double tax;
    @SerializedName("UlipSA")
    @Expose
    private String ulipSA;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getAgeMasterId() {
        return ageMasterId;
    }

    public void setAgeMasterId(Double ageMasterId) {
        this.ageMasterId = ageMasterId;
    }

    public String getEntryAge() {
        return entryAge;
    }

    public void setEntryAge(String entryAge) {
        this.entryAge = entryAge;
    }

    public List<StringKeyValuePairSmallCase> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<StringKeyValuePairSmallCase> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public String getGeneralError() {
        return generalError;
    }

    public void setGeneralError(String generalError) {
        this.generalError = generalError;
    }

    public List<Object> getIpKeyword() {
        return ipKeyword;
    }

    public void setIpKeyword(List<Object> ipKeyword) {
        this.ipKeyword = ipKeyword;
    }

    public List<Object> getIpKwMessage() {
        return ipKwMessage;
    }

    public void setIpKwMessage(List<Object> ipKwMessage) {
        this.ipKwMessage = ipKwMessage;
    }

    public String getMIError() {
        return mIError;
    }

    public void setMIError(String mIError) {
        this.mIError = mIError;
    }

    public String getMaturityAge() {
        return maturityAge;
    }

    public void setMaturityAge(String maturityAge) {
        this.maturityAge = maturityAge;
    }

    public Double getModeDisc() {
        return modeDisc;
    }

    public void setModeDisc(Double modeDisc) {
        this.modeDisc = modeDisc;
    }

    public Integer getModeFreq() {
        return modeFreq;
    }

    public void setModeFreq(Integer modeFreq) {
        this.modeFreq = modeFreq;
    }

    public List<Object> getOptionError() {
        return optionError;
    }

    public void setOptionError(List<Object> optionError) {
        this.optionError = optionError;
    }

    public Integer getPPT() {
        return pPT;
    }

    public void setPPT(Integer pPT) {
        this.pPT = pPT;
    }

    public Integer getPT() {
        return pT;
    }

    public void setPT(Integer pT) {
        this.pT = pT;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getRiderMessage() {
        return riderMessage;
    }

    public void setRiderMessage(String riderMessage) {
        this.riderMessage = riderMessage;
    }

    public String getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(String sumAssured) {
        this.sumAssured = sumAssured;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getUlipSA() {
        return ulipSA;
    }

    public void setUlipSA(String ulipSA) {
        this.ulipSA = ulipSA;
    }

    public Double getAnnualPremium() {
        return annualPremium;
    }

    public void setAnnualPremium(Double annualPremium) {
        this.annualPremium = annualPremium;
    }

    public Double getLoadAnnPrems() {
        return loadAnnPrems;
    }

    public void setLoadAnnPrems(Double loadAnnPrems) {
        this.loadAnnPrems = loadAnnPrems;
    }

    public String getmIError() {
        return mIError;
    }

    public void setmIError(String mIError) {
        this.mIError = mIError;
    }

    public Double getModalPremium() {
        return modalPremium;
    }

    public void setModalPremium(Double modalPremium) {
        this.modalPremium = modalPremium;
    }

    public Integer getpPT() {
        return pPT;
    }

    public void setpPT(Integer pPT) {
        this.pPT = pPT;
    }

    public Integer getpT() {
        return pT;
    }

    public void setpT(Integer pT) {
        this.pT = pT;
    }

    public Double getsA() {
        return sA;
    }

    public void setsA(Double sA) {
        this.sA = sA;
    }
}
