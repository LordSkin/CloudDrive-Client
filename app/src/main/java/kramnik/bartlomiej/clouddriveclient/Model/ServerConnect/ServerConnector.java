package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.Event;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;

/**
 * Providing basic CRUD operations on files on server
 */

public interface ServerConnector {



    List<FileDetails> getList(String url, JsonConverter converter) throws IOException;

    int setPassword(String userName, String password) throws IOException;

    int delete(String url) throws IOException;

    int rename(String url, String newName) throws IOException;

    int addFile(File file, String url) throws IOException;

    int addFolder(String url) throws IOException;

    boolean ping();

    String getBaseAddress();

    String getFileToken(String filePath) throws IOException;

    List<Event> getLogs(JsonConverter converter) throws IOException;
}
