package kramnik.bartlomiej.clouddriveclient.Model.ServerConnect;

import android.accounts.AuthenticatorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Model.Exceptions.WrongPathException;
import kramnik.bartlomiej.clouddriveclient.Model.JsonConverter;

/**
 * adapter for server connector, to make it easier navigating in files
 */

public class ServerConnectorAdapter {

    public static char fileSeparator = '&';
    public static char space = '%';

    private ServerConnector serverConnector;

    private String path;


    private JsonConverter converter;

    public ServerConnectorAdapter(ServerConnector serverConnector, JsonConverter converter) {
        this.serverConnector = serverConnector;
        this.path = "";
        this.converter = converter;
    }

    public boolean setPassword(String userName, String password) throws IOException {
        int code =  serverConnector.setPassword(userName, password);
        return code==200||code==201;
    }

    public List<FileDetails> getList() throws IOException {
        List<FileDetails> list = serverConnector.getList(path, converter);
        return serverConnector.getList(path, converter);
    }

    public boolean delete(String name) throws IOException, WrongPathException {
        if (name == null) throw new NullPointerException();
        int code =  serverConnector.delete(path + name);

        if (code==404) throw new WrongPathException();
        return code==200||code==201;
    }

    public boolean rename(String name, String newName) throws IOException, WrongPathException {
        int code =  serverConnector.rename(path +fileSeparator+ name, newName);
        if (code==404) throw new WrongPathException();
        return code==200||code==201;

    }


    public boolean addFile(File file, String name) throws IOException, WrongPathException {
        if (name == null) throw new NullPointerException();
        int code =  serverConnector.addFile(file, path + fileSeparator + name);
        if (code==404) throw new WrongPathException();
        return code==200||code==201;
    }

    public boolean addFolder(String name) throws IOException, WrongPathException {
        if (name == null) throw new NullPointerException();
        int code =  serverConnector.addFolder(path + fileSeparator + name);
        if (code==404) throw new WrongPathException();
        return code==200||code==201;
    }

    public void goUp() {
        int ind = path.lastIndexOf(fileSeparator);
        if (ind >= 0) path = path.substring(0, ind);
        int x =0;
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

    public void goTobaseDir() {
        path = "";
    }

    public String getFileAddress(String name) throws IOException {
        String token = serverConnector.getFileToken(path+fileSeparator+name);
        return (serverConnector.getBaseAddress()+"/get/"+path +fileSeparator + name+"/"+token).replace("\\\\","//");
    }
}
