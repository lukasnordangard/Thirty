package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {

    private var totalScore = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val gameRounds = intent.getSerializableExtra("gameRounds") as? Array<GameRound>
        val tv:TextView = findViewById(R.id.tvTotalScore)

        // Check if the list is not null
        if (gameRounds != null) {
            for (gameRound in gameRounds) {
                Log.i("Dice","score: ${gameRound.getScore()}, category: ${gameRound.getCategory()}")
                totalScore += gameRound.getScore()
            }
            tv.text = totalScore.toString()
        }
    }
}