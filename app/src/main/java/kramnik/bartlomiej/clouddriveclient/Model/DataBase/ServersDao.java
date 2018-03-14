package kramnik.bartlomiej.clouddriveclient.Model.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;

/**
 * Dao required by room library
 */

@Dao
public interface ServersDao {

    @Query("SELECT * FROM ServerEntity")
    List<ServerEntity> getAll();

    @Insert
    void addServer(ServerEntity server);

    @Delete
    void deleteServer(ServerEntity server);
}
