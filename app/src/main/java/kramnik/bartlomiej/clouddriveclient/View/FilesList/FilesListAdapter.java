package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;
import kramnik.bartlomiej.clouddriveclient.Presenter.FilesListAdapterDataSource;
import kramnik.bartlomiej.clouddriveclient.R;

/**
 * List adapter for list of files
 */

public class FilesListAdapter extends BaseAdapter {

    @Inject
    FilesListAdapterDataSource dataSource;

    @Inject
    Context context;

    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return dataSource.getAll().size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater==null) inflater = LayoutInflater.from(context);
        FileDetails file = dataSource.getItem(i);

        View result = inflater.inflate(R.layout.files_list_cell,null);
        switch (file.getFileType()){
            case Audio:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.audio);
                break;

            case Image:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.image);
                break;

            case Folder:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.folder);
                break;

            case Program:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.code);
                break;

            case TextFile:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.doc);
                break;

            case Other:
                ((ImageView)result.findViewById(R.id.icon)).setImageResource(R.drawable.question);
                break;
        }

        ((TextView)result.findViewById(R.id.textView)).setText(file.getName());

        return result;
    }
}
