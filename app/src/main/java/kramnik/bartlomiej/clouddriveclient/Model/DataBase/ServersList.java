package kramnik.bartlomiej.clouddriveclient.Model.DataBase;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;

/**
 * reading data from dataBase
 */

public class ServersList {

    private ServerDataBase dataBase;

    private List<ServerEntity> servers;

    public ServersList(Context context) {
        dataBase = Room.databaseBuilder(context, ServerDataBase.class, "testDataBase1").build();
        servers = dataBase.serversDao().getAll();
    }

    public ServersList(ServerDataBase dataBase) {
        this.dataBase = dataBase;
        servers = dataBase.serversDao().getAll();
    }

    public List<ServerEntity> getServers(){
        return servers;
    }

    public int getCount(){
        return servers.size();
    }

    public ServerEntity getServer(int i){
        return servers.get(i);
    }

    public void deleteServer(int pos){
        ServerEntity serverEntity = servers.get(pos);
        servers.remove(pos);
        dataBase.serversDao().deleteServer(serverEntity);
    }

    public void deleteServer(ServerEntity serverEntity){
        if (serverEntity==null) throw new NullPointerException();
        servers.remove(serverEntity);
        dataBase.serversDao().deleteServer(serverEntity);
    }

    public void addServer(ServerEntity serverEntity){
        if (serverEntity==null) throw new NullPointerException();
        servers.add(serverEntity);
        dataBase.serversDao().addServer(serverEntity);
    }
}
