package nvest.com.nvestlibrary.solutionDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolutionProducts implements Parcelable {
    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("SolutionId")
    @Expose
    private int solutionId;

    @SerializedName("ProductId")
    @Expose
    private int productId;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("InputString")
    @Expose
    private String inputString;

    public SolutionProducts(Parcel in) {
        id = in.readInt();
        solutionId = in.readInt();
        productId = in.readInt();
        productName = in.readString();
        inputString = in.readString();
    }

    public SolutionProducts() {
    }

    public static final Creator<SolutionProducts> CREATOR = new Creator<SolutionProducts>() {
        @Override
        public SolutionProducts createFromParcel(Parcel in) {
            return new SolutionProducts(in);
        }

        @Override
        public SolutionProducts[] newArray(int size) {
            return new SolutionProducts[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(int solutionId) {
        this.solutionId = solutionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(solutionId);
        dest.writeInt(productId);
        dest.writeString(productName);
        dest.writeString(inputString);
    }
}
