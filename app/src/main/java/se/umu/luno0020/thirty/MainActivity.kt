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

/**
 * The main activity for the game Thirty. Handles dice rolling, scoring,
 * and navigation to the result screen.
 *
 * @author Lukas Nordangård id20lsd
 * @version 2.0
 */
class MainActivity : AppCompatActivity() {

    private lateinit var diceManager: DiceManager
    private lateinit var scoreManager: ScoreManager
    private var diceButtons = listOf<ImageButton>()
    private val dropDownItems = mutableListOf("LOW", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var itemSelected = ""
    private var gameRounds = mutableListOf<GameRound>()
    private var buttonsVisible = false

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textInputLayout: TextInputLayout = findViewById(R.id.textInputLayout)
        val rollNrText: TextView = findViewById(R.id.tvRollNr)
        val totalScoreText: TextView = findViewById(R.id.tvTotalScore)
        val currentScoreText: TextView = findViewById(R.id.tvCurrentScore)

        diceButtons = addDiceButtons()
        diceManager = DiceManager(this, diceButtons, rollNrText)
        scoreManager = ScoreManager(this, diceManager, totalScoreText, currentScoreText)

        // Roll dice
        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            diceManager.rollAllDice(textInputLayout)
        }

        // Listening for dices (to save during next roll)
        setDiceListener()

        // Select scoring category
        createDropDownMenu()

        // Add dice
        val addDice: Button = findViewById(R.id.btnAdd)
        addDice.setOnClickListener {
            scoreManager.addDice(itemSelected, textInputLayout, rollButton)
        }

        // Next
        val btnNextRound: Button = findViewById(R.id.btnNext)
        btnNextRound.setOnClickListener{
            // Save current round
            gameRounds.add(scoreManager.saveCurrentRound(itemSelected))

            if (dropDownItems.size > 1) {
                prepareNextRound()
                if (dropDownItems.size == 1) {
                    val nextBtn: Button = findViewById(R.id.btnNext)
                    nextBtn.text = "Result"
                }
            } else {
                goToResultView()
            }
        }

    }

    /**
     * onSaveInstanceState: Saves the current state data into the given Bundle.
     * @param outState The Bundle into which to save the data.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("diceManager", diceManager.saveInstanceState())
        outState.putBundle("scoreManager", scoreManager.saveInstanceState())
        outState.putString("itemSelected", itemSelected)
        outState.putStringArrayList("dropDownItems", ArrayList(dropDownItems))
        outState.putSerializable("gameRounds", ArrayList(gameRounds))
        outState.putBoolean("buttonsVisible", buttonsVisible)
        super.onSaveInstanceState(outState)
    }

    /**
     * onRestoreInstanceState: Restores the saved instance state data from the given Bundle.
     * @param savedInstanceState The Bundle containing the saved instance state data.
     */
    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore diceManager and scoreManager data
        savedInstanceState.getBundle("diceManager")?.let { diceManager.restoreInstanceState(it) }
        savedInstanceState.getBundle("scoreManager")?.let { scoreManager.restoreInstanceState(it) }

        // Restore DropDownMenu
        dropDownItems.clear()
        dropDownItems.addAll(savedInstanceState.getStringArrayList("dropDownItems") ?: emptyList())
        itemSelected = savedInstanceState.getString("itemSelected", "")
        createDropDownMenu()
        val currentDropDownItemsText: TextView = findViewById(R.id.tvCurrentDropDownItems)
        currentDropDownItemsText.text = "Alternative: $dropDownItems"

        gameRounds = savedInstanceState.getSerializable("gameRounds") as? MutableList<GameRound> ?: mutableListOf()
        buttonsVisible = savedInstanceState.getBoolean("buttonsVisible")

        if (diceManager.getNumberOfRolls() > 2 && !diceManager.hasDicesBeenAdded()) {
            val textInputLayout: TextInputLayout = findViewById(R.id.textInputLayout)
            textInputLayout.visibility = View.VISIBLE
        }
        if (itemSelected != "" && buttonsVisible) {
            makeButtonsVisible()
            setDiceListener()
        }
        if (diceManager.hasDicesBeenAdded()){
            val rollButton: Button = findViewById(R.id.btnRoll)
            rollButton.visibility = View.INVISIBLE
        }
    }

    /**
     * createDropDownMenu: Sets up the drop-down menu for selecting score categories.
     * It configures the AutoCompleteTextView with the appropriate adapter and item click listener.
     */
    private fun createDropDownMenu(){
        val autoComplete: AutoCompleteTextView = findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(this, R.layout.list_item, dropDownItems)
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                itemSelected = dropDownItems[i]

                // Find score category that matches chosen one in drop down menu
                dropDownItems.find { it == itemSelected }?.run {
                    setDiceListener()
                    makeButtonsVisible()
                    scoreManager.resetCurrentScore()
                }
            }
    }

    /**
     * addDiceButtons: Retrieves a list of ImageButton objects corresponding to the dice buttons in the layout.
     * @return List<ImageButton>: List of ImageButton objects.
     */
    private fun addDiceButtons(): List<ImageButton> {
        return listOf(
            findViewById(R.id.ibDice1), findViewById(R.id.ibDice2), findViewById(R.id.ibDice3),
            findViewById(R.id.ibDice4), findViewById(R.id.ibDice5), findViewById(R.id.ibDice6)
        )
    }

    /**
     * setDiceListener: Sets up the click listeners for the dice buttons based on the current game state.
     */
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

    /**
     * makeButtonsVisible: Makes the "Add" and "Next" buttons visible and unselects all dice.
     */
    private fun makeButtonsVisible(){
        val addDice: Button = findViewById(R.id.btnAdd)
        val btnNext: Button = findViewById(R.id.btnNext)
        addDice.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        diceManager.unselectAllDice()
        buttonsVisible = true
    }

    /**
     * prepareNextRound: Resets and updates the necessary components for the next game round.
     */
    @SuppressLint("SetTextI18n")
    private fun prepareNextRound() {
        val rollNrText: TextView = findViewById(R.id.tvRollNr)
        rollNrText.text = "Roll nr: 0"
        diceManager.resetDice()
        diceManager.setNumberOfRolls(0)
        setNewRoundVisibility()
        updateDropDownMenu()
        setDiceListener()
    }

    /**
     * setNewRoundVisibility: Sets the visibility of UI components for a new game round.
     */
    @SuppressLint("SetTextI18n")
    private fun setNewRoundVisibility(){
        val currentScoreText: TextView = findViewById(R.id.tvCurrentScore)
        val addDice: Button = findViewById(R.id.btnAdd)
        val rollButton: Button = findViewById(R.id.btnRoll)
        val btnNextRound: Button = findViewById(R.id.btnNext)
        currentScoreText.text = "Current score: 0"
        rollButton.visibility = View.VISIBLE
        addDice.visibility = View.INVISIBLE
        btnNextRound.visibility = View.INVISIBLE
        buttonsVisible = false
    }

    /**
     * updateDropDownMenu: Updates the drop-down menu by removing the currently selected item.
     */
    @SuppressLint("SetTextI18n")
    private fun updateDropDownMenu() {
        dropDownItems.remove(itemSelected)
        val currentDropDownItemsText: TextView = findViewById(R.id.tvCurrentDropDownItems)
        currentDropDownItemsText.text = "Alternative: $dropDownItems"
    }

    /**
     * goToResultView: Navigates to the ResultActivity with the game rounds data.
     */
    private fun goToResultView() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("gameRounds", gameRounds.toTypedArray())
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}