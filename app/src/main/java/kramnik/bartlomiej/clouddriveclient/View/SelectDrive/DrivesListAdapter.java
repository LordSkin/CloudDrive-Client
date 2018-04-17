package kramnik.bartlomiej.clouddriveclient.View.SelectDrive;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.ServerEntity;
import kramnik.bartlomiej.clouddriveclient.Presenter.DrivesListAdapterDataSource;
import kramnik.bartlomiej.clouddriveclient.R;

/**
 * List adapterfor drives list
 */

public class DrivesListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    @Inject
    DrivesListAdapterDataSource source;

    @Inject
    Context context;

    @Override
    public int getCount() {
        return source.getCount();
    }

    @Override
    public Object getItem(int i) {
        return source.getServer(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater==null) inflater = LayoutInflater.from(context);

        ServerEntity serverEntity = source.getServer(i);

        View result = inflater.inflate(R.layout.select_drive_cell, null);
        ((TextView)result.findViewById(R.id.name)).setText(serverEntity.getName());

        if(serverEntity.isAvalible()){
            result.findViewById(R.id.colorBox).setBackgroundColor(Color.GREEN);
        }
        else {
            result.findViewById(R.id.colorBox).setBackgroundColor(Color.RED);
        }

        return result;
    }
}
