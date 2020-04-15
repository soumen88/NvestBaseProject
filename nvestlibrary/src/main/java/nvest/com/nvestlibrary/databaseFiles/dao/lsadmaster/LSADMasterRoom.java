package nvest.com.nvestlibrary.databaseFiles.dao.lsadmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;


@Entity(tableName = NvestLibraryConfig.LSAD_MASTER_TABLE)
public class LSADMasterRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "LSADId")
    private int lsadId;


    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name =  "PT")
    private int PT;

    @ColumnInfo(name =  "PPT")
    private int PPT;

    @ColumnInfo(name =  "OPTIONID")
    private int optionId;

    @ColumnInfo(name =  "FROMSA")
    private Double fromSa;

    @ColumnInfo(name =  "TOSA")
    private Double toSa;

    @ColumnInfo(name =  "SAINTERVAL")
    private Double saInterval;

    @ColumnInfo(name =  "DISCOUNTRATE")
    private Double discountRate;

    @ColumnInfo(name =  "FIELD1")
    private Double field1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLsadId() {
        return lsadId;
    }

    public void setLsadId(int lsadId) {
        this.lsadId = lsadId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPT() {
        return PT;
    }

    public void setPT(int PT) {
        this.PT = PT;
    }

    public int getPPT() {
        return PPT;
    }

    public void setPPT(int PPT) {
        this.PPT = PPT;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public Double getFromSa() {
        return fromSa;
    }

    public void setFromSa(Double fromSa) {
        this.fromSa = fromSa;
    }

    public Double getToSa() {
        return toSa;
    }

    public void setToSa(Double toSa) {
        this.toSa = toSa;
    }

    public Double getSaInterval() {
        return saInterval;
    }

    public void setSaInterval(Double saInterval) {
        this.saInterval = saInterval;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getField1() {
        return field1;
    }

    public void setField1(Double field1) {
        this.field1 = field1;
    }
}
