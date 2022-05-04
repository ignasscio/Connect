package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import itson.equipo4.connect.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}