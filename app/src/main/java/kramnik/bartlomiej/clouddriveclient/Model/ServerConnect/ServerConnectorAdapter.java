package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

/**
 * adapter for server connector, to make it easier navigating in files
 */

public class ServerConnectorAdapter {

    public static char fileSeparator = '%';

    private ServerConnector serverConnector;

    private String path;

    private JsonConverter converter;

    public ServerConnectorAdapter(ServerConnector serverConnector, JsonConverter converter) {
        this.serverConnector = serverConnector;
        this.path = "";
        this.converter = converter;
    }

    public void getFile(String name, ProgressIndicator indicator) throws IOException {
        serverConnector.getFile(path + name, name, indicator);
    }

    public File getFile(String name) throws IOException {
        if (name == null) throw new NullPointerException();
        return serverConnector.getFile(path + name, name);
    }

    public List<FileDetails> getList() throws IOException {
        return serverConnector.getList(path, converter);
    }

    public boolean delete(String name) throws IOException {
        if (name == null) throw new NullPointerException();
        return serverConnector.delete(path + name);
    }

    public boolean rename(String name, String newName) throws IOException {
        return serverConnector.rename(path + name, newName);
    }

    public boolean addFile(File file, String name) throws IOException {
        if (name == null) throw new NullPointerException();
        return serverConnector.addFile(file, path + name);
    }

    public boolean addFolder(String name) throws IOException {
        if (name == null) throw new NullPointerException();
        return serverConnector.addFolder(path + name);
    }

    public void goUp() {
        int ind = path.lastIndexOf(fileSeparator);
        if (ind >= 0) path = path.substring(0, ind);
    }

    public boolean isBaseFolder() {
        return path.equals("");
    }

    public List<FileDetails> goTo(String name) throws IOException {
        if (name == null) throw new NullPointerException();
        List<FileDetails> result = serverConnector.getList(path + fileSeparator + name, converter);
        path += fileSeparator + name;
        return result;
    }

    public List<FileDetails> goTobaseDir() throws IOException {
        path = "";
        return getList();
    }
}
