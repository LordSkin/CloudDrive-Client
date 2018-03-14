package kramnik.bartlomiej.clouddriveclient.Presenter.Dagger;

import dagger.Component;
import kramnik.bartlomiej.clouddriveclient.Presenter.AppPresenter;

/**
 * Created by Mao on 14.03.2018.
 */

@Component(modules = PresenterModule.class)
public interface PresenterComponent {
    void inject(AppPresenter presenter);
}
