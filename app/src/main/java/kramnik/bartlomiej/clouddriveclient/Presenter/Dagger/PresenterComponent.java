package kramnik.bartlomiej.clouddriveclient.Presenter.Dagger;

import dagger.Component;
import kramnik.bartlomiej.clouddriveclient.Presenter.AppPresenter;

/**
 * Dagger component for presenter
 */

@Component(modules = PresenterModule.class)
public interface PresenterComponent {
    void inject(AppPresenter presenter);
}
