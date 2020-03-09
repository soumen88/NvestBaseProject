package nvest.com.nvestlibrary.databaseFiles.dao.keyvaluestoretable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
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
