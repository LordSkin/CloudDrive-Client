package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

/**
 * Interface for file list view
 */

public interface FilesListView {

    void showLoading();

    void hideLoading();

    void refreshView();

    ProgressIndicator getProgressIndocator();

}
