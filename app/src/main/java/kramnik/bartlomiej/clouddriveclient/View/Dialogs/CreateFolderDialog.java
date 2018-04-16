package kramnik.bartlomiej.clouddriveclient.View.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Presenter.CreateFolderDialogPresenter;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * Dialog for cerating new folder
 */

public class CreateFolderDialog extends DialogFragment {

    @Inject
    CreateFolderDialogPresenter presenter;

    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        editText = new EditText(getContext());

        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.createFolder(editText.getText().toString());
            }
        });

        return builder.create();
    }
}
