package nvest.com.nvestlibrary.databaseFiles.dao.fundstrategytable;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.FUND_STRATEGY_TABLE)
public class FundStrategyMasterRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;

    @ColumnInfo(name = "STRATEGYID")
    private int strategyId;

    @ColumnInfo(name = "STRATEGYNAME")
    private String strategyName;

    @ColumnInfo(name = "PARENTID")
    private int parentId;

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

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
