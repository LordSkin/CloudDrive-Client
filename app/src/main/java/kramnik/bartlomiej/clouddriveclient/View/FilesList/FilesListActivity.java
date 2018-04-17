package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import javax.inject.Inject;

import kramnik.bartlomiej.clouddriveclient.Presenter.FilesListPresenter;
import kramnik.bartlomiej.clouddriveclient.R;
import kramnik.bartlomiej.clouddriveclient.Root.App;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.CreateFolderDialog;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.FileOptionsDialog;
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.FilterDialog;

public class FilesListActivity extends AppCompatActivity implements FilesListView, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar progressBar;
    private ListView listView;
    private FilesListAdapter adapter;
    private FloatingActionButton addButton;
    private FloatingActionButton backButton;
    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;


    private final int requestCodeGetFile = 23485;


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
        refresh = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        addButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        refresh.setOnRefreshListener(this);

        ((App) getApplication()).getAppComponent().inject(this);

        adapter = new FilesListAdapter();
        ((App) getApplication()).getAppComponent().inject(adapter);

        getSupportActionBar().setTitle(presenter.getDriveName());
        presenter.setFilesListView(this);
        listView.setAdapter(adapter);
        presenter.getFilesList();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 001);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 002);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFolder:
                CreateFolderDialog dialog = new CreateFolderDialog();
                dialog.show(getFragmentManager(), "");
                return true;

            case R.id.filter:
                FilterDialog dialog2 = new FilterDialog();
                dialog2.show(getFragmentManager(), "");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void operFile(File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(getExtension(file.getName()));
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if (mimeType == null) throw new NullPointerException();
            startActivity(newIntent);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloadComplete), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void refreshView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void shareFile(String address) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = address;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, this.getResources().getString(R.string.shareVia)));
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
    public void onClick(View view) {
        if (view.getId() == addButton.getId()) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");

            startActivityForResult(intent, requestCodeGetFile);

        }
        if (view.getId() == backButton.getId()) {
            presenter.goBack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCodeGetFile) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();

                String s = uri.getPath();

                presenter.addFile(uri);

            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.itemClicked(i);
    }


    private String getExtension(String name) {
        int ind = name.lastIndexOf(".");
        return name.substring(ind + 1);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putInt("pos", i);

        FileOptionsDialog dialog = new FileOptionsDialog();
        dialog.setPosition(i);
        dialog.show(getFragmentManager(), getResources().getString(R.string.fileDetails));
        return true;
    }


    @Override
    public void onRefresh() {
        presenter.getFilesList();
    }
}
