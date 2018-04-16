package kramnik.bartlomiej.clouddriveclient.View.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Presenter.FilterDialogPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * Dialog for selecting filter
 */

public class FilterDialog extends DialogFragment implements View.OnClickListener {

    @Inject
    FilterDialogPresenter presenter;

    private ImageButton audioButton, imageButton, docsButton, execButton, othersButton, clearButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.filter_dialog, null);
        builder.setView(v);

        audioButton = (ImageButton)v.findViewById(R.id.audioButton);
        imageButton = (ImageButton)v.findViewById(R.id.imageButton);
        docsButton = (ImageButton)v.findViewById(R.id.docsButton);
        execButton = (ImageButton)v.findViewById(R.id.execButton);
        othersButton = (ImageButton)v.findViewById(R.id.otherButton);
        clearButton = (ImageButton)v.findViewById(R.id.clearButton);

        audioButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        docsButton.setOnClickListener(this);
        execButton.setOnClickListener(this);
        othersButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.audioButton:
                presenter.filterAudio();
                break;
            case R.id.imageButton:
                presenter.filterImages();
                break;
            case R.id.docsButton:
                presenter.filterDocs();
                break;
            case R.id.execButton:
                presenter.filterExec();
                break;
            case R.id.otherButton:
                presenter.filterOthers();
                break;
            case R.id.clearButton:
                presenter.clearFilters();
                break;
        }
        this.dismiss();
    }
}
