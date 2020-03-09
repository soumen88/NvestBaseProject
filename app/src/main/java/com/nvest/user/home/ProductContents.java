package com.nvest.user.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductContents {
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
    private Object endDate;
    @SerializedName("EntryStartBy")
    @Expose
    private Integer entryStartBy;
    @SerializedName("EntryStartDate")
    @Expose
    private Object entryStartDate;
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
    private Integer lastEditBy;
    @SerializedName("LastEditDate")
    @Expose
    private Object lastEditDate;
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
    @SerializedName("SalesApp")
    @Expose
    private Integer salesApp;
    @SerializedName("StartDate")
    @Expose
    private Object startDate;
    @SerializedName("StatusFlag")
    @Expose
    private Integer statusFlag;

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

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public Integer getEntryStartBy() {
        return entryStartBy;
    }

    public void setEntryStartBy(Integer entryStartBy) {
        this.entryStartBy = entryStartBy;
    }

    public Object getEntryStartDate() {
        return entryStartDate;
    }

    public void setEntryStartDate(Object entryStartDate) {
        this.entryStartDate = entryStartDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPension() {
        return isPension;
    }

    public void setIsPension(Boolean isPension) {
        this.isPension = isPension;
    }

    public Boolean getIslive() {
        return islive;
    }

    public void setIslive(Boolean islive) {
        this.islive = islive;
    }

    public Integer getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(Integer lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public Object getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Object lastEditDate) {
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

    public Integer getSalesApp() {
        return salesApp;
    }

    public void setSalesApp(Integer salesApp) {
        this.salesApp = salesApp;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }
}
