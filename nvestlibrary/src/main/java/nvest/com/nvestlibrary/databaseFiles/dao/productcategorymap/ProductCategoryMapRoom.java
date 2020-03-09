package nvest.com.nvestlibrary.databaseFiles.dao.productcategorymap;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.PRODUCT_CATEGORY_MAP_TABLE)
public class ProductCategoryMapRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "PRODUCTCATERYMAPID")
    private int productCateryMapId;

    @ColumnInfo(name = "PRODUCTID")
    private int productId;


    @ColumnInfo(name = "OPTIONID")
    private int optionId;

    @ColumnInfo(name = "TYPEID")
    private int typeId;

    @ColumnInfo(name = "TAXGROUP")
    private int taxGroup;

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

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTaxGroup() {
        return taxGroup;
    }

    public void setTaxGroup(int taxGroup) {
        this.taxGroup = taxGroup;
    }

    public int getProductCateryMapId() {
        return productCateryMapId;
    }

    public void setProductCateryMapId(int productCateryMapId) {
        this.productCateryMapId = productCateryMapId;
    }
}
