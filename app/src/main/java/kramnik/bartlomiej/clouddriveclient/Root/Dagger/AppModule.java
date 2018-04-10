package kramnik.bartlomiej.clouddriveclient.Root.Dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import kramnik.bartlomiej.clouddriveclient.Presenter.AddServerPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.AppPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.DrivesListAdapterDataSource;
import kramnik.bartlomiej.clouddriveclient.Presenter.EnterPasswordPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.FileDetailsPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.FilesListAdapterDataSource;
import kramnik.bartlomiej.clouddriveclient.Presenter.FilesListPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.SelectDrivePresenter;

/**
 * Dagger module
 */

@Module
public class AppModule {

    private AppPresenter presenter;

    private Context context;

    public AppModule(AppPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Provides
    public Context getContext() {
        return context;
    }

    @Provides
    public DrivesListAdapterDataSource getAdapterSource() {
        return presenter;
    }

    @Provides
    public SelectDrivePresenter getSelectDrivePresenter(){
        return presenter;
    }

    @Provides
    public AddServerPresenter getAddServerPresenter(){
        return presenter;
    }

    @Provides
    public FilesListAdapterDataSource getFilesListAdapterDataSource(){
        return presenter;
    }

    @Provides
    public FilesListPresenter getFilesPresenter(){
        return presenter;
    }

    @Provides
    public FileDetailsPresenter provideFileDetailsPresenter(){
        return presenter;
    }

    @Provides
    public EnterPasswordPresenter provideEnterPasswordPresenter(){
        return presenter;
    }
}
