package nvest.com.nvestlibrary.databaseFiles.dao.bonusscrtable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.BONUS_SCR_TABLE)
public class BonusScrRoom {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name = "BONUSSCRID")
    private int bonusScrId;

    @ColumnInfo(name = "BONUSNAME")
    private String bonusName;

    @ColumnInfo(name = "BONUSVALUE")
    private Double bonusValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBonusScrId() {
        return bonusScrId;
    }

    public void setBonusScrId(int bonusScrId) {
        this.bonusScrId = bonusScrId;
    }

    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

    public Double getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(Double bonusValue) {
        this.bonusValue = bonusValue;
    }
}
