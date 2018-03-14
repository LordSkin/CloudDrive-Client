package kramnik.bartlomiej.clouddriveclient.Root.Dagger;

import dagger.Component;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.DrivesListAdapter;
import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveActivity;

/**
 * Created by Mao on 14.03.2018.
 */

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(DrivesListAdapter adapter);

    void inject(SelectDriveActivity selectDriveActivity);
}
