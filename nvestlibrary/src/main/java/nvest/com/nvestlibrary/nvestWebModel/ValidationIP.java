package nvest.com.nvestlibrary.nvestWebModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class ValidationIP implements Parcelable {
    @SerializedName("ProductId")
    @Expose
    public Integer ProductId;
    @SerializedName("ProductName")
    @Expose
    public String ProductName;
    @SerializedName("FailedCount")
    @Expose
    public Integer FailedCount;
    @SerializedName("AgeMasterId")
    @Expose
    Integer AgeMasterId;
    @SerializedName("AnnualPremium")
    @Expose
    Float AnnualPremium;
    @SerializedName("ModalPremium")
    @Expose
    Float ModalPremium;
    @SerializedName("LoadAnnPrems")
    @Expose
    Float LoadAnnPrems;
    @SerializedName("Tax")
    @Expose
    Float Tax;
    @SerializedName("TaxYr2")
    @Expose
    Float TaxYr2;
    @SerializedName("SA")
    @Expose
    Float SA;
    @SerializedName("SAMF")
    @Expose
    Float SAMF;
    @SerializedName("MonthlyIncome")
    @Expose
    Float MonthlyIncome;
    @SerializedName("ModeFreq")
    @Expose
    Integer ModeFreq;
    @SerializedName("ModeDisc")
    @Expose
    Float ModeDisc;
    HashMap<String, String> ErrorMessage;
    @SerializedName("PT")
    @Expose
    Integer PT;
    @SerializedName("PPT")
    @Expose
    Integer PPT;
    @SerializedName("WOProducts")
    @Expose
    List<ValidationIP> WOProducts;

    public ValidationIP() {

    }


    protected ValidationIP(Parcel in) {
        if (in.readByte() == 0) {
            ProductId = null;
        } else {
            ProductId = in.readInt();
        }
        ProductName = in.readString();
        if (in.readByte() == 0) {
            FailedCount = null;
        } else {
            FailedCount = in.readInt();
        }
        if (in.readByte() == 0) {
            AgeMasterId = null;
        } else {
            AgeMasterId = in.readInt();
        }
        if (in.readByte() == 0) {
            AnnualPremium = null;
        } else {
            AnnualPremium = in.readFloat();
        }
        if (in.readByte() == 0) {
            ModalPremium = null;
        } else {
            ModalPremium = in.readFloat();
        }
        if (in.readByte() == 0) {
            LoadAnnPrems = null;
        } else {
            LoadAnnPrems = in.readFloat();
        }
        if (in.readByte() == 0) {
            Tax = null;
        } else {
            Tax = in.readFloat();
        }
        if (in.readByte() == 0) {
            TaxYr2 = null;
        } else {
            TaxYr2 = in.readFloat();
        }
        if (in.readByte() == 0) {
            SA = null;
        } else {
            SA = in.readFloat();
        }
        if (in.readByte() == 0) {
            SAMF = null;
        } else {
            SAMF = in.readFloat();
        }
        if (in.readByte() == 0) {
            MonthlyIncome = null;
        } else {
            MonthlyIncome = in.readFloat();
        }
        if (in.readByte() == 0) {
            ModeFreq = null;
        } else {
            ModeFreq = in.readInt();
        }
        if (in.readByte() == 0) {
            ModeDisc = null;
        } else {
            ModeDisc = in.readFloat();
        }
        if (in.readByte() == 0) {
            PT = null;
        } else {
            PT = in.readInt();
        }
        if (in.readByte() == 0) {
            PPT = null;
        } else {
            PPT = in.readInt();
        }
        WOProducts = in.createTypedArrayList(ValidationIP.CREATOR);
    }

    public static final Creator<ValidationIP> CREATOR = new Creator<ValidationIP>() {
        @Override
        public ValidationIP createFromParcel(Parcel in) {
            return new ValidationIP(in);
        }

        @Override
        public ValidationIP[] newArray(int size) {
            return new ValidationIP[size];
        }
    };

    public Integer getProductId() {
        return ProductId;
    }

    public void setProductId(Integer ProductId) {
        this.ProductId = ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Integer getFailedCount() {
        return FailedCount;
    }

    public void setFailedCount(Integer FailedCount) {
        this.FailedCount = FailedCount;
    }

    public Integer getAgeMasterId() {
        return AgeMasterId;
    }

    public void setAgeMasterId(Integer AgeMasterId) {
        this.AgeMasterId = AgeMasterId;
    }

    public Float getAnnualPremium() {
        return AnnualPremium;
    }

    public void setAnnualPremium(Float AnnualPremium) {
        this.AnnualPremium = AnnualPremium;
    }

    public Float getModalPremium() {
        return ModalPremium;
    }

    public void setModalPremium(Float ModalPremium) {
        this.ModalPremium = ModalPremium;
    }

    public Float getLoadAnnPrems() {
        return LoadAnnPrems;
    }

    public void setLoadAnnPrems(Float LoadAnnPrems) {
        this.LoadAnnPrems = LoadAnnPrems;
    }

    public Float getTax() {
        return Tax;
    }

    public void setTax(Float Tax) {
        this.Tax = Tax;
    }

    public Float getTaxYr2() {
        return TaxYr2;
    }

    public void setTaxYr2(Float TaxYr2) {
        this.TaxYr2 = TaxYr2;
    }

    public Float getSA() {
        return SA;
    }

    public void setSA(Float SA) {
        this.SA = SA;
    }

    public Float getSAMF() {
        return SAMF;
    }

    public void setSAMF(Float SAMF) {
        this.SAMF = SAMF;
    }

    public Float getMonthlyIncome() {
        return MonthlyIncome;
    }

    public void setMonthlyIncome(Float MonthlyIncome) {
        this.MonthlyIncome = MonthlyIncome;
    }

    public Integer getModeFreq() {
        return ModeFreq;
    }

    public void setModeFreq(Integer ModeFreq) {
        this.ModeFreq = ModeFreq;
    }

    public Float getModeDisc() {
        return ModeDisc;
    }

    public void setModeDisc(Float ModeDisc) {
        this.ModeDisc = ModeDisc;
    }

    public HashMap<String, String> getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(HashMap<String, String> ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

    public Integer getPT() {
        return PT;
    }

    public void setPT(Integer PT) {
        this.PT = PT;
    }

    public Integer getPPT() {
        return PPT;
    }

    public void setPPT(Integer PPT) {
        this.PPT = PPT;
    }

    public List<ValidationIP> getWOProducts() {
        return WOProducts;
    }

    public void setWOProducts(List<ValidationIP> WOProducts) {
        this.WOProducts = WOProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ProductId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ProductId);
        }
        if (FailedCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(FailedCount);
        }
        if (AgeMasterId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(AgeMasterId);
        }
        if (AnnualPremium == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(AnnualPremium);
        }
        if (ModalPremium == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(ModalPremium);
        }
        if (LoadAnnPrems == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(LoadAnnPrems);
        }
        if (Tax == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(Tax);
        }
        if (TaxYr2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(TaxYr2);
        }
        if (SA == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(SA);
        }
        if (SAMF == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(SAMF);
        }
        if (MonthlyIncome == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(MonthlyIncome);
        }
        if (ModeFreq == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ModeFreq);
        }
        if (ModeDisc == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(ModeDisc);
        }
        if (PT == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(PT);
        }
        if (PPT == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(PPT);
        }
        dest.writeTypedList(WOProducts);
    }
}
