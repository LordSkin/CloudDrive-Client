package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.content.Context;

import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersDao;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;

/**
 * Created by Mao on 13.03.2018.
 */

public interface DrivesListAdapterDataSource {

    ServerEntity getServer(int i);

    int getCount();

}
