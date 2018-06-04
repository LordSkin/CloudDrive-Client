package kramnik.bartlomiej.clouddriveclient.View.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Presenter.AddServerPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * dialog for adding server
 */

public class AddServerDialog extends DialogFragment implements DialogInterface.OnClickListener {

    @Inject
    AddServerPresenter presenter;

     EditText name;
     EditText address;
     EditText port;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getContext()).inflate(R.layout.add_server_dialog, null);

        name = (EditText) v.findViewById(R.id.name);
        address = (EditText) v.findViewById(R.id.address);
        port = (EditText) v.findViewById(R.id.port);

        builder.setView(v);
        builder.setPositiveButton("OK", this);


        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (name.getText().toString().trim().length()<=0){
            Toast.makeText(this.getContext(), R.string.emptyName, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (address.getText().toString().trim().length()<=0){
            Toast.makeText(this.getContext(), R.string.emptyAddress, Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            presenter.addServer(new ServerEntity(name.getText().toString(), "http:\\\\"+address.getText()+":"+port.getText()));
        }
    }
}
