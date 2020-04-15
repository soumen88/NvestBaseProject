package nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;


/*@Entity(tableName = NvestLibraryConfig.KEY_VALUE_STORE_TABLE,
        indices = {@Index(value = {"keyName"},
        unique = true)})*/
@Entity(tableName = NvestLibraryConfig.KEY_VALUE_STORE_TABLE)
public class KeyValueStoreRoom {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "keyName")
    private String keyName;

    @ColumnInfo(name = "keyValue")
    private String keyValue;

    @ColumnInfo(name = "className")
    private String className;


    @ColumnInfo(name = "fieldName")
    private String fieldName;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

