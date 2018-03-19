package kramnik.bartlomiej.clouddriveclient.Presenter;

import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;

/**
 * data source for FilesListAdapter
 */

public interface FilesListAdapterDataSource {

    FileDetails getItem(int i);

    List<FileDetails> getAll();

}
