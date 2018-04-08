package kramnik.bartlomiej.clouddriveclient.Presenter.Dagger;

import android.app.DownloadManager;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.esafirm.rxdownloader.RxDownloader;

import dagger.Module;
import dagger.Provides;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServerDataBase;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.Model.UriResolver;
import kramnik.bartlomiej.clouddriveclient.Presenter.AddServerPresenter;

/**
 * Dagger module for presenter
 */

@Module
public class PresenterModule {

    private ServersList serversList;

    private Context context;

    private UriResolver resolver;

    private DownloadManager downloadManager;

    private RxDownloader downloader;


    public PresenterModule(Context context) {
        this.context = context;
        resolver = new UriResolver(context);
        serversList = new ServersList(context);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloader = new RxDownloader(context);
    }

    @Provides
    public ServersList getServersList() {
        return serversList;
    }

    @Provides
    public Context getContext() {
        return context;
    }

    @Provides
    public UriResolver getResolver() {
        return resolver;
    }

    @Provides
    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    @Provides
    public RxDownloader getDownloader() {
        return downloader;
    }
}
