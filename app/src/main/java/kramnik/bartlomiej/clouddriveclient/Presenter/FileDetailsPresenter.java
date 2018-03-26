package kramnik.bartlomiej.clouddriveclient.Presenter;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;

/**
 * Presenter for file details dialog
 */

public interface FileDetailsPresenter {

    void deleteFile(int pos);

    void renameFile(int pos, String newName);

    FileDetails getFileDetails(int pos);

    String getFileAddress(int pos);

}
