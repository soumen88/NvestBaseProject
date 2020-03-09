package nvest.com.nvestlibrary.databaseFiles.dao;

import android.arch.persistence.room.Room;
import android.content.Context;


import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;

public class RoomDatabaseSingleton {
    //private static final String DB_NAME = NvestLibraryConfig.DB_NAME_WITH_FORMULAS_SQLITE;
    private static final String DB_NAME = NvestLibraryConfig.DB_NAME_GREENDAO;
    private Context context;
    private static RoomDatabaseSingleton instance;
    private static RoomAppDatabase roomAppDatabase;
    private static String TAG  = RoomDatabaseSingleton.class.getSimpleName();

    private RoomDatabaseSingleton(Context context) {
        roomAppDatabase = Room.databaseBuilder(context, RoomAppDatabase.class, DB_NAME).build();
    }

    public static synchronized void initialize(Context context) {
        if (instance == null) {
            new RoomDatabaseSingleton(context);
        }
    }

    public static RoomAppDatabase getRoomDatabaseSession() {
        if (roomAppDatabase == null) {
            throw new RuntimeException("Dao session not initialized");
        }
        return roomAppDatabase;
    }
}
