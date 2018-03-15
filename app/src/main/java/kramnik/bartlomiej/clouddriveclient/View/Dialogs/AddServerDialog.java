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
import kramnik.bartlomiej.clouddriveclient.Presenter.AddServerPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * dialog for adding server
 */

public class AddServerDialog extends DialogFragment {

    @Inject
    AddServerPresenter presenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getContext()).inflate(R.layout.add_server_dialog, null);

        final EditText name = (EditText) v.findViewById(R.id.name);
        final EditText address = (EditText) v.findViewById(R.id.address);
        final EditText port = (EditText) v.findViewById(R.id.port);

        builder.setView(v);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.addServer(new ServerEntity(name.getText().toString(), address.getText()+":"+port.getText()));
            }
        });


        return builder.create();
    }
}
