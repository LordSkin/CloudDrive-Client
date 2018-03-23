package kramnik.bartlomiej.clouddriveclient.Presenter;

import android.net.Uri;

import java.io.File;

import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListView;

/**
 * Presenter for files list activity
 */

public interface FilesListPresenter {

    void setFilesListView(FilesListView view);

    void getFilesList();

    void addFile(Uri file);

    void goBack();
}
