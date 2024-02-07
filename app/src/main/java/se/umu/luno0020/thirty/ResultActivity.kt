package se.umu.luno0020.thirty

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
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
            val arrayAdapter: ArrayAdapter<*>
            val roundScore = arrayListOf<String>()

            for (gameRound in gameRounds) {
                roundScore.add("Category: ${gameRound.getCategory()},\t Score: ${gameRound.getScore()}")
                totalScore += gameRound.getScore()
            }
            tv.text = totalScore.toString()

            // Access the listView from xml file.
            val mListView = findViewById<ListView>(R.id.lvRoundScoreList)
            arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, roundScore)
            mListView.adapter = arrayAdapter
        }
    }
}