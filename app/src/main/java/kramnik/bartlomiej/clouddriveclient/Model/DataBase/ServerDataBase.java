package kramnik.bartlomiej.clouddriveclient.Model.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;

/**
 * DataBase class for room
 */

@Database(entities = {ServerEntity.class}, version = 1)
public abstract class ServerDataBase extends RoomDatabase {
    public abstract ServersDao serversDao();
}
