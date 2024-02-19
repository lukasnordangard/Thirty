package se.umu.luno0020.thirty

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity displaying the result of the game rounds and providing an option to restart the game.
 */
class ResultActivity : AppCompatActivity() {

    private var totalScore = 0
    private lateinit var gameRounds: ArrayList<GameRound>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Retrieve gameRounds array using Parcelable
        gameRounds = intent.getParcelableArrayListExtra("gameRounds") ?: arrayListOf()
        val tvTotalScore: TextView = findViewById(R.id.tvTotalScore)
        val listViewRoundScore: ListView = findViewById(R.id.lvRoundScoreList)

        val roundScoreList = mutableListOf<String>()

        for (gameRound in gameRounds) {
            val category = gameRound.getCategory()
            val score = gameRound.getScore()
            roundScoreList.add("Category: $category,\t Score: $score")
            totalScore += score
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, roundScoreList)
        listViewRoundScore.adapter = arrayAdapter
        tvTotalScore.text = totalScore.toString()

        val restartButton: Button = findViewById(R.id.btnRestart)
        restartButton.setOnClickListener {
            restartGame()
        }
    }

    /**
     * Restarts the game by navigating to the MainActivity and clearing the activity stack.
     */
    private fun restartGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
