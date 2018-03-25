package kramnik.bartlomiej.clouddriveclient.View.FilesList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import kramnik.bartlomiej.clouddriveclient.View.Dialogs.FileOptionsDialog;
import kramnik.bartlomiej.clouddriveclient.View.ProgressIndicator;

public class FilesListActivity extends Activity implements FilesListView, View.OnClickListener, AdapterView.OnItemClickListener, ProgressIndicator, AdapterView.OnItemLongClickListener {

    private ProgressBar progressBar, downloadingProgress;
    private ListView listView;
    private FilesListAdapter adapter;
    private FloatingActionButton addButton;
    private FloatingActionButton backButton;

    private final int requestCodeGetFile = 23485;


    @Inject
    FilesListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        downloadingProgress = (ProgressBar) findViewById(R.id.downloadPropgressbar);
        listView = (ListView) findViewById(R.id.listView);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        backButton = (FloatingActionButton) findViewById(R.id.backButton);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
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
                listView.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
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
            }
        });
    }

    @Override
    public ProgressIndicator getProgressIndocator() {
        return this;
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

    @Override
    public void setProgress(final double current, final double max) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadingProgress.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    downloadingProgress.setProgress((int) ((current / max) * 100), true);
                } else {
                    downloadingProgress.setProgress((int) ((current / max) * 100));
                }
            }
        });

    }

    private String getExtension(String name) {
        int ind = name.lastIndexOf(".");
        return name.substring(ind + 1);
    }

    @Override
    public void completed(final File file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadingProgress.setVisibility(View.GONE);

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(getExtension(file.getName()));
                newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    if (mimeType==null) throw new NullPointerException();
                    startActivity(newIntent);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloadComplete), Toast.LENGTH_LONG).show();
                }


            }
        });

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
}
