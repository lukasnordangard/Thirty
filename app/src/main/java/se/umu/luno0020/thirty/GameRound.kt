package se.umu.luno0020.thirty

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a game round with a score and category.
 *
 * @property score The score achieved in this game round.
 * @property category The category associated with this game round.
 */
class GameRound(private var score: Int, private var category: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(score)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameRound> {
        override fun createFromParcel(parcel: Parcel): GameRound {
            return GameRound(parcel)
        }

        override fun newArray(size: Int): Array<GameRound?> {
            return arrayOfNulls(size)
        }
    }
}
