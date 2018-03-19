package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListView;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * App presenter implementation
 */

public class AppPresenter implements DrivesListAdapterDataSource, SelectDrivePresenter, AddServerPresenter, FilesListPresenter, FilesListAdapterDataSource {

    @Inject
    Context context;

    @Inject
    ServersList serversList;

    private ServerConnector connector;

    private SelectDriveView selectDriveView;

    private FilesListView filesListView;

    private PublishSubject<ServerEntity> addServerObservable;

    private PublishSubject<Void> pingServersObservable;

    private PublishSubject<String> filesListObservable;

    private List<FileDetails> actualFiles;

    private String directoryPath;

    public AppPresenter() {

        directoryPath = "";
        actualFiles = new ArrayList<FileDetails>();
        pingServersObservable = PublishSubject.create();
        pingServersObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Void v) {

                        selectDriveView.showLoading();

                        for(ServerEntity serverEntity2 : serversList.getServers()){
                            ServerConnector serverConnector = new ServerConnectorImpl(context, serverEntity2.getIp());
                            serverEntity2.setAvalible(serverConnector.ping());
                        }

                        selectDriveView.updateList();

                        selectDriveView.hideLoading();


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

        filesListObservable = PublishSubject.create();

        filesListObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String path) {
                        try {
                            filesListView.showLoading();
                            actualFiles = connector.getList(path, new JsonConverter(new Gson()));
                            filesListView.hideLoading();
                            filesListView.refreshView();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            filesListView.hideLoading();
                        }
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
    public void itemSelected(int pos) {
        connector = new ServerConnectorImpl(context, serversList.getServer(pos).getIp());
        directoryPath = "";
    }

    @Override
    public void addServer(ServerEntity serverEntity) {
        addServerObservable.onNext(serverEntity);
        refreshStatus();
    }

    private void refreshStatus(){
        pingServersObservable.onNext(null);
    }

    @Override
    public FileDetails getItem(int i) {
        return actualFiles.get(i);
    }

    @Override
    public List<FileDetails> getAll() {
        return actualFiles;
    }

    @Override
    public void setFilesListView(FilesListView view) {
        this.filesListView = view;

    }

    @Override
    public void getFilesList() {
        filesListObservable.onNext(directoryPath);

    }
}
