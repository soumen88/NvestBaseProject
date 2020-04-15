package nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

@Dao
public interface KeyValueStoreRoomDao {
    @Query("SELECT COUNT(*) FROM " + NvestLibraryConfig.KEY_VALUE_STORE_TABLE)
    int getKeyValueStoreCount();

    @Insert
    void insertIntoKeyValueStore(List<KeyValueStoreRoom> keyValueStoreRoomList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertKeyValueVariable(final KeyValueStoreRoom keyValueStoreRoom);

    @Query("SELECT * FROM " + NvestLibraryConfig.KEY_VALUE_STORE_TABLE + " WHERE keyName = :keyName")
    Single<KeyValueStoreRoom> getValueByKey(String keyName);

    @Query("SELECT * FROM " + NvestLibraryConfig.KEY_VALUE_STORE_TABLE + " WHERE keyName = :keyName")
    Maybe<List<KeyValueStoreRoom>> getValueByKeyNew(String keyName);
}
