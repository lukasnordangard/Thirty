package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""

    private lateinit var diceManager:DiceManager
    private lateinit var scoreManager: ScoreManager
    private var diceButtons = listOf<ImageButton>()

    private var gameRounds = mutableListOf<GameRound>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textInputLayout: TextInputLayout = findViewById(R.id.textInputLayout)
        val totalScoreText: TextView = findViewById(R.id.tvTotalScore)
        val currentScoreText: TextView = findViewById(R.id.tvCurrentScore)


        diceButtons = addDiceButtons()
        diceManager = DiceManager(this, diceButtons)
        scoreManager = ScoreManager(this, diceManager, totalScoreText, currentScoreText)

        // Roll dice.
        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            diceManager.rollAllDice(textInputLayout)
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
                    scoreManager.setCurrentScore(0)
                }
            }

        // Add dice.
        val addDice: Button = findViewById(R.id.btnAdd)
        addDice.setOnClickListener {
            scoreManager.addDice(itemSelected, textInputLayout)
        }

        // Next roll round.
        val btnNextRound: Button = findViewById(R.id.btnNext)
        btnNextRound.setOnClickListener{
            saveCurrentRound()

            // Check if there are score categories left in dropdown menu.
            if (dropDownItems.size > 1) {
                prepareNextRound()
            } else {
                goToResultView()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("itemSelected", itemSelected)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        itemSelected = savedInstanceState.getString("itemSelected") ?: ""
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

    private fun saveCurrentRound() {
        gameRounds.add(scoreManager.saveCurrentRound(itemSelected))
    }

    /**
     * Resets and updates the necessary components for the next game round.
     */
    private fun prepareNextRound() {
        resetDiceAndVisibility()
        updateDropDownMenu()
        resetRollNumberAndListeners()
    }

    private fun resetDiceAndVisibility() {
        diceManager.resetDice()
        setNewRoundVisibility()
    }

    @SuppressLint("SetTextI18n")
    private fun setNewRoundVisibility(){
        val currentScoreText: TextView = findViewById(R.id.tvCurrentScore)
        val addDice: Button = findViewById(R.id.btnAdd)
        val btnNextRound: Button = findViewById(R.id.btnNext)
        currentScoreText.text = "Current score: 0"
        addDice.visibility = View.INVISIBLE
        btnNextRound.visibility = View.INVISIBLE
    }

    private fun updateDropDownMenu() {
        dropDownItems.remove(itemSelected)
        /*
        val dropDownAlternatives = "Alternativ: " + dropDownItems.toString()
        val currentDropDownItemsText: TextView = findViewById(R.id.tvCurrentDropDownItems)
        currentDropDownItemsText.text = dropDownAlternatives
         */
    }

    private fun resetRollNumberAndListeners() {
        diceManager.setNumberOfRolls(0)
        setDiceListener()
    }

    private fun goToResultView() {
        val intent = Intent(this, ResultActivity::class.java)
        //intent.putExtra("gameRounds", gameRounds.toTypedArray())
        startActivity(intent)
    }

}