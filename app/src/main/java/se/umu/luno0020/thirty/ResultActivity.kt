package se.umu.luno0020.thirty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val button:Button = findViewById(R.id.button2)
        button.setOnClickListener {
            val i = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(i)

        }
    }
}