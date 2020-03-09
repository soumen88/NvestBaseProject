package nvest.com.nvestlibrary.databaseFiles.dao.productMasterTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.PRODUCT_MASTER_TABLE)
public class ProductMasterRoom {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name = "ProductId")
    private int productid;

    @ColumnInfo(name = "CompanyId")
    private int companyId;

    @ColumnInfo(name = "CategoryId")
    private  int categoryId ;

    @ColumnInfo(name = "ProductName")
    private  String productName ;

    @ColumnInfo(name = "ProductUIN")
    private  String productUIN ;

    @ColumnInfo(name = "StatusFlag")
    private  int statusFlag;

    @ColumnInfo(name = "IsPension")
    private  boolean isPension ;

    @ColumnInfo(name = "Platform")
    private  String platform ;

    @ColumnInfo(name = "Islive")
    private  boolean islive ;

    @ColumnInfo(name = "StartDate")
    private  String startDate ;

    @ColumnInfo(name = "EndDate")
    private  String endDate ;

    @ColumnInfo(name = "EntryStartDate")
    private  String entryStartDate ;

    @ColumnInfo(name = "EntryStartBy")
    private  int entryStartBy ;

    @ColumnInfo(name = "CurrentStatus")
    private  String currentStatus ;

    @ColumnInfo(name = "LastEditBy")
    private  int lastEditBy ;

    @ColumnInfo(name = "LastEditDate")
    private  String lastEditDate;

    @ColumnInfo(name = "Description")
    private  String description;

    @ColumnInfo(name = "ImageUrl")
    private  String imageUrl;

    @ColumnInfo(name = "SalesApp")
    private  int salesapp;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public boolean isPension() {
        return isPension;
    }

    public void setPension(boolean pension) {
        isPension = pension;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEntryStartDate() {
        return entryStartDate;
    }

    public void setEntryStartDate(String entryStartDate) {
        this.entryStartDate = entryStartDate;
    }

    public int getEntryStartBy() {
        return entryStartBy;
    }

    public void setEntryStartBy(int entryStartBy) {
        this.entryStartBy = entryStartBy;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(int lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSalesapp() {
        return salesapp;
    }

    public void setSalesapp(int salesapp) {
        this.salesapp = salesapp;
    }
}
