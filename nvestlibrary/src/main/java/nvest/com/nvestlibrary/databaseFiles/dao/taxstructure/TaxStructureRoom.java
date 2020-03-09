package nvest.com.nvestlibrary.databaseFiles.dao.taxstructure;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;


@Entity(tableName = NvestLibraryConfig.TAX_STRUCTURE_TABLE)
public class TaxStructureRoom {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "TAXID")
    private int TaxID ;

    @ColumnInfo(name = "TAXGROUP")
    private int TaxGroup;

    @ColumnInfo(name = "TAXGROUPNAME")
    private String TaxGroupName;

    @ColumnInfo(name = "TAXKEYWORD")
    private String TaxKeyword;

    @ColumnInfo(name = "TAXNAME")
    private String TaxName;

    @ColumnInfo(name = "FROMYEAR")
    private int FromYear;

    @ColumnInfo(name = "TOYEAR")
    private int ToYear;

    @ColumnInfo(name = "FROMDATE")
    private String FromDate;

    @ColumnInfo(name = "TODATE")
    private String ToDate;

    @ColumnInfo(name = "RATE")
    private Double Rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTaxID() {
        return TaxID;
    }

    public void setTaxID(int taxID) {
        TaxID = taxID;
    }

    public int getTaxGroup() {
        return TaxGroup;
    }

    public void setTaxGroup(int taxGroup) {
        TaxGroup = taxGroup;
    }

    public String getTaxGroupName() {
        return TaxGroupName;
    }

    public void setTaxGroupName(String taxGroupName) {
        TaxGroupName = taxGroupName;
    }

    public String getTaxKeyword() {
        return TaxKeyword;
    }

    public void setTaxKeyword(String taxKeyword) {
        TaxKeyword = taxKeyword;
    }

    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        TaxName = taxName;
    }

    public int getFromYear() {
        return FromYear;
    }

    public void setFromYear(int fromYear) {
        FromYear = fromYear;
    }

    public int getToYear() {
        return ToYear;
    }

    public void setToYear(int toYear) {
        ToYear = toYear;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }
}
