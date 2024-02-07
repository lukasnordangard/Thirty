package se.umu.luno0020.thirty

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val gameRounds = intent.getSerializableExtra("gameRounds") as? Array<GameRound>
        val tvTotalScore: TextView = findViewById(R.id.tvTotalScore)
        val listViewRoundScore: ListView = findViewById(R.id.lvRoundScoreList)

        if (gameRounds != null) {
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
        }

        val restartButton: Button = findViewById(R.id.btnRestart)
        restartButton.setOnClickListener {
            restartGame()
        }
    }

    private fun restartGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
