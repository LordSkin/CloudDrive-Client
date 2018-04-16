package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.accounts.AuthenticatorException;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.esafirm.rxdownloader.RxDownloader;
import com.google.gson.Gson;

import java.io.File;
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
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileType;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Model.Exceptions.WrongPathException;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnector;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorAdapter;
import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorImpl;
import kramnik.bartlomiej.clouddriveclient.Model.UriResolver;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListView;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * App presenter implementation
 */

public class AppPresenter implements DrivesListAdapterDataSource, SelectDrivePresenter, AddServerPresenter, FilesListPresenter, FilesListAdapterDataSource, FileDetailsPresenter, EnterPasswordPresenter, FilterDialogPresenter, CreateFolderDialogPresenter {

    @Inject
    Context context;

    @Inject
    ServersList serversList;

    @Inject
    UriResolver resolver;

    @Inject
    DownloadManager downloadManager;

    @Inject
    RxDownloader downloader;

    private ServerConnectorAdapter serverConnectorAdapter;

    private SelectDriveView selectDriveView;

    private FilesListView filesListView;

    private PublishSubject<ServerEntity> addServerObservable;

    private PublishSubject<Integer> deleteServerObservable;

    private PublishSubject<String> pingServersObservable;

    private PublishSubject<String> filesListObservable;

    private PublishSubject<Integer> selectFolderObservable;

    private PublishSubject<Uri> addFileObservable;

    private PublishSubject<String> deleteFileObservable;

    private PublishSubject<String[]> renameObservable;

    private PublishSubject<String[]> setPasswordObservable;

    private PublishSubject<Integer[]> addressFileObbservable;

    private PublishSubject<String> createFolderObservable;

    private List<FileDetails> actualFiles, allFiles;

    private String selectedDriveName;


