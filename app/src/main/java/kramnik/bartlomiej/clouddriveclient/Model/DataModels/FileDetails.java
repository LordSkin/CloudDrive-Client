package kramnik.bartlomiej.clouddriveclient.Model.DataModels;

/**
 * Data model for file details
 */

public class FileDetails {
    FileType fileType;
    String name;
    String path;

    public FileDetails(FileType fileType, String name, String path) {
        this.fileType = fileType;
        this.name = name;
        this.path = path;
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
}
