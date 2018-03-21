package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Presenter.FilesListPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;

public class FilesListActivity extends Activity implements FilesListView, View.OnClickListener {

    private ProgressBar progressBar;
    private ListView listView;
    private FilesListAdapter adapter;
    private FloatingActionButton addButton;
    private FloatingActionButton backButton;


    @Inject
    FilesListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        listView = (ListView) findViewById(R.id.listView);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        backButton = (FloatingActionButton) findViewById(R.id.backButton);

        addButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        ((App) getApplication()).getAppComponent().inject(this);

        adapter = new FilesListAdapter();
        ((App) getApplication()).getAppComponent().inject(adapter);


        presenter.setFilesListView(this);
        presenter.getFilesList();
        listView.setAdapter(adapter);

    }


    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void refreshView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==addButton.getId()){
            // TODO: 21.03.18
        }
        if (view.getId()==backButton.getId()){
            presenter.goBack();
        }
    }
}
