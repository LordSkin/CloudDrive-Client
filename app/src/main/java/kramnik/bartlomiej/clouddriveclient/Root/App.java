package kramnik.bartlomiej.clouddriveclient.Root;

import android.app.Application;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kramnik.bartlomiej.clouddriveclient.Presenter.AppPresenter;
import kramnik.bartlomiej.clouddriveclient.Presenter.Dagger.DaggerPresenterComponent;
import kramnik.bartlomiej.clouddriveclient.Presenter.Dagger.PresenterComponent;
import kramnik.bartlomiej.clouddriveclient.Presenter.Dagger.PresenterModule;
import kramnik.bartlomiej.clouddriveclient.Root.Dagger.AppComponent;
import kramnik.bartlomiej.clouddriveclient.Root.Dagger.AppModule;
import kramnik.bartlomiej.clouddriveclient.Root.Dagger.DaggerAppComponent;

/**
 * Custom Application implementation
 */

public class App extends Application {

    private AppPresenter presenter;

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        presenter = new AppPresenter();
        final App app = this;

        Observable<PresenterComponent> observable = new Observable<PresenterComponent>() {
            @Override
            protected void subscribeActual(Observer<? super PresenterComponent> observer) {
                observer.onNext(DaggerPresenterComponent.builder().presenterModule(new PresenterModule(app)).build());
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<PresenterComponent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PresenterComponent presenterComponent) {
                        presenterComponent.inject(presenter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(presenter, this)).build();

    }
}
