package kramnik.bartlomiej.clouddriveclient.Presenter;

import kramnik.bartlomiej.clouddriveclient.View.SelectDrive.SelectDriveView;

/**
 * Presenter for selectDrive
 */

public interface SelectDrivePresenter {

    void setSelectDriveView(SelectDriveView view);

    void itemSelected(int pos);

    void itemDeleted(int pos);
}
