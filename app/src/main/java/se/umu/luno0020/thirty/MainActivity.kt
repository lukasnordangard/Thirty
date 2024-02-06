package se.umu.luno0020.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""
    private var currentScore = 0

    private lateinit var diceManager:DiceManager
    private lateinit var scoreManager: ScoreManager
    private var diceButtons = listOf<ImageButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceButtons = addDiceButtons()
        diceManager = DiceManager(this, diceButtons)

        // Roll dice.
        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            diceManager.rollAllDice()
        }

        // Listening for dices (to save during next roll).
        setDiceListener()

        // Select scoring category.
        val autoComplete: AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.list_item, dropDownItems)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                itemSelected = dropDownItems[i]

                // Find score category that matches chosen one in drop down menu.
                dropDownItems.find { it == itemSelected }?.run {
                    setDiceListener()
                    makeButtonsVisible()
                    currentScore = 0
                }
            }

        // Add dice.
        scoreManager = ScoreManager(this, diceManager)

        val addDice: Button = findViewById(R.id.btnAdd)
        addDice.setOnClickListener {
            println("Hejhej")
            scoreManager.addDice(itemSelected)
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

    private fun setDiceListener(){
        for (diceButton in diceButtons) {
            val diceIndex = diceButtons.indexOf(diceButton)
            if (diceManager.getNumberOfRolls() < 2){
                diceButton.setOnClickListener {
                    diceManager.toggleDiceSelected(diceManager.getDiceList().elementAt(diceIndex))
                }
            } else {
                diceButton.setOnClickListener {
                    diceManager.toggleDiceToAdd(diceManager.getDiceList().elementAt(diceIndex))
                }
            }
        }
    }

    private fun makeButtonsVisible(){
        val addDice: Button = findViewById(R.id.btnAdd)
        val btnNext: Button = findViewById(R.id.btnNext)
        addDice.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        diceManager.unselectAllDice()
    }

}