package se.umu.luno0020.thirty

import android.widget.ImageButton

/**
 * Class that creates and handel the Dice() object.
 */
class Dice(var diceButton: ImageButton) {

    private var value = 1
    private var isSelected = false
    private var selectedScoreTerm = false

    fun getValue(): Int {
        return value
    }

    fun setValue(diceValue: Int){
        value = diceValue
    }

    fun getSelectedStatus(): Boolean {
        return isSelected
    }

    fun setSelectedStatus(status: Boolean){
        isSelected = status
    }

    fun getSelectedScoreTerm(): Boolean {
        return selectedScoreTerm
    }

    fun setSelectedScoreTerm(status: Boolean){
        selectedScoreTerm = status
    }

    fun roll(): Int {
        return (1..6).random()
    }
}