package se.umu.luno0020.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""
    private var currentScore = 0

    private lateinit var diceManager:DiceManager
    private var diceButtons = listOf<ImageButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceButtons = addDiceButtons()
        diceManager = DiceManager(this, diceButtons)

        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            diceManager.rollAllDice()
        }

        // Select scoring category.
        val autoComplete: AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.list_item, dropDownItems)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                itemSelected = dropDownItems[i]

                // Find score category that matches chosen one in drop down menu.
                dropDownItems.find { it == itemSelected }?.run {
                    currentScore = 0
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("itemSelected", itemSelected)
        outState.putInt("currentScore", currentScore)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        itemSelected = savedInstanceState.getString("itemSelected") ?: ""
        currentScore = savedInstanceState.getInt("currentScore")
    }

    private fun addDiceButtons(): List<ImageButton> {
        return listOf(
            findViewById(R.id.ibDice1), findViewById(R.id.ibDice2), findViewById(R.id.ibDice3),
            findViewById(R.id.ibDice4), findViewById(R.id.ibDice5), findViewById(R.id.ibDice6)
        )
    }

}