package kramnik.bartlomiej.clouddriveclient.View.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Provides;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileType;
import kramnik.bartlomiej.clouddriveclient.Presenter.FileDetailsPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

/**
 * Dialog providing options of file
 */

public class FileOptionsDialog extends DialogFragment implements View.OnClickListener {

    String fileName;
    FileType type;
    int position;

    public void setPosition(int position) {
        this.position = position;
    }

    @Inject
    FileDetailsPresenter presenter;

    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ((App)getActivity().getApplication()).getAppComponent().inject(this);

        fileName = presenter.getFileDetails(position).getName();
        type = presenter.getFileDetails(position).getFileType();

        View result = LayoutInflater.from(getContext()).inflate(R.layout.file_options_dialog, null);


        ImageView imageView = (ImageView)result.findViewById(R.id.typeIcon);
        switch (type){
            case Audio:
                imageView.setImageResource(R.drawable.audio_pink);
                break;

            case Image:
                imageView.setImageResource(R.drawable.image_pink);
                break;

            case Program:
                imageView.setImageResource(R.drawable.code_pink);
                break;

            case TextFile:
                imageView.setImageResource(R.drawable.doc_pink);
                break;

            case Folder:
                imageView.setImageResource(R.drawable.folder_pink);
                break;

            default:
                imageView.setImageResource(R.drawable.question_pink);
                break;
        }

        TextView name = (TextView)result.findViewById(R.id.fileName);
        name.setText(fileName);

        ImageButton shareButton = (ImageButton)result.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(this);

        editText = (EditText) result.findViewById(R.id.editText);
        editText.setText(fileName);


        builder.setView(result);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.renameFile(position, editText.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.deleteFile(position);
            }
        });

        return builder.create();

    }

    @Override
    public void onClick(View view) {
        presenter.getFileAddress(position);

    }
}
