package se.umu.luno0020.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""
    private var currentScore = 0

    private var numberOfRolls = 0
    private var diceButtons = listOf<ImageButton>()
    private val diceList = mutableListOf<Dice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceButtons = initDiceButtons()
        initializeDice()

        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            if (numberOfRolls < 3) {
                updateDice()
                numberOfRolls++
            } else {
                Toast.makeText(this, "Max number of rolls!", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun initDiceButtons(): List<ImageButton> {
        return listOf(
            findViewById(R.id.ibDice1), findViewById(R.id.ibDice2), findViewById(R.id.ibDice3),
            findViewById(R.id.ibDice4), findViewById(R.id.ibDice5), findViewById(R.id.ibDice6)
        )
    }

    private fun initializeDice() {
        for (diceButton in diceButtons) {
            val dice = Dice(diceButton)
            rollDice(dice)
            diceList.add(dice)
            dice.diceButton.visibility = View.INVISIBLE
        }
    }

    private fun rollDice(dice: Dice) {
        if (!dice.getSelectedStatus()) {
            dice.setValue(dice.roll())
            updateDiceImg(dice)
        }
    }

    private fun updateDiceImg(dice: Dice) {
        val imageResource = when {
            (!dice.getSelectedScoreTerm() && !dice.getSelectedStatus()) -> {
                when (dice.getValue()) {
                    1 -> R.drawable.white1
                    2 -> R.drawable.white2
                    3 -> R.drawable.white3
                    4 -> R.drawable.white4
                    5 -> R.drawable.white5
                    6 -> R.drawable.white6
                    else -> R.drawable.white1
                }
            }
            (dice.getSelectedStatus()) -> {
                when (dice.getValue()) {
                    1 -> R.drawable.grey1
                    2 -> R.drawable.grey2
                    3 -> R.drawable.grey3
                    4 -> R.drawable.grey4
                    5 -> R.drawable.grey5
                    6 -> R.drawable.grey6
                    else -> R.drawable.grey1
                }
            }
            (dice.getSelectedScoreTerm()) -> {
                when (dice.getValue()) {
                    1 -> R.drawable.red1
                    2 -> R.drawable.red2
                    3 -> R.drawable.red3
                    4 -> R.drawable.red4
                    5 -> R.drawable.red5
                    6 -> R.drawable.red6
                    else -> R.drawable.red1
                }
            }
            else -> {
                when (dice.getValue()) {
                    1 -> R.drawable.white1
                    2 -> R.drawable.white2
                    3 -> R.drawable.white3
                    4 -> R.drawable.white4
                    5 -> R.drawable.white5
                    6 -> R.drawable.white6
                    else -> R.drawable.white1
                }
            }
        }
        dice.diceButton.setImageResource(imageResource)
    }

    private fun updateDice() {
        for (dice in diceList) {
            rollDice(dice)
            dice.diceButton.visibility = View.VISIBLE
        }
    }
}