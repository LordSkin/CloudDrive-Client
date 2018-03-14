package kramnik.bartlomiej.clouddriveclient.Root.Dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import kramnik.bartlomiej.clouddriveclient.Presenter.AppPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.ListAdapterDataSource;
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
    public ListAdapterDataSource getAdapterSource() {
        return presenter;
    }

    @Provides
    public SelectDrivePresenter getSelectDrivePresenter(){
        return presenter;
    }
}
