package nvest.com.nvestlibrary.databaseFiles.dao.bonusguaranteetable;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.BONUS_GUARANTEE_TABLE)
public class BonusGuaranteeRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "BONUSGID")
    private int bonusgId;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name = "PT")
    private int pt;

    @ColumnInfo(name = "PPT")
    private int ppt;

    @ColumnInfo(name = "OPTIONID")
    private int optionId;

    @ColumnInfo(name = "FROMSA")
    private Double fromSA;

    @ColumnInfo(name = "TOSA")
    private Double toSA;

    @ColumnInfo(name = "FROMAGE")
    private int fromAge;

    @ColumnInfo(name = "TOAGE")
    private int toAge;

    @ColumnInfo(name = "FROMPOLICYYEAR")
    private int frompolicyyear;

    @ColumnInfo(name = "TOPOLICYYEAR")
    private int topolicyyear;

    @ColumnInfo(name = "RATE")
    private Double rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBonusgId() {
        return bonusgId;
    }

    public void setBonusgId(int bonusgId) {
        this.bonusgId = bonusgId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public int getPpt() {
        return ppt;
    }

    public void setPpt(int ppt) {
        this.ppt = ppt;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public Double getFromSA() {
        return fromSA;
    }

    public void setFromSA(Double fromSA) {
        this.fromSA = fromSA;
    }

    public Double getToSA() {
        return toSA;
    }

    public void setToSA(Double toSA) {
        this.toSA = toSA;
    }

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    public int getFrompolicyyear() {
        return frompolicyyear;
    }

    public void setFrompolicyyear(int frompolicyyear) {
        this.frompolicyyear = frompolicyyear;
    }

    public int getTopolicyyear() {
        return topolicyyear;
    }

    public void setTopolicyyear(int topolicyyear) {
        this.topolicyyear = topolicyyear;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
