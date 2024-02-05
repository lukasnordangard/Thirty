package se.umu.luno0020.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""
    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Select scoring category.
        val autoComplete: AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.list_item, dropDownItems)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                itemSelected = dropDownItems[i]

                // Find score category that matches chosen one in drop down menu.
                dropDownItems.find { it == itemSelected }?.run {
                    currentScore = 0
                }
            }
    }
}