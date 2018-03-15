package kramnik.bartlomiej.clouddriveclient.Presenter.Dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServerDataBase;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.Presenter.AddServerPresenter;

/**
 * Dagger module for presenter
 */

@Module
public class PresenterModule {

    private ServersList serversList;

    private Context context;


    public PresenterModule(Context context) {
        this.context = context;
        serversList = new ServersList(context);
    }

    @Provides
    public ServersList getServersList() {
        return serversList;
    }

    @Provides
    public Context getContext() {
        return context;
    }

}
