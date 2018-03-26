package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import android.accounts.NetworkErrorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

/**
 * Providing basic CRUD operations on files on server
 */

public interface ServerConnector {

    void getFile(String url, String name, ProgressIndicator indicator) throws  IOException;

    File getFile(String url, String name) throws  IOException;

    List<FileDetails> getList(String url, JsonConverter converter) throws IOException;

    boolean delete(String url) throws IOException;

    boolean rename(String url, String newName) throws IOException;

    boolean addFile(File file, String url) throws IOException;

    boolean addFolder(String url) throws IOException;

    boolean ping();

    String getBaseAddress();
}
