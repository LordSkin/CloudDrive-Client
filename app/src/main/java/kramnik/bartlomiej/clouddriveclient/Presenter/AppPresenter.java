package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.content.Context;

import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersDao;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * App presenter implementation
 */

public class AppPresenter implements ListAdapterDataSource, SelectDrivePresenter {
    @Override
    public ServerEntity getServer(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void setSelectDriveView(SelectDriveView view) {

    }
}
