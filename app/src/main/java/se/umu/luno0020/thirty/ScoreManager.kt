package se.umu.luno0020.thirty

import android.content.Context
import android.view.View
import android.widget.Toast
import java.util.Stack

class ScoreManager(private val context: Context, private val diceManager:DiceManager) {
    private var totalScoreList = ArrayList<Int>()
    private var currentScore = 0

    fun addDice(itemSelected: String) {
        val diceValuesToAdd = mutableListOf<Int>()
        val diceStack = Stack<Dice>()

        diceManager.findAllDiceForAddition(totalScoreList,diceValuesToAdd,diceStack)

        val addResult = diceValuesToAdd.sum()
        currentScore += addResult

        if (checkAdditionRules(itemSelected, addResult)) {
            updateScoreView(currentScore, totalScoreList.sum())
        } else {
            totalScoreList = restoreTotalScore(diceStack, totalScoreList)
            currentScore -= addResult
            Toast.makeText(context, "Sum != $itemSelected", Toast.LENGTH_SHORT).show()
        }
        diceValuesToAdd.clear()
        diceStack.clear()
    }

    private fun checkAdditionRules(itemSelected: String, addResult: Int): Boolean {
        return (itemSelected == "LOW" && addResult <= 3) ||
                (itemSelected.toIntOrNull() == addResult)
    }

    private fun updateScoreView(currentScore: Int, totalScore: Int) {
        // Update the score view, possibly by communicating back to the MainActivity
        println("currentScore: $currentScore")
        println("totalScore: $totalScore")
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

    fun setCurrentScore(score: Int){
        currentScore = score
    }
}
