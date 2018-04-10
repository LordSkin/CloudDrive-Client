package kramnik.bartlomiej.clouddriveclient.Root.Dagger;

import dagger.Component;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.AddServerDialog;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.EntryPasswordDialog;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.FileOptionsDialog;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListActivity;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListAdapter;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.DrivesListAdapter;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveActivity;

/**
 * Dagger component
 */

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(DrivesListAdapter adapter);

    void inject(SelectDriveActivity selectDriveActivity);

    void inject(AddServerDialog dialog);

    void inject(FilesListAdapter adapter);

    void inject(FilesListActivity filesListActivity);

    void inject(FileOptionsDialog dialog);

    void inject(EntryPasswordDialog dialog);
}
