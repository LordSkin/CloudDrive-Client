package kramnik.bartlomiej.clouddriveclient.Presenter;

import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListView;

/**
 * Presenter for files list activity
 */

public interface FilesListPresenter {

    void setFilesListView(FilesListView view);

    void getFilesList();
}
