package se.umu.luno0020.thirty

import android.widget.ImageButton

/**
 * Represents a dice in the game.
 *
 * @property diceButton The ImageButton associated with the dice.
 */
class Dice(var diceButton: ImageButton) {

    private var value = 1
    private var isSelected = false
    private var selectedScoreTerm = false
    private var hasBeenAdded = false

    /**
     * Retrieves the current value of the dice.
     *
     * @return The current value of the dice.
     */
    fun getValue(): Int {
        return value
    }

    /**
     * Sets the value of the dice.
     *
     * @param diceValue The value to set for the dice.
     */
    fun setValue(diceValue: Int) {
        value = diceValue
    }

    /**
     * Retrieves the selection status of the dice.
     *
     * @return The selection status of the dice.
     */
    fun getSelectedStatus(): Boolean {
        return isSelected
    }

    /**
     * Sets the selection status of the dice.
     *
     * @param status The selection status to set for the dice.
     */
    fun setSelectedStatus(status: Boolean) {
        isSelected = status
    }

    /**
     * Retrieves the selection status of the dice as a scoring term.
     *
     * @return The selection status of the dice as a scoring term.
     */
    fun getSelectedScoreTerm(): Boolean {
        return selectedScoreTerm
    }

    /**
     * Sets the selection status of the dice as a scoring term.
     *
     * @param status The selection status to set for the dice as a scoring term.
     */
    fun setSelectedScoreTerm(status: Boolean) {
        selectedScoreTerm = status
    }

    /**
     * Retrieves the status indicating whether the dice has been added for scoring.
     *
     * @return True if the dice has been added for scoring, false otherwise.
     */
    fun getHasBeenAddedStatus(): Boolean {
        return hasBeenAdded
    }

    /**
     * Sets the status indicating whether the dice has been added for scoring.
     *
     * @param status The status to set indicating whether the dice has been added for scoring.
     */
    fun setHasBeenAddedStatus(status: Boolean) {
        hasBeenAdded = status
    }

    /**
     * Simulates rolling the dice and returns a random integer between 1 and 6.
     *
     * @return A random integer representing the result of rolling the dice.
     */
    fun roll(): Int {
        return (1..6).random()
    }
}
