package kramnik.bartlomiej.clouddriveclient.View.ShowLogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kramnik.bartlomiej.clouddriveclient.Presenter.LogsAdapterPresenter
import kramnik.bartlomiej.clouddriveclient.R
import javax.inject.Inject

/**
 * lkist adapter for showing logs activity
 */
class LogsListAdapter : BaseAdapter() {

    @Inject
    lateinit var presenter : LogsAdapterPresenter

    @Inject
    lateinit var context: Context

    private var inflater : LayoutInflater? = null

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        if (inflater == null) inflater = LayoutInflater.from(context)

        val event = presenter.getLogItem(p0)
        val result = inflater!!.inflate(R.layout.logs_cell, null)
        (result.findViewById<TextView>(R.id.title)).text = event.path
        when(event.operation){
            event.ADDED_DIR, event.ADDED_FILE -> result.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.upload)
            event.DOWNLOADED -> result.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.download)
            event.DELETED -> result.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.delete)
            event.RENAMED -> result.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.edit)
        }

        return result
    }

    override fun getItem(p0: Int): Any {
        return presenter.getLogItem(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 1
    }

    override fun getCount(): Int {
        return presenter.itemsCount
    }
}