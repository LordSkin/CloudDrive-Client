package kramnik.bartlomiej.clouddriveclient.Presenter;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.Event;

public interface LogsAdapterPresenter {

    Event getLogItem(int i);

    int getItemsCount();



}
