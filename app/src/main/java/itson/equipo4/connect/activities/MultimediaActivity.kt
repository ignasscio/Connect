package itson.equipo4.connect.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import itson.equipo4.connect.databinding.ActivityMultimediaBinding

class MultimediaActivity : AppCompatActivity() {

    lateinit var binding: ActivityMultimediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultimediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}