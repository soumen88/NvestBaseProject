package nvest.com.nvestlibrary.solutionDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolutionMaster implements Parcelable {

    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("SolutionName")
    @Expose
    private String solutionName;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("Needs")
    @Expose
    private int needs;

    @SerializedName("PremiumInput")
    @Expose
    private String premiumInput;

    @SerializedName("ValidationError")
    @Expose
    private String validationError;

    @SerializedName("AllowEdit")
    @Expose
    private String AllowEdit;

    public SolutionMaster() {
    }

    private SolutionMaster(Parcel in) {
        id = in.readInt();
        solutionName = in.readString();
        description = in.readString();
        needs = in.readInt();
        premiumInput = in.readString();
        validationError = in.readString();
        AllowEdit = in.readString();
    }

    public static final Creator<SolutionMaster> CREATOR = new Creator<SolutionMaster>() {
        @Override
        public SolutionMaster createFromParcel(Parcel in) {
            return new SolutionMaster(in);
        }

        @Override
        public SolutionMaster[] newArray(int size) {
            return new SolutionMaster[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNeeds() {
        return needs;
    }

    public void setNeeds(int needs) {
        this.needs = needs;
    }

    public String getPremiumInput() {
        return premiumInput;
    }

    public void setPremiumInput(String premiumInput) {
        this.premiumInput = premiumInput;
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }

    public String getAllowEdit() {
        return AllowEdit;
    }

    public void setAllowEdit(String allowEdit) {
        AllowEdit = allowEdit;
    }

    @Override
    public String toString() {
        return "SolutionMaster{" +
                "id=" + id +
                ", solutionName='" + solutionName + '\'' +
                ", description='" + description + '\'' +
                ", needs=" + needs +
                ", premiumInput='" + premiumInput + '\'' +
                ", validationError='" + validationError + '\'' +
                ", AllowEdit='" + AllowEdit + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(solutionName);
        dest.writeString(description);
        dest.writeInt(needs);
        dest.writeString(premiumInput);
        dest.writeString(validationError);
        dest.writeString(AllowEdit);
    }
}
