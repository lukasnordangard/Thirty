package se.umu.luno0020.thirty

import java.io.Serializable

/**
 * Represents a game round with a score and category.
 *
 * @property score The score achieved in this game round.
 * @property category The category associated with this game round.
 */
class GameRound(private var score: Int, private var category: String) : Serializable {

    /**
     * Retrieves the score of the game round.
     *
     * @return The score of the game round.
     */
    fun getScore(): Int {
        return score
    }

    /**
     * Retrieves the category of the game round.
     *
     * @return The category of the game round.
     */
    fun getCategory(): String {
        return category
    }
}
