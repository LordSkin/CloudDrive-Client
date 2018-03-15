package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * App presenter implementation
 */

public class AppPresenter implements ListAdapterDataSource, SelectDrivePresenter, AddServerPresenter {

    @Inject
    Context context;

    @Inject
    ServersList serversList;

    private ServerConnector connector;

    private SelectDriveView selectDriveView;

    private PublishSubject<ServerEntity> addServerObservable;

    private PublishSubject<ServerEntity> pingServersObservable;

    public AppPresenter() {

        pingServersObservable = PublishSubject.create();
        pingServersObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<ServerEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerEntity serverEntity) {
                        ServerConnector serverConnector = new ServerConnectorImpl(context, serverEntity.getIp());
                        serverEntity.setAvalible(serverConnector.ping());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        addServerObservable = PublishSubject.create();

        addServerObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<ServerEntity>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerEntity serverEntity) {
                        serversList.addServer(serverEntity);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public ServerEntity getServer(int i) {
        return serversList.getServer(i);
    }

    @Override
    public int getCount() {
        return serversList.getCount();
    }

    @Override
    public void setSelectDriveView(SelectDriveView view) {
        this.selectDriveView = view;
        refreshStatus();
    }

    @Override
    public void addServer(ServerEntity serverEntity) {
        addServerObservable.onNext(serverEntity);
        refreshStatus();
    }

    private void refreshStatus(){
        selectDriveView.showLoading();

        for(ServerEntity serverEntity : serversList.getServers()){
            pingServersObservable.onNext(serverEntity);
        }

        selectDriveView.updateList();

        selectDriveView.hideLoading();
    }
}
