package se.umu.luno0020.thirty

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import java.util.Locale
import java.util.Stack

class DiceManager(private val context: Context, private val diceButtons:List<ImageButton>) {

    private var numberOfRolls = 0
    private val diceList = mutableListOf<Dice>()

    init {
        initializeDice()
    }

    fun getNumberOfRolls(): Int{
        return numberOfRolls
    }

    fun setNumberOfRolls(rollnr: Int){
        numberOfRolls = rollnr
    }

    fun getDiceList(): List<Dice>{
        return diceList
    }

    private fun initializeDice() {
        for (diceButton in diceButtons) {
            val dice = Dice(diceButton)
            rollDice(dice)
            diceList.add(dice)
            dice.diceButton.visibility = View.INVISIBLE
        }
    }

    fun rollAllDice(){
        if (numberOfRolls < 3) {
            updateDice()
            numberOfRolls++
        } else {
            Toast.makeText(context, "Max number of rolls!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDice() {
        for (dice in diceList) {
            rollDice(dice)
            dice.diceButton.visibility = View.VISIBLE
        }
    }

    private fun rollDice(dice: Dice) {
        if (!dice.getSelectedStatus()) {
            dice.setValue(dice.roll())
            updateDiceImg(dice)
        }
    }

    private fun updateDiceImg(dice: Dice) {
        val diceColor = when {
            dice.getSelectedScoreTerm() -> "red"
            dice.getSelectedStatus() -> "grey"
            else -> "white"
        }
        val imageResource = when (dice.getValue()) {
            1 -> getDrawableResource("$diceColor 1")
            2 -> getDrawableResource("$diceColor 2")
            3 -> getDrawableResource("$diceColor 3")
            4 -> getDrawableResource("$diceColor 4")
            5 -> getDrawableResource("$diceColor 5")
            6 -> getDrawableResource("$diceColor 6")
            else -> getDrawableResource("$diceColor 1")
        }
        dice.diceButton.setImageResource(imageResource)
    }

    private fun getDrawableResource(name: String): Int {
        val resourceId = try {
            val fieldName = name.lowercase(Locale.getDefault()).replace(" ", "")
            R.drawable::class.java.getField(fieldName).getInt(null)
        } catch (e: Exception) {
            R.drawable.white1
        }
        return resourceId
    }

    fun toggleDiceSelected(dice: Dice) {
        dice.setSelectedStatus(!dice.getSelectedStatus())
        updateDiceImg(dice)
    }

    fun toggleDiceToAdd(dice: Dice) {
        dice.setSelectedScoreTerm(!dice.getSelectedScoreTerm())
        updateDiceImg(dice)
    }

    fun unselectAllDice(){
        for (dice in diceList){
            dice.setSelectedStatus(false)
            updateDiceImg(dice)
        }
    }

    fun findAllDiceForAddition(totalScoreList: ArrayList<Int>, diceValuesToAdd: MutableList<Int>, diceStack: Stack<Dice>
    ){
        for (dice in diceList) {
            if (dice.getSelectedScoreTerm()) {
                totalScoreList.add(dice.getValue())
                diceValuesToAdd.add(dice.getValue())
                diceStack.push(dice)
                toggleDiceToAdd(dice)
                dice.diceButton.visibility = View.INVISIBLE
            }
        }
    }

    fun resetDice(){
        for (dice in diceList){
            dice.diceButton.visibility = View.INVISIBLE
            dice.setSelectedStatus(false)
            dice.setSelectedScoreTerm(false)
        }
    }
}