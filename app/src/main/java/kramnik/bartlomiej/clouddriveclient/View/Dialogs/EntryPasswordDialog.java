package kramnik.bartlomiej.clouddriveclient.View.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Presenter.EnterPasswordPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * Dialog for entering password for drive
 */
public class EntryPasswordDialog extends DialogFragment {

    @Inject
    EnterPasswordPresenter presenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getContext()).inflate(R.layout.enter_password, null);

        final EditText userName = (EditText) v.findViewById(R.id.userName);
        final EditText password = (EditText) v.findViewById(R.id.password);

        builder.setView(v);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.setPassword(userName.getText().toString(), password.getText().toString());
            }
        });

        return builder.create();
    }
}
