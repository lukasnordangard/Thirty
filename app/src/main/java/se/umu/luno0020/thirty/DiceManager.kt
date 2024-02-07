package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale
import java.util.Stack

class DiceManager(private val context: Context, private val diceButtons: List<ImageButton>,
                  private val rollNrText: TextView) {

    private var numberOfRolls = 0
    private val diceList = mutableListOf<Dice>()

    init {
        initializeDice()
    }

    fun getNumberOfRolls(): Int{
        return numberOfRolls
    }

    fun setNumberOfRolls(rollNr: Int){
        numberOfRolls = rollNr
    }

    fun getDiceList(): List<Dice>{
        return diceList
    }

    private fun initializeDice() {
        for (diceButton in diceButtons) {
            val dice = Dice(diceButton)
            //rollDice(dice)
            diceList.add(dice)
            //dice.diceButton.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun rollAllDice(textInputLayout: TextInputLayout){
        if (numberOfRolls < 3) {
            updateDice()
            numberOfRolls++
            rollNrText.text = "Roll nr: $numberOfRolls"
            if (numberOfRolls > 2) {
                textInputLayout.visibility = View.VISIBLE
            }
        } else {
            textInputLayout.visibility = View.VISIBLE
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
                dice.setHasBeenAddedStatus(true)
            }
        }
    }

    fun resetDice(){
        for (dice in diceList){
            dice.diceButton.visibility = View.INVISIBLE
            dice.setSelectedStatus(false)
            dice.setSelectedScoreTerm(false)
            dice.setHasBeenAddedStatus(false)
        }
    }

    fun hasDicesBeenAdded(): Boolean {
        for (dice in diceList){
            if (dice.getHasBeenAddedStatus()) {
                return true
            }
        }
        return false
    }

    fun saveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putInt("numberOfRolls", numberOfRolls)

        // Save dice data
        val diceValues = mutableListOf<Int>()
        val diceSelectedStatus = mutableListOf<Boolean>()
        val diceSelectedScoreTerm = mutableListOf<Boolean>()
        val diceHasBeenAddedStatus = mutableListOf<Boolean>()
        for (dice in diceList) {
            diceValues.add(dice.getValue())
            diceSelectedStatus.add(dice.getSelectedStatus())
            diceSelectedScoreTerm.add(dice.getSelectedScoreTerm())
            diceHasBeenAddedStatus.add(dice.getHasBeenAddedStatus())
        }
        bundle.putIntArray("diceValues", diceValues.toIntArray())
        bundle.putBooleanArray("diceSelectedStatus", diceSelectedStatus.toBooleanArray())
        bundle.putBooleanArray("diceSelectedScoreTerm", diceSelectedScoreTerm.toBooleanArray())
        bundle.putBooleanArray("diceHasBeenAddedStatus", diceHasBeenAddedStatus.toBooleanArray())

        return bundle
    }

    @SuppressLint("SetTextI18n")
    fun restoreInstanceState(savedInstanceState: Bundle) {
        numberOfRolls = savedInstanceState.getInt("numberOfRolls", 0)
        rollNrText.text = "Roll nr: $numberOfRolls"
        // Restore dice data
        val diceValues = savedInstanceState.getIntArray("diceValues") ?: intArrayOf()
        val diceSelectedStatus = savedInstanceState.getBooleanArray("diceSelectedStatus") ?: booleanArrayOf()
        val diceSelectedScoreTerm = savedInstanceState.getBooleanArray("diceSelectedScoreTerm") ?: booleanArrayOf()
        val diceHasBeenAddedStatus = savedInstanceState.getBooleanArray("diceHasBeenAddedStatus") ?: booleanArrayOf()
        if (diceValues.size == diceHasBeenAddedStatus.size &&
            diceValues.size == diceSelectedStatus.size &&
            diceValues.size == diceSelectedScoreTerm.size) {
            for (i in diceValues.indices) {
                if (i < diceList.size) {
                    val dice = diceList[i]
                    dice.setValue(diceValues[i])
                    dice.setSelectedStatus(diceSelectedStatus[i])
                    dice.setSelectedScoreTerm(diceSelectedScoreTerm[i])
                    dice.setHasBeenAddedStatus(diceHasBeenAddedStatus[i])
                    updateDiceImg(dice)
                    if (dice.getHasBeenAddedStatus()){
                        dice.diceButton.visibility = View.INVISIBLE
                    }
                }
            }
        }

    }



}