package se.umu.luno0020.thirty

import java.io.Serializable

class GameRound(private var score: Int ,private var category: String) : Serializable {

    fun getScore(): Int {
        return score
    }

    fun getCategory(): String {
        return category
    }

}