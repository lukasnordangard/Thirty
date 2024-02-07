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

/**
 * Manages the dice and their operations in the game.
 * Handles rolling, updating images, toggling selection status,
 * finding dice for addition, resetting, and saving/restoring instance state.
 *
 * @property context The context in which the DiceManager operates.
 * @property diceButtons The list of ImageButtons representing the dice.
 * @property rollNrText The TextView displaying the current roll number.
 */
class DiceManager(
    private val context: Context,
    private val diceButtons: List<ImageButton>,
    private val rollNrText: TextView) {

    private var numberOfRolls = 0
    private val diceList = mutableListOf<Dice>()

    init {
        initializeDice()
    }

    /**
     * Retrieves the current roll number.
     *
     * @return The current roll number.
     */
    fun getNumberOfRolls(): Int {
        return numberOfRolls
    }

    /**
     * Sets the current roll number.
     *
     * @param rollNr The roll number to set.
     */
    fun setNumberOfRolls(rollNr: Int) {
        numberOfRolls = rollNr
    }

    /**
     * Retrieves the list of dice.
     *
     * @return The list of dice.
     */
    fun getDiceList(): List<Dice> {
        return diceList
    }

    /**
     * Initializes the dice by creating Dice objects and adding them to the dice list.
     */
    private fun initializeDice() {
        for (diceButton in diceButtons) {
            val dice = Dice(diceButton)
            diceList.add(dice)
        }
    }

    fun makeDicesInvisible() {
        for (dice in diceList) {
            dice.diceButton.visibility = View.INVISIBLE
        }
    }

    /**
     * Rolls all dice, updates their images, and increments the roll number.
     *
     * @param textInputLayout The TextInputLayout for user input.
     */
    @SuppressLint("SetTextI18n")
    fun rollAllDice(textInputLayout: TextInputLayout) {
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

    /**
     * Updates all dice images based on their current values and status.
     */
    private fun updateDice() {
        for (dice in diceList) {
            rollDice(dice)
            dice.diceButton.visibility = View.VISIBLE
        }
    }

    /**
     * Rolls the specified dice and updates its image.
     *
     * @param dice The dice to roll.
     */
    private fun rollDice(dice: Dice) {
        if (!dice.getSelectedStatus()) {
            dice.setValue(dice.roll())
            updateDiceImg(dice)
        }
    }

    /**
     * Updates the image of the specified dice based on its value and status.
     *
     * @param dice The dice to update the image for.
     */
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

    /**
     * Retrieves the drawable resource ID based on the specified name.
     *
     * @param name The name of the drawable resource.
     * @return The ID of the drawable resource.
     */
    private fun getDrawableResource(name: String): Int {
        val resourceId = try {
            val fieldName = name.lowercase(Locale.getDefault()).replace(" ", "")
            R.drawable::class.java.getField(fieldName).getInt(null)
        } catch (e: Exception) {
            R.drawable.white1
        }
        return resourceId
    }

    /**
     * Toggles the selection status of the specified dice and updates its image accordingly.
     *
     * @param dice The dice to toggle selection for.
     */
    fun toggleDiceSelected(dice: Dice) {
        dice.setSelectedStatus(!dice.getSelectedStatus())
        updateDiceImg(dice)
    }

    /**
     * Toggles the score term selection status of the specified dice and updates its image accordingly.
     *
     * @param dice The dice to toggle score term selection for.
     */
    fun toggleDiceToAdd(dice: Dice) {
        dice.setSelectedScoreTerm(!dice.getSelectedScoreTerm())
        updateDiceImg(dice)
    }

    /**
     * Unselects all dice, resetting their selection status and updating their images.
     */
    fun unselectAllDice() {
        for (dice in diceList) {
            dice.setSelectedStatus(false)
            updateDiceImg(dice)
        }
    }

    /**
     * Finds all dice selected for addition, updates their statuses, adds their values to the total score list,
     * and hides their buttons. Also marks them as added.
     *
     * @param totalScoreList The list to store the total score.
     * @param diceValuesToAdd The list to store dice values to add.
     * @param diceStack The stack to push dice objects to.
     */
    fun findAllDiceForAddition(
        totalScoreList: ArrayList<Int>,
        diceValuesToAdd: MutableList<Int>,
        diceStack: Stack<Dice>
    ) {
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

    /**
     * Resets all dice, hiding their buttons and resetting their statuses.
     */
    fun resetDice() {
        for (dice in diceList) {
            dice.diceButton.visibility = View.INVISIBLE
            dice.setSelectedStatus(false)
            dice.setSelectedScoreTerm(false)
            dice.setHasBeenAddedStatus(false)
        }
    }

    /**
     * Checks if any dice have been marked as added.
     *
     * @return True if any dice have been added, false otherwise.
     */
    fun hasDicesBeenAdded(): Boolean {
        for (dice in diceList) {
            if (dice.getHasBeenAddedStatus()) {
                return true
            }
        }
        return false
    }

    /**
     * Saves the current state of the DiceManager, including the number of rolls and dice data.
     *
     * @return A Bundle containing the saved instance state data.
     */
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

    /**
     * Restores the previous state of the DiceManager, including the number of rolls and dice data.
     *
     * @param savedInstanceState A Bundle containing the saved instance state data.
     */
    @SuppressLint("SetTextI18n")
    fun restoreInstanceState(savedInstanceState: Bundle) {
        numberOfRolls = savedInstanceState.getInt("numberOfRolls", 0)
        rollNrText.text = "Roll nr: $numberOfRolls"
        if (numberOfRolls == 0) {
            makeDicesInvisible()
        }
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
                    if (dice.getHasBeenAddedStatus()) {
                        dice.diceButton.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

}