package kramnik.bartlomiej.clouddriveclient.Model.DataModels;

import kramnik.bartlomiej.clouddriveclient.Model.ServerConnect.ServerConnectorAdapter;

/**
 * Data model for file details
 */

public class FileDetails {
    FileType fileType;
    String name;
    String path;
    String displayName;

    public FileDetails(FileType fileType, String name, String path) {
        this.fileType = fileType;
        this.name = name;
        this.path = path;
        this.displayName = name.replace(ServerConnectorAdapter.space, ' ');
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        if (displayName==null) this.displayName = name.replace(ServerConnectorAdapter.space, ' ');
        return displayName;
    }
}
