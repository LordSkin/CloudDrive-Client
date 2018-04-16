package kramnik.bartlomiej.clouddriveclient.View.SelectDrive;

/**
 * Interface for selectDriveView
 */

public interface SelectDriveView {

    void updateList();

    void showLoading();

    void hideLoading();

    void showPasswordDialog();

    void gotoFileView();

    void showError(int id);

}
