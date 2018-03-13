package kramnik.bartlomiej.clouddriveclient.View;

import java.io.File;

/**
 * Indicator of progress
 */

public interface ProgressIndicator {

    void setProgress(double current, double max);

    void completed(File file);

}
