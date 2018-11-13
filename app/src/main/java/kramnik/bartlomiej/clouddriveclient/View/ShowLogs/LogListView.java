package kramnik.bartlomiej.clouddriveclient.View.ShowLogs;

public interface LogListView {
    void reloadList();

    void showError(int resId);

    void showLoading();

    void hideLoading();
}
