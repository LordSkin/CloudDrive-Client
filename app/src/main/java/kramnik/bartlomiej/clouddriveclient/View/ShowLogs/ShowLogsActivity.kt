package kramnik.bartlomiej.clouddriveclient.View.ShowLogs

import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import kramnik.bartlomiej.clouddriveclient.Presenter.LogsActivityPresenter
import kramnik.bartlomiej.clouddriveclient.R
import kramnik.bartlomiej.clouddriveclient.Root.App
import javax.inject.Inject

/**
 * Activity for presenting system logs
 */
class ShowLogsActivity : AppCompatActivity(), LogListView {


    private lateinit var listView : ListView
    private lateinit var progressBar : ProgressBar;

    @Inject
    lateinit var presenter: LogsActivityPresenter

    private var adapter = LogsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_logs)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE

        (application as App).appComponent.inject(this)
        presenter.setLogsView(this)

        listView = findViewById<ListView>(R.id.logsListView)

        (application as App).appComponent.inject(adapter)
        listView.adapter = adapter

        presenter.updateLogsList()

    }

    override fun reloadList() {
        runOnUiThread { adapter.notifyDataSetChanged() }
    }


    override fun showLoading() {
        runOnUiThread { progressBar.visibility = ProgressBar.VISIBLE }

    }

    override fun hideLoading() {
        runOnUiThread { progressBar.visibility = ProgressBar.GONE }
    }

    override fun showError(resId: Int) {
        runOnUiThread { Toast.makeText(getApplicationContext(), getResources().getString(resId), Toast.LENGTH_SHORT).show(); }
    }
}
