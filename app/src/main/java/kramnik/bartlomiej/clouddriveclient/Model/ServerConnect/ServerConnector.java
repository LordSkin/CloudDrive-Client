package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;

/**
 * Providing basic CRUD operations on files on server
 */

public interface ServerConnector {



    List<FileDetails> getList(String url, JsonConverter converter) throws IOException;

    boolean setPassword(String userName, String password);

    boolean delete(String url) throws IOException;

    boolean rename(String url, String newName) throws IOException;

    boolean addFile(File file, String url) throws IOException;

    boolean addFolder(String url) throws IOException;

    boolean ping();

    String getBaseAddress();
}