    public AppPresenter() {

        selectedDriveName = "";
        actualFiles = new ArrayList<FileDetails>();
        allFiles = new ArrayList<FileDetails>();
        pingServersObservable = PublishSubject.create();
        pingServersObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String v) {

                        selectDriveView.showLoading();

                        for (ServerEntity serverEntity2 : serversList.getServers()) {
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
                .subscribe(new Observer<ServerEntity>() {

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

        deleteServerObservable = PublishSubject.create();
        deleteServerObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        serversList.deleteServer(integer);
                        selectDriveView.updateList();
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
                            allFiles.clear();
                            allFiles.addAll(actualFiles);
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
                        catch (OutOfMemoryError e) {
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

        selectFolderObservable = PublishSubject.create();
        selectFolderObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            filesListView.showLoading();
                            serverConnectorAdapter.goTo(actualFiles.get(integer).getName());
                            actualFiles = serverConnectorAdapter.getList();
                            allFiles.clear();
                            allFiles.addAll(actualFiles);
                            filesListView.refreshView();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        catch (Error e) {
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
                            allFiles.clear();
                            allFiles.addAll(actualFiles);
                        }
                        catch (IOException e) {
                            filesListView.showError(R.string.connectionError);
                            e.printStackTrace();
                        }
                        catch (WrongPathException e) {
                            filesListView.showError(R.string.fileError);
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
                            allFiles.clear();
                            allFiles.addAll(actualFiles);
                        }
                        catch (IOException e) {
                            filesListView.showError(R.string.connectionError);
                            e.printStackTrace();
                        }
                        catch (WrongPathException e) {
                            filesListView.showError(R.string.fileError);
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

        setPasswordObservable = PublishSubject.create();
        setPasswordObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        selectDriveView.showLoading();
                        try {
                            if (serverConnectorAdapter.setPassword(strings[0], strings[1])) {
                                selectDriveView.hideLoading();
                                selectDriveView.gotoFileView();
                            }
                            else {
                                selectDriveView.showPasswordDialog();
                            }
                        }
                        catch (IOException e) {
                            selectDriveView.showError(R.string.connectionError);
                            e.printStackTrace();
                        }
                        selectDriveView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        addressFileObbservable = PublishSubject.create();
        addressFileObbservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer[] s) {
                        if (s[0] == 0) {
                            filesListView.showLoading();
                            try {
                                String address = serverConnectorAdapter.getFileAddress(actualFiles.get(s[1]).getName());
                                filesListView.shareFile(address);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            finally {
                                filesListView.hideLoading();
                            }
                        }
                        else {
                            try {
                                String url = serverConnectorAdapter.getFileAddress(actualFiles.get(s[1]).getName());
                                downloader.download(url, actualFiles.get(s[1]).getName(), false)
                                        .subscribe(new Observer<String>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(String s) {
                                                File result = new File(s.replace("file://", ""));
                                                filesListView.operFile(result);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        createFolderObservable = PublishSubject.create();
        createFolderObservable.observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            filesListView.showLoading();
                            serverConnectorAdapter.addFolder(s);
                            getFilesList();
                        }
                        catch (IOException e) {
                            filesListView.showError(R.string.connectionError);
                            e.printStackTrace();
                        }
                        catch (WrongPathException e) {
                            filesListView.showError(R.string.fileError);
                            e.printStackTrace();
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
        selectedDriveName = serversList.getServer(pos).getName();
    }

    @Override
    public void itemDeleted(int pos) {
        deleteServerObservable.onNext(pos);
    }

    @Override
    public void addServer(ServerEntity serverEntity) {
        if (serverEntity.getName().trim().length()<1||serverEntity.getIp().trim().length()<1){
            selectDriveView.showError(R.string.emptyName);
            return;
        }
        addServerObservable.onNext(serverEntity);
        refreshStatus();
    }

    private void refreshStatus() {
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

        try {
            if (actualFiles.get(pos).getFileType() == FileType.Folder) {
                serverConnectorAdapter.goTo(actualFiles.get(pos).getName());
                actualFiles = serverConnectorAdapter.getList();
                allFiles.clear();
                allFiles.addAll(actualFiles);
                filesListView.refreshView();
            }
            else {
                Integer[] temp = {1, pos};
                addressFileObbservable.onNext(temp);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        catch (Error e) {
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
    public String getDriveName() {
        return selectedDriveName;
    }

    @Override
    public void deleteFile(int pos) {
        deleteFileObservable.onNext(actualFiles.get(pos).getName());
    }

    @Override
    public void renameFile(int pos, String newName) {
        if (newName.trim().length()<1){
            filesListView.showError(R.string.emptyName);
            return;
        }

        String[] strings = {actualFiles.get(pos).getName(), newName};
        renameObservable.onNext(strings);
    }

    @Override
    public FileDetails getFileDetails(int pos) {
        return actualFiles.get(pos);
    }

    @Override
    public void getFileAddress(int pos) {
        Integer[] temp = {0, pos};
        addressFileObbservable.onNext(temp);
    }


    @Override
    public void setPassword(String userName, String password) {
        String[] temp = {userName, password};
        setPasswordObservable.onNext(temp);
    }

    @Override
    public void filterAudio() {
        actualFiles.clear();
        for (FileDetails f : allFiles) {
            if (f.getFileType() == FileType.Audio) {
                actualFiles.add(f);
            }
        }
        filesListView.refreshView();
    }

    @Override
    public void filterImages() {
        actualFiles.clear();
        for (FileDetails f : allFiles) {
            if (f.getFileType() == FileType.Image) {
                actualFiles.add(f);
            }
        }
        filesListView.refreshView();
    }

    @Override
    public void filterDocs() {
        actualFiles.clear();
        for (FileDetails f : allFiles) {
            if (f.getFileType() == FileType.TextFile) {
                actualFiles.add(f);
            }
        }
        filesListView.refreshView();
    }

    @Override
    public void filterExec() {
        actualFiles.clear();
        for (FileDetails f : allFiles) {
            if (f.getFileType() == FileType.Program) {
                actualFiles.add(f);
            }
        }
        filesListView.refreshView();
    }

    @Override
    public void filterOthers() {
        actualFiles.clear();
        for (FileDetails f : allFiles) {
            if (f.getFileType() == FileType.Other) {
                actualFiles.add(f);
            }
        }
        filesListView.refreshView();
    }

    @Override
    public void clearFilters() {
        actualFiles.clear();
        actualFiles.addAll(allFiles);
        filesListView.refreshView();
    }

    @Override
    public void createFolder(String name) {
        if(name.trim().length()<1){
            filesListView.showError(R.string.emptyName);
            return;
        }
        createFolderObservable.onNext(name);
    }
}
