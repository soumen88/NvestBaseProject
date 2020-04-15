package nvest.com.nvestlibrary.nvestCursorModel;


import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products implements Parcelable, Comparable<Products> {

    @SerializedName("CompCat")
    @Expose
    private Integer compCat;

    @SerializedName("CompCatName")
    @Expose
    private String compCatName;

    @SerializedName("CategoryId")
    @Expose
    private Integer categoryId;

    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;

    @SerializedName("CurrentStatus")
    @Expose
    private String currentStatus;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("EndDate")
    @Expose
    private String endDate;

    @SerializedName("EntryStartBy")
    @Expose
    private String entryStartBy;

    @SerializedName("EntryStartDate")
    @Expose
    private String entryStartDate;

    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("IsPension")
    @Expose
    private Boolean isPension;

    @SerializedName("Islive")
    @Expose
    private Boolean islive;

    @SerializedName("LastEditBy")
    @Expose
    private String lastEditBy;

    @SerializedName("LastEditDate")
    @Expose
    private String lastEditDate;

    @SerializedName("Platform")
    @Expose
    private String platform;

    @SerializedName("ProductId")
    @Expose
    private Integer productId;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("ProductUIN")
    @Expose
    private String productUIN;

    @SerializedName("StartDate")
    @Expose
    private String startDate;

    @SerializedName("StatusFlag")
    @Expose
    private int statusFlag;

    @SerializedName("SalesApp")
    @Expose
    private int salesApp;

    @SerializedName("ulip")
    @Expose
    private boolean ulip;

    public Products() {

    }

    public Products(String productName) {
        this.productName = productName;
    }

    protected Products(Parcel in) {
        if (in.readByte() == 0) {
            compCat = null;
        } else {
            compCat = in.readInt();
        }
        compCatName = in.readString();
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readInt();
        }
        if (in.readByte() == 0) {
            companyId = null;
        } else {
            companyId = in.readInt();
        }
        currentStatus = in.readString();
        description = in.readString();
        endDate = in.readString();
        entryStartBy = in.readString();
        entryStartDate = in.readString();
        imageUrl = in.readString();
        byte tmpIsPension = in.readByte();
        isPension = tmpIsPension == 0 ? null : tmpIsPension == 1;
        byte tmpIslive = in.readByte();
        islive = tmpIslive == 0 ? null : tmpIslive == 1;
        lastEditBy = in.readString();
        lastEditDate = in.readString();
        platform = in.readString();
        if (in.readByte() == 0) {
            productId = null;
        } else {
            productId = in.readInt();
        }
        productName = in.readString();
        productUIN = in.readString();
        startDate = in.readString();
        statusFlag = in.readInt();
        salesApp = in.readInt();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public Integer getCompCat() {
        return compCat;
    }

    public void setCompCat(Integer compCat) {
        this.compCat = compCat;
    }

    public String getCompCatName() {
        return compCatName;
    }

    public void setCompCatName(String compCatName) {
        this.compCatName = compCatName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEntryStartBy() {
        return entryStartBy;
    }

    public void setEntryStartBy(String entryStartBy) {
        this.entryStartBy = entryStartBy;
    }

    public String getEntryStartDate() {
        return entryStartDate;
    }

    public void setEntryStartDate(String entryStartDate) {
        this.entryStartDate = entryStartDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getPension() {
        return isPension;
    }

    public void setPension(Boolean pension) {
        isPension = pension;
    }

    public Boolean getIslive() {
        return islive;
    }

    public void setIslive(Boolean islive) {
        this.islive = islive;
    }

    public String getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(String lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUIN() {
        return productUIN;
    }

    public void setProductUIN(String productUIN) {
        this.productUIN = productUIN;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public int getSalesApp() {
        return salesApp;
    }

    public void setSalesApp(int salesApp) {
        this.salesApp = salesApp;
    }

    public void setUlip() {
        this.ulip = getPlatform().equals("4");
    }

    public boolean isUlip() {
        return getPlatform().equals("4");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (compCat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(compCat);
        }
        dest.writeString(compCatName);
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(categoryId);
        }
        if (companyId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(companyId);
        }
        dest.writeString(currentStatus);
        dest.writeString(description);
        dest.writeString(endDate);
        dest.writeString(entryStartBy);
        dest.writeString(entryStartDate);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isPension == null ? 0 : isPension ? 1 : 2));
        dest.writeByte((byte) (islive == null ? 0 : islive ? 1 : 2));
        dest.writeString(lastEditBy);
        dest.writeString(lastEditDate);
        dest.writeString(platform);
        if (productId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(productId);
        }
        dest.writeString(productName);
        dest.writeString(productUIN);
        dest.writeString(startDate);
        dest.writeInt(statusFlag);
        dest.writeInt(salesApp);
    }

    @Override
    public int compareTo(@NonNull Products products) {
        return productName.compareTo(products.getProductName());
        //return productId > products.getProductId();
    }


    @Override
    public boolean equals(Object obj) {
        if (this.productName.equals(((Products)obj).getProductName())) {
            return true;
        }
        return false;
    }
}