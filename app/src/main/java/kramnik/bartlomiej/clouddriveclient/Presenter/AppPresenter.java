package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kramnik.bartlomiej.clouddriveclient.Model.DataBase.ServersList;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileType;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorAdapter;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.Model.UriResolver;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.FileOptionsDialog;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListView;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * App presenter implementation
 */

public class AppPresenter implements DrivesListAdapterDataSource, SelectDrivePresenter, AddServerPresenter, FilesListPresenter, FilesListAdapterDataSource, FileDetailsPresenter {

    @Inject
    Context context;

    @Inject
    ServersList serversList;

    @Inject
    UriResolver resolver;

    @Inject
    DownloadManager downloadManager;

    private ServerConnectorAdapter serverConnectorAdapter;

    private SelectDriveView selectDriveView;

    private FilesListView filesListView;

    private PublishSubject<ServerEntity> addServerObservable;

    private PublishSubject<String> pingServersObservable;

    private PublishSubject<String> filesListObservable;

    private PublishSubject<Integer> fileSelectedObservable;

    private PublishSubject<Uri> addFileObservable;

    private PublishSubject<String> deleteFileObservable;

    private PublishSubject<String[]> renameObservable;

    private List<FileDetails> actualFiles;


    public AppPresenter() {

        actualFiles = new ArrayList<FileDetails>();
        pingServersObservable = PublishSubject.create();
        pingServersObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String v) {

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
                    public void onNext(String v) {
                        try {
                            filesListView.showLoading();
                            actualFiles = serverConnectorAdapter.getList();
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

        addFileObservable = PublishSubject.create();
        addFileObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Uri file) {
                        try {

                            filesListView.showLoading();
                            Cursor cursor = null;
                            String displayName = "name";
                                cursor = context.getContentResolver().query(file, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                }
                            serverConnectorAdapter.addFile(resolver.getFile(file), displayName);
                                filesListView.hideLoading();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        catch (OutOfMemoryError e){
                            //
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        fileSelectedObservable = PublishSubject.create();
        fileSelectedObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        try{
                            filesListView.showLoading();
                            if(actualFiles.get(integer).getFileType()== FileType.Folder){
                                serverConnectorAdapter.goTo(actualFiles.get(integer).getName());
                                actualFiles = serverConnectorAdapter.getList();
                                filesListView.refreshView();
                            }
                            else {
                                serverConnectorAdapter.getFile(actualFiles.get(integer).getName(), filesListView.getProgressIndocator());
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        catch (Error e)
                        {
                            e.printStackTrace();
                        }
                        finally {
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

        deleteFileObservable = PublishSubject.create();
        deleteFileObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        try {
                            filesListView.showLoading();
                            serverConnectorAdapter.delete(s);
                            actualFiles = serverConnectorAdapter.getList();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            filesListView.refreshView();
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

        renameObservable = PublishSubject.create();
        renameObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        try {
                            filesListView.showLoading();
                            serverConnectorAdapter.rename(strings[0], strings[1]);
                            actualFiles = serverConnectorAdapter.getList();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            filesListView.refreshView();
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
        serverConnectorAdapter = new ServerConnectorAdapter(new ServerConnectorImpl(context, serversList.getServer(pos).getIp()), new JsonConverter(new Gson()));
    }

    @Override
    public void addServer(ServerEntity serverEntity) {
        addServerObservable.onNext(serverEntity);
        refreshStatus();
    }

    private void refreshStatus(){
        pingServersObservable.onNext("");
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
    public void itemClicked(int pos) {
        //fileSelectedObservable.onNext(pos);

        try{
            //filesListView.showLoading();
            if(actualFiles.get(pos).getFileType()== FileType.Folder){
                serverConnectorAdapter.goTo(actualFiles.get(pos).getName());
                actualFiles = serverConnectorAdapter.getList();
                filesListView.refreshView();
            }
            else {
                //serverConnectorAdapter.getFile(actualFiles.get(pos).getName(), filesListView.getProgressIndocator());
                downloadFile(serverConnectorAdapter.getFileAddress(actualFiles.get(pos).getName()), actualFiles.get(pos).getName());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        catch (Error e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void getFilesList() {
        filesListObservable.onNext("");

    }

    @Override
    public void addFile(Uri file) {
        addFileObservable.onNext(file);
    }

    @Override
    public void goBack() {
        serverConnectorAdapter.goUp();
        filesListObservable.onNext("");
    }

    @Override
    public void deleteFile(int pos) {
        deleteFileObservable.onNext(actualFiles.get(pos).getName());
    }

    @Override
    public void renameFile(int pos, String newName) {
        String[] strings = {actualFiles.get(pos).getName(), newName};
        renameObservable.onNext(strings);
    }

    @Override
    public FileDetails getFileDetails(int pos) {
        return actualFiles.get(pos);
    }

    @Override
    public String getFileAddress(int pos) {
        return serverConnectorAdapter.getFileAddress(actualFiles.get(pos).getName());
    }

    private void downloadFile(String url, String name) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        request.setTitle("title");
        request.setDescription("desc");
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setVisibleInDownloadsUi(true);

        //Enqueue a new download and same the referenceId
        downloadManager.enqueue(request);
    }
}
