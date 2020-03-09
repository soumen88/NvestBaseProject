package nvest.com.nvestlibrary.databaseFiles.dao.svtable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.SV_TABLE)
public class SVRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "GSVID")
    private int gsvId;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name = "FROMYEAR")
    private int fromYear;

    @ColumnInfo(name = "TOYEAR")
    private int toYear;

    @ColumnInfo(name = "AGE")
    private int age;

    @ColumnInfo(name = "PT")
    private int PT;

    @ColumnInfo(name = "PPT")
    private int PPT;

    @ColumnInfo(name = "OPTIONID")
    private int optionId;

    @ColumnInfo(name = "REFYEAR")
    int refYear;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGsvId() {
        return gsvId;
    }

    public void setGsvId(int gsvId) {
        this.gsvId = gsvId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public int getRefYear() {
        return refYear;
    }

    public void setRefYear(int refYear) {
        this.refYear = refYear;
    }
}
