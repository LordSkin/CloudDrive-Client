package kramnik.bartlomiej.clouddriveclient.Root.Dagger;

import dagger.Component;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.AddServerDialog;
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
}
