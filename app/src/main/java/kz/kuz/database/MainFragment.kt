package kz.kuz.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class MainFragment : Fragment() {
    private lateinit var mDatabase: SQLiteDatabase

    private inner class Item {
        var id = 0
        var column1: String? = null
        var column2 = 0
    }

    private val items: MutableList<Item> = ArrayList()

    // методы фрагмента должны быть открытыми
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.toolbar_title)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mDatabase = DatabaseHelper(context).writableDatabase

        // готовим данные для записи либо для обновления
//        val values = ContentValues()
//        values.put("column1", "zhenya")
//        values.put("column2", 50)

        // записываем данные
//        mDatabase.insert("table_name", null, values)
        // обновляем данные
//        mDatabase.update("table_name", values, "column1 = ?", arrayOf("petya"))

        // удаляем данные
//        mDatabase.delete("table_name", "column1 = ?", arrayOf("micheal"))

        // выгружаем данные
//        val cursor = queryItems("column2 > ? AND column2 < ?", arrayOf("35", "65"))
        val cursor = queryItems(null, null)
        cursor.use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val item = Item()
                item.id = cursor.getInt(0)
                item.column1 = cursor.getString(1)
                item.column2 = cursor.getInt(2)
                items.add(item)
                cursor.moveToNext()
            }
        }

        // выводим представление
        val outerLinearLayout = view.findViewById<LinearLayout>(R.id.container)
        var innerLinerLayout: LinearLayout
        var title: TextView
        var value: TextView
        for (i in items.indices) {
            title = TextView(context)
            title.width = 200
            title.text = "id"
            value = TextView(context)
            value.width = 200
            value.text = items[i].id.toString()
            innerLinerLayout = LinearLayout(context)
            innerLinerLayout.orientation = LinearLayout.HORIZONTAL
            innerLinerLayout.addView(title)
            innerLinerLayout.addView(value)
            outerLinearLayout.addView(innerLinerLayout)
            title = TextView(context)
            title.width = 200
            title.text = "column1"
            value = TextView(context)
            value.width = 200
            value.text = items[i].column1.toString()
            innerLinerLayout = LinearLayout(context)
            innerLinerLayout.orientation = LinearLayout.HORIZONTAL
            innerLinerLayout.addView(title)
            innerLinerLayout.addView(value)
            outerLinearLayout.addView(innerLinerLayout)
            title = TextView(context)
            title.width = 200
            title.text = "column2"
            value = TextView(context)
            value.width = 200
            value.text = items[i].column2.toString()
            innerLinerLayout = LinearLayout(context)
            innerLinerLayout.orientation = LinearLayout.HORIZONTAL
            innerLinerLayout.addView(title)
            innerLinerLayout.addView(value)
            outerLinearLayout.addView(innerLinerLayout)
        }

        return view
    }

    class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "database.db",
            null, Companion.VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE table_name " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, column1 TEXT, column2 INTEGER)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

        companion object {
            private const val VERSION = 1
        }
    }

    private fun queryItems(whereClause: String?, whereArgs: Array<String>?): Cursor {
        return mDatabase.query(
                "table_name",
                null,  // null - все столбцы, можно - arrayOf("column1", "column2") или arrayOf("SUM(column2)")
                whereClause,
                whereArgs,
                null,
                null,  // используется только с groupBy
                null // можно - "column1 DESC, id"
        )
    }
}