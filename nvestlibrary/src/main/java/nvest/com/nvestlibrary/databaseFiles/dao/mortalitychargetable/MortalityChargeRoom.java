package nvest.com.nvestlibrary.databaseFiles.dao.mortalitychargetable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.MORTALITY_CHARGE_TABLE)
public class MortalityChargeRoom {

    /*@PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private int _id;*/

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "MCId")
    @NonNull
    private String mcId;

    @ColumnInfo(name = "ProductId")
    private String productid;

    @ColumnInfo(name = "LaAge")
    private  String laAge ;

    @ColumnInfo(name = "Gender")
    private  String gender ;

    @ColumnInfo(name = "OptionId")
    @Nullable
    private String optionId;

    @ColumnInfo(name = "PT")
    @Nullable
    private String pt ;

    @ColumnInfo(name = "PPT")
    @Nullable
    private String ppt ;

    @ColumnInfo(name = "SpouseAge")
    @Nullable
    private String spouseAge ;

    @ColumnInfo(name = "SmokeStatus")
    @Nullable
    private String smokeStatus ;

    @ColumnInfo(name = "Rate")
    private String rate ;


    public MortalityChargeRoom() {

    }


    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getLaAge() {
        return laAge;
    }

    public void setLaAge(String laAge) {
        this.laAge = laAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Nullable
    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(@Nullable String optionId) {
        this.optionId = optionId;
    }

    @Nullable
    public String getPt() {
        return pt;
    }

    public void setPt(@Nullable String pt) {
        this.pt = pt;
    }

    @Nullable
    public String getPpt() {
        return ppt;
    }

    public void setPpt(@Nullable String ppt) {
        this.ppt = ppt;
    }

    @Nullable
    public String getSpouseAge() {
        return spouseAge;
    }

    public void setSpouseAge(@Nullable String spouseAge) {
        this.spouseAge = spouseAge;
    }

    @Nullable
    public String getSmokeStatus() {
        return smokeStatus;
    }

    public void setSmokeStatus(@Nullable String smokeStatus) {
        this.smokeStatus = smokeStatus;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
