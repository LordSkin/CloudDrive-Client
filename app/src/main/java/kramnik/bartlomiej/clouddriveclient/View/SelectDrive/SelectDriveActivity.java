package kramnik.bartlomiej.clouddriveclient.View.SelectDrive;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Presenter.SelectDrivePresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;
import kramnik.bartlomiej.clouddriveclient.Root.Dagger.AppComponent;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.AddServerDialog;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.EntryPasswordDialog;
import kramnik.bartlomiej.clouddriveclient.View.FilesList.FilesListActivity;

public class SelectDriveActivity extends AppCompatActivity implements View.OnClickListener, SelectDriveView, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ProgressBar progressBar;
    private ListView listView;
    private DrivesListAdapter listAdapter;
    FloatingActionButton addButton;

    @Inject
    SelectDrivePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drive);

        listAdapter = new DrivesListAdapter();
        AppComponent appComponent = ((App)getApplication()).getAppComponent();
        appComponent.inject(this);
        appComponent.inject(listAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView)findViewById(R.id.listView);
        addButton = (FloatingActionButton)findViewById(R.id.addButton);

        addButton.setOnClickListener(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        presenter.setSelectDriveView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setSelectDriveView(this);
    }

    @Override
    public void onClick(View view) {
        AddServerDialog dialog = new AddServerDialog();
        dialog.show(getFragmentManager(), getResources().getString(R.string.addServerDialog));
    }

    @Override
    public void updateList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void showPasswordDialog() {
        EntryPasswordDialog dialog = new EntryPasswordDialog();
        dialog.show(getFragmentManager(), getResources().getString(R.string.enterPass));
    }

    @Override
    public void gotoFileView() {
        Intent intent = new Intent(this, FilesListActivity.class);
        startActivity(intent);
    }



    @Override
    public void showError(final int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(id), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.itemSelected(i);
        showLoading();
        EntryPasswordDialog dialog = new EntryPasswordDialog();
        dialog.show(getFragmentManager(), getResources().getString(R.string.enterPass));


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.itemDeleted(i);
        return true;
    }
}
