package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.Stack

/**
 * ScoreManager class manages the scoring logic of the game. It keeps track of the total score,
 * current score, and provides methods to add dice values, save and restore game state.
 *
 * @property context The context in which the ScoreManager operates.
 * @property diceManager The DiceManager instance used for managing dice.
 * @property totalScoreText The TextView displaying the total score.
 * @property currentScoreText The TextView displaying the current score.
 */
class ScoreManager( private val context: Context, private val diceManager:DiceManager,
                    private val totalScoreText: TextView, private val currentScoreText: TextView) {
    private var totalScoreList = ArrayList<Int>()
    private var currentScore = 0

    /**
     * Adds the dice values according to the selected scoring category and updates the current score.
     *
     * @param itemSelected The selected scoring category.
     * @param textInputLayout The TextInputLayout for user input.
     * @param rollButton The roll button to hide after adding dice.
     */
    fun addDice(itemSelected: String, textInputLayout: TextInputLayout, rollButton: Button) {
        rollButton.visibility = View.INVISIBLE
        val diceValuesToAdd = mutableListOf<Int>()
        val diceStack = Stack<Dice>()

        diceManager.findAllDiceForAddition(totalScoreList,diceValuesToAdd,diceStack)

        val addResult = diceValuesToAdd.sum()
        currentScore += addResult

        if (checkAdditionRules(itemSelected, addResult)) {
            updateScoreView(currentScore, totalScoreList.sum())
            textInputLayout.visibility = View.GONE
        } else {
            totalScoreList = restoreTotalScore(diceStack, totalScoreList)
            currentScore -= addResult
            //textInputLayout.visibility = View.VISIBLE
            Toast.makeText(context, "Sum != $itemSelected", Toast.LENGTH_SHORT).show()
        }
        diceValuesToAdd.clear()
        diceStack.clear()
    }

    /**
     * Checks if the addition result satisfies the rules of the selected category.
     *
     * @param itemSelected The selected scoring category.
     * @param addResult The result of adding the dice values.
     * @return True if the addition satisfies the rules, false otherwise.
     */
    private fun checkAdditionRules(itemSelected: String, addResult: Int): Boolean {
        return (itemSelected == "LOW" && addResult <= 3) ||
                (itemSelected.toIntOrNull() == addResult)
    }

    /**
     * Updates the score view with the current score and total score.
     *
     * @param currentScore The current score to display.
     * @param totalScore The total score to display.
     */
    @SuppressLint("SetTextI18n")
    private fun updateScoreView(currentScore: Int, totalScore: Int) {
        totalScoreText.text = "Total score: $totalScore"
        currentScoreText.text = "Current score: $currentScore"
    }

    /**
     * Restores the total score list and unhides the dice buttons popped from the dice stack.
     *
     * @param diceStack The stack containing the dice objects for restoration.
     * @param scoreList The total score list to restore.
     * @return The restored total score list.
     */
    private fun restoreTotalScore(diceStack: Stack<Dice>, scoreList: ArrayList<Int>): ArrayList<Int> {
        while (!diceStack.empty()) {
            val dice = diceStack.pop()
            scoreList.removeLast()
            dice.diceButton.visibility = View.VISIBLE
        }
        return scoreList
    }

    /**
     * Saves the current round as a GameRound object.
     *
     * @param itemSelected The selected scoring category.
     * @return The GameRound object representing the current round.
     */
    fun saveCurrentRound(itemSelected: String): GameRound {
        return GameRound(currentScore, itemSelected)
    }

    /**
     * Resets the current score to zero.
     */
    fun resetCurrentScore(){
        currentScore = 0
    }

    /**
     * Saves the instance state of the ScoreManager.
     *
     * @return A Bundle containing the saved instance state data.
     */
    fun saveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putInt("currentScore", currentScore)
        bundle.putIntegerArrayList("totalScoreList", totalScoreList)
        return bundle
    }

    /**
     * Restores the instance state of the ScoreManager.
     *
     * @param savedInstanceState A Bundle containing the saved instance state data.
     */
    @SuppressLint("SetTextI18n")
    fun restoreInstanceState(savedInstanceState: Bundle) {
        currentScore = savedInstanceState.getInt("currentScore", 0)
        totalScoreList = savedInstanceState.getIntegerArrayList("totalScoreList") ?: ArrayList()
        totalScoreText.text = "Total score: ${totalScoreList.sum()}"
        currentScoreText.text = "Current score: $currentScore"
    }
}
