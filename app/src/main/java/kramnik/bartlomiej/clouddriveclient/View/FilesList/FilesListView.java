package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import java.io.File;

/**
 * Interface for file list view
 */

public interface FilesListView {

    void showLoading();

    void operFile(File file);

    void hideLoading();

    void refreshView();

    void shareFile(String address);

    void showError(int id);

}
