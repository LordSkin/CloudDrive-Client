package kramnik.bartlomiej.clouddriveclient.Presenter;

import kramnik.bartlomiej.clouddriveclient.View.ShowLogs.LogListView;

public interface LogsActivityPresenter {
    void setLogsView(LogListView view);

    void updateLogsList();
}
