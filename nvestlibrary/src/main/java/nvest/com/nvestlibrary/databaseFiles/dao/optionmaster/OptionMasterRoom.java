package nvest.com.nvestlibrary.databaseFiles.dao.optionmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Entity(tableName = NvestLibraryConfig.OPTION_MASTER_TABLE)
public class OptionMasterRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name = "ProductId")
    private int productid;

    @ColumnInfo(name = "OptionId")
    private int optionId;

    @ColumnInfo(name = "OptionLevelId")
    private  int optionLevelId ;

    @ColumnInfo(name = "OptionName")
    private  String optionName ;

    @ColumnInfo(name = "OptionParent")
    private  String optionParent ;

    @ColumnInfo(name = "InputFieldName")
    private  String inputFieldName ;

    @ColumnInfo(name = "InputFieldType")
    private  String inputFieldType ;

    @ColumnInfo(name = "IsDefault")
    private  boolean isDefault ;

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

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getOptionLevelId() {
        return optionLevelId;
    }

    public void setOptionLevelId(int optionLevelId) {
        this.optionLevelId = optionLevelId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionParent() {
        return optionParent;
    }

    public void setOptionParent(String optionParent) {
        this.optionParent = optionParent;
    }

    public String getInputFieldName() {
        return inputFieldName;
    }

    public void setInputFieldName(String inputFieldName) {
        this.inputFieldName = inputFieldName;
    }

    public String getInputFieldType() {
        return inputFieldType;
    }

    public void setInputFieldType(String inputFieldType) {
        this.inputFieldType = inputFieldType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
