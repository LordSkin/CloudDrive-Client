package kramnik.bartlomiej.clouddriveclient.Presenter;

/**
 * Presenter for filtering fiels dialog
 */

public interface FilterDialogPresenter {
    void filterAudio();

    void filterImages();

    void filterDocs();

    void filterExec();

    void filterOthers();

    void clearFilters();
}
