package kramnik.bartlomiej.clouddriveclient.Model.DataModels

import android.arch.persistence.room.util.TableInfo.Column


/**
 * data model for log event
 */
class Event() {

    val ADDED_FILE = 0
    val ADDED_DIR = 1
    val DELETED = 2
    val RENAMED = 3
    val DOWNLOADED = 4

    var id: Int = 0

    var operation: Int = 0

    lateinit var path: String

    lateinit var date: String

    lateinit var newName: String


}