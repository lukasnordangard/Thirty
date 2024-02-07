package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.Stack

class ScoreManager( private val context: Context, private val diceManager:DiceManager,
                    private val totalScoreText: TextView, private val currentScoreText: TextView) {
    private var totalScoreList = ArrayList<Int>()
    private var currentScore = 0

    fun addDice(itemSelected: String, textInputLayout: TextInputLayout) {
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
            textInputLayout.visibility = View.GONE
            Toast.makeText(context, "Sum != $itemSelected", Toast.LENGTH_SHORT).show()
        }
        diceValuesToAdd.clear()
        diceStack.clear()
    }

    private fun checkAdditionRules(itemSelected: String, addResult: Int): Boolean {
        return (itemSelected == "LOW" && addResult <= 3) ||
                (itemSelected.toIntOrNull() == addResult)
    }

    @SuppressLint("SetTextI18n")
    private fun updateScoreView(currentScore: Int, totalScore: Int) {
        // Update the score view, possibly by communicating back to the MainActivity
        totalScoreText.text = "Total score: $totalScore"
        currentScoreText.text = "Current score: $currentScore"
    }

    private fun restoreTotalScore(diceStack: Stack<Dice>, scoreList: ArrayList<Int>): ArrayList<Int> {
        while (!diceStack.empty()) {
            val dice = diceStack.pop()
            scoreList.removeLast()
            dice.diceButton.visibility = View.VISIBLE
        }
        return scoreList
    }

    fun saveCurrentRound(itemSelected: String): GameRound {
        return GameRound(currentScore, itemSelected)
    }

    fun resetCurrentScore(){
        currentScore = 0
    }

    fun saveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putInt("currentScore", currentScore)
        bundle.putIntegerArrayList("totalScoreList", totalScoreList)
        // You may add more data to the bundle if necessary
        return bundle
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        currentScore = savedInstanceState.getInt("currentScore", 0)
        totalScoreList = savedInstanceState.getIntegerArrayList("totalScoreList") ?: ArrayList()
        // You may restore more data from the bundle if necessary
    }
}
