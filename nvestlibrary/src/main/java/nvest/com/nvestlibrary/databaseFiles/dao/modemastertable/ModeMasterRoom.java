package nvest.com.nvestlibrary.databaseFiles.dao.modemastertable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;


@Entity(tableName = NvestLibraryConfig.MODE_MASTER_TABLE)
public class ModeMasterRoom {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    private Long id;

    @ColumnInfo(name = "MODEID")
    private int modeId;

    @ColumnInfo(name = "MODENAME")
    private String modeName;

    @ColumnInfo(name = "FREQUENCY")
    private int frequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
